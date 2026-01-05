package com.bibliotheque.service;

import java.sql.SQLException;
import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.impl.EmpruntDAOImpl;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.dao.impl.MembreDAOImpl;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.infra.DatabaseConnection;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;

/**
 * Service métier pour la bibliothèque
 * Contient toute la logique métier (validation, orchestration, etc.)
 * Les appels à la base de données passent par les DAO
 */
public class BibliothequeService {

    private final LivreDAO livreDAO;
    private final MembreDAO membreDAO;
    private final EmpruntDAO empruntDAO;

    /**
     * Constructeur avec injection des DAO
     */
    public BibliothequeService(LivreDAO livreDAO, MembreDAO membreDAO, EmpruntDAO empruntDAO) {
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
        this.empruntDAO = empruntDAO;
    }

    /**
     * Constructeur avec injection du DAO Livre (et DAO Membre par défaut)
     */
    public BibliothequeService(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
        this.membreDAO = new MembreDAOImpl();
        this.empruntDAO = new EmpruntDAOImpl(DatabaseConnection.getInstance().getConnection());
    }

    /**
     * Constructeur par défaut utilisant les impl DAO
     */
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
        this.membreDAO = new MembreDAOImpl();
        this.empruntDAO = new EmpruntDAOImpl(DatabaseConnection.getInstance().getConnection());
    }

    // =========================================================
    // ====================== PARTIE LIVRE ======================
    // =========================================================

    public void ajouterLivre(Livre livre) throws ValidationException {
        if (livre == null) throw new ValidationException("Le livre ne peut pas être null");

        validerLivre(livre);
        try {
            livreDAO.save(livre);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de l'ajout du livre: " + e.getMessage());
        }
    }

    public void modifierLivre(Livre livre) throws ValidationException {
        if (livre == null) throw new ValidationException("Le livre ne peut pas être null");

        validerLivre(livre);
        try {
            livreDAO.update(livre);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la modification du livre: " + e.getMessage());
        }
    }

    public void supprimerLivre(String isbn) throws ValidationException {
        if (isbn == null || isbn.trim().isEmpty()) throw new ValidationException("L'ISBN ne peut pas être vide");

        try {
            livreDAO.delete(isbn);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la suppression du livre: " + e.getMessage());
        }
    }

    public List<Livre> getTousLesLivres() {
        try {
            return livreDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les livres: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public List<Livre> rechercherLivres(String motCle) {
        if (motCle == null || motCle.trim().isEmpty()) {
            return getTousLesLivres();
        }

        try {
            List<Livre> resultats = livreDAO.findByTitre(motCle);
            if (resultats.isEmpty()) resultats = livreDAO.findByAuteur(motCle);
            return resultats;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public Livre getLivreByIsbn(String isbn) throws ValidationException {
        if (isbn == null || isbn.trim().isEmpty()) throw new ValidationException("L'ISBN ne peut pas être vide");

        try {
            Livre livre = livreDAO.findById(isbn);
            if (livre == null) throw new ValidationException("Aucun livre trouvé avec l'ISBN: " + isbn);
            return livre;
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la récupération du livre: " + e.getMessage());
        }
    }

    public List<Livre> rechercherParAuteur(String auteur) {
        try {
            return livreDAO.findByAuteur(auteur);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par auteur: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public List<Livre> rechercherParTitre(String titre) {
        try {
            return livreDAO.findByTitre(titre);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par titre: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public List<Livre> getLivresDisponibles() {
        try {
            return livreDAO.findDisponibles();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres disponibles: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public void emprunterLivre(String isbn) throws ValidationException {
        Livre livre = getLivreByIsbn(isbn);

        if (!livre.estDisponible()) {
            throw new ValidationException("Le livre '" + livre.getTitre() + "' n'est pas disponible");
        }

        livre.emprunter();
        modifierLivre(livre);
    }

    public void retournerLivre(String isbn) throws ValidationException {
        Livre livre = getLivreByIsbn(isbn);
        livre.retourner();
        modifierLivre(livre);
    }

    public Map<String, Object> obtenirStatistiques() {
        Map<String, Object> stats = new HashMap<>();
        try {
            List<Livre> tousLesLivres = livreDAO.findAll();
            List<Livre> disponibles = livreDAO.findDisponibles();

            stats.put("totalLivres", tousLesLivres.size());
            stats.put("livresDisponibles", disponibles.size());
            stats.put("livresEmpruntes", tousLesLivres.size() - disponibles.size());
            return stats;

        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul des statistiques: " + e.getMessage());
            return stats;
        }
    }

    private void validerLivre(Livre livre) throws ValidationException {
        if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty())
            throw new ValidationException("L'ISBN est obligatoire");

        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty())
            throw new ValidationException("Le titre est obligatoire");

        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty())
            throw new ValidationException("L'auteur est obligatoire");

        int anneeActuelle = Year.now().getValue();
        if (livre.getAnneePublication() < 1000 || livre.getAnneePublication() > anneeActuelle)
            throw new ValidationException("L'année de publication doit être entre 1000 et " + anneeActuelle);
    }

    // =========================================================
    // ===================== PARTIE MEMBRE =====================
    // =========================================================

    public void ajouterMembre(Membre m) throws ValidationException {
        if (m == null) throw new ValidationException("Le membre ne peut pas être null");
        validerMembre(m);

        try {
            membreDAO.save(m);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de l'ajout du membre: " + e.getMessage());
        }
    }

    public void modifierMembre(Membre m) throws ValidationException {
        if (m == null) throw new ValidationException("Le membre ne peut pas être null");
        if (m.getId() == null) throw new ValidationException("L'id du membre est obligatoire pour modifier");

        validerMembre(m);

        try {
            membreDAO.update(m);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la modification du membre: " + e.getMessage());
        }
    }

    public void supprimerMembre(Long id) throws ValidationException {
        if (id == null) throw new ValidationException("L'id ne peut pas être null");

        try {
            membreDAO.delete(id);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la suppression du membre: " + e.getMessage());
        }
    }

    public void activerMembre(Long id) throws ValidationException {
        if (id == null) throw new ValidationException("L'id ne peut pas être null");
        try {
            Membre m = membreDAO.findById(id).orElse(null);
            if (m != null) {
                m.setActif(true);
                membreDAO.update(m);
            }
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de l'activation: " + e.getMessage());
        }
    }

    public void desactiverMembre(Long id) throws ValidationException {
        if (id == null) throw new ValidationException("L'id ne peut pas être null");
        try {
            Membre m = membreDAO.findById(id).orElse(null);
            if (m != null) {
                m.setActif(false);
                membreDAO.update(m);
            }
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la désactivation: " + e.getMessage());
        }
    }

    public List<Membre> listerMembres() {
        try {
            List<Membre> membres = membreDAO.findAll();
            System.out.println("[BibliothequeService] Nombre de membres chargés: " + membres.size());
            return membres;
        } catch (SQLException e) {
            System.err.println("Erreur lors du listing membres: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    public List<Membre> rechercherMembres(String motCle) {
        if (motCle == null || motCle.trim().isEmpty()) return listerMembres();

        try {
            List<Membre> tous = membreDAO.findAll();
            List<Membre> resultats = new java.util.ArrayList<>();
            String lowerMotCle = motCle.toLowerCase();
            for (Membre m : tous) {
                if (m.getNom().toLowerCase().contains(lowerMotCle) || 
                    m.getPrenom().toLowerCase().contains(lowerMotCle)) {
                    resultats.add(m);
                }
            }
            return resultats;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche membres: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public List<String> historiqueEmprunts(Long membreId) {
        if (membreId == null) return Collections.emptyList();

        try {
            List<Emprunt> emprunts = empruntDAO.findByMemberId(membreId);
            return emprunts.stream()
                .map(e -> {
                    String livreInfo = "";
                    // Try to get book title from ISBN first
                    if (e.getIsbnLivre() != null && !e.getIsbnLivre().isEmpty()) {
                        try {
                            Livre livre = livreDAO.findById(e.getIsbnLivre());
                            if (livre != null) {
                                livreInfo = livre.getTitre() + " - ";
                            }
                        } catch (SQLException ignored) {
                            // If we can't get the book, continue without title
                        }
                    }
                    // If no ISBN but we have id_livre, show the ID
                    if (livreInfo.isEmpty() && e.getIdLivre() != null) {
                        livreInfo = "Livre ID: " + e.getIdLivre() + " - ";
                    }
                    
                    String dateInfo = e.getDateEmprunt() != null ? e.getDateEmprunt().toString() : "Date inconnue";
                    String retourInfo = "";
                    if (e.getDateRetourEffective() != null) {
                        retourInfo = " retourné le " + e.getDateRetourEffective().toString();
                    } else if (e.getDateRetour() != null) {
                        retourInfo = " retourné le " + e.getDateRetour().toString();
                    } else {
                        retourInfo = " (en cours)";
                    }
                    
                    // Add status if available
                    if (e.getStatut() != null && !e.getStatut().isEmpty()) {
                        retourInfo += " [" + e.getStatut() + "]";
                    }
                    
                    return livreInfo + "Emprunt du " + dateInfo + retourInfo;
                })
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'historique: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void validerMembre(Membre m) throws ValidationException {
        if (m.getNom() == null || m.getNom().trim().isEmpty())
            throw new ValidationException("Le nom est obligatoire");

        if (m.getPrenom() == null || m.getPrenom().trim().isEmpty())
            throw new ValidationException("Le prénom est obligatoire");

        if (m.getEmail() == null || m.getEmail().trim().isEmpty())
            throw new ValidationException("L'email est obligatoire");

        // (Option simple) contrôle basique email
        if (!m.getEmail().contains("@"))
            throw new ValidationException("Email invalide");
    }
}


package com.bibliotheque.service;

import com.bibliotheque.model.Livre;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.exception.ValidationException;
import java.sql.SQLException;
import java.time.Year;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Service métier pour la gestion des livres
 * Contient toute la logique métier (validation, orchestration, etc.)
 * Les appels à la base de données passent par le DAO
 */
public class BibliothequeService {
    private final LivreDAO livreDAO;

    /**
     * Constructeur avec injection du DAO
     */
    public BibliothequeService(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Constructeur par défaut utilisant LivreDAOImpl
     */
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
    }

    // ========== CRUD ==========

    /**
     * Ajoute un nouveau livre à la bibliothèque
     * 
     * @param livre Le livre à ajouter
     * @throws ValidationException si le livre est invalide
     */
    public void ajouterLivre(Livre livre) throws ValidationException {
        if (livre == null) {
            throw new ValidationException("Le livre ne peut pas être null");
        }

        validerLivre(livre);

        try {
            livreDAO.save(livre);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de l'ajout du livre: " + e.getMessage());
        }
    }

    /**
     * Modifie un livre existant
     * 
     * @param livre Le livre avec les nouvelles données
     * @throws ValidationException si le livre est invalide
     */
    public void modifierLivre(Livre livre) throws ValidationException {
        if (livre == null) {
            throw new ValidationException("Le livre ne peut pas être null");
        }

        validerLivre(livre);

        try {
            livreDAO.update(livre);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la modification du livre: " + e.getMessage());
        }
    }

    /**
     * Supprime un livre par son ISBN
     * 
     * @param isbn L'ISBN du livre à supprimer
     * @throws ValidationException si l'ISBN est invalide
     */
    public void supprimerLivre(String isbn) throws ValidationException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new ValidationException("L'ISBN ne peut pas être vide");
        }

        try {
            livreDAO.delete(isbn);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la suppression du livre: " + e.getMessage());
        }
    }

    // ========== RECHERCHES ==========

    /**
     * Récupère tous les livres
     * 
     * @return Liste de tous les livres
     */
    public List<Livre> getTousLesLivres() {
        try {
            return livreDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les livres: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Recherche des livres par titre ou auteur
     * 
     * @param motCle Le mot-clé à chercher
     * @return Liste des livres trouvés
     */
    public List<Livre> rechercherLivres(String motCle) {
        if (motCle == null || motCle.trim().isEmpty()) {
            return getTousLesLivres();
        }

        try {
            // Rechercher d'abord par titre
            List<Livre> resultats = livreDAO.findByTitre(motCle);

            // Si rien trouvé par titre, rechercher par auteur
            if (resultats.isEmpty()) {
                resultats = livreDAO.findByAuteur(motCle);
            }

            return resultats;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Recherche un livre par son ISBN
     * 
     * @param isbn L'ISBN du livre
     * @return Le livre trouvé, ou null si non trouvé
     * @throws ValidationException si l'ISBN est invalide
     */
    public Livre getLivreByIsbn(String isbn) throws ValidationException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new ValidationException("L'ISBN ne peut pas être vide");
        }

        try {
            Livre livre = livreDAO.findById(isbn);
            if (livre == null) {
                throw new ValidationException("Aucun livre trouvé avec l'ISBN: " + isbn);
            }
            return livre;
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la récupération du livre: " + e.getMessage());
        }
    }

    /**
     * Recherche des livres par auteur
     * 
     * @param auteur Le nom de l'auteur
     * @return Liste des livres de cet auteur
     */
    public List<Livre> rechercherParAuteur(String auteur) {
        try {
            return livreDAO.findByAuteur(auteur);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par auteur: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Recherche des livres par titre
     * 
     * @param titre Le titre à chercher
     * @return Liste des livres trouvés
     */
    public List<Livre> rechercherParTitre(String titre) {
        try {
            return livreDAO.findByTitre(titre);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par titre: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Récupère tous les livres disponibles
     * 
     * @return Liste des livres disponibles
     */
    public List<Livre> getLivresDisponibles() {
        try {
            return livreDAO.findDisponibles();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres disponibles: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    // ========== GESTION DES EMPRUNTS ==========

    /**
     * Emprunte un livre
     * 
     * @param isbn L'ISBN du livre à emprunter
     * @throws ValidationException si le livre n'est pas disponible
     */
    public void emprunterLivre(String isbn) throws ValidationException {
        Livre livre = getLivreByIsbn(isbn);

        if (!livre.estDisponible()) {
            throw new ValidationException("Le livre '" + livre.getTitre() + "' n'est pas disponible");
        }

        livre.emprunter();
        try {
            modifierLivre(livre);
        } catch (ValidationException e) {
            throw new ValidationException("Erreur lors de l'enregistrement de l'emprunt: " + e.getMessage());
        }
    }

    /**
     * Retourne un livre emprunté
     * 
     * @param isbn L'ISBN du livre à retourner
     * @throws ValidationException si une erreur se produit
     */
    public void retournerLivre(String isbn) throws ValidationException {
        Livre livre = getLivreByIsbn(isbn);
        livre.retourner();

        try {
            modifierLivre(livre);
        } catch (ValidationException e) {
            throw new ValidationException("Erreur lors de l'enregistrement du retour: " + e.getMessage());
        }
    }

    // ========== STATISTIQUES ==========

    /**
     * Récupère des statistiques sur les livres
     * 
     * @return Map contenant les statistiques
     */
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

    // ========== VALIDATION ==========

    /**
     * Valide les données d'un livre
     * 
     * @param livre Le livre à valider
     * @throws ValidationException si le livre est invalide
     */
    private void validerLivre(Livre livre) throws ValidationException {
        // Vérifier ISBN
        if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty()) {
            throw new ValidationException("L'ISBN est obligatoire");
        }

        // Vérifier Titre
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            throw new ValidationException("Le titre est obligatoire");
        }

        // Vérifier Auteur
        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
            throw new ValidationException("L'auteur est obligatoire");
        }

        // Vérifier Année de publication
        int anneeActuelle = Year.now().getValue();
        if (livre.getAnneePublication() < 1000 || livre.getAnneePublication() > anneeActuelle) {
            throw new ValidationException("L'année de publication doit être entre 1000 et " + anneeActuelle);
        }
    }
}

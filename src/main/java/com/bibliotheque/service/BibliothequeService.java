package com.bibliotheque.service;

<<<<<<< HEAD
public class BibliothequeService {
    
=======
<<<<<<< HEAD
import com.bibliotheque.model.Livre;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.LivreDAOImpl;
import com.bibliotheque.exception.ValidationException;
import java.sql.SQLException;
import java.util.List;

/**
 * Service métier pour la gestion des livres
 * Contient toute la logique métier et valide les données
 */
public class BibliothequeService implements BookService {
    private final LivreDAO livreDAO;

    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
    }

    public BibliothequeService(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Ajoute un nouveau livre à la bibliothèque
     */
    @Override
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
     */
    @Override
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

    /**
     * Recherche des livres par titre ou auteur
     */
    @Override
    public List<Livre> rechercherLivres(String motCle) {
        if (motCle == null || motCle.trim().isEmpty()) {
            return getTousLesLivres();
        }

        try {
            // Rechercher d'abord par titre, puis par auteur
            List<Livre> resultats = livreDAO.findByTitre(motCle);
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
     * Récupère les livres disponibles
     */
    public List<Livre> getLivresDisponibles() throws ValidationException {
        try {
            return livreDAO.findDisponibles();
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la récupération des livres disponibles: " + e.getMessage());
        }
    }

    /**
     * Récupère tous les livres
     */
    @Override
    public List<Livre> getTousLesLivres() {
        try {
            return livreDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les livres: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Récupère un livre par son ISBN
     */
    public Livre getLivreById(String isbn) throws ValidationException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new ValidationException("L'ISBN ne peut pas être vide");
        }

        try {
            Livre livre = livreDAO.findById(isbn);
            if (livre == null) {
                throw new ValidationException("Aucun livre trouvé avec cet ISBN: " + isbn);
            }
            return livre;
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la récupération du livre: " + e.getMessage());
        }
    }

    /**
     * Valide les données d'un livre
     */
    private void validerLivre(Livre livre) throws ValidationException {
        if (livre.getId() == null || livre.getId().trim().isEmpty()) {
            throw new ValidationException("L'ISBN est obligatoire");
        }
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            throw new ValidationException("Le titre est obligatoire");
        }
        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
            throw new ValidationException("L'auteur est obligatoire");
        }
        if (livre.getAnneePublication() < 0 || livre.getAnneePublication() > java.time.Year.now().getValue()) {
            throw new ValidationException("L'année de publication est invalide");
        }
    }
=======
import com.bibliotheque.model.Book;
import java.util.List;

/**
 * Service facade for library operations used by controllers.
 * Business rules live in implementations; controllers call this service only.
 */
public interface BibliothequeService {
    /**
     * Return list of books (Livres) from persistence.
     */
    List<Book> listLivres();
>>>>>>> e014484e0ecce728e18711c7d7edda1ec5b547bb
>>>>>>> c75e8a4d70968152889cc97ac35171ab07e01860
}

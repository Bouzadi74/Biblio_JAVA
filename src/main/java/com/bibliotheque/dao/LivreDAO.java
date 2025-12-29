package com.bibliotheque.dao;

import com.bibliotheque.model.Livre;
import java.util.List;
import java.sql.SQLException;

/**
 * Interface DAO pour la gestion des livres
 * Définit le contrat pour toutes les opérations sur les livres
 */
public interface LivreDAO {
    /**
     * Ajoute un nouveau livre à la base de données
     * 
     * @param livre Le livre à ajouter
     * @throws SQLException en cas d'erreur BD
     */
    void save(Livre livre) throws SQLException;

    /**
     * Récupère un livre par son ISBN
     * 
     * @param isbn L'ISBN du livre
     * @return Le livre trouvé, ou null si non trouvé
     * @throws SQLException en cas d'erreur BD
     */
    Livre findById(String isbn) throws SQLException;

    /**
     * Récupère tous les livres
     * 
     * @return Liste de tous les livres
     * @throws SQLException en cas d'erreur BD
     */
    List<Livre> findAll() throws SQLException;

    /**
     * Met à jour les informations d'un livre
     * 
     * @param livre Le livre avec les données mises à jour
     * @throws SQLException en cas d'erreur BD
     */
    void update(Livre livre) throws SQLException;

    /**
     * Supprime un livre par son ISBN
     * 
     * @param isbn L'ISBN du livre à supprimer
     * @throws SQLException en cas d'erreur BD
     */
    void delete(String isbn) throws SQLException;

    /**
     * Recherche des livres par auteur
     * 
     * @param auteur L'auteur à chercher
     * @return Liste des livres correspondants
     * @throws SQLException en cas d'erreur BD
     */
    List<Livre> findByAuteur(String auteur) throws SQLException;

    /**
     * Recherche des livres par titre
     * 
     * @param titre Le titre à chercher (recherche partielle)
     * @return Liste des livres correspondants
     * @throws SQLException en cas d'erreur BD
     */
    List<Livre> findByTitre(String titre) throws SQLException;

    /**
     * Récupère tous les livres disponibles
     * 
     * @return Liste des livres disponibles
     * @throws SQLException en cas d'erreur BD
     */
    List<Livre> findDisponibles() throws SQLException;
}

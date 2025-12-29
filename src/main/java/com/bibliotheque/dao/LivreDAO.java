package com.bibliotheque.dao;

import java.sql.SQLException;
import java.util.List;

import com.bibliotheque.model.Livre;

/**
 * Interface DAO pour la gestion des livres
 */
public interface LivreDAO {
    void save(Livre livre) throws SQLException;

    Livre findById(String isbn) throws SQLException;

    List<Livre> findAll() throws SQLException;

    void update(Livre livre) throws SQLException;

    void delete(String isbn) throws SQLException;

    // Méthodes spécifiques
    List<Livre> findByAuteur(String auteur) throws SQLException;

    List<Livre> findByTitre(String titre) throws SQLException;

    List<Livre> findDisponibles() throws SQLException;
}

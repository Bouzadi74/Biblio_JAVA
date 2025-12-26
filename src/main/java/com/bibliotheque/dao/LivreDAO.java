package com.bibliotheque.dao;

<<<<<<< HEAD
import com.bibliotheque.model.Livre;
import java.sql.SQLException;
import java.util.List;

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
=======
import com.bibliotheque.model.Book;
import java.util.List;

/**
 * DAO interface for LIVRE table. CRUD signatures only.
 */
public interface LivreDAO {
    List<Book> findAll();
    java.util.Optional<Book> findById(Integer id);
    void insert(Book book);
    void update(Book book);
    void delete(Integer id);
>>>>>>> e014484e0ecce728e18711c7d7edda1ec5b547bb
}

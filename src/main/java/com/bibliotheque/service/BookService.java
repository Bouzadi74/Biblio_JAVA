package com.bibliotheque.service;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Book;
import java.sql.SQLException;
import java.util.List;

public interface BookService {
    void ajouterLivre(Book livre) throws ValidationException;

    void supprimerLivre(int livreId) throws SQLException;

    void modifierLivre(Book livre) throws SQLException;

    Book getLivreById(int livreId) throws SQLException;

    List<Book> getTousLesLivres() throws SQLException;

    List<Book> rechercherLivres(String motCle) throws SQLException;
}

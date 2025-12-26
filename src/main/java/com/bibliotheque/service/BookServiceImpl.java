package com.bibliotheque.service;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Book;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {

    @Override
    public void ajouterLivre(Book livre) throws ValidationException {
        if (livre == null)
            throw new ValidationException("Livre is null");
        if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty())
            throw new ValidationException("ISBN is required");
        if (livre.getTitle() == null || livre.getTitle().trim().isEmpty())
            throw new ValidationException("Title is required");
        if (livre.getTotalCopies() < 0)
            throw new ValidationException("Copies must be >= 0");

        // TODO: delegate to DAO for persistence. This is a minimal skeleton.
        System.out.println("[LivreService] Persisting livre: " + livre.getTitle());
    }

    @Override
    public void supprimerLivre(int livreId) throws SQLException {
        // TODO: delegate to DAO for deletion
        System.out.println("[LivreService] Deleting livre with ID: " + livreId);
    }

    @Override
    public void modifierLivre(Book livre) throws SQLException {
        // TODO: delegate to DAO for update
        System.out.println("[LivreService] Updating livre: " + livre.getTitle());
    }

    @Override
    public Book getLivreById(int livreId) throws SQLException {
        // TODO: delegate to DAO to retrieve livre by ID
        return null;
    }

    @Override
    public List<Book> getTousLesLivres() throws SQLException {
        // TODO: delegate to DAO to retrieve all livres
        return new ArrayList<>();
    }

    @Override
    public List<Book> rechercherLivres(String motCle) throws SQLException {
        // TODO: delegate to DAO to search livres by keyword
        return new ArrayList<>();
    }
}

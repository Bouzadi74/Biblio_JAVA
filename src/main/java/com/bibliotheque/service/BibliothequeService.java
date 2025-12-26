package com.bibliotheque.service;

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
}

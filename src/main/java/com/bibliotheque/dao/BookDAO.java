package com.bibliotheque.dao;

import com.bibliotheque.model.Book;
import java.util.List;

/**
 * DAO interface for Book entity (CRUD only)
 */
public interface BookDAO extends GenericDAO<Book, Integer> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
}

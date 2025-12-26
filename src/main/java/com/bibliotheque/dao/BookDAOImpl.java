package com.bibliotheque.dao;

import com.bibliotheque.model.Book;
import java.util.List;

/**
 * Concrete BookDAO implementation (persistence code omitted).
 * Contains only method signatures required by the contract.
 */
public class BookDAOImpl implements BookDAO {
    public BookDAOImpl(com.bibliotheque.infra.DatabaseConnection db) {}

    public List<Book> findAll() { return java.util.Collections.emptyList(); }
    public java.util.Optional<Book> findById(Integer id) { return java.util.Optional.empty(); }
    public void insert(Book entity) {}
    public void update(Book entity) {}
    public void delete(Integer id) {}
    public List<Book> findByTitle(String title) { return java.util.Collections.emptyList(); }
    public List<Book> findByAuthor(String author) { return java.util.Collections.emptyList(); }
}

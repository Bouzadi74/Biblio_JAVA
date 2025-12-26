package com.bibliotheque.dao;

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
}

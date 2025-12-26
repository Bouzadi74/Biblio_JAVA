package com.bibliotheque.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD DAO contract. Implementations perform persistence only.
 */
public interface GenericDAO<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    void insert(T entity);
    void update(T entity);
    void delete(ID id);
}

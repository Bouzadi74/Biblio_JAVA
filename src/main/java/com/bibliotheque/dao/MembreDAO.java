package com.bibliotheque.dao;

import com.bibliotheque.model.Membre;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

public interface MembreDAO {

    Membre save(Membre membre) throws SQLException;

    Optional<Membre> findById(Long id) throws SQLException;

    Optional<Membre> findByEmail(String email) throws SQLException;

    List<Membre> findAll() throws SQLException;

    List<Membre> findActifs() throws SQLException;

    Membre update(Membre membre) throws SQLException;

    boolean delete(Long id) throws SQLException;
}

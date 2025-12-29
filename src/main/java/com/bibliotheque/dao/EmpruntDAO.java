package com.bibliotheque.dao;

import java.sql.SQLException;
import java.util.List;

import com.bibliotheque.model.Emprunt;

/**
 * DAO interface for Loan entity
 */
public interface EmpruntDAO {
    void save(Emprunt e) throws SQLException;
    Emprunt findById(int id) throws SQLException;
    List<Emprunt> findAll() throws SQLException;
    void update(Emprunt e) throws SQLException;
    void delete(int id) throws SQLException;

    List<Emprunt> findByMembre(int idMembre) throws SQLException;
    List<Emprunt> findEnCours() throws SQLException;
    int countEmpruntsEnCours(int idMembre) throws SQLException;

    List<Emprunt> findByMemberId(Integer memberId) throws SQLException;
    List<Emprunt> findActiveByBookId(Integer bookId) throws SQLException;
}

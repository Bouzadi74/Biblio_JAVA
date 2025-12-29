package com.bibliotheque.dao;

import java.sql.SQLException;
import java.util.List;

import com.bibliotheque.model.Emprunt;

<<<<<<< HEAD
/**
 * DAO interface for Loan entity
 */
public interface EmpruntDAO extends GenericDAO<Emprunt, Integer> {
    List<Emprunt> findByMemberId(Integer memberId);
    List<Emprunt> findActiveByBookId(Integer bookId);
}
// this is a comment just for the test ,what do you
=======
public interface EmpruntDAO {

    void save(Emprunt emprunt) throws SQLException;

    Emprunt findById(int id) throws SQLException;

    List<Emprunt> findAll() throws SQLException;

    void update(Emprunt emprunt) throws SQLException;

    List<Emprunt> findByMembre(int membreId) throws SQLException;

    List<Emprunt> findEnCours() throws SQLException;

    int countEmpruntsEnCours(int membreId) throws SQLException;
}

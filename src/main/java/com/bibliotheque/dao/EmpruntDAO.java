package com.bibliotheque.dao;

import java.sql.SQLException;
import java.util.List;

import com.bibliotheque.model.Emprunt;

public interface EmpruntDAO {

    void save(Emprunt emprunt) throws SQLException;

    Emprunt findById(int id) throws SQLException;

    List<Emprunt> findAll() throws SQLException;

    void update(Emprunt emprunt) throws SQLException;

    List<Emprunt> findByMembre(int membreId) throws SQLException;

    List<Emprunt> findEnCours() throws SQLException;

    int countEmpruntsEnCours(int membreId) throws SQLException;
}

package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.model.Emprunt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAOImpl implements EmpruntDAO {

    private Connection connection;

    public EmpruntDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Emprunt e) throws SQLException {
        String sql = "INSERT INTO emprunts (id_membre, id_livre, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, e.getIdMembre());
        ps.setInt(2, e.getIdLivre());
        ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
        ps.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
        ps.executeUpdate();
    }

    @Override
    public Emprunt findById(int id) throws SQLException {
        String sql = "SELECT * FROM emprunts WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return map(rs);
        }
        return null;
    }

    @Override
    public List<Emprunt> findAll() throws SQLException {
        String sql = "SELECT * FROM emprunts";
        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        List<Emprunt> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public void update(Emprunt e) throws SQLException {
        String sql = "UPDATE emprunts SET id_membre=?, id_livre=?, date_emprunt=?, date_retour=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, e.getIdMembre());
        ps.setInt(2, e.getIdLivre());
        ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
        ps.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
        ps.setInt(5, e.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Emprunt> findByMembre(int idMembre) throws SQLException {
        String sql = "SELECT * FROM emprunts WHERE id_membre=? AND date_retour IS NULL";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idMembre);

        ResultSet rs = ps.executeQuery();
        List<Emprunt> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public List<Emprunt> findEnCours() throws SQLException {
        String sql = "SELECT * FROM emprunts WHERE date_retour IS NULL";
        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        List<Emprunt> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    @Override
    public int countEmpruntsEnCours(int idMembre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour IS NULL";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idMembre);

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private Emprunt map(ResultSet rs) throws SQLException {
        Emprunt e = new Emprunt();
        e.setId(rs.getInt("id"));
        e.setIdMembre(rs.getInt("id_membre"));
        e.setIdLivre(rs.getInt("id_livre"));
        e.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
        if (rs.getDate("date_retour") != null)
            e.setDateRetour(rs.getDate("date_retour").toLocalDate());
        return e;
    }
}

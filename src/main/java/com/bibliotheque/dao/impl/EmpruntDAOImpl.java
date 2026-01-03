package com.bibliotheque.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.model.Emprunt;

public class EmpruntDAOImpl implements EmpruntDAO {

    private final Connection connection;

    public EmpruntDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Emprunt e) {
        String sql = "INSERT INTO emprunts (id_membre, id_livre, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, e.getIdMembre());
            ps.setInt(2, e.getIdLivre());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    public Emprunt findById(Integer id) {
        String sql = "SELECT * FROM emprunts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Emprunt> findAll() {
        String sql = "SELECT * FROM emprunts";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Emprunt> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Emprunt> findByMemberId(Integer memberId) {
        String sql = "SELECT * FROM emprunts WHERE id_membre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Emprunt> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Emprunt> findActiveByBookId(Integer bookId) {
        String sql = "SELECT * FROM emprunts WHERE id_livre = ? AND date_retour IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Emprunt> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(Emprunt e) {
        String sql = "UPDATE emprunts SET id_membre=?, id_livre=?, date_emprunt=?, date_retour=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, e.getIdMembre());
            ps.setInt(2, e.getIdLivre());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM emprunts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Legacy / helper methods kept (not part of the current GenericDAO/EmpruntDAO contract)
    public List<Emprunt> findByMembre(int idMembre) {
        String sql = "SELECT * FROM emprunts WHERE id_membre=? AND date_retour IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMembre);
            try (ResultSet rs = ps.executeQuery()) {
                List<Emprunt> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Emprunt> findEnCours() {
        String sql = "SELECT * FROM emprunts WHERE date_retour IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Emprunt> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int countEmpruntsEnCours(int idMembre) {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idMembre);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Emprunt map(ResultSet rs) throws SQLException {
        Emprunt e = new Emprunt();
        e.setId(rs.getInt("id"));
        e.setIdMembre(rs.getInt("id_membre"));
        e.setIdLivre(rs.getInt("id_livre"));
        Date dEmprunt = rs.getDate("date_emprunt");
        if (dEmprunt != null) e.setDateEmprunt(dEmprunt.toLocalDate());
        Date dRetour = rs.getDate("date_retour");
        if (dRetour != null) e.setDateRetour(dRetour.toLocalDate());
        return e;
    }
}

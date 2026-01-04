package com.bibliotheque.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.model.Emprunt;

public class EmpruntDAOImpl implements EmpruntDAO {

    private final Connection connection;

    public EmpruntDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Emprunt e) {
        // Insert uses schema columns: id_membre, id_livre, date_emprunt, date_retour_prevue, statut
        String sql = "INSERT INTO emprunts (id_membre, id_livre, date_emprunt, date_retour_prevue, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, e.getIdMembre());
            ps.setInt(2, e.getIdLivre());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetourPrevue() == null ? null : Date.valueOf(e.getDateRetourPrevue()));
            ps.setString(5, e.getStatut());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // EmpruntDAO defines `save` as well in some variants; expose it to be safe
    public void save(Emprunt e) {
        insert(e);
    }


    @Override
    public Optional<Emprunt> findById(Integer id) {
        // Primary key column is `id_emprunt`
        String sql = "SELECT * FROM emprunts WHERE id_emprunt = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
            return Optional.empty();
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
        // Active when date_retour_effective IS NULL
        String sql = "SELECT * FROM emprunts WHERE id_livre = ? AND date_retour_effective IS NULL";
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
        // Update uses explicit schema columns including planned/effective return dates and statut
        String sql = "UPDATE emprunts SET id_membre=?, id_livre=?, date_emprunt=?, date_retour_prevue=?, date_retour_effective=?, statut=? WHERE id_emprunt=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, e.getIdMembre());
            ps.setInt(2, e.getIdLivre());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetourPrevue() == null ? null : Date.valueOf(e.getDateRetourPrevue()));
            ps.setDate(5, e.getDateRetourEffective() == null ? null : Date.valueOf(e.getDateRetourEffective()));
            ps.setString(6, e.getStatut());
            ps.setInt(7, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM emprunts WHERE id_emprunt = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    public List<Emprunt> findEnCours() {
        // Emprunts en cours: date_retour_effective IS NULL and statut = 'EN_COURS'
        String sql = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL";
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
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour_effective IS NULL";
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
        // map id_emprunt -> id
        e.setId(rs.getInt("id_emprunt"));
        e.setIdMembre(rs.getInt("id_membre"));
        // id_livre stored as foreign key referencing livres.isbn -> may be stored as VARCHAR in DB;
        // keep compatibility: attempt getInt then fallback to parsing string if needed
        try {
            e.setIdLivre(rs.getInt("id_livre"));
        } catch (SQLException ex) {
            String s = rs.getString("id_livre");
            if (s != null) {
                try { e.setIdLivre(Integer.parseInt(s)); } catch (NumberFormatException ignore) {}
            }
        }
        Date dEmprunt = rs.getDate("date_emprunt");
        if (dEmprunt != null) e.setDateEmprunt(dEmprunt.toLocalDate());
        Date dRetourPrevue = rs.getDate("date_retour_prevue");
        if (dRetourPrevue != null) e.setDateRetourPrevue(dRetourPrevue.toLocalDate());
        Date dRetourEffect = rs.getDate("date_retour_effective");
        if (dRetourEffect != null) e.setDateRetourEffective(dRetourEffect.toLocalDate());
        String statut = rs.getString("statut");
        if (statut != null) e.setStatut(statut);
        return e;
    }
}

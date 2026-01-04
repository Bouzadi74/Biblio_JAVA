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
<<<<<<< HEAD
        // Prefer new schema (date_retour_prevue, statut) but fall back to legacy (date_retour) for tests
        String sqlNew = "INSERT INTO emprunts (id_membre, id_livre, date_emprunt, date_retour_prevue, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setInt(1, e.getIdMembre());
            ps.setInt(2, e.getIdLivre());
=======
        // Insert uses schema columns: id_membre, isbn_livre, date_emprunt, date_retour_prevue, statut
        String sql = "INSERT INTO emprunts (id_membre, isbn_livre, date_emprunt, date_retour_prevue, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, e.getIdMembre());
            ps.setString(2, e.getIsbnLivre());
>>>>>>> 2a57626cdf38e7cc9a1afb11539b1b4a50a29e6f
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetourPrevue() == null ? null : Date.valueOf(e.getDateRetourPrevue()));
            ps.setString(5, e.getStatut());
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            // fallback to legacy schema (date_retour)
            String sqlOld = "INSERT INTO emprunts (id_membre, id_livre, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
                ps2.setInt(1, e.getIdMembre());
                ps2.setInt(2, e.getIdLivre());
                ps2.setDate(3, Date.valueOf(e.getDateEmprunt()));
                ps2.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
                ps2.executeUpdate();
                return;
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    // EmpruntDAO defines `save` as well in some variants; expose it to be safe
    public void save(Emprunt e) {
        insert(e);
    }


    @Override
<<<<<<< HEAD
    public Optional<Emprunt> findById(Integer id) {
        // Try new primary key column `id_emprunt`, fallback to legacy `id`
        String sqlNew = "SELECT * FROM emprunts WHERE id_emprunt = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setInt(1, id);
=======
    public Optional<Emprunt> findById(Long id) {
        // Primary key column is `id`
        String sql = "SELECT * FROM emprunts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
>>>>>>> 2a57626cdf38e7cc9a1afb11539b1b4a50a29e6f
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
            return Optional.empty();
        } catch (SQLException ex) {
            try {
                String sqlOld = "SELECT * FROM emprunts WHERE id = ?";
                try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
                    ps2.setInt(1, id);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        if (rs2.next()) return Optional.of(map(rs2));
                    }
                }
                return Optional.empty();
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
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
    public List<Emprunt> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM emprunts WHERE id_membre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, memberId);
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
<<<<<<< HEAD
    public List<Emprunt> findActiveByBookId(Integer bookId) {
        // Prefer new column date_retour_effective; fallback to legacy date_retour IS NULL
        String sqlNew = "SELECT * FROM emprunts WHERE id_livre = ? AND date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setInt(1, bookId);
=======
    public List<Emprunt> findActiveByBookIsbn(String isbn) {
        // Active when date_retour_effective IS NULL
        String sql = "SELECT * FROM emprunts WHERE isbn_livre = ? AND date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, isbn);
>>>>>>> 2a57626cdf38e7cc9a1afb11539b1b4a50a29e6f
            try (ResultSet rs = ps.executeQuery()) {
                List<Emprunt> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            try {
                String sqlOld = "SELECT * FROM emprunts WHERE id_livre = ? AND date_retour IS NULL";
                try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
                    ps2.setInt(1, bookId);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        List<Emprunt> list = new ArrayList<>();
                        while (rs2.next()) list.add(map(rs2));
                        return list;
                    }
                }
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    @Override
    public void update(Emprunt e) {
<<<<<<< HEAD
        // Try update with new schema, fall back to legacy update
        String sqlNew = "UPDATE emprunts SET id_membre=?, id_livre=?, date_emprunt=?, date_retour_prevue=?, date_retour_effective=?, statut=? WHERE id_emprunt=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setInt(1, e.getIdMembre());
            ps.setInt(2, e.getIdLivre());
=======
        // Update uses explicit schema columns including planned/effective return dates and statut
        String sql = "UPDATE emprunts SET id_membre=?, isbn_livre=?, date_emprunt=?, date_retour_prevue=?, date_retour_effective=?, statut=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, e.getIdMembre());
            ps.setString(2, e.getIsbnLivre());
>>>>>>> 2a57626cdf38e7cc9a1afb11539b1b4a50a29e6f
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetourPrevue() == null ? null : Date.valueOf(e.getDateRetourPrevue()));
            ps.setDate(5, e.getDateRetourEffective() == null ? null : Date.valueOf(e.getDateRetourEffective()));
            ps.setString(6, e.getStatut());
            ps.setLong(7, e.getId());
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            try {
                String sqlOld = "UPDATE emprunts SET id_membre=?, id_livre=?, date_emprunt=?, date_retour=? WHERE id=?";
                try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
                    ps2.setInt(1, e.getIdMembre());
                    ps2.setInt(2, e.getIdLivre());
                    ps2.setDate(3, Date.valueOf(e.getDateEmprunt()));
                    ps2.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
                    ps2.setInt(5, e.getId());
                    ps2.executeUpdate();
                    return;
                }
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }


    @Override
<<<<<<< HEAD
    public void delete(Integer id) {
        String sqlNew = "DELETE FROM emprunts WHERE id_emprunt = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setInt(1, id);
=======
    public void delete(Long id) {
        String sql = "DELETE FROM emprunts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
>>>>>>> 2a57626cdf38e7cc9a1afb11539b1b4a50a29e6f
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            try {
                String sqlOld = "DELETE FROM emprunts WHERE id = ?";
                try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
                    ps2.setInt(1, id);
                    ps2.executeUpdate();
                    return;
                }
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }


    public List<Emprunt> findEnCours() {
        String sqlNew = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew);
             ResultSet rs = ps.executeQuery()) {
            List<Emprunt> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException ex) {
            try {
                String sqlOld = "SELECT * FROM emprunts WHERE date_retour IS NULL";
                try (PreparedStatement ps2 = connection.prepareStatement(sqlOld);
                     ResultSet rs2 = ps2.executeQuery()) {
                    List<Emprunt> list = new ArrayList<>();
                    while (rs2.next()) list.add(map(rs2));
                    return list;
                }
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

<<<<<<< HEAD
    public int countEmpruntsEnCours(int idMembre) {
        String sqlNew = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setInt(1, idMembre);
=======
    public int countEmpruntsEnCours(long idMembre) {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idMembre);
>>>>>>> 2a57626cdf38e7cc9a1afb11539b1b4a50a29e6f
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            try {
                String sqlOld = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour IS NULL";
                try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
                    ps2.setInt(1, idMembre);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        rs2.next();
                        return rs2.getInt(1);
                    }
                }
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    private Emprunt map(ResultSet rs) throws SQLException {
        Emprunt e = new Emprunt();
<<<<<<< HEAD
        // id: try id_emprunt then fallback to id
        try {
            e.setId(rs.getInt("id_emprunt"));
        } catch (SQLException ex) {
            e.setId(rs.getInt("id"));
        }
        e.setIdMembre(rs.getInt("id_membre"));
        // id_livre as int or string
        try {
            e.setIdLivre(rs.getInt("id_livre"));
        } catch (SQLException ex) {
            String s = rs.getString("id_livre");
            if (s != null) {
                try { e.setIdLivre(Integer.parseInt(s)); } catch (NumberFormatException ignore) {}
            }
        }
=======
        // map id -> id
        e.setId(rs.getLong("id"));
        e.setIdMembre(rs.getLong("id_membre"));
        // isbn_livre stored as VARCHAR
        e.setIsbnLivre(rs.getString("isbn_livre"));
>>>>>>> 2a57626cdf38e7cc9a1afb11539b1b4a50a29e6f
        Date dEmprunt = rs.getDate("date_emprunt");
        if (dEmprunt != null) e.setDateEmprunt(dEmprunt.toLocalDate());

        // try both return date column names
        try {
            Date dRetourPrevue = rs.getDate("date_retour_prevue");
            if (dRetourPrevue != null) e.setDateRetourPrevue(dRetourPrevue.toLocalDate());
        } catch (SQLException ignored) { }

        try {
            Date dRetourEffect = rs.getDate("date_retour_effective");
            if (dRetourEffect != null) e.setDateRetourEffective(dRetourEffect.toLocalDate());
        } catch (SQLException ignored) { }

        // legacy single return column
        try {
            Date dRetourLegacy = rs.getDate("date_retour");
            if (dRetourLegacy != null) e.setDateRetour(dRetourLegacy.toLocalDate());
        } catch (SQLException ignored) { }

        try {
            String statut = rs.getString("statut");
            if (statut != null) e.setStatut(statut);
        } catch (SQLException ignored) { }

        return e;
    }
}

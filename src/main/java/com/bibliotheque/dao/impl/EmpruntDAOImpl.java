package com.bibliotheque.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;
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
        // Try new schema first (isbn + date_retour_prevue), fall back to legacy numeric id_livre
        String sqlNew = "INSERT INTO emprunts (id_membre, isbn_livre, date_emprunt, date_retour_prevue, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setObject(1, e.getIdMembre());
            ps.setString(2, e.getIsbnLivre());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetourPrevue() == null ? null : Date.valueOf(e.getDateRetourPrevue()));
            ps.setString(5, e.getStatut());
            ps.executeUpdate();
            return;
        } catch (SQLException ignored) {
            // fallback to legacy schema
        }

        String sqlOld = "INSERT INTO emprunts (id_membre, id_livre, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
            if (e.getIdMembre() != null) ps2.setLong(1, e.getIdMembre()); else ps2.setNull(1, Types.BIGINT);
            if (e.getIdLivre() != null) ps2.setInt(2, e.getIdLivre()); else ps2.setNull(2, Types.INTEGER);
            ps2.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps2.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
            ps2.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(Emprunt e) {
        insert(e);
    }

    @Override
    public Optional<Emprunt> findById(Long id) {
        // try common primary key `id` first, then `id_emprunt`
        String sql = "SELECT * FROM emprunts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException ignored) {}

        String sqlAlt = "SELECT * FROM emprunts WHERE id_emprunt = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlAlt)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return Optional.empty();
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
    public void update(Emprunt e) {
        // try new schema update
        String sqlNew = "UPDATE emprunts SET id_membre=?, isbn_livre=?, date_emprunt=?, date_retour_prevue=?, date_retour_effective=?, statut=? WHERE id_emprunt=?";
        try (PreparedStatement ps = connection.prepareStatement(sqlNew)) {
            ps.setObject(1, e.getIdMembre());
            ps.setString(2, e.getIsbnLivre());
            ps.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps.setDate(4, e.getDateRetourPrevue() == null ? null : Date.valueOf(e.getDateRetourPrevue()));
            ps.setDate(5, e.getDateRetourEffective() == null ? null : Date.valueOf(e.getDateRetourEffective()));
            ps.setString(6, e.getStatut());
            ps.setObject(7, e.getId());
            ps.executeUpdate();
            return;
        } catch (SQLException ignored) {}

        // fallback legacy
        String sqlOld = "UPDATE emprunts SET id_membre=?, id_livre=?, date_emprunt=?, date_retour=? WHERE id=?";
        try (PreparedStatement ps2 = connection.prepareStatement(sqlOld)) {
            if (e.getIdMembre() != null) ps2.setLong(1, e.getIdMembre()); else ps2.setNull(1, Types.BIGINT);
            if (e.getIdLivre() != null) ps2.setInt(2, e.getIdLivre()); else ps2.setNull(2, Types.INTEGER);
            ps2.setDate(3, Date.valueOf(e.getDateEmprunt()));
            ps2.setDate(4, e.getDateRetour() == null ? null : Date.valueOf(e.getDateRetour()));
            ps2.setObject(5, e.getId());
            ps2.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM emprunts WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            return;
        } catch (SQLException ignored) {}

        String sqlAlt = "DELETE FROM emprunts WHERE id_emprunt = ?";
        try (PreparedStatement ps2 = connection.prepareStatement(sqlAlt)) {
            ps2.setLong(1, id);
            ps2.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Emprunt> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM emprunts WHERE id_membre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Emprunt> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // compatibility overload
    public List<Emprunt> findByMemberId(long memberId) {
        return findByMemberId(Long.valueOf(memberId));
    }

    @Override
    public List<Emprunt> findActiveByBookIsbn(String isbn) {
        String sql = "SELECT * FROM emprunts WHERE isbn_livre = ? AND date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                List<Emprunt> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // compatibility for numeric book id used in tests
    public List<Emprunt> findActiveByBookId(int bookId) {
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
    public List<Emprunt> findEnCours() {
        String sql = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Emprunt> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException ex) {
            // fallback to legacy
            try (PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM emprunts WHERE date_retour IS NULL");
                 ResultSet rs2 = ps2.executeQuery()) {
                List<Emprunt> list = new ArrayList<>();
                while (rs2.next()) list.add(map(rs2));
                return list;
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    @Override
    public int countEmpruntsEnCours(long idMembre) {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour_effective IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idMembre);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            // fallback
            try (PreparedStatement ps2 = connection.prepareStatement("SELECT COUNT(*) FROM emprunts WHERE id_membre=? AND date_retour IS NULL")) {
                ps2.setLong(1, idMembre);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    rs2.next();
                    return rs2.getInt(1);
                }
            } catch (SQLException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    private Emprunt map(ResultSet rs) throws SQLException {
        Emprunt e = new Emprunt();
        // id
        try { e.setId(rs.getLong("id")); } catch (SQLException ignored) { }
        try { e.setId(rs.getLong("id_emprunt")); } catch (SQLException ignored) { }

        // member id
        try { long m = rs.getLong("id_membre"); if (!rs.wasNull()) e.setIdMembre(m); } catch (SQLException ignored) { }

        // numeric book id
        try { int il = rs.getInt("id_livre"); if (!rs.wasNull()) e.setIdLivre(il); } catch (SQLException ignored) { }

        // isbn
        try { String isbn = rs.getString("isbn_livre"); if (isbn != null) e.setIsbnLivre(isbn); } catch (SQLException ignored) { }

        // dates
        try { Date d = rs.getDate("date_emprunt"); if (d != null) e.setDateEmprunt(d.toLocalDate()); } catch (SQLException ignored) { }
        try { Date d = rs.getDate("date_retour_prevue"); if (d != null) e.setDateRetourPrevue(d.toLocalDate()); } catch (SQLException ignored) { }
        try { Date d = rs.getDate("date_retour_effective"); if (d != null) e.setDateRetourEffective(d.toLocalDate()); } catch (SQLException ignored) { }
        try { Date d = rs.getDate("date_retour"); if (d != null) e.setDateRetour(d.toLocalDate()); } catch (SQLException ignored) { }

        try { String s = rs.getString("statut"); if (s != null) e.setStatut(s); } catch (SQLException ignored) { }

        return e;
    }
}

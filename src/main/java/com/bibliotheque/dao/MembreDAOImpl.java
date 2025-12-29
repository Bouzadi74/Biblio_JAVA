package com.bibliotheque.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bibliotheque.infra.DatabaseConnection;
import com.bibliotheque.model.Membre;

/**
 * Implémentation du DAO pour Membre utilisant JDBC
 */
public class MembreDAOImpl implements MembreDAO {

    @Override
    public Membre save(Membre membre) throws SQLException {
        String sql = "INSERT INTO membres (nom, prenom, email, actif, telephone, adresse) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            stmt.setString(5, membre.getTelephone());
            stmt.setString(6, membre.getAdresse());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    membre.setId(rs.getLong(1));
                }
            }
            System.out.println("[MembreDAO] Membre inséré: " + membre.getNom());
            return membre;
        }
    }

    @Override
    public Optional<Membre> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM membres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMembre(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Membre> findByEmail(String email) throws SQLException {
        if (email == null) return Optional.empty();
        String e = email.trim().toLowerCase();
        String sql = "SELECT * FROM membres WHERE LOWER(email) = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMembre(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Membre> findAll() throws SQLException {
        String sql = "SELECT * FROM membres";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            List<Membre> membres = new ArrayList<>();
            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
            return membres;
        }
    }

    @Override
    public List<Membre> findActifs() throws SQLException {
        String sql = "SELECT * FROM membres WHERE actif = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            List<Membre> membres = new ArrayList<>();
            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
            return membres;
        }
    }

    @Override
    public Membre update(Membre membre) throws SQLException {
        if (membre.getId() == null) {
            throw new SQLException("Impossible de modifier: id null");
        }
        String sql = "UPDATE membres SET nom = ?, prenom = ?, email = ?, actif = ?, telephone = ?, adresse = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            stmt.setString(5, membre.getTelephone());
            stmt.setString(6, membre.getAdresse());
            stmt.setLong(7, membre.getId());

            stmt.executeUpdate();
            System.out.println("[MembreDAO] Membre modifié: " + membre.getNom());
            return membre;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM membres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            int rows = stmt.executeUpdate();
            System.out.println("[MembreDAO] Membre supprimé: " + (rows > 0));
            return rows > 0;
        }
    }

    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        Membre m = new Membre();
        m.setId(rs.getLong("id"));
        m.setNom(rs.getString("nom"));
        m.setPrenom(rs.getString("prenom"));
        m.setEmail(rs.getString("email"));
        m.setActif(rs.getBoolean("actif"));
        m.setTelephone(rs.getString("telephone"));
        m.setAdresse(rs.getString("adresse"));
        return m;
    }
}
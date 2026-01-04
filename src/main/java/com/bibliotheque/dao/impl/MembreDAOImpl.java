package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.model.Membre;
import com.bibliotheque.infra.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation JDBC du DAO pour Membre
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
        }
        return membre;
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
        String sql = "SELECT * FROM membres WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
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
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres ORDER BY nom ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
        }
        return membres;
    }

    @Override
    public List<Membre> findActifs() throws SQLException {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres WHERE actif = true ORDER BY nom ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
        }
        return membres;
    }

    @Override
    public Membre update(Membre membre) throws SQLException {
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
        }
        return membre;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM membres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        return new Membre(
            rs.getLong("id"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("email"),
            rs.getBoolean("actif"),
            rs.getString("telephone"),
            rs.getString("adresse")
        );
    }
}

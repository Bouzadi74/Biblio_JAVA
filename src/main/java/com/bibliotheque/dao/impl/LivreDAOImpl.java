package com.bibliotheque.dao.impl;

import com.bibliotheque.model.Livre;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.infra.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation JDBC du DAO pour Livre
 * Utilise PreparedStatement pour éviter les injections SQL
 * Gère la connexion via le Singleton DatabaseConnection
 */
public class LivreDAOImpl implements LivreDAO {

    /**
     * Ajoute un nouveau livre à la base de données
     */
    @Override
    public void save(Livre livre) throws SQLException {
        String sql = "INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livre.getIsbn());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.estDisponible());

            stmt.executeUpdate();
        }
    }

    /**
     * Récupère un livre par son ISBN
     */
    @Override
    public Livre findById(String isbn) throws SQLException {
        String sql = "SELECT * FROM livres WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLivre(rs);
                }
            }
        }
        return null;
    }

    /**
     * Récupère tous les livres
     */
    @Override
    public List<Livre> findAll() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres ORDER BY titre ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    /**
     * Met à jour les informations d'un livre
     */
    @Override
    public void update(Livre livre) throws SQLException {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, annee_publication = ?, disponible = ? WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setInt(3, livre.getAnneePublication());
            stmt.setBoolean(4, livre.estDisponible());
            stmt.setString(5, livre.getIsbn());

            stmt.executeUpdate();
        }
    }

    /**
     * Supprime un livre par son ISBN
     */
    @Override
    public void delete(String isbn) throws SQLException {
        String sql = "DELETE FROM livres WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    /**
     * Recherche des livres par auteur
     */
    @Override
    public List<Livre> findByAuteur(String auteur) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE auteur LIKE ? ORDER BY titre ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + auteur + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToLivre(rs));
                }
            }
        }
        return livres;
    }

    /**
     * Recherche des livres par titre
     */
    @Override
    public List<Livre> findByTitre(String titre) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE titre LIKE ? ORDER BY titre ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + titre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToLivre(rs));
                }
            }
        }
        return livres;
    }

    /**
     * Récupère tous les livres disponibles
     */
    @Override
    public List<Livre> findDisponibles() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = true ORDER BY titre ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers un objet Livre
     * 
     * @param rs Le ResultSet de la base de données
     * @return Un objet Livre initialisé avec les données du ResultSet
     * @throws SQLException en cas d'erreur
     */
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        return new Livre(
                rs.getString("isbn"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getInt("annee_publication"),
                rs.getBoolean("disponible"));
    }
}

package com.bibliotheque.dao;

<<<<<<< HEAD
import com.bibliotheque.model.Livre;
import com.bibliotheque.infra.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du DAO pour Livre utilisant JDBC
 */
public class LivreDAOImpl implements LivreDAO {

    @Override
    public void save(Livre livre) throws SQLException {
        String sql = "INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livre.getId());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.estDisponible());

            stmt.executeUpdate();
            System.out.println("[LivreDAO] Livre inséré: " + livre.getTitre());
        }
    }

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

    @Override
    public List<Livre> findAll() throws SQLException {
        String sql = "SELECT * FROM livres";
        List<Livre> livres = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    @Override
    public void update(Livre livre) throws SQLException {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, annee_publication = ?, disponible = ? WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setInt(3, livre.getAnneePublication());
            stmt.setBoolean(4, livre.estDisponible());
            stmt.setString(5, livre.getId());

            stmt.executeUpdate();
            System.out.println("[LivreDAO] Livre mise à jour: " + livre.getTitre());
        }
    }

    @Override
    public void delete(String isbn) throws SQLException {
        String sql = "DELETE FROM livres WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            stmt.executeUpdate();
            System.out.println("[LivreDAO] Livre supprimé avec ISBN: " + isbn);
        }
    }

    @Override
    public List<Livre> findByAuteur(String auteur) throws SQLException {
        String sql = "SELECT * FROM livres WHERE auteur LIKE ?";
        List<Livre> livres = new ArrayList<>();

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

    @Override
    public List<Livre> findByTitre(String titre) throws SQLException {
        String sql = "SELECT * FROM livres WHERE titre LIKE ?";
        List<Livre> livres = new ArrayList<>();

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

    @Override
    public List<Livre> findDisponibles() throws SQLException {
        String sql = "SELECT * FROM livres WHERE disponible = TRUE";
        List<Livre> livres = new ArrayList<>();

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
     * Helper méthode pour mapper un ResultSet à un objet Livre
     */
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        String isbn = rs.getString("isbn");
        String titre = rs.getString("titre");
        String auteur = rs.getString("auteur");
        int anneePublication = rs.getInt("annee_publication");
        boolean disponible = rs.getBoolean("disponible");

        Livre livre = new Livre(isbn, titre, auteur, anneePublication);
        livre.setDisponible(disponible);
        return livre;
    }
=======
import com.bibliotheque.model.Book;
import com.bibliotheque.infra.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Skeleton implementation for LivreDAO. Real SQL and resource handling to be implemented by students.
 */
public class LivreDAOImpl implements LivreDAO {
    private final DatabaseConnection db;

    public LivreDAOImpl(DatabaseConnection db) {
        this.db = db;
    }

    @Override
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        // Example of how DAO should use PreparedStatement (no real SQL execution here)
        // Connection conn = (Connection) db.getConnection();
        // PreparedStatement ps = conn.prepareStatement("SELECT id, isbn, titre, auteur, annee, total_copies, available_copies FROM LIVRE");
        // ResultSet rs = ps.executeQuery();
        // while (rs.next()) { ... }
        return list;
    }

    @Override
    public Optional<Book> findById(Integer id) { return Optional.empty(); }

    @Override
    public void insert(Book book) { /* use PreparedStatement to insert into LIVRE */ }

    @Override
    public void update(Book book) { /* use PreparedStatement to update LIVRE */ }

    @Override
    public void delete(Integer id) { /* use PreparedStatement to delete from LIVRE */ }
>>>>>>> e014484e0ecce728e18711c7d7edda1ec5b547bb
}

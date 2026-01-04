package com.bibliotheque.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton pour la gestion de la connexion à la base de données
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Configuration de la base de données (peut être surchargée via System properties ou variables d'environnement)
    // Default values: adjust to match your local MySQL schema/name/user/password
    // The project SQL files create schema `bib` (see /sql/*.sql) so use that by default
    private static final String DB_DRIVER = System.getProperty("DB_DRIVER", System.getenv().getOrDefault("DB_DRIVER", "com.mysql.cj.jdbc.Driver"));

    private static String resolve(String propName, String defaultValue) {
        String v = System.getProperty(propName);
        if (v != null && !v.isBlank()) return v;
        v = System.getenv(propName);
        if (v != null && !v.isBlank()) return v;
        return defaultValue;
    }

    private static final String DB_URL = resolve("DB_URL", "jdbc:mysql://localhost:3306/bib");
    private static final String DB_USER = resolve("DB_USER", "root");
    private static final String DB_PASSWORD = resolve("DB_PASSWORD", "");

    private DatabaseConnection() {
        try {
            Class.forName(DB_DRIVER);
            if (DB_PASSWORD == null || DB_PASSWORD.isEmpty()) {
                System.out.println("[DatabaseConnection] Tentative de connexion avec utilisateur='" + DB_USER + "' et mot de passe vide (vérifiez vos propriétés/variables DB_PASSWORD)");
            }
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DatabaseConnection] Connexion établie avec succès to " + DB_URL + " as " + DB_USER);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[DatabaseConnection] Erreur de connexion: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Récupère l'instance unique du Singleton
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Retourne la connexion à la base de données
     */
    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                try {
                    Class.forName(DB_DRIVER);
                } catch (ClassNotFoundException ignored) {
                    // Driver may be provided by module path or JDBC driver manager
                }
                this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Erreur lors de l'ouverture de la connexion: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Impossible d'ouvrir la connexion à la base de données: " + e.getMessage(), e);
        }
        if (this.connection == null) {
            throw new RuntimeException("Connexion à la base de données non établie (connection == null)");
        }
        return this.connection;
    }

    /**
     * Ferme la connexion
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DatabaseConnection] Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Erreur lors de la fermeture: " + e.getMessage());
        }
    }
}

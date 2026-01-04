package com.bibliotheque.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.flywaydb.core.Flyway;

/**
 * Singleton pour la gestion de la connexion à la base de données
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Configuration de la base de données (peut être surchargée via System properties ou variables d'environnement)
    // Default values: adjust to match your local MySQL schema/name/user/password
    // The project SQL files create schema `bib` (see /sql/*.sql) so use that by default
    private static final String DB_DRIVER = resolve("DB_DRIVER", "org.h2.Driver");

    private static String resolve(String propName, String defaultValue) {
        String v = System.getProperty(propName);
        if (v != null && !v.isBlank()) return v;
        v = System.getenv(propName);
        if (v != null && !v.isBlank()) return v;
        return defaultValue;
    }

    private static final String DB_URL = resolve("DB_URL", "jdbc:h2:mem:bib;MODE=MySQL;DATABASE_TO_LOWER=TRUE");
    private static final String DB_USER = resolve("DB_USER", "sa");
    private static final String DB_PASSWORD = resolve("DB_PASSWORD", "");

    private DatabaseConnection() {
        try {
            // First try configured DB
            Class.forName(DB_DRIVER);
            if (DB_PASSWORD == null || DB_PASSWORD.isEmpty()) {
                System.out.println("[DatabaseConnection] Tentative de connexion avec utilisateur='" + DB_USER + "' et mot de passe vide (vérifiez vos propriétés/variables DB_PASSWORD)");
            }
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DatabaseConnection] Connexion établie avec succès to " + DB_URL + " as " + DB_USER);

            // Run Flyway migrations if possible — failures here should not crash the app
            try {
                Flyway flyway = Flyway.configure()
                        .dataSource(DB_URL, DB_USER, DB_PASSWORD)
                        .load();
                flyway.migrate();
                System.out.println("[DatabaseConnection] Migrations Flyway exécutées avec succès");
            } catch (Exception fx) {
                System.err.println("[DatabaseConnection] Flyway migration skipped: " + fx.getMessage());
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[DatabaseConnection] Erreur de connexion au DB configuré: " + e.getMessage());
            // Attempt fallback to embedded H2 in MySQL compatibility mode
            try {
                String fallbackDriver = "org.h2.Driver";
                String fallbackUrl = "jdbc:h2:mem:bib;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";
                String fallbackUser = "sa";
                String fallbackPassword = "";
                System.out.println("[DatabaseConnection] Tentative de fallback vers H2 embarquée: " + fallbackUrl);
                Class.forName(fallbackDriver);
                this.connection = DriverManager.getConnection(fallbackUrl, fallbackUser, fallbackPassword);
                System.out.println("[DatabaseConnection] Connexion H2 fallback établie avec succès");
                try {
                    Flyway flyway = Flyway.configure()
                            .dataSource(fallbackUrl, fallbackUser, fallbackPassword)
                            .load();
                    flyway.migrate();
                    System.out.println("[DatabaseConnection] Migrations Flyway exécutées sur H2 fallback");
                } catch (Exception fx2) {
                    System.err.println("[DatabaseConnection] Flyway migration on H2 skipped: " + fx2.getMessage());
                }
            } catch (ClassNotFoundException | SQLException ex2) {
                System.err.println("[DatabaseConnection] Erreur lors du fallback H2: " + ex2.getMessage());
                ex2.printStackTrace(System.err);
            }
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

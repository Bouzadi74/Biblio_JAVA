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

    // Configuration de la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private DatabaseConnection() {
        try {
            Class.forName(DB_DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DatabaseConnection] Connexion établie avec succès");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[DatabaseConnection] Erreur de connexion: " + e.getMessage());
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

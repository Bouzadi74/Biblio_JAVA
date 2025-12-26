package com.bibliotheque.infra;

/**
 * Singleton managing database connections. No implementation here.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;

    private DatabaseConnection() {}

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) instance = new DatabaseConnection();
        return instance;
    }

    /**
     * Abstract connection handle; concrete type depends on chosen persistence (JDBC, JPA, etc.)
     */
    public Object getConnection() { return null; }
}

package com.bibliotheque.infra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    void getInstance_returnsSingleton() {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        assertNotNull(instance);
    }

}

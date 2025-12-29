package com.bibliotheque.dao;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Integration-style DAO tests should be enabled when a test database is available.
 * For now this is disabled by default. Use Testcontainers or a local MySQL to run these.
 */
@Disabled("Requires database; enable when DB available (Testcontainers or local MySQL)")
public class LivreDAOTest {

    @Test
    void placeholderDaoIntegrationTest() throws Exception {
        // TODO: Enable and implement integration tests using Testcontainers or a local test DB
    }
}

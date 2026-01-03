package com.bibliotheque.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.infra.DatabaseConnection;
import com.bibliotheque.model.Livre;

public class LivreDAOTest {

    @BeforeEach
    void setup() throws Exception {
        // Use H2 in-memory for DAO tests
        System.setProperty("DB_URL", "jdbc:h2:mem:livre;DB_CLOSE_DELAY=-1");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");
        System.setProperty("DB_DRIVER", "org.h2.Driver");

        // Reset singleton instance
        Field f = DatabaseConnection.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE livres (isbn VARCHAR(50) PRIMARY KEY, titre VARCHAR(255), auteur VARCHAR(255), annee_publication INT, disponible BOOLEAN);");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS livres");
        }
    }

    @Test
    void saveAndFind() throws Exception {
        LivreDAOImpl dao = new LivreDAOImpl();
        Livre livre = new Livre("isbn-123", "Titre Test", "Auteur Test", 2020, true);
        dao.save(livre);

        Livre found = dao.findById("isbn-123");
        assertNotNull(found);
        assertEquals("Titre Test", found.getTitre());
    }

    @Test
    void updateAndDelete() throws Exception {
        LivreDAOImpl dao = new LivreDAOImpl();
        Livre livre = new Livre("isbn-456", "Titre A", "Auteur A", 2019, true);
        dao.save(livre);

        livre.setTitre("Titre B");
        dao.update(livre);
        Livre found = dao.findById("isbn-456");
        assertEquals("Titre B", found.getTitre());

        dao.delete("isbn-456");
        assertNull(dao.findById("isbn-456"));
    }

    @Test
    void findDisponibles() throws Exception {
        LivreDAOImpl dao = new LivreDAOImpl();
        dao.save(new Livre("i1", "T1", "A1", 2018, true));
        dao.save(new Livre("i2", "T2", "A2", 2017, false));

        List<Livre> dispo = dao.findDisponibles();
        assertEquals(1, dispo.size());
        assertEquals("i1", dispo.get(0).getIsbn());
    }
}

package com.bibliotheque.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bibliotheque.model.Emprunt;

public class EmpruntDAOImplTest {

    private Connection conn;
    private EmpruntDAOImpl dao;

    @BeforeEach
    void setup() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:emprunt;DB_CLOSE_DELAY=-1", "sa", "");
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS emprunts (id INT AUTO_INCREMENT PRIMARY KEY, id_membre INT, id_livre INT, date_emprunt DATE, date_retour DATE);");
        }
        dao = new EmpruntDAOImpl(conn);
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS emprunts");
        }
        conn.close();
    }

    @Test
    void insertAndFind() {
        Emprunt e = new Emprunt();
        e.setIdMembre(1);
        e.setIdLivre(10);
        e.setDateEmprunt(LocalDate.now());

        dao.insert(e);

        List<Emprunt> list = dao.findByMemberId(1);
        assertFalse(list.isEmpty());
    }

    @Test
    void findActiveByBookId() {
        Emprunt e = new Emprunt();
        e.setIdMembre(2);
        e.setIdLivre(20);
        e.setDateEmprunt(LocalDate.now());
        dao.insert(e);

        List<Emprunt> list = dao.findActiveByBookId(20);
        assertEquals(1, list.size());
    }
}

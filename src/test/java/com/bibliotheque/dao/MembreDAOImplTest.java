package com.bibliotheque.dao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.bibliotheque.dao.impl.MembreDAOImpl;
import com.bibliotheque.model.Membre;

public class MembreDAOImplTest {

    @Test
    void saveFindUpdateDelete() {
        MembreDAOImpl dao = new MembreDAOImpl();
        Membre m = new Membre( null, "Dupont", "Jean", "j.dupont@example.com", true, "0123456789", "Rue" );
        dao.save(m);

        Optional<Membre> maybe = dao.findByEmail("j.dupont@example.com");
        assertTrue(maybe.isPresent());

        Membre found = maybe.get();
        found.setTelephone("0987654321");
        dao.update(found);

        Optional<Membre> after = dao.findById(found.getId());
        assertTrue(after.isPresent());
        assertEquals("0987654321", after.get().getTelephone());

        boolean deleted = dao.delete(found.getId());
        assertTrue(deleted);
    }
}

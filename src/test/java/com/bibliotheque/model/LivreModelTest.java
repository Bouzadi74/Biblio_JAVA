package com.bibliotheque.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LivreModelTest {

    @Test
    void constructorAndGetters() {
        Livre livre = new Livre("978-1234567890", "Mon Titre", "Auteur Test", 2025);
        assertEquals("978-1234567890", livre.getId());
        assertEquals("Mon Titre", livre.getTitre());
        assertEquals("Auteur Test", livre.getAuteur());
        assertEquals(2025, livre.getAnneePublication());
        assertTrue(livre.estDisponible());
    }
}

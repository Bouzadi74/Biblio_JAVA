package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Test
    void ajouterLivre_callsDaoSave() throws Exception {
        LivreDAO mockDao = mock(LivreDAO.class);
        BookServiceImpl service = new BookServiceImpl(mockDao);

        Livre livre = new Livre("isbn-1", "Titre", "Auteur", 2024);
        service.ajouterLivre(livre);

        verify(mockDao, times(1)).save(livre);
    }

    @Test
    void ajouterLivre_missingIsbn_throwsValidation() {
        LivreDAO mockDao = mock(LivreDAO.class);
        BookServiceImpl service = new BookServiceImpl(mockDao);

        Livre livre = new Livre("", "Titre", "Auteur", 2024);
        assertThrows(ValidationException.class, () -> service.ajouterLivre(livre));
    }
}

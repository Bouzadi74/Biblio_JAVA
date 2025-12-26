package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Book;
import java.util.List;

/**
 * Simple service implementation delegating to LivreDAO (no business logic here).
 */
public class BibliothequeServiceImpl implements BibliothequeService {
    private final LivreDAO livreDAO;

    public BibliothequeServiceImpl(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    @Override
    public List<Book> listLivres() {
        return livreDAO.findAll();
    }
}

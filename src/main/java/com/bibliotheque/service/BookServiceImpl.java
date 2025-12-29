package com.bibliotheque.service;

import java.util.List;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;

public class BookServiceImpl implements BookService {

    private final LivreDAO dao;

    public BookServiceImpl(LivreDAO dao) {
        this.dao = dao;
    }

    @Override
    public void ajouterLivre(Livre livre) throws ValidationException {
        // basic validation
        if (livre.getId() == null || livre.getId().trim().isEmpty())
            throw new ValidationException("ISBN manquant");
        try {
            dao.save(livre);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifierLivre(Livre livre) throws ValidationException {
        if (livre.getId() == null || livre.getId().trim().isEmpty())
            throw new ValidationException("ISBN manquant");
        try {
            dao.update(livre);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Livre> getTousLesLivres() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Livre> rechercherLivres(String motCle) {
        try {
            // search by titre and auteur then merge
            List<Livre> byTitre = dao.findByTitre(motCle);
            List<Livre> byAuteur = dao.findByAuteur(motCle);
            byTitre.addAll(byAuteur);
            return byTitre;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimerLivre(String isbn) {
        try {
            dao.delete(isbn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

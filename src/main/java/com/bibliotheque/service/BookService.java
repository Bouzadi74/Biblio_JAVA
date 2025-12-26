package com.bibliotheque.service;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import java.util.List;

public interface BookService {
    void ajouterLivre(Livre livre) throws ValidationException;

    void modifierLivre(Livre livre) throws ValidationException;

    List<Livre> getTousLesLivres();

    List<Livre> rechercherLivres(String motCle);
}

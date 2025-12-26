package com.bibliotheque.service;

import com.bibliotheque.model.Membre;
import java.util.List;

public interface BibliothequeService {

    /* ===== Module MEMBRES ===== */

    void ajouterMembre(Membre membre);

    void modifierMembre(Membre membre);

    void supprimerMembre(Long id);

    List<Membre> listerMembres();

    List<Membre> rechercherMembres(String motCle);

    void activerMembre(Long id);

    void desactiverMembre(Long id);

    List<String> historiqueEmprunts(Long membreId);
}


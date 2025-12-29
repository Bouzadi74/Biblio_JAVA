package com.bibliotheque.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.model.Emprunt;

public class EmpruntService {

    private EmpruntDAO dao;

    public EmpruntService(EmpruntDAO dao) {
        this.dao = dao;
    }

    public void emprunterLivre(int idMembre, int idLivre) throws Exception {

        // Règle métier : maximum 3 emprunts en cours
        int nb = dao.countEmpruntsEnCours(idMembre);
        if (nb >= 3)
            throw new Exception("Un membre ne peut pas dépasser 3 emprunts en cours.");

        Emprunt e = new Emprunt();
        e.setIdMembre(idMembre);
        e.setIdLivre(idLivre);
        e.setDateEmprunt(LocalDate.now());
        e.setDateRetour(null);

        dao.save(e);
    }

    public void retournerLivre(int idEmprunt) throws Exception {
        Emprunt e = dao.findById(idEmprunt);
        if (e == null)
            throw new Exception("Emprunt introuvable.");

        if (e.getDateRetour() != null)
            throw new Exception("Ce livre est déjà retourné.");

        e.setDateRetour(LocalDate.now());
        dao.update(e);
    }

    public double calculerPenalite(Emprunt e) {
        LocalDate dateRetour = (e.getDateRetour() == null) ? LocalDate.now() : e.getDateRetour();

        long jours = ChronoUnit.DAYS.between(e.getDateEmprunt(), dateRetour);

        long retard = jours - 14; // 14 jours autorisés
        if (retard <= 0) return 0;

        return retard * 2; // pénalité : 2 DH par jour de retard
    }

    public List<Emprunt> getEmpruntsEnRetard() throws Exception {
        List<Emprunt> enCours = dao.findEnCours();

        return enCours.stream()
                .filter(e -> ChronoUnit.DAYS.between(e.getDateEmprunt(), LocalDate.now()) > 14)
                .toList();
    }
}


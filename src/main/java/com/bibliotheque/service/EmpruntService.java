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

    public void emprunterLivre(long idMembre, String isbnLivre) throws Exception {

        // Règle métier : maximum 3 emprunts en cours
        int nb = dao.countEmpruntsEnCours(idMembre);
        if (nb >= 3)
            throw new Exception("Un membre ne peut pas dépasser 3 emprunts en cours.");

        Emprunt e = new Emprunt();
        e.setIdMembre(idMembre);
        e.setIsbnLivre(isbnLivre);
        e.setDateEmprunt(LocalDate.now());
        e.setDateRetourPrevue(LocalDate.now().plusDays(14)); // 2 semaines
        e.setStatut("EN_COURS");

        dao.save(e);
    }

    // Compatibility overload used by tests that reference a numeric book id
    public void emprunterLivre(long idMembre, int idLivre) throws Exception {
        int nb = dao.countEmpruntsEnCours(idMembre);
        if (nb >= 3)
            throw new Exception("Un membre ne peut pas dépasser 3 emprunts en cours.");

        Emprunt e = new Emprunt();
        e.setIdMembre(idMembre);
        e.setIdLivre(idLivre);
        e.setDateEmprunt(LocalDate.now());
        e.setDateRetourPrevue(LocalDate.now().plusDays(14));
        e.setStatut("EN_COURS");

        dao.save(e);
    }

    public void retournerLivre(long idEmprunt) throws Exception {
        Emprunt e = dao.findById(idEmprunt).orElse(null);
        if (e == null)
            throw new Exception("Emprunt introuvable.");

        if (e.getDateRetourEffective() != null)
            throw new Exception("Ce livre est déjà retourné.");

        e.setDateRetourEffective(LocalDate.now());
        e.setStatut("RETOURNE");

        dao.update(e);
    }

    public double calculerPenalite(Emprunt e) {
        LocalDate dateRetour = (e.getDateRetourEffective() == null) ? LocalDate.now() : e.getDateRetourEffective();

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


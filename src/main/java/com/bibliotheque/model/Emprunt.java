package com.bibliotheque.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprunt {
    private int id;
    private String titreLivre;
    private String nomMembre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private double penalite;
    private String statut; // "EN_COURS", "RETOURNE", "EN_RETARD"

    // Constructeur complet
    public Emprunt(int id, String titreLivre, String nomMembre, 
                   LocalDate dateEmprunt, LocalDate dateRetourPrevue, 
                   LocalDate dateRetourEffective, double penalite, String statut) {
        this.id = id;
        this.titreLivre = titreLivre;
        this.nomMembre = nomMembre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.penalite = penalite;
        this.statut = statut;
    }

    // Constructeur pour nouvel emprunt
    public Emprunt(String titreLivre, String nomMembre, 
                   LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.titreLivre = titreLivre;
        this.nomMembre = nomMembre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.statut = "EN_COURS";
        this.penalite = 0.0;
    }

    // Constructeur vide
    public Emprunt() {
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitreLivre() {
        return titreLivre;
    }

    public void setTitreLivre(String titreLivre) {
        this.titreLivre = titreLivre;
    }

    public String getNomMembre() {
        return nomMembre;
    }

    public void setNomMembre(String nomMembre) {
        this.nomMembre = nomMembre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public double getPenalite() {
        return penalite;
    }

    public void setPenalite(double penalite) {
        this.penalite = penalite;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    // Méthode utilitaire pour calculer les jours de retard
    public long getJoursRetard() {
        if (dateRetourEffective != null) {
            return ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
        } else {
            LocalDate aujourdhui = LocalDate.now();
            if (aujourdhui.isAfter(dateRetourPrevue)) {
                return ChronoUnit.DAYS.between(dateRetourPrevue, aujourdhui);
            }
        }
        return 0;
    }

    // Vérifier si l'emprunt est en retard
    public boolean isEnRetard() {
        return getJoursRetard() > 0 && dateRetourEffective == null;
    }

    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", livre='" + titreLivre + '\'' +
                ", membre='" + nomMembre + '\'' +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue=" + dateRetourPrevue +
                ", statut='" + statut + '\'' +
                ", penalite=" + penalite +
                '}';
    }
}
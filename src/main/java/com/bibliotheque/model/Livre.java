package com.bibliotheque.model;

// Doit étendre Document et implémenter Empruntable [cite: 556]
public class Livre extends Document implements Empruntable {
    private String auteur;
    private int anneePublication;
    private boolean disponible;

    public Livre(String isbn, String titre, String auteur, int anneePublication) {
        super(isbn, titre); // id du parent = isbn [cite: 112]
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = true; // Par défaut disponible
    }

    @Override
    public boolean estDisponible() {
        return disponible;
    }

    @Override
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Getters et Setters spécifiques
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public int getAnneePublication() { return anneePublication; }
    public void setAnneePublication(int anneePublication) { this.anneePublication = anneePublication; }
}
package com.bibliotheque.model;

<<<<<<< HEAD
/**
 * Classe Livre représentant un livre dans la bibliothèque
 * Extends Document et implements Empruntable pour une gestion complète
 */
=======
// Doit étendre Document et implémenter Empruntable [cite: 556]
>>>>>>> 23e1e4fb064834a1628e4ab848cde59643820c49
public class Livre extends Document implements Empruntable {
    private String auteur;
    private int anneePublication;
    private boolean disponible;

<<<<<<< HEAD
    /**
     * Constructeur complet
     * 
     * @param isbn             Identifiant unique du livre (ISBN)
     * @param titre            Titre du livre
     * @param auteur           Auteur du livre
     * @param anneePublication Année de publication
     */
    public Livre(String isbn, String titre, String auteur, int anneePublication) {
        super(isbn, titre);
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = true;
    }

    /**
     * Constructeur avec disponibilité
     */
    public Livre(String isbn, String titre, String auteur, int anneePublication, boolean disponible) {
        super(isbn, titre);
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    // ========== Implémentation de Empruntable ==========

=======
    public Livre(String isbn, String titre, String auteur, int anneePublication) {
        super(isbn, titre); // id du parent = isbn [cite: 112]
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = true; // Par défaut disponible
    }

>>>>>>> 23e1e4fb064834a1628e4ab848cde59643820c49
    @Override
    public boolean estDisponible() {
        return disponible;
    }

    @Override
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

<<<<<<< HEAD
    @Override
    public void emprunter() throws IllegalStateException {
        if (!disponible) {
            throw new IllegalStateException("Ce livre n'est pas disponible pour emprunt");
        }
        this.disponible = false;
    }

    @Override
    public void retourner() {
        this.disponible = true;
    }

    // ========== Implémentation abstraite ==========

    @Override
    public double calculerPenaliteRetard(int joursRetard) {
        // Frais de retard: 0.50€ par jour (spécifique aux livres)
        return joursRetard * 0.50;
    }

    // ========== Getters et Setters ==========

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    // Alias pour ISBN (id du Document)
    public String getIsbn() {
        return this.id;
    }

    public void setIsbn(String isbn) {
        this.id = isbn;
    }

    @Override
    public String toString() {
        return "Livre{" +
                "isbn='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", anneePublication=" + anneePublication +
                ", disponible=" + disponible +
                '}';
    }
}
=======
    // Getters et Setters spécifiques
    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public int getAnneePublication() { return anneePublication; }
    public void setAnneePublication(int anneePublication) { this.anneePublication = anneePublication; }
}
>>>>>>> 23e1e4fb064834a1628e4ab848cde59643820c49

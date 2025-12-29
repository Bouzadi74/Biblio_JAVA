package com.bibliotheque.model;

/**
 * Classe abstraite représentant un document de bibliothèque
 * Tous les documents doivent avoir un ID et un titre
 */
public abstract class Document {
    protected String id; // ISBN pour un Livre
    protected String titre;

    public Document(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Méthode abstraite pour calculer les pénalités de retard
     * Chaque type de document peut avoir une logique différente
     */
    public abstract double calculerPenaliteRetard(int joursRetard);

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                '}';
    }
}

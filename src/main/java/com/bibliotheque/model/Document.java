package com.bibliotheque.model;

public abstract class Document {
    private String id; // Correspond à l'ISBN pour un livre [cite: 112]
    private String titre;

    public Document(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    // Méthode abstraite potentielle mentionnée p.9 (optionnelle selon implémentation)
    // public abstract double calculerPenaliteRetard(); [cite: 116]
}
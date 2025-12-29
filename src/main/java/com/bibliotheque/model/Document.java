package com.bibliotheque.model;

<<<<<<< HEAD
/**
 * Classe abstraite représentant un document de bibliothèque
 * Tous les documents doivent avoir un ID et un titre
 */
public abstract class Document {
    protected String id; // ISBN pour un Livre
    protected String titre;
=======
public abstract class Document {
    private String id; // Correspond à l'ISBN pour un livre [cite: 112]
    private String titre;
>>>>>>> 23e1e4fb064834a1628e4ab848cde59643820c49

    public Document(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    // Getters et Setters
<<<<<<< HEAD
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
=======
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    // Méthode abstraite potentielle mentionnée p.9 (optionnelle selon implémentation)
    // public abstract double calculerPenaliteRetard(); [cite: 116]
}
>>>>>>> 23e1e4fb064834a1628e4ab848cde59643820c49

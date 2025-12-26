package com.bibliotheque.model;

public class Membre extends Personne {

    private String telephone;
    private String adresse; // optionnel

    public Membre() {}

    public Membre(Long id, String nom, String prenom, String email, boolean actif,
                  String telephone, String adresse) {
        super(id, nom, prenom, email, actif);
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public String toString() {
        return getNom() + " " + getPrenom() + " (" + getEmail() + ")";
    }
}

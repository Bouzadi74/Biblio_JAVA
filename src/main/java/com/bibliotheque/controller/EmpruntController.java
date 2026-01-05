package com.bibliotheque.controller;


import com.bibliotheque.service.EmpruntService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EmpruntController {

    @FXML private TextField txtIdMembre;
    @FXML private TextField txtIdLivre;
    @FXML private TextField txtIdEmprunt;
    @FXML private Label lblMessage;

    private EmpruntService service;

    public void setService(EmpruntService service) {
        this.service = service;
    }

    @FXML
    public void formulaireEmprunt() {
        try {
            long idMembre = Long.parseLong(txtIdMembre.getText());
            String isbnLivre = txtIdLivre.getText();

            service.emprunterLivre(idMembre, isbnLivre);
            lblMessage.setText("Livre emprunté avec succès !");
        } 
        catch (Exception ex) {
            lblMessage.setText("Erreur : " + ex.getMessage());
        }
    }

    @FXML
    public void formulaireRetour() {
        try {
            int idEmprunt = Integer.parseInt(txtIdEmprunt.getText());
            service.retournerLivre(idEmprunt);
            lblMessage.setText("Retour effectué ✔");
        } 
        catch (Exception ex) {
            lblMessage.setText("Erreur : " + ex.getMessage());
        }
    }

    @FXML
    public void gestionExceptions() {
        lblMessage.setText("Cette zone affichera les exceptions métier.");
    }
}

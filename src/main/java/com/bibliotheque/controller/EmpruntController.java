package com.bibliotheque.controller;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.service.EmpruntService;

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
            int idMembre = Integer.parseInt(txtIdMembre.getText());
            int idLivre = Integer.parseInt(txtIdLivre.getText());

            service.emprunterLivre(idMembre, idLivre);
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

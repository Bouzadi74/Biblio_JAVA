package com.bibliotheque.controller;

import com.bibliotheque.model.Membre;
import com.bibliotheque.service.BibliothequeService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MembreController {

    // ======== FXML fields (à lier dans MembreView.fxml) ========
    @FXML private TableView<Membre> membresTable;
    @FXML private TableColumn<Membre, Long> idCol;
    @FXML private TableColumn<Membre, String> nomCol;
    @FXML private TableColumn<Membre, String> prenomCol;
    @FXML private TableColumn<Membre, String> emailCol;
    @FXML private TableColumn<Membre, Boolean> actifCol;

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField adresseField;
    @FXML private TextField searchField;

    @FXML private ListView<String> historiqueList;
    @FXML private Label messageLabel;

    // ======== Service (injecté plus tard / initialisé dans Main) ========
    private BibliothequeService service;

    private final ObservableList<Membre> membresData = FXCollections.observableArrayList();

    // Appelé depuis Main après création du controller (ou via DI)
    public void setService(BibliothequeService service) {
        this.service = service;
        chargerMembres();
    }

    @FXML
    public void initialize() {
        // Configure cell value factories for table columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        actifCol.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().isActif()));
        actifCol.setCellFactory(tc -> new TableCell<Membre, Boolean>() {
            @Override
            protected void updateItem(Boolean actif, boolean empty) {
                super.updateItem(actif, empty);
                if (empty || actif == null) {
                    setText(null);
                } else {
                    setText(actif ? "Oui" : "Non");
                }
            }
        });

        membresTable.setItems(membresData);

        membresTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, selected) -> {
            if (selected != null) {
                remplirFormulaire(selected);
                chargerHistorique(selected.getId());
            }
        });
    }

    // ======================
    // ======= CRUD =========
    // ======================

    @FXML
    private void onAjouter() {
        if (service == null) { afficher("Service non initialisé"); return; }

        Membre m = lireFormulaire(null);
        try {
            service.ajouterMembre(m);
            afficher("Membre ajouté ✅");
            chargerMembres();
            viderFormulaire();
        } catch (Exception e) {
            afficher("Erreur: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }

    @FXML
    private void onModifier() {
        if (service == null) { afficher("Service non initialisé"); return; }

        Membre selected = membresTable.getSelectionModel().getSelectedItem();
        if (selected == null) { afficher("Sélectionne un membre"); return; }

        Membre m = lireFormulaire(selected.getId());
        // garder l’état actif actuel si ton formulaire ne le modifie pas
        m.setActif(selected.isActif());

        try {
            service.modifierMembre(m);
            afficher("Membre modifié ✅");
            chargerMembres();
        } catch (Exception e) {
            afficher("Erreur: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }

    @FXML
    private void onSupprimer() {
        if (service == null) { afficher("Service non initialisé"); return; }

        Membre selected = membresTable.getSelectionModel().getSelectedItem();
        if (selected == null) { afficher("Sélectionne un membre"); return; }

        try {
            service.supprimerMembre(selected.getId());
            afficher("Membre supprimé ✅");
            chargerMembres();
            viderFormulaire();
            historiqueList.getItems().clear();
        } catch (Exception e) {
            afficher("Erreur: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }

    // ===============================
    // === Activation / Désactivation
    // ===============================

    @FXML
    private void onActiver() {
        if (service == null) { afficher("Service non initialisé"); return; }

        Membre selected = membresTable.getSelectionModel().getSelectedItem();
        if (selected == null) { afficher("Sélectionne un membre"); return; }

        try {
            service.activerMembre(selected.getId());
            afficher("Membre activé ✅");
            chargerMembres();
        } catch (Exception e) {
            afficher("Erreur: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }

    @FXML
    private void onDesactiver() {
        if (service == null) { afficher("Service non initialisé"); return; }

        Membre selected = membresTable.getSelectionModel().getSelectedItem();
        if (selected == null) { afficher("Sélectionne un membre"); return; }

        try {
            service.desactiverMembre(selected.getId());
            afficher("Membre désactivé ✅");
            chargerMembres();
        } catch (Exception e) {
            afficher("Erreur: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }

    // ======================
    // ===== Recherche ======
    // ======================

    @FXML
    private void onRechercher() {
        if (service == null) { afficher("Service non initialisé"); return; }

        String motCle = searchField.getText();
        List<Membre> resultat = service.rechercherMembres(motCle);
        membresData.setAll(resultat);
        afficher(resultat.size() + " membre(s) trouvé(s)");
    }

    // ======================
    // ===== Historique =====
    // ======================

    private void chargerHistorique(Long membreId) {
        if (service == null || membreId == null) {
            if (historiqueList != null) {
                historiqueList.getItems().clear();
            }
            return;
        }
        try {
            List<String> hist = service.historiqueEmprunts(membreId);
            if (historiqueList != null) {
                if (hist.isEmpty()) {
                    historiqueList.getItems().setAll("Aucun emprunt pour ce membre");
                } else {
                    historiqueList.getItems().setAll(hist);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'historique: " + e.getMessage());
            e.printStackTrace();
            if (historiqueList != null) {
                historiqueList.getItems().setAll("Erreur lors du chargement de l'historique");
            }
        }
    }

    // ======================
    // ===== Helpers ========
    // ======================

    private void chargerMembres() {
        if (service == null) return;
        membresData.setAll(service.listerMembres());
    }

    private void remplirFormulaire(Membre m) {
        nomField.setText(m.getNom());
        prenomField.setText(m.getPrenom());
        emailField.setText(m.getEmail());
        telephoneField.setText(m.getTelephone());
        adresseField.setText(m.getAdresse());
    }

    private Membre lireFormulaire(Long id) {
        Membre m = new Membre();
        m.setId(id);
        m.setNom(nomField.getText());
        m.setPrenom(prenomField.getText());
        m.setEmail(emailField.getText());
        m.setTelephone(telephoneField.getText());
        m.setAdresse(adresseField.getText());
        return m;
    }

    private void viderFormulaire() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
    }

    private void afficher(String msg) {
        if (messageLabel != null) messageLabel.setText(msg);
    }
}

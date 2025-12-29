package com.bibliotheque.controller;

import java.util.List;

import com.bibliotheque.dao.LivreDAOImpl;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.service.BookService;
import com.bibliotheque.service.BookServiceImpl;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Contrôleur pour la gestion des livres
 * Gère l'interaction entre la vue FXML et le service métier
 */
public class LivreController {

    // --- Champs FXML (Liaison avec la Vue) ---
    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtTitre;
    @FXML
    private TextField txtAuteur;
    @FXML
    private TextField txtAnnee;
    @FXML
    private TextField txtRecherche;

    @FXML
    private TableView<Livre> tableLivres;
    @FXML
    private TableColumn<Livre, String> colIsbn;
    @FXML
    private TableColumn<Livre, String> colTitre;
    @FXML
    private TableColumn<Livre, String> colAuteur;
    @FXML
    private TableColumn<Livre, Integer> colAnnee;
    @FXML
    private TableColumn<Livre, Boolean> colDisponible;

    // --- Service ---
    private BookService service;

    /**
     * Constructeur pour l'injection de dépendances
     */
    public LivreController(BookService service) {
        this.service = service;
    }

    /**
     * Constructeur sans arguments pour le chargeur FXML
     */
    public LivreController() {
    }

    /**
     * Méthode d'initialisation appelée automatiquement par JavaFX après le
     * chargement du FXML.
     */
    @FXML
    public void initialize() {
        // 1. Initialisation du Service (si non injecté)
        if (this.service == null) {
            this.service = new BookServiceImpl(new LivreDAOImpl());
        }

        // 2. Configuration des colonnes de la TableView
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        // 3. Chargement initial des données
        chargerLivres();

        // 4. Gestion de la sélection dans le tableau
        tableLivres.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirChamps(newSelection);
            }
        });
    }

    // --- Méthodes CRUD (Create, Read, Update, Delete) ---

    @FXML
    private void handleAjouter() {
        try {
            // Validation des champs
            if (txtIsbn.getText().trim().isEmpty() ||
                    txtTitre.getText().trim().isEmpty() ||
                    txtAuteur.getText().trim().isEmpty() ||
                    txtAnnee.getText().trim().isEmpty()) {
                afficherErreur("Validation", "Tous les champs sont obligatoires.");
                return;
            }

            // Création de l'objet à partir des champs
            Livre nouveauLivre = new Livre(
                    txtIsbn.getText(),
                    txtTitre.getText(),
                    txtAuteur.getText(),
                    Integer.parseInt(txtAnnee.getText()));

            // Appel au Service pour la logique métier
            service.ajouterLivre(nouveauLivre);

            afficherSucces("Livre ajouté avec succès !");
            viderChamps();
            chargerLivres(); // Rafraîchir le tableau

        } catch (NumberFormatException e) {
            afficherErreur("Erreur de format", "L'année doit être un nombre entier.");
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'ajout", e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherErreur("Aucune sélection", "Veuillez sélectionner un livre à modifier.");
            return;
        }

        try {
            // Validation des champs
            if (txtTitre.getText().trim().isEmpty() ||
                    txtAuteur.getText().trim().isEmpty() ||
                    txtAnnee.getText().trim().isEmpty()) {
                afficherErreur("Validation", "Tous les champs sont obligatoires.");
                return;
            }

            // Mise à jour des informations de l'objet sélectionné
            livreSelectionne.setTitre(txtTitre.getText());
            livreSelectionne.setAuteur(txtAuteur.getText());
            livreSelectionne.setAnneePublication(Integer.parseInt(txtAnnee.getText()));

            // Appel au Service
            service.modifierLivre(livreSelectionne);

            afficherSucces("Livre modifié avec succès !");
            viderChamps();
            chargerLivres();

        } catch (NumberFormatException e) {
            afficherErreur("Erreur de format", "L'année doit être un nombre entier.");
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            afficherErreur("Erreur lors de la modification", e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherErreur("Aucune sélection", "Veuillez sélectionner un livre à supprimer.");
            return;
        }

        // Confirmation (Bonne pratique UX)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce livre ?",
                ButtonType.YES,
                ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                // Appel au Service
                service.supprimerLivre(livreSelectionne.getId());

                afficherSucces("Livre supprimé !");
                viderChamps();
                chargerLivres();
            } catch (Exception e) {
                afficherErreur("Erreur lors de la suppression", e.getMessage());
            }
        }
    }

    @FXML
    private void handleRechercher() {
        String motCle = txtRecherche.getText();
        try {
            List<Livre> resultats;
            if (motCle == null || motCle.trim().isEmpty()) {
                resultats = service.getTousLesLivres();
            } else {
                // Appel au Service pour la recherche
                resultats = service.rechercherLivres(motCle);
            }
            tableLivres.getItems().setAll(resultats);
        } catch (Exception e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }

    // --- Méthodes Utilitaires ---

    private void chargerLivres() {
        try {
            // Le contrôleur délègue la récupération des données au service
            List<Livre> livres = service.getTousLesLivres();
            tableLivres.getItems().setAll(livres);
        } catch (Exception e) {
            afficherErreur("Erreur de chargement", "Impossible de charger la liste des livres.\n" + e.getMessage());
        }
    }

    private void remplirChamps(Livre livre) {
        txtIsbn.setText(livre.getId());
        txtTitre.setText(livre.getTitre());
        txtAuteur.setText(livre.getAuteur());
        txtAnnee.setText(String.valueOf(livre.getAnneePublication()));
        // Désactiver le champ ISBN lors de la modification pour éviter les incohérences
        txtIsbn.setDisable(true);
    }

    @FXML
    private void viderChamps() {
        txtIsbn.clear();
        txtTitre.clear();
        txtAuteur.clear();
        txtAnnee.clear();
        txtIsbn.setDisable(false); // Réactiver pour un nouvel ajout
        tableLivres.getSelectionModel().clearSelection();
    }

    // Affichage des messages (Popups)
    private void afficherSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
            alert.showAndWait();
    }
}

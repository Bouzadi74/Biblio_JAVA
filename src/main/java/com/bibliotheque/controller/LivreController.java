package com.bibliotheque.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.bibliotheque.model.Book;
import com.bibliotheque.service.BookService;
import com.bibliotheque.service.BookServiceImpl;
import java.sql.SQLException;
import java.util.List;

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
    private TableView<Book> tableLivres;
    @FXML
    private TableColumn<Book, String> colIsbn;
    @FXML
    private TableColumn<Book, String> colTitre;
    @FXML
    private TableColumn<Book, String> colAuteur;
    @FXML
    private TableColumn<Book, Integer> colAnnee;
    @FXML
    private TableColumn<Book, Integer> colDisponible;

    // --- Service ---
    private BookService service;

    // Constructeur pour l'injection de dépendances
    public LivreController(BookService service) {
        this.service = service;
    }

    // Constructeur sans arguments pour le chargeur FXML
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
            this.service = new BookServiceImpl();
        }

        // 2. Configuration des colonnes de la TableView
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("author"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));

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
            // Création de l'objet à partir des champs
            Book nouveauLivre = new Book(
                    null,
                    txtIsbn.getText(),
                    txtTitre.getText(),
                    txtAuteur.getText(),
                    Integer.parseInt(txtAnnee.getText()),
                    Integer.parseInt(txtAnnee.getText()));

            // Appel au Service pour la logique métier
            service.ajouterLivre(nouveauLivre);

            afficherSucces("Livre ajouté avec succès !");
            viderChamps();
            chargerLivres(); // Rafraîchir le tableau

        } catch (NumberFormatException e) {
            afficherErreur("Erreur de format", "L'année doit être un nombre entier.");
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'ajout", e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        Book livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherErreur("Aucune sélection", "Veuillez sélectionner un livre à modifier.");
            return;
        }

        try {
            // Mise à jour des informations de l'objet sélectionné
            livreSelectionne.setTitle(txtTitre.getText());
            livreSelectionne.setAuthor(txtAuteur.getText());
            livreSelectionne.setTotalCopies(Integer.parseInt(txtAnnee.getText()));
            // Note: On ne modifie généralement pas l'ID (ISBN)

            // Appel au Service
            service.modifierLivre(livreSelectionne);

            afficherSucces("Livre modifié avec succès !");
            viderChamps();
            chargerLivres();

        } catch (Exception e) {
            afficherErreur("Erreur lors de la modification", e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        Book livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
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
            List<Book> resultats;
            if (motCle == null || motCle.trim().isEmpty()) {
                resultats = service.getTousLesLivres();
            } else {
                // Appel au Service pour la recherche
                resultats = service.rechercherLivres(motCle);
            }
            tableLivres.getItems().setAll(resultats);
        } catch (SQLException e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }

    // --- Méthodes Utilitaires ---

    private void chargerLivres() {
        try {
            // Le contrôleur délègue la récupération des données au service
            List<Book> livres = service.getTousLesLivres();
            tableLivres.getItems().setAll(livres);
        } catch (SQLException e) {
            afficherErreur("Erreur de chargement", "Impossible de charger la liste des livres.\n" + e.getMessage());
        }
    }

    private void remplirChamps(Book livre) {
        txtIsbn.setText(livre.getIsbn());
        txtTitre.setText(livre.getTitle());
        txtAuteur.setText(livre.getAuthor());
        txtAnnee.setText(String.valueOf(livre.getTotalCopies()));
        // Désactiver le champ ISBN lors de la modification pour éviter les incohérences
        txtIsbn.setDisable(true);
    }

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

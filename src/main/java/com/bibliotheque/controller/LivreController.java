package com.bibliotheque.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.bibliotheque.model.Livre;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.exception.ValidationException;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur JavaFX pour la gestion des livres
 * Gère l'interaction entre la vue FXML et le service métier
 * Respecte l'architecture en couches : Controller -> Service -> DAO
 */
public class LivreController {

    // ========== Injection FXML ==========
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

    @FXML
    private Label labelStatistiques;

    // ========== Service métier ==========
    private BibliothequeService service;

    /**
     * Constructeur pour injection de dépendances
     */
    public LivreController(BibliothequeService service) {
        this.service = service;
    }

    /**
     * Constructeur par défaut pour le chargeur FXML
     */
    public LivreController() {
    }

    /**
     * Initialisation du contrôleur (appelée automatiquement par JavaFX)
     */
    @FXML
    public void initialize() {
        // Initialiser le service si non injecté
        if (this.service == null) {
            this.service = new BibliothequeService();
        }

        // Configurer les colonnes de la TableView
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        // Charger les données initiales
        chargerLivres();
        mettreAJourStatistiques();

        // Gestion de la sélection d'une ligne du tableau
        tableLivres.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirChamps(newSelection);
            }
        });
    }

    // ========== CRUD ==========

    /**
     * Ajouter un nouveau livre
     */
    @FXML
    private void handleAjouter() {
        try {
            // Validation des champs
            if (txtIsbn.getText().trim().isEmpty() ||
                    txtTitre.getText().trim().isEmpty() ||
                    txtAuteur.getText().trim().isEmpty() ||
                    txtAnnee.getText().trim().isEmpty()) {
                afficherErreur("Validation", "Tous les champs doivent être remplis");
                return;
            }

            // Créer l'objet Livre
            Livre nouveauLivre = new Livre(
                    txtIsbn.getText(),
                    txtTitre.getText(),
                    txtAuteur.getText(),
                    Integer.parseInt(txtAnnee.getText()));

            // Appeler le service
            service.ajouterLivre(nouveauLivre);

            afficherSucces("Livre ajouté avec succès !");
            viderChamps();
            chargerLivres();
            mettreAJourStatistiques();

        } catch (NumberFormatException e) {
            afficherErreur("Format invalide", "L'année doit être un nombre entier");
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    /**
     * Modifier un livre sélectionné
     */
    @FXML
    private void handleModifier() {
        Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherErreur("Aucune sélection", "Veuillez sélectionner un livre à modifier");
            return;
        }

        try {
            // Validation
            if (txtTitre.getText().trim().isEmpty() ||
                    txtAuteur.getText().trim().isEmpty() ||
                    txtAnnee.getText().trim().isEmpty()) {
                afficherErreur("Validation", "Tous les champs doivent être remplis");
                return;
            }

            // Mettre à jour l'objet
            livreSelectionne.setTitre(txtTitre.getText());
            livreSelectionne.setAuteur(txtAuteur.getText());
            livreSelectionne.setAnneePublication(Integer.parseInt(txtAnnee.getText()));

            // Appeler le service
            service.modifierLivre(livreSelectionne);

            afficherSucces("Livre modifié avec succès !");
            viderChamps();
            chargerLivres();
            mettreAJourStatistiques();

        } catch (NumberFormatException e) {
            afficherErreur("Format invalide", "L'année doit être un nombre entier");
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            afficherErreur("Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    /**
     * Supprimer un livre sélectionné
     */
    @FXML
    private void handleSupprimer() {
        Livre livreSelectionne = tableLivres.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherErreur("Aucune sélection", "Veuillez sélectionner un livre à supprimer");
            return;
        }

        // Confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le livre ?");
        alert.setContentText("Voulez-vous vraiment supprimer le livre '" + livreSelectionne.getTitre() + "' ?");

        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                service.supprimerLivre(livreSelectionne.getIsbn());
                afficherSucces("Livre supprimé avec succès !");
                viderChamps();
                chargerLivres();
                mettreAJourStatistiques();
            } catch (ValidationException e) {
                afficherErreur("Erreur", e.getMessage());
            } catch (Exception e) {
                afficherErreur("Erreur", "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    /**
     * Rechercher des livres
     */
    @FXML
    private void handleRechercher() {
        String motCle = txtRecherche.getText();
        try {
            List<Livre> resultats;
            if (motCle == null || motCle.trim().isEmpty()) {
                resultats = service.getTousLesLivres();
            } else {
                resultats = service.rechercherLivres(motCle);
            }
            tableLivres.getItems().setAll(resultats);
        } catch (Exception e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }

    /**
     * Rafraîchir la liste des livres
     */
    @FXML
    private void handleRafraichir() {
        txtRecherche.clear();
        chargerLivres();
        mettreAJourStatistiques();
        afficherSucces("Liste rafraîchie");
    }

    /**
     * Afficher les livres disponibles
     */
    @FXML
    private void handleAfficherDisponibles() {
        try {
            List<Livre> disponibles = service.getLivresDisponibles();
            tableLivres.getItems().setAll(disponibles);
        } catch (Exception e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    // ========== Méthodes utilitaires ==========

    /**
     * Charge tous les livres dans la TableView
     */
    private void chargerLivres() {
        try {
            List<Livre> livres = service.getTousLesLivres();
            tableLivres.getItems().setAll(livres);
        } catch (Exception e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les livres: " + e.getMessage());
        }
    }

    /**
     * Remplit les champs de saisie avec les données d'un livre sélectionné
     */
    private void remplirChamps(Livre livre) {
        txtIsbn.setText(livre.getIsbn());
        txtTitre.setText(livre.getTitre());
        txtAuteur.setText(livre.getAuteur());
        txtAnnee.setText(String.valueOf(livre.getAnneePublication()));
        txtIsbn.setDisable(true);
    }

    /**
     * Vide tous les champs de saisie
     */
    @FXML
    private void viderChamps() {
        txtIsbn.clear();
        txtTitre.clear();
        txtAuteur.clear();
        txtAnnee.clear();
        txtIsbn.setDisable(false);
        tableLivres.getSelectionModel().clearSelection();
    }

    /**
     * Met à jour les statistiques affichées
     */
    private void mettreAJourStatistiques() {
        try {
            Map<String, Object> stats = service.obtenirStatistiques();
            String statsText = String.format(
                    "Total: %d | Disponibles: %d | Empruntés: %d",
                    stats.get("totalLivres"),
                    stats.get("livresDisponibles"),
                    stats.get("livresEmpruntes"));
            labelStatistiques.setText(statsText);
        } catch (Exception e) {
            labelStatistiques.setText("Erreur lors du calcul des stats");
        }
    }

    // ========== Affichage des messages ==========

    /**
     * Affiche un message de succès
     */
    private void afficherSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche un message d'erreur
     */
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

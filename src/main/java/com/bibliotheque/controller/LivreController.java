package com.bibliotheque.controller;

<<<<<<< HEAD
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.bibliotheque.model.Livre;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.exception.ValidationException;
import java.util.List;
import java.sql.SQLException;

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
    private BibliothequeService service;

    /**
     * Constructeur pour l'injection de dépendances
     */
    public LivreController(BibliothequeService service) {
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
            this.service = new BibliothequeService();
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
=======
import com.bibliotheque.model.Book;
import com.bibliotheque.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML controller for the landing page (MainView.fxml).
 * UI logic only: binds table to ObservableList and reacts to selection.
 * Controller MUST call BibliothequeService only (no DAO access).
 */
public class LivreController {
    private final BibliothequeService bibliothequeService;

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colAvailability;

    @FXML private Label detailTitle;
    @FXML private Label detailAuthor;
    @FXML private Label detailYear;
    @FXML private Label detailAvailability;
    @FXML private Label detailIsbn;

    private final ObservableList<Book> books = FXCollections.observableArrayList();

    public LivreController(BibliothequeService bibliothequeService) {
        this.bibliothequeService = bibliothequeService;
    }

    @FXML
    private void initialize() {
        // bind columns to properties (students implement PropertyValueFactory in detail)
        // load data via service
        books.clear();
        books.addAll(bibliothequeService.listLivres());
        booksTable.setItems(books);

        // selection handling (UI only)
        booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            showDetails(newSel);
        });
    }

    @FXML
    private void onSearch() {
        // UI-only: students may implement filtering here using ObservableList / FilteredList
    }

    private void showDetails(Book b) {
        if (b == null) {
            detailTitle.setText("");
            detailAuthor.setText("");
            detailYear.setText("");
            detailAvailability.setText("");
            detailIsbn.setText("");
            return;
        }
        detailTitle.setText(b.getTitle());
        detailAuthor.setText(b.getAuthor());
        detailYear.setText(b.getYear() == null ? "" : b.getYear().toString());
        detailAvailability.setText(b.getAvailableCopies() > 0 ? "Available" : "Not available");
        detailIsbn.setText(b.getIsbn());
>>>>>>> e014484e0ecce728e18711c7d7edda1ec5b547bb
    }
}

package com.bibliotheque.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.service.BibliothequeService;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

public class LivreController implements Initializable {

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

	private final BibliothequeService service = new BibliothequeService();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// wire simple properties
		colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
		colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));

		// annee is an int; use a SimpleObjectProperty
		colAnnee.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getAnneePublication()));

		// disponible uses custom accessor estDisponible()
		colDisponible.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().estDisponible()));
		colDisponible.setCellFactory(tc -> new TableCell<>() {
			@Override
			protected void updateItem(Boolean available, boolean empty) {
				super.updateItem(available, empty);
				if (empty || available == null) {
					setText(null);
				} else {
					setText(available ? "Oui" : "Non");
				}
			}
		});

		loadLivres();

		// when a row is selected, populate the form fields for edit/delete
		tableLivres.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
			if (newSel != null) {
				txtIsbn.setText(newSel.getIsbn());
				txtTitre.setText(newSel.getTitre());
				txtAuteur.setText(newSel.getAuteur());
				txtAnnee.setText(String.valueOf(newSel.getAnneePublication()));
			} else {
				viderChamps(new ActionEvent());
			}
		});
	}

	private void loadLivres() {
		List<Livre> livres = service.getTousLesLivres();
		ObservableList<Livre> items = FXCollections.observableArrayList(livres);
		tableLivres.setItems(items);
	}

	@FXML
	void handleAjouter(ActionEvent event) {
		String isbn = txtIsbn == null ? null : txtIsbn.getText();
		String titre = txtTitre == null ? null : txtTitre.getText();
		String auteur = txtAuteur == null ? null : txtAuteur.getText();
		String anneeStr = txtAnnee == null ? null : txtAnnee.getText();

		int annee = 0;
		try {
			annee = Integer.parseInt(anneeStr.trim());
		} catch (Exception e) {
			System.err.println("Année invalide: " + anneeStr);
		}

		// basic validation
		if (isbn == null || isbn.trim().isEmpty() || titre == null || titre.trim().isEmpty()) {
			System.err.println("ISBN et Titre sont obligatoires");
			return;
		}

		Livre livre = new Livre(isbn.trim(), titre.trim(), auteur == null ? "" : auteur.trim(), annee <= 0 ? 0 : annee);

		try {
			service.ajouterLivre(livre);
			System.out.println("Livre ajouté: " + livre.getTitre());
			loadLivres();
			viderChamps(event);
		} catch (ValidationException ex) {
			System.err.println("Erreur ajout livre: " + ex.getMessage());
		} catch (RuntimeException ex) {
			System.err.println("Erreur technique lors de l'ajout: " + ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}

	@FXML
	void handleModifier(ActionEvent event) {
		Livre selected = tableLivres.getSelectionModel().getSelectedItem();
		if (selected == null) {
			System.err.println("Aucun livre sélectionné pour modification");
			return;
		}

		String titre = txtTitre == null ? null : txtTitre.getText();
		String auteur = txtAuteur == null ? null : txtAuteur.getText();
		String anneeStr = txtAnnee == null ? null : txtAnnee.getText();

		int annee = selected.getAnneePublication();
		try {
			if (anneeStr != null && !anneeStr.isBlank()) annee = Integer.parseInt(anneeStr.trim());
		} catch (NumberFormatException e) {
			System.err.println("Année invalide: " + anneeStr);
		}

		// apply changes to the model
		selected.setTitre(titre == null ? selected.getTitre() : titre.trim());
		selected.setAuteur(auteur == null ? selected.getAuteur() : auteur.trim());
		selected.setAnneePublication(annee);

		try {
			service.modifierLivre(selected);
			System.out.println("Livre modifié: " + selected.getTitre());
			loadLivres();
		} catch (ValidationException ex) {
			System.err.println("Erreur modification livre: " + ex.getMessage());
		} catch (RuntimeException ex) {
			System.err.println("Erreur technique lors de la modification: " + ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}

	@FXML
	void handleSupprimer(ActionEvent event) {
		Livre selected = tableLivres.getSelectionModel().getSelectedItem();
		if (selected == null) {
			System.err.println("Aucun livre sélectionné pour suppression");
			return;
		}
		String isbn = selected.getIsbn();
		try {
			service.supprimerLivre(isbn);
			System.out.println("Livre supprimé: " + isbn);
			loadLivres();
			viderChamps(event);
		} catch (ValidationException ex) {
			System.err.println("Erreur suppression livre: " + ex.getMessage());
		} catch (RuntimeException ex) {
			System.err.println("Erreur technique lors de la suppression: " + ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}

	@FXML
	void handleRafraichir(ActionEvent event) {
		loadLivres();
	}

	@FXML
	void viderChamps(ActionEvent event) {
		if (txtIsbn != null) txtIsbn.clear();
		if (txtTitre != null) txtTitre.clear();
		if (txtAuteur != null) txtAuteur.clear();
		if (txtAnnee != null) txtAnnee.clear();
	}

	@FXML
	void handleRechercher(ActionEvent event) {
		// simple search by titre or auteur
		String motCle = txtRecherche == null ? null : txtRecherche.getText();
		if (motCle == null || motCle.trim().isEmpty()) {
			loadLivres();
			return;
		}
		ObservableList<Livre> results = FXCollections.observableArrayList(service.rechercherParTitre(motCle));
		if (results.isEmpty()) results = FXCollections.observableArrayList(service.rechercherParAuteur(motCle));
		tableLivres.setItems(results);
	}

	@FXML
	void handleMembres(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Membre.fxml"));
			Parent root = loader.load();
			MembreController controller = loader.getController();
			controller.setService(service);
			Stage stage = new Stage();
			stage.setTitle("Gestion des Membres");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.bibliotheque.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LivreController {

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
	private TableView<?> tableLivres;

	@FXML
	private TableColumn<?, ?> colIsbn;

	@FXML
	private TableColumn<?, ?> colTitre;

	@FXML
	private TableColumn<?, ?> colAuteur;

	@FXML
	private TableColumn<?, ?> colAnnee;

	@FXML
	private TableColumn<?, ?> colDisponible;

	@FXML
	void handleAjouter(ActionEvent event) {
		// TODO: implement ajouter logic
	}

	@FXML
	void handleModifier(ActionEvent event) {
		// TODO: implement modifier logic
	}

	@FXML
	void handleSupprimer(ActionEvent event) {
		// TODO: implement supprimer logic
	}

	@FXML
	void handleRafraichir(ActionEvent event) {
		// TODO: implement rafraichir logic
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
		// TODO: implement rechercher logic
	}
}

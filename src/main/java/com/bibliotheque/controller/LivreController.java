package com.bibliotheque.controller;

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
    }
}

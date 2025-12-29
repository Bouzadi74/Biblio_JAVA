package com.bibliotheque.controller;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Book;
import com.bibliotheque.service.BookService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BookController {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField copiesField;
    @FXML
    private Label statusLabel;

    public BookController() {
    }

    @FXML
    private void onAddBook(ActionEvent event) {
        try {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            // keep parsing but don't call external service (adapter not available)
            Integer.parseInt(copiesField.getText().trim());

            statusLabel.setText("Book accepted (no service wired)");
            isbnField.clear(); titleField.clear(); authorField.clear(); copiesField.clear();
        } catch (NumberFormatException nfe) {
            statusLabel.setText("Copies must be a number");
        } catch (Exception e) {
            statusLabel.setText("Unexpected error");
        }
    }
}

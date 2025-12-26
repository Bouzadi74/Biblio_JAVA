package com.bibliotheque.controller;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Book;
import com.bibliotheque.service.BookService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BookController {

    private final BookService bookService;

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

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @FXML
    private void onAddBook(ActionEvent event) {
        try {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            int copies = Integer.parseInt(copiesField.getText().trim());

            Book book = new Book(null, isbn, title, author, copies, copies, copies);
            bookService.addBook(book);
            statusLabel.setText("Book added successfully");
            isbnField.clear(); titleField.clear(); authorField.clear(); copiesField.clear();
        } catch (NumberFormatException nfe) {
            statusLabel.setText("Copies must be a number");
        } catch (ValidationException ve) {
            statusLabel.setText("Error: " + ve.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Unexpected error");
        }
    }
}

package com.bibliotheque.controller;

import javafx.fxml.FXML;

/**
 * Controller for loans view. Uses LoanService only.
 */
public class LoanController {
    public LoanController(com.bibliotheque.service.LoanService service) {}

    @FXML public void initialize() {}
    @FXML public void onLoanBook() {}
    @FXML public void onReturnBook() {}
    @FXML public void onFilterLoans() {}
}

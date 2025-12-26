package com.bibliotheque.controller;

import javafx.fxml.FXML;

/**
 * Controller for members view. Should only call MemberService (injected via constructor in real app).
 */
public class MemberController {
    public MemberController(com.bibliotheque.service.MemberService service) {}

    @FXML public void initialize() {}
    @FXML public void onRegisterMember() {}
    @FXML public void onEditMember() {}
    @FXML public void onDeleteMember() {}
    @FXML public void onSearchMember() {}
}

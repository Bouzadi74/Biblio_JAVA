package com.bibliotheque.model;

public interface Empruntable {
    boolean estDisponible();
    void setDisponible(boolean disponible);
}
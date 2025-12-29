package com.bibliotheque.model;

<<<<<<< HEAD
/**
 * Interface définissant les contrats d'emprunt/retour pour les ressources
 * empruntables
 */
public interface Empruntable {
    /**
     * Vérifie si le document est disponible
     * 
     * @return true si disponible, false sinon
     */
    boolean estDisponible();

    /**
     * Définit la disponibilité du document
     * 
     * @param disponible true pour disponible, false pour emprunté
     */
    void setDisponible(boolean disponible);

    /**
     * Emprunte le document (le marque comme non disponible)
     * 
     * @throws IllegalStateException si le document n'est pas disponible
     */
    void emprunter() throws IllegalStateException;

    /**
     * Retourne le document (le marque comme disponible)
     */
    void retourner();
}
=======
public interface Empruntable {
    boolean estDisponible();
    void setDisponible(boolean disponible);
}
>>>>>>> 23e1e4fb064834a1628e4ab848cde59643820c49

package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;
import java.util.List;

public interface EmpruntDAO {
    
    /**
     * Enregistrer un nouvel emprunt
     * @param emprunt L'emprunt à sauvegarder
     * @return true si succès, false sinon
     */
    boolean save(Emprunt emprunt);
    
    /**
     * Rechercher un emprunt par son ID
     * @param id L'identifiant de l'emprunt
     * @return L'emprunt trouvé ou null
     */
    Emprunt findById(int id);
    
    /**
     * Récupérer tous les emprunts
     * @return Liste de tous les emprunts
     */
    List<Emprunt> findAll();
    
    /**
     * Mettre à jour un emprunt existant
     * @param emprunt L'emprunt à mettre à jour
     * @return true si succès, false sinon
     */
    boolean update(Emprunt emprunt);
    
    /**
     * Rechercher les emprunts d'un membre spécifique
     * @param membreId L'identifiant du membre
     * @return Liste des emprunts du membre
     */
    List<Emprunt> findByMembre(int membreId);
    
    /**
     * Rechercher les emprunts en cours pour un membre
     * @param membreId L'identifiant du membre
     * @return Liste des emprunts en cours
     */
    List<Emprunt> findEnCours(int membreId);
    
    /**
     * Compter le nombre d'emprunts en cours pour un membre
     * @param membreId L'identifiant du membre
     * @return Le nombre d'emprunts en cours
     */
    int countEmpruntsEnCours(int membreId);
}
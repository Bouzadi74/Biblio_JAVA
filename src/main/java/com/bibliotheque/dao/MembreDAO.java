package com.bibliotheque.dao;

import com.bibliotheque.model.Membre;
import java.util.List;
import java.util.Optional;

public interface MembreDAO {

    Membre save(Membre membre);

    Optional<Membre> findById(Long id);

    Optional<Membre> findByEmail(String email);

    List<Membre> findAll();

    List<Membre> findActifs();

    Membre update(Membre membre);

    boolean delete(Long id);
}

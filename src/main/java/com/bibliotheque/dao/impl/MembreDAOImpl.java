package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.model.Membre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MembreDAOImpl implements MembreDAO {

    private static final List<Membre> DB = new ArrayList<>();
    private static final AtomicLong ID_GEN = new AtomicLong(0);

    @Override
    public Membre save(Membre membre) {
        if (membre.getId() == null) {
            membre.setId(ID_GEN.incrementAndGet());
        }
        DB.add(membre);
        return membre;
    }

    @Override
    public Optional<Membre> findById(Long id) {
        return DB.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Membre> findByEmail(String email) {
        if (email == null) return Optional.empty();
        String e = email.trim().toLowerCase();
        return DB.stream()
                .filter(m -> m.getEmail() != null && m.getEmail().trim().toLowerCase().equals(e))
                .findFirst();
    }

    @Override
    public List<Membre> findAll() {
        return new ArrayList<>(DB);
    }

    @Override
    public List<Membre> findActifs() {
        return DB.stream().filter(Membre::isActif).collect(Collectors.toList());
    }

    @Override
    public Membre update(Membre membre) {
        if (membre.getId() == null) {
            throw new IllegalArgumentException("Impossible de modifier: id null");
        }
        delete(membre.getId());
        DB.add(membre);
        return membre;
    }

    @Override
    public boolean delete(Long id) {
        return DB.removeIf(m -> m.getId().equals(id));
    }
}

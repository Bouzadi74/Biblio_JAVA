package com.bibliotheque.dao;

import com.bibliotheque.model.Book;
import com.bibliotheque.infra.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Skeleton implementation for LivreDAO. Real SQL and resource handling to be implemented by students.
 */
public class LivreDAOImpl implements LivreDAO {
    private final DatabaseConnection db;

    public LivreDAOImpl(DatabaseConnection db) {
        this.db = db;
    }

    @Override
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        // Example of how DAO should use PreparedStatement (no real SQL execution here)
        // Connection conn = (Connection) db.getConnection();
        // PreparedStatement ps = conn.prepareStatement("SELECT id, isbn, titre, auteur, annee, total_copies, available_copies FROM LIVRE");
        // ResultSet rs = ps.executeQuery();
        // while (rs.next()) { ... }
        return list;
    }

    @Override
    public Optional<Book> findById(Integer id) { return Optional.empty(); }

    @Override
    public void insert(Book book) { /* use PreparedStatement to insert into LIVRE */ }

    @Override
    public void update(Book book) { /* use PreparedStatement to update LIVRE */ }

    @Override
    public void delete(Integer id) { /* use PreparedStatement to delete from LIVRE */ }
}

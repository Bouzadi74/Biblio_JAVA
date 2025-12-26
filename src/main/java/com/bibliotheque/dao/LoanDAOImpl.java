package com.bibliotheque.dao;

import com.bibliotheque.model.Loan;
import java.util.List;

/**
 * Concrete LoanDAO implementation (persistence omitted)
 */
public class LoanDAOImpl implements LoanDAO {
    public LoanDAOImpl(com.bibliotheque.infra.DatabaseConnection db) {}

    public List<Loan> findAll() { return java.util.Collections.emptyList(); }
    public java.util.Optional<Loan> findById(Integer id) { return java.util.Optional.empty(); }
    public void insert(Loan entity) {}
    public void update(Loan entity) {}
    public void delete(Integer id) {}
    public List<Loan> findByMemberId(Integer memberId) { return java.util.Collections.emptyList(); }
    public List<Loan> findActiveByBookId(Integer bookId) { return java.util.Collections.emptyList(); }
}

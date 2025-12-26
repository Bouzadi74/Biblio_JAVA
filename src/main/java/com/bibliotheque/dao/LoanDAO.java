package com.bibliotheque.dao;

import com.bibliotheque.model.Loan;
import java.util.List;

/**
 * DAO interface for Loan entity
 */
public interface LoanDAO extends GenericDAO<Loan, Integer> {
    List<Loan> findByMemberId(Integer memberId);
    List<Loan> findActiveByBookId(Integer bookId);
}

package com.bibliotheque.service;

import com.bibliotheque.model.Loan;

/**
 * Business operations for loans (emprunts)
 */
public interface LoanService {
    java.util.List<Loan> listLoans();
    Loan getLoan(Integer id) throws com.bibliotheque.exception.NotFoundException;
    void loanBook(Integer bookId, Integer memberId) throws com.bibliotheque.exception.ValidationException, com.bibliotheque.exception.BusinessException;
    void returnBook(Integer loanId) throws com.bibliotheque.exception.NotFoundException, com.bibliotheque.exception.BusinessException;
}

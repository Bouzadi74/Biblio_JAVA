package com.bibliotheque.service;

import com.bibliotheque.dao.LoanDAO;

/**
 * Loan service implementation skeleton. Business rules omitted.
 */
public class LoanServiceImpl implements LoanService {
    private final LoanDAO loanDAO;
    private final com.bibliotheque.dao.BookDAO bookDAO;
    private final com.bibliotheque.dao.MemberDAO memberDAO;

    public LoanServiceImpl(LoanDAO loanDAO, com.bibliotheque.dao.BookDAO bookDAO, com.bibliotheque.dao.MemberDAO memberDAO) {
        this.loanDAO = loanDAO;
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
    }

    public java.util.List<com.bibliotheque.model.Loan> listLoans() { return java.util.Collections.emptyList(); }
    public com.bibliotheque.model.Loan getLoan(Integer id) throws com.bibliotheque.exception.NotFoundException { return null; }
    public void loanBook(Integer bookId, Integer memberId) throws com.bibliotheque.exception.ValidationException, com.bibliotheque.exception.BusinessException {}
    public void returnBook(Integer loanId) throws com.bibliotheque.exception.NotFoundException, com.bibliotheque.exception.BusinessException {}
}

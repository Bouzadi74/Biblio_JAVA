package com.bibliotheque.model;

import java.time.LocalDate;

/**
 * Entity representing a loan (emprunt)
 */
public class Loan {
    private Integer id;
    private Integer bookId;
    private Integer memberId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnedDate; // nullable when not returned

    public Loan() {}

    public Loan(Integer id, Integer bookId, Integer memberId, LocalDate loanDate, LocalDate dueDate, LocalDate returnedDate) {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnedDate() { return returnedDate; }
    public void setReturnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; }
}

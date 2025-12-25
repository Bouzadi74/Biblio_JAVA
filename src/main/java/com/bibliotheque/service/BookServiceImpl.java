package com.bibliotheque.service;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Book;

public class BookServiceImpl implements BookService {

    @Override
    public void addBook(Book book) throws ValidationException {
        if (book == null) throw new ValidationException("Book is null");
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty())
            throw new ValidationException("ISBN is required");
        if (book.getTitle() == null || book.getTitle().trim().isEmpty())
            throw new ValidationException("Title is required");
        if (book.getTotalCopies() < 0) throw new ValidationException("Copies must be >= 0");

        // TODO: delegate to DAO for persistence. This is a minimal skeleton.
        System.out.println("[BookService] Persisting book: " + book.getTitle());
    }
}

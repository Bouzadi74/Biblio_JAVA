package com.bibliotheque.service;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Book;

public interface BookService {
    void addBook(Book book) throws ValidationException;
}

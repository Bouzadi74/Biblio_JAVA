package com.bibliotheque.exception;

/**
 * Thrown when an entity is not found.
 */
public class NotFoundException extends Exception {
    public NotFoundException(String message) { super(message); }
}

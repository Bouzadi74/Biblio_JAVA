package com.bibliotheque.exception;

/**
 * Generic business exception for service-layer errors.
 */
public class BusinessException extends Exception {
    public BusinessException(String message) { super(message); }
}

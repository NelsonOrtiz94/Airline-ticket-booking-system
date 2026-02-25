package org.example.domain.exception;

/**
 * Excepción de dominio cuando una reservación es inválida
 */
public class InvalidBookingException extends DomainException {
    public InvalidBookingException(String message) {
        super(message);
    }
}


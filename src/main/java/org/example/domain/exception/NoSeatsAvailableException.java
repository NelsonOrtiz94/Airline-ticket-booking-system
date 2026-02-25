package org.example.domain.exception;

/**
 * Excepción de dominio cuando no hay asientos disponibles
 */
public class NoSeatsAvailableException extends DomainException {
    public NoSeatsAvailableException(String message) {
        super(message);
    }
}


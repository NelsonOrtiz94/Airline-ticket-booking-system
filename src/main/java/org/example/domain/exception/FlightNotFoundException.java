package org.example.domain.exception;

/**
 * Excepción de dominio cuando un vuelo no es encontrado
 */
public class FlightNotFoundException extends DomainException {
    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(Long flightId) {
        super("Flight not found with ID: " + flightId);
    }
}


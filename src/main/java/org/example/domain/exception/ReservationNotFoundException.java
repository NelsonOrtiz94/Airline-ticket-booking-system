package org.example.domain.exception;

/**
 * Excepción de dominio cuando una reservación no es encontrada
 */
public class ReservationNotFoundException extends DomainException {
    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(Long reservationId) {
        super("Reservation not found with ID: " + reservationId);
    }
}


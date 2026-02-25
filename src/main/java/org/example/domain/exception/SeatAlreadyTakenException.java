package org.example.domain.exception;

/**
 * Excepción de dominio cuando un asiento ya está ocupado
 */
public class SeatAlreadyTakenException extends DomainException {
    public SeatAlreadyTakenException(String seatNumber) {
        super("Seat " + seatNumber + " is already taken");
    }
}


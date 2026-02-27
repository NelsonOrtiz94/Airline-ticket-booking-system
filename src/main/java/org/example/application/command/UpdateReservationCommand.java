package org.example.application.command;


public record UpdateReservationCommand(
    Long reservationId,
    String seatNumber,
    String observations
) {
    public UpdateReservationCommand {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be positive");
        }
    }
}


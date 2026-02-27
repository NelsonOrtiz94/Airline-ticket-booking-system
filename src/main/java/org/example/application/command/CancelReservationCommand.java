package org.example.application.command;


public record CancelReservationCommand(
    Long reservationId,
    String reason
) {
    public CancelReservationCommand {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be positive");
        }
    }
}


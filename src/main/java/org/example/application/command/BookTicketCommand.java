package org.example.application.command;


public record BookTicketCommand(
    Long userId,
    Long flightId,
    String passengerName,
    String seatNumber,
    String ticketClass
) {
    public BookTicketCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (flightId == null || flightId <= 0) {
            throw new IllegalArgumentException("Flight ID must be positive");
        }
        if (passengerName == null || passengerName.isBlank()) {
            throw new IllegalArgumentException("Passenger name cannot be empty");
        }
        if (seatNumber == null || seatNumber.isBlank()) {
            throw new IllegalArgumentException("Seat number cannot be empty");
        }
        if (ticketClass == null || ticketClass.isBlank()) {
            throw new IllegalArgumentException("Ticket class cannot be empty");
             }
    }
}


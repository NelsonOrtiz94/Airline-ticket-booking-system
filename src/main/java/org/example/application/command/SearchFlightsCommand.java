package org.example.application.command;

import java.time.LocalDateTime;


public record SearchFlightsCommand(
    String origin,
    String destination,
    LocalDateTime departureDate,
    Integer passengers
) {
    public SearchFlightsCommand {
        if (origin == null || origin.isBlank()) {
            throw new IllegalArgumentException("Origin cannot be empty");
        }
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination cannot be empty");
        }
    }
}


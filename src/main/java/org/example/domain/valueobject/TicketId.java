package org.example.domain.valueobject;

/**
 * Value Object que representa el identificador único de un ticket
 */
public record TicketId(Long value) {
    public TicketId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Ticket ID must be positive");
        }
    }
}


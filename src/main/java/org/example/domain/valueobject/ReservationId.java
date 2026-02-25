package org.example.domain.valueobject;

/**
 * Value Object que representa el identificador único de una reservación
 */
public record ReservationId(Long value) {
    public ReservationId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Reservation ID must be positive");
        }
    }
}


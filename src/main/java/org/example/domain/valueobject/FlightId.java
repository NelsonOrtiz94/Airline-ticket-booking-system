package org.example.domain.valueobject;

/**
 * Value Object que representa el identificador único de un vuelo
 */
public record FlightId(Long value) {
    public FlightId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Flight ID must be positive");
        }
    }
}


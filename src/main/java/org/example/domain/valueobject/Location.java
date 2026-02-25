package org.example.domain.valueobject;

/**
 * Value Object que representa una ubicación (ciudad/aeropuerto)
 */
public record Location(String value) {
    public Location {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        if (value.length() < 3 || value.length() > 100) {
            throw new IllegalArgumentException("Location must be between 3 and 100 characters");
        }
    }
}


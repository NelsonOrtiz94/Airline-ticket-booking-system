package org.example.domain.valueobject;

/**
 * Value Object que representa una aerolínea
 */
public record Airline(String name) {
    public Airline {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Airline name cannot be empty");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Airline name must be between 2 and 100 characters");
        }
    }
}


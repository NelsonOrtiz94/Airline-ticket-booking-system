package org.example.domain.valueobject;

import java.util.regex.Pattern;

/**
 * Value Object que representa un número de asiento
 * Formato: 1-2 dígitos seguidos de una letra (ej: 12A, 5B)
 */
public record SeatNumber(String value) {
    private static final Pattern SEAT_PATTERN = Pattern.compile("^\\d{1,2}[A-F]$");

    public SeatNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Seat number cannot be empty");
        }
        if (!SEAT_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "Invalid seat number format. Expected format: 12A (1-2 digits + letter A-F)"
            );
        }
    }
}


package org.example.domain.valueobject;

import java.util.regex.Pattern;

/**
 * Value Object que representa un número de vuelo
 * Formato: 2-3 letras seguidas de 3-4 dígitos (ej: AA123, BA1234)
 */
public record FlightNumber(String value) {
    private static final Pattern FLIGHT_NUMBER_PATTERN =
        Pattern.compile("^[A-Z]{2,3}\\d{3,4}$");

    public FlightNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Flight number cannot be empty");
        }
        if (!FLIGHT_NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "Invalid flight number format. Expected format: AA123 or AAA1234"
            );
        }
    }
}


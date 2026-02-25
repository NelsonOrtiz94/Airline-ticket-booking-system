package org.example.domain.valueobject;

/**
 * Value Object que representa el identificador único de un usuario
 */
public record UserId(Long value) {
    public UserId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
    }
}


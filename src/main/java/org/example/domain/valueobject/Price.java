package org.example.domain.valueobject;

import java.math.BigDecimal;

/**
 * Value Object que representa un precio con validaciones
 */
public record Price(BigDecimal amount, String currency) {

    private static final String DEFAULT_CURRENCY = "USD";

    public Price {
        if (amount == null) {
            throw new IllegalArgumentException("Price amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (currency == null || currency.isBlank()) {
            currency = DEFAULT_CURRENCY;
        }
    }

    public Price(BigDecimal amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    public boolean isGreaterThan(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare prices with different currencies");
        }
        return this.amount.compareTo(other.amount) > 0;
    }

    public Price add(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add prices with different currencies");
        }
        return new Price(this.amount.add(other.amount), this.currency);
    }
}


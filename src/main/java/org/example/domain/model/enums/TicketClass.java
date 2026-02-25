package org.example.domain.model.enums;

/**
 * Clases de tickets disponibles.
 * Ubicado en la capa de dominio porque representa reglas de negocio core.
 * El multiplicador de precio es una regla de negocio importante.
 */
public enum TicketClass {
    ECONOMY("ECONOMY", "Clase económica", 1.0),
    PREMIUM_ECONOMY("PREMIUM_ECONOMY", "Clase económica premium", 1.5),
    BUSINESS("BUSINESS", "Clase ejecutiva", 2.5),
    FIRST_CLASS("FIRST_CLASS", "Primera clase", 4.0);

    private final String code;
    private final String description;
    private final double priceMultiplier;

    TicketClass(String code, String description, double priceMultiplier) {
        this.code = code;
        this.description = description;
        this.priceMultiplier = priceMultiplier;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public static TicketClass fromCode(String code) {
        for (TicketClass ticketClass : values()) {
            if (ticketClass.code.equals(code)) {
                return ticketClass;
            }
        }
        throw new IllegalArgumentException("Clase de ticket inválida: " + code);
    }
}


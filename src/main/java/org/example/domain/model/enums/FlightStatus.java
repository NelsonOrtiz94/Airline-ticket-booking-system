package org.example.domain.model.enums;


public enum FlightStatus {
    ACTIVE("ACTIVE", "Vuelo activo"),
    CANCELLED("CANCELLED", "Vuelo cancelado"),
    DELAYED("DELAYED", "Vuelo retrasado"),
    COMPLETED("COMPLETED", "Vuelo completado"),
    BOARDING("BOARDING", "Abordaje");

    private final String code;
    private final String description;

    FlightStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static FlightStatus fromCode(String code) {
        for (FlightStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de vuelo inválido: " + code);
    }
}


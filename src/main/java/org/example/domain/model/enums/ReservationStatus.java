package org.example.domain.model.enums;

/**
 * Estados posibles de una reserva.
 * Ubicado en la capa de dominio porque representa reglas de negocio core.
 */
public enum ReservationStatus {
    CONFIRMED("CONFIRMED", "Reserva confirmada"),
    CANCELLED("CANCELLED", "Reserva cancelada"),
    PENDING("PENDING", "Reserva pendiente"),
    EXPIRED("EXPIRED", "Reserva expirada");

    private final String code;
    private final String description;

    ReservationStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ReservationStatus fromCode(String code) {
        for (ReservationStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de reserva inválido: " + code);
    }
}


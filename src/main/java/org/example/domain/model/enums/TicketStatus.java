package org.example.domain.model.enums;

/**
 * Estados posibles de un ticket.
 * Ubicado en la capa de dominio porque representa reglas de negocio core.
 */
public enum TicketStatus {
    CONFIRMED("CONFIRMED", "Ticket confirmado"),
    CANCELLED("CANCELLED", "Ticket cancelado"),
    USED("USED", "Ticket usado"),
    REFUNDED("REFUNDED", "Ticket reembolsado");

    private final String code;
    private final String description;

    TicketStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TicketStatus fromCode(String code) {
        for (TicketStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de ticket inválido: " + code);
    }
}


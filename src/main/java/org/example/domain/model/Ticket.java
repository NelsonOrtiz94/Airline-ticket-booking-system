package org.example.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.valueobject.*;
import org.example.domain.model.enums.TicketClass;
import org.example.domain.model.enums.TicketStatus;

import java.time.LocalDateTime;

/**
 * Modelo de dominio puro de Ticket (sin dependencias de frameworks)
 */
@Getter
@Builder
public class Ticket {
    private TicketId id;
    private FlightId flightId;
    private UserId userId;
    private String passengerName;
    private SeatNumber seatNumber;
    private Price price;
    private TicketClass ticketClass;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Factory method para crear un nuevo ticket
     */
    public static Ticket create(
            UserId userId,
            FlightId flightId,
            String passengerName,
            SeatNumber seatNumber,
            Price price,
            TicketClass ticketClass
    ) {
        return Ticket.builder()
                .userId(userId)
                .flightId(flightId)
                .passengerName(passengerName)
                .seatNumber(seatNumber)
                .price(price)
                .ticketClass(ticketClass)
                .status(TicketStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Cancela el ticket
     */
    public void cancel() {
        if (status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled");
        }
        this.status = TicketStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Actualiza el número de asiento
     */
    public void updateSeatNumber(SeatNumber newSeatNumber) {
        if (status == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update seat number for cancelled ticket");
        }
        this.seatNumber = newSeatNumber;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica si el ticket está activo
     */
    public boolean isActive() {
        return status == TicketStatus.CONFIRMED;
    }

    /**
     * Verifica si el ticket está cancelado
     */
    public boolean isCancelled() {
        return status == TicketStatus.CANCELLED;
    }
}


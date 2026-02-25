package org.example.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.valueobject.*;
import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.valueobject.FlightId;
import org.example.domain.valueobject.ReservationId;
import org.example.domain.valueobject.TicketId;
import org.example.domain.valueobject.UserId;

import java.time.LocalDateTime;

/**
 * Modelo de dominio puro de Reservation (sin dependencias de frameworks)
 */
@Getter
@Builder
public class Reservation {
    private ReservationId id;
    private UserId userId;
    private TicketId ticketId;
    private FlightId flightId;
    private ReservationStatus status;
    private String observations;
    private LocalDateTime reservationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Factory method para crear una nueva reservación
     */
    public static Reservation create(
            UserId userId,
            FlightId flightId,
            TicketId ticketId
    ) {
        return Reservation.builder()
                .userId(userId)
                .flightId(flightId)
                .ticketId(ticketId)
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Confirma la reservación
     */
    public void confirm() {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot confirm a cancelled reservation");
        }
        this.status = ReservationStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Cancela la reservación
     */
    public void cancel(String reason) {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        this.status = ReservationStatus.CANCELLED;
        this.observations = reason;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Actualiza observaciones
     */
    public void updateObservations(String newObservations) {
        this.observations = newObservations;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica si la reservación está confirmada
     */
    public boolean isConfirmed() {
        return status == ReservationStatus.CONFIRMED;
    }

    /**
     * Verifica si la reservación está cancelada
     */
    public boolean isCancelled() {
        return status == ReservationStatus.CANCELLED;
    }

    /**
     * Verifica si la reservación está pendiente
     */
    public boolean isPending() {
        return status == ReservationStatus.PENDING;
    }
}


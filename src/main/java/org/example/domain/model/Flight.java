package org.example.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.exception.NoSeatsAvailableException;
import org.example.domain.valueobject.*;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;

import java.time.LocalDateTime;

/**
 * Modelo de dominio puro de Flight (sin dependencias de frameworks)
 */
@Getter
@Builder(toBuilder = true)
public class Flight {
    private FlightId id;
    private FlightNumber flightNumber;
    private Location origin;
    private Location destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private Integer totalSeats;
    private Price price;
    private Airline airline;
    private FlightStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Verifica si el vuelo tiene asientos disponibles
     */
    public boolean hasAvailableSeats(int requested) {
        return availableSeats != null && availableSeats >= requested;
    }

    /**
     * Reserva asientos en el vuelo
     */
    public void reserveSeats(int quantity) {
        if (!hasAvailableSeats(quantity)) {
            throw new NoSeatsAvailableException(
                "No hay suficientes asientos disponibles en el vuelo " + flightNumber.value()
            );
        }
        this.availableSeats -= quantity;
    }

    /**
     * Libera asientos reservados
     */
    public void releaseSeats(int quantity) {
        if (availableSeats + quantity > totalSeats) {
            throw new IllegalStateException("Cannot release more seats than total capacity");
        }
        this.availableSeats += quantity;
    }

    /**
     * Verifica si el vuelo es reservable
     */
    public boolean isBookable() {
        return status == FlightStatus.ACTIVE
            && departureTime.isAfter(LocalDateTime.now())
            && hasAvailableSeats(1);
    }

    /**
     * Verifica si el vuelo está activo
     */
    public boolean isActive() {
        return status == FlightStatus.ACTIVE;
    }

    /**
     * Calcula el porcentaje de ocupación
     */
    public double getOccupancyRate() {
        if (totalSeats == null || totalSeats == 0) {
            return 0.0;
        }
        int occupiedSeats = totalSeats - (availableSeats != null ? availableSeats : 0);
        return (double) occupiedSeats / totalSeats * 100;
    }
}


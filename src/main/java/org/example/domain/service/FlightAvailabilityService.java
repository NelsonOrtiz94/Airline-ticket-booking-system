package org.example.domain.service;

import org.example.domain.model.Flight;
import org.example.domain.model.enums.FlightStatus;

import java.time.LocalDateTime;

/**
 * Servicio de dominio para verificar disponibilidad de vuelos
 */
public class FlightAvailabilityService {

    /**
     * Verifica si un vuelo está disponible para reservas
     */
    public boolean isFlightAvailable(Flight flight) {
        return flight.isActive()
            && flight.hasAvailableSeats(1)
            && flight.getDepartureTime().isAfter(LocalDateTime.now());
    }

    /**
     * Verifica si un vuelo puede ser cancelado
     */
    public boolean canBeCancelled(Flight flight) {
        return flight.getStatus() != FlightStatus.CANCELLED
            && flight.getDepartureTime().isAfter(LocalDateTime.now());
    }

    /**
     * Calcula el tiempo restante hasta la salida del vuelo
     */
    public long getHoursUntilDeparture(Flight flight) {
        return java.time.Duration.between(
            LocalDateTime.now(),
            flight.getDepartureTime()
        ).toHours();
    }
}


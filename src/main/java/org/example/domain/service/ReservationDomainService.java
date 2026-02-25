package org.example.domain.service;

import org.example.domain.exception.InvalidBookingException;
import org.example.domain.exception.NoSeatsAvailableException;
import org.example.domain.model.Flight;
import org.example.domain.model.Reservation;

/**
 * Servicio de dominio para validación de reservaciones
 */
public class ReservationDomainService {

    /**
     * Valida si se puede realizar una reservación
     */
    public void validateReservation(Flight flight, int requestedSeats) {
        if (!flight.isBookable()) {
            throw new InvalidBookingException(
                "El vuelo " + flight.getFlightNumber().value() + " no está disponible para reservas"
            );
        }

        if (!flight.hasAvailableSeats(requestedSeats)) {
            throw new NoSeatsAvailableException(
                "No hay suficientes asientos disponibles en el vuelo " +
                flight.getFlightNumber().value()
            );
        }
    }

    /**
     * Valida si una reservación puede ser cancelada
     */
    public void validateCancellation(Reservation reservation) {
        if (reservation.isCancelled()) {
            throw new InvalidBookingException(
                "La reservación ya está cancelada"
            );
        }
    }

    /**
     * Valida si una reservación puede ser actualizada
     */
    public void validateUpdate(Reservation reservation) {
        if (reservation.isCancelled()) {
            throw new InvalidBookingException(
                "No se puede actualizar una reservación cancelada"
            );
        }
    }
}


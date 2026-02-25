package org.example.domain.service;

import org.example.domain.exception.InvalidBookingException;
import org.example.domain.exception.NoSeatsAvailableException;
import org.example.domain.model.Flight;
import org.example.domain.model.Reservation;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ReservationDomainService - 100% Coverage
 */
class ReservationDomainServiceTest {

    private ReservationDomainService reservationDomainService;
    private Flight bookableFlight;
    private Flight cancelledFlight;
    private Flight fullFlight;

    @BeforeEach
    void setUp() {
        reservationDomainService = new ReservationDomainService();

        bookableFlight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(50)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        cancelledFlight = Flight.builder()
                .id(new FlightId(2L))
                .flightNumber(new FlightNumber("AV102"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(50)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.CANCELLED)
                .build();

        fullFlight = Flight.builder()
                .id(new FlightId(3L))
                .flightNumber(new FlightNumber("AV103"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(0)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Debe pasar validación cuando el vuelo es reservable y hay asientos")
    void testValidateReservation_WhenValid_ShouldNotThrowException() {
        assertDoesNotThrow(() ->
            reservationDomainService.validateReservation(bookableFlight, 1)
        );
    }

    @Test
    @DisplayName("Debe lanzar InvalidBookingException cuando el vuelo está cancelado")
    void testValidateReservation_WhenFlightCancelled_ShouldThrowException() {
        assertThrows(InvalidBookingException.class, () ->
            reservationDomainService.validateReservation(cancelledFlight, 1)
        );
    }

    @Test
    @DisplayName("Debe lanzar NoSeatsAvailableException cuando no hay asientos suficientes")
    void testValidateReservation_WhenNoSeats_ShouldThrowException() {
        assertThrows(NoSeatsAvailableException.class, () ->
            reservationDomainService.validateReservation(bookableFlight, 100)
        );
    }

    @Test
    @DisplayName("Debe lanzar InvalidBookingException cuando vuelo lleno no es reservable")
    void testValidateReservation_WhenFlightFull_ShouldThrowException() {
        assertThrows(InvalidBookingException.class, () ->
            reservationDomainService.validateReservation(fullFlight, 1)
        );
    }

    @Test
    @DisplayName("Debe pasar validación de cancelación cuando reservación está confirmada")
    void testValidateCancellation_WhenConfirmed_ShouldNotThrowException() {
        Reservation confirmedReservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() ->
            reservationDomainService.validateCancellation(confirmedReservation)
        );
    }

    @Test
    @DisplayName("Debe lanzar InvalidBookingException cuando reservación ya está cancelada")
    void testValidateCancellation_WhenAlreadyCancelled_ShouldThrowException() {
        Reservation cancelledReservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CANCELLED)
                .reservationDate(LocalDateTime.now())
                .build();

        assertThrows(InvalidBookingException.class, () ->
            reservationDomainService.validateCancellation(cancelledReservation)
        );
    }

    @Test
    @DisplayName("Debe pasar validación de actualización cuando reservación está confirmada")
    void testValidateUpdate_WhenConfirmed_ShouldNotThrowException() {
        Reservation confirmedReservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() ->
            reservationDomainService.validateUpdate(confirmedReservation)
        );
    }

    @Test
    @DisplayName("Debe lanzar InvalidBookingException al actualizar reservación cancelada")
    void testValidateUpdate_WhenCancelled_ShouldThrowException() {
        Reservation cancelledReservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CANCELLED)
                .reservationDate(LocalDateTime.now())
                .build();

        assertThrows(InvalidBookingException.class, () ->
            reservationDomainService.validateUpdate(cancelledReservation)
        );
    }

    @Test
    @DisplayName("Debe validar múltiples asientos correctamente")
    void testValidateReservation_MultipleSeats_ShouldValidate() {
        assertDoesNotThrow(() ->
            reservationDomainService.validateReservation(bookableFlight, 5)
        );
    }
}


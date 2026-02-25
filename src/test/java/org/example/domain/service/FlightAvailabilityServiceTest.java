package org.example.domain.service;

import org.example.domain.model.Flight;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para FlightAvailabilityService - 100% Coverage
 */
class FlightAvailabilityServiceTest {

    private FlightAvailabilityService flightAvailabilityService;
    private Flight availableFlight;
    private Flight cancelledFlight;
    private Flight pastFlight;
    private Flight fullFlight;

    @BeforeEach
    void setUp() {
        flightAvailabilityService = new FlightAvailabilityService();

        availableFlight = Flight.builder()
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

        pastFlight = Flight.builder()
                .id(new FlightId(3L))
                .flightNumber(new FlightNumber("AV103"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().minusDays(1))
                .arrivalTime(LocalDateTime.now().minusDays(1).plusHours(2))
                .availableSeats(50)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        fullFlight = Flight.builder()
                .id(new FlightId(4L))
                .flightNumber(new FlightNumber("AV104"))
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

    // === isFlightAvailable Tests ===
    @Test
    @DisplayName("Debe retornar true cuando el vuelo está disponible")
    void testIsFlightAvailable_WhenAvailable_ShouldReturnTrue() {
        assertTrue(flightAvailabilityService.isFlightAvailable(availableFlight));
    }

    @Test
    @DisplayName("Debe retornar false cuando el vuelo está cancelado")
    void testIsFlightAvailable_WhenCancelled_ShouldReturnFalse() {
        assertFalse(flightAvailabilityService.isFlightAvailable(cancelledFlight));
    }

    @Test
    @DisplayName("Debe retornar false cuando el vuelo ya pasó")
    void testIsFlightAvailable_WhenPast_ShouldReturnFalse() {
        assertFalse(flightAvailabilityService.isFlightAvailable(pastFlight));
    }

    @Test
    @DisplayName("Debe retornar false cuando el vuelo está lleno")
    void testIsFlightAvailable_WhenFull_ShouldReturnFalse() {
        assertFalse(flightAvailabilityService.isFlightAvailable(fullFlight));
    }

    // === canBeCancelled Tests ===
    @Test
    @DisplayName("Debe retornar true cuando el vuelo puede ser cancelado")
    void testCanBeCancelled_WhenActive_ShouldReturnTrue() {
        assertTrue(flightAvailabilityService.canBeCancelled(availableFlight));
    }

    @Test
    @DisplayName("Debe retornar false cuando el vuelo ya está cancelado")
    void testCanBeCancelled_WhenAlreadyCancelled_ShouldReturnFalse() {
        assertFalse(flightAvailabilityService.canBeCancelled(cancelledFlight));
    }

    @Test
    @DisplayName("Debe retornar false cuando el vuelo ya pasó")
    void testCanBeCancelled_WhenPast_ShouldReturnFalse() {
        assertFalse(flightAvailabilityService.canBeCancelled(pastFlight));
    }

    // === getHoursUntilDeparture Tests ===
    @Test
    @DisplayName("Debe retornar horas positivas para vuelo futuro")
    void testGetHoursUntilDeparture_WhenFuture_ShouldReturnPositive() {
        long hours = flightAvailabilityService.getHoursUntilDeparture(availableFlight);
        assertTrue(hours > 0);
    }

    @Test
    @DisplayName("Debe retornar horas negativas para vuelo pasado")
    void testGetHoursUntilDeparture_WhenPast_ShouldReturnNegative() {
        long hours = flightAvailabilityService.getHoursUntilDeparture(pastFlight);
        assertTrue(hours < 0);
    }

    @Test
    @DisplayName("Debe calcular correctamente las horas")
    void testGetHoursUntilDeparture_ShouldCalculateCorrectly() {
        Flight flightIn24Hours = Flight.builder()
                .id(new FlightId(5L))
                .flightNumber(new FlightNumber("AV105"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusHours(24))
                .arrivalTime(LocalDateTime.now().plusHours(26))
                .availableSeats(50)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        long hours = flightAvailabilityService.getHoursUntilDeparture(flightIn24Hours);
        // Puede ser 23 o 24 dependiendo del momento exacto
        assertTrue(hours >= 23 && hours <= 24);
    }
}


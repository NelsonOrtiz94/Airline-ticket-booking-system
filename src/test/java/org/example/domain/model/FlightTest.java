package org.example.domain.model;

import org.example.domain.exception.NoSeatsAvailableException;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Flight - 100% Coverage
 */
class FlightTest {

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = Flight.builder()
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
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe retornar true cuando hay asientos disponibles")
    void testHasAvailableSeats_WhenSeatsAvailable_ShouldReturnTrue() {
        assertTrue(flight.hasAvailableSeats(10));
    }

    @Test
    @DisplayName("Debe retornar false cuando no hay suficientes asientos")
    void testHasAvailableSeats_WhenNotEnoughSeats_ShouldReturnFalse() {
        assertFalse(flight.hasAvailableSeats(60));
    }

    @Test
    @DisplayName("Debe retornar false cuando availableSeats es null")
    void testHasAvailableSeats_WhenAvailableSeatsNull_ShouldReturnFalse() {
        Flight flightWithNullSeats = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(null)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        assertFalse(flightWithNullSeats.hasAvailableSeats(1));
    }

    @Test
    @DisplayName("Debe disminuir asientos disponibles al reservar")
    void testReserveSeats_WhenSeatsAvailable_ShouldDecreaseAvailableSeats() {
        int initialSeats = flight.getAvailableSeats();
        flight.reserveSeats(10);
        assertEquals(initialSeats - 10, flight.getAvailableSeats());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no hay suficientes asientos para reservar")
    void testReserveSeats_WhenNotEnoughSeats_ShouldThrowException() {
        assertThrows(NoSeatsAvailableException.class, () -> flight.reserveSeats(60));
    }

    @Test
    @DisplayName("Debe aumentar asientos disponibles al liberar")
    void testReleaseSeats_ShouldIncreaseAvailableSeats() {
        flight.reserveSeats(10);
        flight.releaseSeats(5);
        assertEquals(45, flight.getAvailableSeats());
    }

    @Test
    @DisplayName("Debe lanzar excepción al liberar más asientos de los permitidos")
    void testReleaseSeats_WhenExceedsTotalSeats_ShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> flight.releaseSeats(10));
    }

    @Test
    @DisplayName("Debe ser reservable cuando está activo y tiene asientos disponibles")
    void testIsBookable_WhenActiveAndSeatsAvailable_ShouldReturnTrue() {
        assertTrue(flight.isBookable());
    }

    @Test
    @DisplayName("No debe ser reservable cuando no tiene asientos")
    void testIsBookable_WhenNoSeats_ShouldReturnFalse() {
        flight.reserveSeats(50);
        assertFalse(flight.isBookable());
    }

    @Test
    @DisplayName("No debe ser reservable cuando el vuelo está cancelado")
    void testIsBookable_WhenCancelled_ShouldReturnFalse() {
        Flight cancelledFlight = Flight.builder()
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
                .status(FlightStatus.CANCELLED)
                .build();

        assertFalse(cancelledFlight.isBookable());
    }

    @Test
    @DisplayName("No debe ser reservable cuando la fecha de salida ya pasó")
    void testIsBookable_WhenDeparturePassed_ShouldReturnFalse() {
        Flight pastFlight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
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

        assertFalse(pastFlight.isBookable());
    }

    @Test
    @DisplayName("Debe retornar true cuando el status es ACTIVE")
    void testIsActive_WhenStatusActive_ShouldReturnTrue() {
        assertTrue(flight.isActive());
    }

    @Test
    @DisplayName("Debe retornar false cuando el status no es ACTIVE")
    void testIsActive_WhenStatusNotActive_ShouldReturnFalse() {
        Flight cancelledFlight = Flight.builder()
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
                .status(FlightStatus.CANCELLED)
                .build();

        assertFalse(cancelledFlight.isActive());
    }

    @Test
    @DisplayName("Debe calcular tasa de ocupación correctamente")
    void testGetOccupancyRate_ShouldCalculateCorrectly() {
        flight.reserveSeats(25);
        assertEquals(50.0, flight.getOccupancyRate(), 0.01);
    }

    @Test
    @DisplayName("Debe retornar 0 cuando no hay reservas")
    void testGetOccupancyRate_WhenEmpty_ShouldReturnZero() {
        assertEquals(0.0, flight.getOccupancyRate(), 0.01);
    }

    @Test
    @DisplayName("Debe retornar 0 cuando totalSeats es null")
    void testGetOccupancyRate_WhenTotalSeatsNull_ShouldReturnZero() {
        Flight flightWithNullTotal = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(50)
                .totalSeats(null)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        assertEquals(0.0, flightWithNullTotal.getOccupancyRate(), 0.01);
    }

    @Test
    @DisplayName("Debe retornar 0 cuando totalSeats es 0")
    void testGetOccupancyRate_WhenTotalSeatsZero_ShouldReturnZero() {
        Flight flightWithZeroTotal = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(0)
                .totalSeats(0)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        assertEquals(0.0, flightWithZeroTotal.getOccupancyRate(), 0.01);
    }

    @Test
    @DisplayName("Debe calcular ocupación cuando availableSeats es null")
    void testGetOccupancyRate_WhenAvailableSeatsNull_ShouldCalculate() {
        Flight flightWithNullAvailable = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(null)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        assertEquals(100.0, flightWithNullAvailable.getOccupancyRate(), 0.01);
    }

    @Test
    @DisplayName("Debe retornar 100% ocupación cuando todos los asientos están reservados")
    void testGetOccupancyRate_WhenFullyBooked_ShouldReturn100() {
        flight.reserveSeats(50);
        assertEquals(100.0, flight.getOccupancyRate(), 0.01);
    }

    @Test
    @DisplayName("Debe verificar todos los getters del Flight")
    void testAllGetters_ShouldReturnCorrectValues() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departure = now.plusDays(1);
        LocalDateTime arrival = departure.plusHours(2);
        LocalDateTime updated = now.plusHours(1);

        Flight completeFlight = Flight.builder()
                .id(new FlightId(99L))
                .flightNumber(new FlightNumber("LA999"))
                .origin(new Location("CTG"))
                .destination(new Location("CLO"))
                .departureTime(departure)
                .arrivalTime(arrival)
                .availableSeats(100)
                .totalSeats(150)
                .price(new Price(BigDecimal.valueOf(999999)))
                .airline(new Airline("LATAM"))
                .status(FlightStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(updated)
                .build();

        assertEquals(new FlightId(99L), completeFlight.getId());
        assertEquals(new FlightNumber("LA999"), completeFlight.getFlightNumber());
        assertEquals(new Location("CTG"), completeFlight.getOrigin());
        assertEquals(new Location("CLO"), completeFlight.getDestination());
        assertEquals(departure, completeFlight.getDepartureTime());
        assertEquals(arrival, completeFlight.getArrivalTime());
        assertEquals(100, completeFlight.getAvailableSeats());
        assertEquals(150, completeFlight.getTotalSeats());
        assertEquals(new Price(BigDecimal.valueOf(999999)), completeFlight.getPrice());
        assertEquals(new Airline("LATAM"), completeFlight.getAirline());
        assertEquals(FlightStatus.ACTIVE, completeFlight.getStatus());
        assertEquals(now, completeFlight.getCreatedAt());
        assertEquals(updated, completeFlight.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe retornar null para updatedAt cuando no está configurado")
    void testGetUpdatedAt_WhenNotSet_ShouldReturnNull() {
        assertNull(flight.getUpdatedAt());
    }
}

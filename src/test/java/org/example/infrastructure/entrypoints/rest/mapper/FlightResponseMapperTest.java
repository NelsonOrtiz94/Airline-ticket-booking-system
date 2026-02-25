package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.domain.model.Flight;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.entrypoints.rest.dto.response.FlightResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para FlightResponseMapper - 100% Coverage
 */
class FlightResponseMapperTest {

    private FlightResponseMapper mapper;
    private Flight flight;

    @BeforeEach
    void setUp() {
        mapper = new FlightResponseMapper();

        flight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.of(2026, 2, 20, 10, 0))
                .arrivalTime(LocalDateTime.of(2026, 2, 20, 12, 0))
                .availableSeats(50)
                .totalSeats(100)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Debe convertir Flight a FlightResponseDTO correctamente")
    void testToResponse_ShouldMapAllFields() {
        FlightResponseDTO response = mapper.toResponse(flight);

        assertNotNull(response);
        assertEquals(1L, response.getFlightId());
        assertEquals("AV101", response.getFlightNumber());
        assertEquals("BOG", response.getOrigin());
        assertEquals("MDE", response.getDestination());
        assertEquals(LocalDateTime.of(2026, 2, 20, 10, 0), response.getDepartureTime());
        assertEquals(LocalDateTime.of(2026, 2, 20, 12, 0), response.getArrivalTime());
        assertEquals(50, response.getAvailableSeats());
        assertEquals(100, response.getTotalSeats());
        assertEquals(BigDecimal.valueOf(250000), response.getPrice());
        assertEquals("Avianca", response.getAirline());
        assertEquals("ACTIVE", response.getStatus());
    }

    @Test
    @DisplayName("Debe retornar null cuando Flight es null")
    void testToResponse_WhenFlightIsNull_ShouldReturnNull() {
        FlightResponseDTO response = mapper.toResponse(null);

        assertNull(response);
    }

    @Test
    @DisplayName("Debe manejar diferentes estados de vuelo")
    void testToResponse_WithDifferentStatus_ShouldMapCorrectly() {
        Flight cancelledFlight = Flight.builder()
                .id(new FlightId(2L))
                .flightNumber(new FlightNumber("AV102"))
                .origin(new Location("CTG"))
                .destination(new Location("CLO"))
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(2))
                .availableSeats(0)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(180000)))
                .airline(new Airline("LATAM"))
                .status(FlightStatus.CANCELLED)
                .build();

        FlightResponseDTO response = mapper.toResponse(cancelledFlight);

        assertEquals("CANCELLED", response.getStatus());
        assertEquals("LATAM", response.getAirline());
    }
}


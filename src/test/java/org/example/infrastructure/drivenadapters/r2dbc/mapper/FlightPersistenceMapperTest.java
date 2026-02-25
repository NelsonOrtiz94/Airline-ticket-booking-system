package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.Flight;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.drivenadapters.r2dbc.entity.FlightEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para FlightPersistenceMapper - 100% Coverage
 */
class FlightPersistenceMapperTest {

    private FlightPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FlightPersistenceMapper();
    }

    @Test
    @DisplayName("Debe convertir FlightEntity a Flight (dominio)")
    void testToDomain_ShouldMapAllFields() {
        LocalDateTime now = LocalDateTime.now();
        FlightEntity entity = FlightEntity.builder()
                .flightId(1L)
                .flightNumber("AV101")
                .origin("BOG")
                .destination("MDE")
                .departureTime(now)
                .arrivalTime(now.plusHours(2))
                .availableSeats(50)
                .totalSeats(100)
                .price(BigDecimal.valueOf(250000))
                .airline("Avianca")
                .status("ACTIVE")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Flight domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId().value());
        assertEquals("AV101", domain.getFlightNumber().value());
        assertEquals("BOG", domain.getOrigin().value());
        assertEquals("MDE", domain.getDestination().value());
        assertEquals(now, domain.getDepartureTime());
        assertEquals(now.plusHours(2), domain.getArrivalTime());
        assertEquals(50, domain.getAvailableSeats());
        assertEquals(100, domain.getTotalSeats());
        assertEquals(BigDecimal.valueOf(250000), domain.getPrice().amount());
        assertEquals("Avianca", domain.getAirline().name());
        assertEquals(FlightStatus.ACTIVE, domain.getStatus());
    }

    @Test
    @DisplayName("Debe retornar null cuando FlightEntity es null")
    void testToDomain_WhenEntityIsNull_ShouldReturnNull() {
        Flight domain = mapper.toDomain(null);

        assertNull(domain);
    }

    @Test
    @DisplayName("Debe convertir Flight (dominio) a FlightEntity")
    void testToEntity_ShouldMapAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Flight domain = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(now)
                .arrivalTime(now.plusHours(2))
                .availableSeats(50)
                .totalSeats(100)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        FlightEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(1L, entity.getFlightId());
        assertEquals("AV101", entity.getFlightNumber());
        assertEquals("BOG", entity.getOrigin());
        assertEquals("MDE", entity.getDestination());
        assertEquals(now, entity.getDepartureTime());
        assertEquals(now.plusHours(2), entity.getArrivalTime());
        assertEquals(50, entity.getAvailableSeats());
        assertEquals(100, entity.getTotalSeats());
        assertEquals(BigDecimal.valueOf(250000), entity.getPrice());
        assertEquals("Avianca", entity.getAirline());
        assertEquals("ACTIVE", entity.getStatus());
    }

    @Test
    @DisplayName("Debe retornar null cuando Flight es null")
    void testToEntity_WhenDomainIsNull_ShouldReturnNull() {
        FlightEntity entity = mapper.toEntity(null);

        assertNull(entity);
    }

    @Test
    @DisplayName("Debe manejar Flight sin ID (nuevo vuelo)")
    void testToEntity_WhenFlightHasNoId_ShouldMapWithNullId() {
        Flight domain = Flight.builder()
                .id(null)
                .flightNumber(new FlightNumber("AV102"))
                .origin(new Location("CTG"))
                .destination(new Location("CLO"))
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(1))
                .availableSeats(30)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(180000)))
                .airline(new Airline("LATAM"))
                .status(FlightStatus.ACTIVE)
                .build();

        FlightEntity entity = mapper.toEntity(domain);

        assertNull(entity.getFlightId());
    }

    @Test
    @DisplayName("Debe manejar status CANCELLED")
    void testToDomain_WithCancelledStatus_ShouldMapCorrectly() {
        FlightEntity entity = FlightEntity.builder()
                .flightId(2L)
                .flightNumber("AV102")
                .origin("CTG")
                .destination("CLO")
                .departureTime(LocalDateTime.now())
                .arrivalTime(LocalDateTime.now().plusHours(1))
                .availableSeats(0)
                .totalSeats(50)
                .price(BigDecimal.valueOf(180000))
                .airline("LATAM")
                .status("CANCELLED")
                .build();

        Flight domain = mapper.toDomain(entity);

        assertEquals(FlightStatus.CANCELLED, domain.getStatus());
    }
}


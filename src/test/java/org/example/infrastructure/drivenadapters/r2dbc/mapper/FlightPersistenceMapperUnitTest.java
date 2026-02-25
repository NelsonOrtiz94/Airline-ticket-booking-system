package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.Flight;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.drivenadapters.r2dbc.entity.FlightEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightPersistenceMapperUnitTest {

    private final FlightPersistenceMapper mapper = new FlightPersistenceMapper();

    @Test
    void toDomain_and_toEntity_should_map_correctly() {
        LocalDateTime now = LocalDateTime.now();

        FlightEntity entity = FlightEntity.builder()
                .flightId(1L)
                .flightNumber("AV101")
                .origin("BOG")
                .destination("MDE")
                .departureTime(now)
                .arrivalTime(now.plusHours(2))
                .availableSeats(10)
                .totalSeats(100)
                .price(new BigDecimal("123.45"))
                .airline("Avianca")
                .status(FlightStatus.ACTIVE.name())
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
        assertEquals(10, domain.getAvailableSeats());
        assertEquals(100, domain.getTotalSeats());
        assertEquals(new BigDecimal("123.45"), domain.getPrice().amount());
        assertEquals("Avianca", domain.getAirline().name());
        assertEquals(FlightStatus.ACTIVE, domain.getStatus());

        FlightEntity mappedBack = mapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals(entity.getFlightId(), mappedBack.getFlightId());
        assertEquals(entity.getFlightNumber(), mappedBack.getFlightNumber());
        assertEquals(entity.getOrigin(), mappedBack.getOrigin());
        assertEquals(entity.getDestination(), mappedBack.getDestination());
        assertEquals(entity.getDepartureTime(), mappedBack.getDepartureTime());
        assertEquals(entity.getArrivalTime(), mappedBack.getArrivalTime());
        assertEquals(entity.getAvailableSeats(), mappedBack.getAvailableSeats());
        assertEquals(entity.getTotalSeats(), mappedBack.getTotalSeats());
        assertEquals(entity.getPrice(), mappedBack.getPrice());
        assertEquals(entity.getAirline(), mappedBack.getAirline());
        assertEquals(entity.getStatus(), mappedBack.getStatus());
    }
}


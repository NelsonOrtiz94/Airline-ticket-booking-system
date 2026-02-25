package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.Reservation;
import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.drivenadapters.r2dbc.entity.ReservationEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationPersistenceMapperUnitTest {

    private final ReservationPersistenceMapper mapper = new ReservationPersistenceMapper();

    @Test
    void toDomain_and_toEntity_should_map_correctly() {
        LocalDateTime now = LocalDateTime.now();

        ReservationEntity entity = ReservationEntity.builder()
                .reservationId(1L)
                .userId(2L)
                .ticketId(3L)
                .flightId(4L)
                .status(ReservationStatus.CONFIRMED.name())
                .observations("No observations")
                .reservationDate(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Reservation domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals(1L, domain.getId().value());
        assertEquals(2L, domain.getUserId().value());
        assertEquals(3L, domain.getTicketId().value());
        assertEquals(4L, domain.getFlightId().value());
        assertEquals(ReservationStatus.CONFIRMED, domain.getStatus());
        assertEquals("No observations", domain.getObservations());
        assertEquals(now, domain.getReservationDate());

        ReservationEntity mappedBack = mapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals(entity.getReservationId(), mappedBack.getReservationId());
        assertEquals(entity.getUserId(), mappedBack.getUserId());
        assertEquals(entity.getTicketId(), mappedBack.getTicketId());
        assertEquals(entity.getFlightId(), mappedBack.getFlightId());
        assertEquals(entity.getStatus(), mappedBack.getStatus());
        assertEquals(entity.getObservations(), mappedBack.getObservations());
        assertEquals(entity.getReservationDate(), mappedBack.getReservationDate());
    }
}


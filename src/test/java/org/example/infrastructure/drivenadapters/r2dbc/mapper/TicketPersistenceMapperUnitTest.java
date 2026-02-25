package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.Ticket;
import org.example.domain.model.enums.TicketClass;
import org.example.domain.model.enums.TicketStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.drivenadapters.r2dbc.entity.TicketEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketPersistenceMapperUnitTest {

    private final TicketPersistenceMapper mapper = new TicketPersistenceMapper();

    @Test
    void toDomain_and_toEntity_should_map_correctly() {
        LocalDateTime now = LocalDateTime.now();

        TicketEntity entity = TicketEntity.builder()
                .ticketId(3L)
                .flightId(4L)
                .userId(2L)
                .passengerName("Juan Perez")
                .seatNumber("12A")
                .price(new BigDecimal("200.00"))
                .ticketClass(TicketClass.ECONOMY.name())
                .status(TicketStatus.CONFIRMED.name())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Ticket domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals(3L, domain.getId().value());
        assertEquals(4L, domain.getFlightId().value());
        assertEquals(2L, domain.getUserId().value());
        assertEquals("Juan Perez", domain.getPassengerName());
        assertEquals("12A", domain.getSeatNumber().value());
        assertEquals(new BigDecimal("200.00"), domain.getPrice().amount());
        assertEquals(TicketClass.ECONOMY, domain.getTicketClass());
        assertEquals(TicketStatus.CONFIRMED, domain.getStatus());

        TicketEntity mappedBack = mapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals(entity.getTicketId(), mappedBack.getTicketId());
        assertEquals(entity.getFlightId(), mappedBack.getFlightId());
        assertEquals(entity.getUserId(), mappedBack.getUserId());
        assertEquals(entity.getPassengerName(), mappedBack.getPassengerName());
        assertEquals(entity.getSeatNumber(), mappedBack.getSeatNumber());
        assertEquals(entity.getPrice(), mappedBack.getPrice());
        assertEquals(entity.getTicketClass(), mappedBack.getTicketClass());
        assertEquals(entity.getStatus(), mappedBack.getStatus());
    }
}

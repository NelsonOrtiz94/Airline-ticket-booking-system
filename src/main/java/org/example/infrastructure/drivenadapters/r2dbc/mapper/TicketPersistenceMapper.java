package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.Ticket;
import org.example.domain.valueobject.*;
import org.example.infrastructure.drivenadapters.r2dbc.entity.TicketEntity;
import org.example.domain.model.enums.TicketClass;
import org.example.domain.model.enums.TicketStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Ticket (dominio) y TicketEntity (persistencia)
 */
@Component
public class TicketPersistenceMapper {

    public Ticket toDomain(TicketEntity entity) {
        if (entity == null) {
            return null;
        }

        return Ticket.builder()
                .id(new TicketId(entity.getTicketId()))
                .flightId(new FlightId(entity.getFlightId()))
                .userId(new UserId(entity.getUserId()))
                .passengerName(entity.getPassengerName())
                .seatNumber(new SeatNumber(entity.getSeatNumber()))
                .price(new Price(entity.getPrice()))
                .ticketClass(TicketClass.valueOf(entity.getTicketClass()))
                .status(TicketStatus.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public TicketEntity toEntity(Ticket domain) {
        if (domain == null) {
            return null;
        }

        return TicketEntity.builder()
                .ticketId(domain.getId() != null ? domain.getId().value() : null)
                .flightId(domain.getFlightId().value())
                .userId(domain.getUserId().value())
                .passengerName(domain.getPassengerName())
                .seatNumber(domain.getSeatNumber().value())
                .price(domain.getPrice().amount())
                .ticketClass(domain.getTicketClass().name())
                .status(domain.getStatus().name())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}


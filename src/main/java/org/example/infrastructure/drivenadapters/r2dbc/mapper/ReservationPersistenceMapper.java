package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.Reservation;
import org.example.domain.valueobject.FlightId;
import org.example.domain.valueobject.ReservationId;
import org.example.domain.valueobject.TicketId;
import org.example.domain.valueobject.UserId;
import org.example.infrastructure.drivenadapters.r2dbc.entity.ReservationEntity;
import org.example.domain.model.enums.ReservationStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Reservation (dominio) y ReservationEntity (persistencia)
 */
@Component
public class ReservationPersistenceMapper {

    public Reservation toDomain(ReservationEntity entity) {
        if (entity == null) {
            return null;
        }

        return Reservation.builder()
                .id(new ReservationId(entity.getReservationId()))
                .userId(new UserId(entity.getUserId()))
                .ticketId(new TicketId(entity.getTicketId()))
                .flightId(new FlightId(entity.getFlightId()))
                .status(ReservationStatus.valueOf(entity.getStatus()))
                .observations(entity.getObservations())
                .reservationDate(entity.getReservationDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ReservationEntity toEntity(Reservation domain) {
        if (domain == null) {
            return null;
        }

        return ReservationEntity.builder()
                .reservationId(domain.getId() != null ? domain.getId().value() : null)
                .userId(domain.getUserId().value())
                .ticketId(domain.getTicketId().value())
                .flightId(domain.getFlightId().value())
                .status(domain.getStatus().name())
                .observations(domain.getObservations())
                .reservationDate(domain.getReservationDate())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}


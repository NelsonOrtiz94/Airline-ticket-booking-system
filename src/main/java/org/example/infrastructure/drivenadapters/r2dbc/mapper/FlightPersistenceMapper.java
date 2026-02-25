package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.Flight;
import org.example.domain.valueobject.*;
import org.example.infrastructure.drivenadapters.r2dbc.entity.FlightEntity;
import org.example.domain.model.enums.FlightStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Flight (dominio) y FlightEntity (persistencia)
 */
@Component
public class FlightPersistenceMapper {

    public Flight toDomain(FlightEntity entity) {
        if (entity == null) {
            return null;
        }

        return Flight.builder()
                .id(new FlightId(entity.getFlightId()))
                .flightNumber(new FlightNumber(entity.getFlightNumber()))
                .origin(new Location(entity.getOrigin()))
                .destination(new Location(entity.getDestination()))
                .departureTime(entity.getDepartureTime())
                .arrivalTime(entity.getArrivalTime())
                .availableSeats(entity.getAvailableSeats())
                .totalSeats(entity.getTotalSeats())
                .price(new Price(entity.getPrice()))
                .airline(new Airline(entity.getAirline()))
                .status(FlightStatus.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public FlightEntity toEntity(Flight domain) {
        if (domain == null) {
            return null;
        }

        return FlightEntity.builder()
                .flightId(domain.getId() != null ? domain.getId().value() : null)
                .flightNumber(domain.getFlightNumber().value())
                .origin(domain.getOrigin().value())
                .destination(domain.getDestination().value())
                .departureTime(domain.getDepartureTime())
                .arrivalTime(domain.getArrivalTime())
                .availableSeats(domain.getAvailableSeats())
                .totalSeats(domain.getTotalSeats())
                .price(domain.getPrice().amount())
                .airline(domain.getAirline().name())
                .status(domain.getStatus().name())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}


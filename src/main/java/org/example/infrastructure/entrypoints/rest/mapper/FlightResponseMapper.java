package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.domain.model.Flight;
import org.example.infrastructure.entrypoints.rest.dto.response.FlightResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir modelos de dominio Flight a DTOs de respuesta
 */
@Component
public class FlightResponseMapper {

    /**
     * Convierte un modelo Flight a FlightResponseDTO
     */
    public FlightResponseDTO toResponse(Flight flight) {
        if (flight == null) {
            return null;
        }

        return FlightResponseDTO.builder()
                .flightId(flight.getId().value())
                .flightNumber(flight.getFlightNumber().value())
                .origin(flight.getOrigin().value())
                .destination(flight.getDestination().value())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .availableSeats(flight.getAvailableSeats())
                .totalSeats(flight.getTotalSeats())
                .price(flight.getPrice().amount())
                .airline(flight.getAirline().name())
                .status(flight.getStatus().name())
                .build();
    }
}


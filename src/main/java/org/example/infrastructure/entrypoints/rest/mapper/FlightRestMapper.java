package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.SearchFlightsCommand;
import org.example.infrastructure.entrypoints.rest.dto.request.FlightSearchRequestDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DTOs REST y comandos de Flight
 */
@Component
public class FlightRestMapper {

    /**
     * Convierte FlightSearchRequestDTO a SearchFlightsCommand
     */
    public SearchFlightsCommand toSearchCommand(FlightSearchRequestDTO dto) {
        return new SearchFlightsCommand(
            dto.getOrigin(),
            dto.getDestination(),
            dto.getDepartureDate(),
            dto.getPassengers()
        );
    }
}


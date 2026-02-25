package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.SearchFlightsCommand;
import org.example.infrastructure.entrypoints.rest.dto.request.FlightSearchRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para FlightRestMapper - 100% Coverage
 */
class FlightRestMapperTest {

    private FlightRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FlightRestMapper();
    }

    @Test
    @DisplayName("Debe convertir FlightSearchRequestDTO a SearchFlightsCommand")
    void testToSearchCommand_ShouldMapAllFields() {
        LocalDateTime departureDate = LocalDateTime.of(2026, 2, 20, 10, 0);
        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("BOG")
                .destination("MDE")
                .departureDate(departureDate)
                .passengers(2)
                .build();

        SearchFlightsCommand command = mapper.toSearchCommand(dto);

        assertNotNull(command);
        assertEquals("BOG", command.origin());
        assertEquals("MDE", command.destination());
        assertEquals(departureDate, command.departureDate());
        assertEquals(2, command.passengers());
    }

    @Test
    @DisplayName("Debe manejar passengers null")
    void testToSearchCommand_WithNullPassengers_ShouldWork() {
        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("CTG")
                .destination("CLO")
                .departureDate(LocalDateTime.now())
                .passengers(null)
                .build();

        SearchFlightsCommand command = mapper.toSearchCommand(dto);

        assertNull(command.passengers());
    }

    @Test
    @DisplayName("Debe manejar departureDate null")
    void testToSearchCommand_WithNullDepartureDate_ShouldWork() {
        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("BOG")
                .destination("MDE")
                .departureDate(null)
                .passengers(1)
                .build();

        SearchFlightsCommand command = mapper.toSearchCommand(dto);

        assertNull(command.departureDate());
    }
}


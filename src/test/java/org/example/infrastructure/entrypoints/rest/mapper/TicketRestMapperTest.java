package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.BookTicketCommand;
import org.example.infrastructure.entrypoints.rest.dto.request.BookingRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para TicketRestMapper - 100% Coverage
 */
class TicketRestMapperTest {

    private TicketRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TicketRestMapper();
    }

    @Test
    @DisplayName("Debe convertir BookingRequestDTO a BookTicketCommand")
    void testToCommand_ShouldMapAllFields() {
        BookingRequestDTO dto = BookingRequestDTO.builder()
                .userId(1L)
                .flightId(2L)
                .passengerName("John Doe")
                .seatNumber("12A")
                .ticketClass("ECONOMY")
                .build();

        BookTicketCommand command = mapper.toCommand(dto);

        assertNotNull(command);
        assertEquals(1L, command.userId());
        assertEquals(2L, command.flightId());
        assertEquals("John Doe", command.passengerName());
        assertEquals("12A", command.seatNumber());
        assertEquals("ECONOMY", command.ticketClass());
    }

    @Test
    @DisplayName("Debe manejar clase BUSINESS")
    void testToCommand_WithBusinessClass_ShouldWork() {
        BookingRequestDTO dto = BookingRequestDTO.builder()
                .userId(1L)
                .flightId(1L)
                .passengerName("Jane Doe")
                .seatNumber("1A")
                .ticketClass("BUSINESS")
                .build();

        BookTicketCommand command = mapper.toCommand(dto);

        assertEquals("BUSINESS", command.ticketClass());
    }

    @Test
    @DisplayName("Debe manejar clase FIRST_CLASS")
    void testToCommand_WithFirstClass_ShouldWork() {
        BookingRequestDTO dto = BookingRequestDTO.builder()
                .userId(1L)
                .flightId(1L)
                .passengerName("VIP User")
                .seatNumber("1A")
                .ticketClass("FIRST_CLASS")
                .build();

        BookTicketCommand command = mapper.toCommand(dto);

        assertEquals("FIRST_CLASS", command.ticketClass());
    }
}


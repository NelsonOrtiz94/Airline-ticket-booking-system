package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.BookTicketCommand;
import org.example.infrastructure.entrypoints.rest.dto.request.BookingRequestDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DTOs REST y comandos de Ticket/Reservation
 */
@Component
public class TicketRestMapper {

    /**
     * Convierte BookingRequestDTO a BookTicketCommand
     */
    public BookTicketCommand toCommand(BookingRequestDTO dto) {
        return new BookTicketCommand(
            dto.getUserId(),
            dto.getFlightId(),
            dto.getPassengerName(),
            dto.getSeatNumber(),
            dto.getTicketClass()
        );
    }
}


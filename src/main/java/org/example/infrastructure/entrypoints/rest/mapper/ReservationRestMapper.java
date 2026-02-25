package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.UpdateReservationCommand;
import org.example.application.command.CancelReservationCommand;
import org.example.infrastructure.entrypoints.rest.dto.request.UpdateReservationRequestDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DTOs REST y comandos de Reservation
 */
@Component
public class ReservationRestMapper {

    /**
     * Convierte UpdateReservationRequestDTO a UpdateReservationCommand
     */
    public UpdateReservationCommand toUpdateCommand(UpdateReservationRequestDTO dto) {
        return new UpdateReservationCommand(
            dto.getReservationId(),
            dto.getSeatNumber(),
            dto.getObservations()
        );
    }

    /**
     * Crea CancelReservationCommand
     */
    public CancelReservationCommand toCancelCommand(Long reservationId, String reason) {
        return new CancelReservationCommand(reservationId, reason);
    }
}


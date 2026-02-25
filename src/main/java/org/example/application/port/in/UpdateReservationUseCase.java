package org.example.application.port.in;

import org.example.application.command.UpdateReservationCommand;
import org.example.domain.model.Reservation;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada para actualizar reservaciones
 */
public interface UpdateReservationUseCase {
    Mono<Reservation> execute(UpdateReservationCommand command);
}


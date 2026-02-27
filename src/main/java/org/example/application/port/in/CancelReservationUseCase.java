package org.example.application.port.in;

import org.example.application.command.CancelReservationCommand;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada para cancelar reservaciones
 */
public interface CancelReservationUseCase {
    Mono<Void> execute(CancelReservationCommand command);
}


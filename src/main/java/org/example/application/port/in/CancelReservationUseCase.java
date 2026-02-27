package org.example.application.port.in;

import org.example.application.command.CancelReservationCommand;
import reactor.core.publisher.Mono;


public interface CancelReservationUseCase {
    Mono<Void> execute(CancelReservationCommand command);
}


package org.example.application.port.in;

import org.example.application.command.BookTicketCommand;
import org.example.domain.model.Reservation;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada para reservar tickets
 */
public interface BookTicketUseCase {
    Mono<Reservation> execute(BookTicketCommand command);
}


 package org.example.application.port.out;

import org.example.domain.model.Ticket;
import org.example.domain.valueobject.FlightId;
import org.example.domain.valueobject.SeatNumber;
import org.example.domain.valueobject.TicketId;
import org.example.domain.valueobject.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para operaciones de persistencia de Ticket
 */
public interface TicketRepositoryPort {
    Mono<Ticket> findById(TicketId ticketId);
    Mono<Ticket> save(Ticket ticket);
    Mono<Ticket> update(Ticket ticket);
    Mono<Void> deleteById(TicketId ticketId);
    Flux<Ticket> findByUserId(UserId userId);
    Flux<Ticket> findByFlightId(FlightId flightId);
    Mono<Boolean> isSeatTaken(FlightId flightId, SeatNumber seatNumber);
}


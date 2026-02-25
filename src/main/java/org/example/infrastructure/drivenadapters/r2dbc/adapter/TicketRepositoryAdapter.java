package org.example.infrastructure.drivenadapters.r2dbc.adapter;

import lombok.RequiredArgsConstructor;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.model.Ticket;
import org.example.domain.valueobject.FlightId;
import org.example.domain.valueobject.SeatNumber;
import org.example.domain.valueobject.TicketId;
import org.example.domain.valueobject.UserId;
import org.example.infrastructure.drivenadapters.r2dbc.mapper.TicketPersistenceMapper;
import org.example.infrastructure.drivenadapters.r2dbc.repository.TicketR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador que implementa TicketRepositoryPort usando R2DBC
 */
@Component
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepositoryPort {

    private final TicketR2dbcRepository r2dbcRepository;
    private final TicketPersistenceMapper mapper;

    @Override
    public Mono<Ticket> findById(TicketId ticketId) {
        return r2dbcRepository.findById(ticketId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Ticket> save(Ticket ticket) {
        return r2dbcRepository.save(mapper.toEntity(ticket))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Ticket> update(Ticket ticket) {
        return r2dbcRepository.save(mapper.toEntity(ticket))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(TicketId ticketId) {
        return r2dbcRepository.deleteById(ticketId.value());
    }

    @Override
    public Flux<Ticket> findByUserId(UserId userId) {
        return r2dbcRepository.findByUserId(userId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Ticket> findByFlightId(FlightId flightId) {
        return r2dbcRepository.findByFlightId(flightId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> isSeatTaken(FlightId flightId, SeatNumber seatNumber) {
        return r2dbcRepository.existsBySeatNumberAndFlightId(
                seatNumber.value(),
                flightId.value()
        );
    }
}


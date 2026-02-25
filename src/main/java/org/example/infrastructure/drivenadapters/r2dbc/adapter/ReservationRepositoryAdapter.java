package org.example.infrastructure.drivenadapters.r2dbc.adapter;

import lombok.RequiredArgsConstructor;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.domain.model.Reservation;
import org.example.domain.valueobject.ReservationId;
import org.example.domain.valueobject.UserId;
import org.example.infrastructure.drivenadapters.r2dbc.mapper.ReservationPersistenceMapper;
import org.example.infrastructure.drivenadapters.r2dbc.repository.ReservationR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador que implementa ReservationRepositoryPort usando R2DBC
 */
@Component
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationRepositoryPort {

    private final ReservationR2dbcRepository r2dbcRepository;
    private final ReservationPersistenceMapper mapper;

    @Override
    public Mono<Reservation> findById(ReservationId reservationId) {
        return r2dbcRepository.findById(reservationId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Reservation> save(Reservation reservation) {
        return r2dbcRepository.save(mapper.toEntity(reservation))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Reservation> update(Reservation reservation) {
        return r2dbcRepository.save(mapper.toEntity(reservation))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(ReservationId reservationId) {
        return r2dbcRepository.deleteById(reservationId.value());
    }

    @Override
    public Flux<Reservation> findByUserId(UserId userId) {
        return r2dbcRepository.findByUserId(userId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Reservation> findAll() {
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }
}


package org.example.application.port.out;

import org.example.domain.model.Reservation;
import org.example.domain.valueobject.ReservationId;
import org.example.domain.valueobject.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para operaciones de persistencia de Reservation
 */
public interface ReservationRepositoryPort {
    Mono<Reservation> findById(ReservationId reservationId);
    Mono<Reservation> save(Reservation reservation);
    Mono<Reservation> update(Reservation reservation);
    Mono<Void> deleteById(ReservationId reservationId);
    Flux<Reservation> findByUserId(UserId userId);
    Flux<Reservation> findAll();
}


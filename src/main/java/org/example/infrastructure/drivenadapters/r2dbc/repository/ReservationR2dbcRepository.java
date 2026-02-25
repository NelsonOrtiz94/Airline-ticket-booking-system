package org.example.infrastructure.drivenadapters.r2dbc.repository;

import org.example.infrastructure.drivenadapters.r2dbc.entity.ReservationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para ReservationEntity
 */
@Repository
public interface ReservationR2dbcRepository extends ReactiveCrudRepository<ReservationEntity, Long> {

    @Query("SELECT * FROM reservations WHERE user_id = :userId")
    Flux<ReservationEntity> findByUserId(Long userId);

    @Query("SELECT * FROM reservations WHERE flight_id = :flightId")
    Flux<ReservationEntity> findByFlightId(Long flightId);

    @Query("SELECT * FROM reservations WHERE user_id = :userId AND status = :status")
    Flux<ReservationEntity> findByUserIdAndStatus(Long userId, String status);

    @Query("SELECT EXISTS(SELECT 1 FROM reservations WHERE reservation_id = :reservationId AND user_id = :userId)")
    Mono<Boolean> existsByIdAndUserId(Long reservationId, Long userId);

    @Query("SELECT COUNT(*) FROM reservations WHERE flight_id = :flightId")
    Mono<Long> countByFlightId(Long flightId);
}


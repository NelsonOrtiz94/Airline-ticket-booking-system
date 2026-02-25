package org.example.infrastructure.drivenadapters.r2dbc.repository;

import org.example.infrastructure.drivenadapters.r2dbc.entity.TicketEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para TicketEntity
 */
@Repository
public interface TicketR2dbcRepository extends ReactiveCrudRepository<TicketEntity, Long> {

    @Query("SELECT * FROM tickets WHERE user_id = :userId")
    Flux<TicketEntity> findByUserId(Long userId);

    @Query("SELECT * FROM tickets WHERE flight_id = :flightId")
    Flux<TicketEntity> findByFlightId(Long flightId);

    @Query("SELECT * FROM tickets WHERE flight_id = :flightId AND seat_number = :seatNumber")
    Mono<TicketEntity> findByFlightIdAndSeatNumber(Long flightId, String seatNumber);

    @Query("SELECT EXISTS(SELECT 1 FROM tickets WHERE seat_number = :seatNumber AND flight_id = :flightId AND status != 'CANCELLED')")
    Mono<Boolean> existsBySeatNumberAndFlightId(String seatNumber, Long flightId);
}


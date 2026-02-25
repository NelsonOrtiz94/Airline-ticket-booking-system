package org.example.infrastructure.drivenadapters.r2dbc.repository;

import org.example.infrastructure.drivenadapters.r2dbc.entity.FlightEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repositorio R2DBC para FlightEntity
 */
@Repository
public interface FlightR2dbcRepository extends ReactiveCrudRepository<FlightEntity, Long> {

    @Query("SELECT * FROM flights WHERE origin = :origin AND destination = :destination " +
           "AND status = 'ACTIVE' AND available_seats > 0")
    Flux<FlightEntity> findByOriginAndDestination(String origin, String destination);

    @Query("SELECT * FROM flights WHERE origin = :origin AND destination = :destination " +
           "AND DATE(departure_time) = DATE(:departureDate) " +
           "AND status = 'ACTIVE' AND available_seats > 0")
    Flux<FlightEntity> findByOriginDestinationAndDate(
            String origin, String destination, LocalDateTime departureDate);

    @Query("SELECT * FROM flights WHERE status = 'ACTIVE' AND available_seats > 0")
    Flux<FlightEntity> findAvailableFlights();

    @Query("SELECT * FROM flights WHERE flight_id = :id AND status = 'ACTIVE'")
    Mono<FlightEntity> findActiveFlightById(Long id);
}


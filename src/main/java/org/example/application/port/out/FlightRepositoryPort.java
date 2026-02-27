package org.example.application.port.out;

import org.example.domain.model.Flight;
import org.example.domain.valueobject.FlightId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Puerto de salida para operaciones de persistencia de Flight
 */
public interface FlightRepositoryPort {
    Mono<Flight> findById(FlightId flightId);
    Mono<Flight> save(Flight flight);
    Mono<Flight> update(Flight flight);
    Mono<Void> deleteById(FlightId flightId);
    Flux<Flight> findAll();
    Flux<Flight> searchFlights(String origin, String destination, LocalDateTime departureDate);
}


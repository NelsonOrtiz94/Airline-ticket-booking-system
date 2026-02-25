package org.example.infrastructure.drivenadapters.r2dbc.adapter;

import lombok.RequiredArgsConstructor;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.domain.model.Flight;
import org.example.domain.valueobject.FlightId;
import org.example.infrastructure.drivenadapters.r2dbc.mapper.FlightPersistenceMapper;
import org.example.infrastructure.drivenadapters.r2dbc.repository.FlightR2dbcRepository;
import org.example.shared.util.CityCodeNormalizer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Adaptador que implementa FlightRepositoryPort usando R2DBC
 */
@Component
@RequiredArgsConstructor
public class FlightRepositoryAdapter implements FlightRepositoryPort {

    private final FlightR2dbcRepository r2dbcRepository;
    private final FlightPersistenceMapper mapper;

    @Override
    public Mono<Flight> findById(FlightId flightId) {
        return r2dbcRepository.findById(flightId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Flight> save(Flight flight) {
        return r2dbcRepository.save(mapper.toEntity(flight))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Flight> update(Flight flight) {
        return r2dbcRepository.save(mapper.toEntity(flight))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(FlightId flightId) {
        return r2dbcRepository.deleteById(flightId.value());
    }

    @Override
    public Flux<Flight> findAll() {
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Flight> searchFlights(String origin, String destination, LocalDateTime departureDate) {
        // Normalizar nombres de ciudades a códigos IATA
        String normalizedOrigin = CityCodeNormalizer.normalize(origin);
        String normalizedDestination = CityCodeNormalizer.normalize(destination);

        if (departureDate != null) {
            return r2dbcRepository.findByOriginDestinationAndDate(
                    normalizedOrigin, normalizedDestination, departureDate)
                    .map(mapper::toDomain);
        }
        return r2dbcRepository.findByOriginAndDestination(normalizedOrigin, normalizedDestination)
                .map(mapper::toDomain);
    }
}


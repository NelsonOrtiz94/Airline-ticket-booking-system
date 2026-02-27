package org.example.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.command.SearchFlightsCommand;
import org.example.application.port.in.SearchFlightsUseCase;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.domain.model.Flight;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Slf4j
@Service
@RequiredArgsConstructor
public class SearchFlightsUseCaseImpl implements SearchFlightsUseCase {

    private final FlightRepositoryPort flightRepository;

    @Override
    public Flux<Flight> execute(SearchFlightsCommand command) {
        log.info("Buscando vuelos de {} a {} para fecha: {}",
                command.origin(), command.destination(), command.departureDate());

        return flightRepository.searchFlights(
                command.origin(),
                command.destination(),
                command.departureDate()
        )
        .filter(Flight::isBookable)
        .filter(flight -> {
            if (command.passengers() != null) {
                return flight.hasAvailableSeats(command.passengers());
            }
            return true;
        })
        .doOnComplete(() -> log.info("Búsqueda de vuelos completada"))
        .doOnError(error -> log.error("Error al buscar vuelos: {}", error.getMessage()));
    }
}


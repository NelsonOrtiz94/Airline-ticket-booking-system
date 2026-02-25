package org.example.application.port.in;

import org.example.application.command.SearchFlightsCommand;
import org.example.domain.model.Flight;
import reactor.core.publisher.Flux;

/**
 * Puerto de entrada para buscar vuelos
 */
public interface SearchFlightsUseCase {
    Flux<Flight> execute(SearchFlightsCommand command);
}


package org.example.infrastructure.entrypoints.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.port.in.SearchFlightsUseCase;
import org.example.infrastructure.entrypoints.rest.dto.request.FlightSearchRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.ApiResponse;
import org.example.infrastructure.entrypoints.rest.dto.response.FlightResponseDTO;
import org.example.infrastructure.entrypoints.rest.mapper.FlightResponseMapper;
import org.example.infrastructure.entrypoints.rest.mapper.FlightRestMapper;
import org.example.shared.constants.MessageConstants;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/airline/flights")
@RequiredArgsConstructor
public class FlightController {

    private final SearchFlightsUseCase searchFlightsUseCase;
    private final FlightRestMapper flightRestMapper;
    private final FlightResponseMapper flightResponseMapper;

    @PostMapping("/search")
    public Mono<ApiResponse<List<FlightResponseDTO>>> searchFlights(
            @Valid @RequestBody FlightSearchRequestDTO request) {

        log.info("Recibida solicitud de búsqueda de vuelos: {} -> {}",
                request.getOrigin(), request.getDestination());

        return searchFlightsUseCase.execute(flightRestMapper.toSearchCommand(request))
                .map(flightResponseMapper::toResponse)
                .collectList()
                .map(flights -> {
                    if (flights.isEmpty()) {
                        return ApiResponse.success(flights, MessageConstants.NO_FLIGHTS_FOUND);
                    } else if (flights.size() == 1) {
                        return ApiResponse.success(flights, MessageConstants.FLIGHTS_FOUND_SINGULAR);
                    } else {
                        return ApiResponse.success(flights,
                                String.format(MessageConstants.FLIGHTS_FOUND, flights.size()));
                    }
                })
                .defaultIfEmpty(ApiResponse.success(List.of(), MessageConstants.NO_FLIGHTS_FOUND));
    }
}

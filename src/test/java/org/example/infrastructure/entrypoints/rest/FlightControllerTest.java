package org.example.infrastructure.entrypoints.rest;

import org.example.application.command.SearchFlightsCommand;
import org.example.application.port.in.SearchFlightsUseCase;
import org.example.domain.model.Flight;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.entrypoints.rest.dto.request.FlightSearchRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.FlightResponseDTO;
import org.example.infrastructure.entrypoints.rest.mapper.FlightResponseMapper;
import org.example.infrastructure.entrypoints.rest.mapper.FlightRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para FlightController - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    @Mock
    private SearchFlightsUseCase searchFlightsUseCase;

    @Mock
    private FlightRestMapper flightRestMapper;

    @Mock
    private FlightResponseMapper flightResponseMapper;

    @InjectMocks
    private FlightController flightController;

    private FlightSearchRequestDTO searchRequest;
    private SearchFlightsCommand searchCommand;
    private Flight flight;
    private FlightResponseDTO flightResponse;

    @BeforeEach
    void setUp() {
        searchRequest = FlightSearchRequestDTO.builder()
                .origin("BOG")
                .destination("MDE")
                .departureDate(LocalDateTime.now().plusDays(1))
                .passengers(2)
                .build();

        searchCommand = new SearchFlightsCommand("BOG", "MDE", LocalDateTime.now().plusDays(1), 2);

        flight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(50)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        flightResponse = FlightResponseDTO.builder()
                .flightId(1L)
                .flightNumber("AV101")
                .origin("BOG")
                .destination("MDE")
                .availableSeats(50)
                .price(BigDecimal.valueOf(250000))
                .airline("Avianca")
                .status("ACTIVE")
                .build();
    }

    @Test
    @DisplayName("Debe retornar vuelos cuando existen")
    void testSearchFlights_WhenFlightsExist_ShouldReturnFlights() {
        when(flightRestMapper.toSearchCommand(any(FlightSearchRequestDTO.class))).thenReturn(searchCommand);
        when(searchFlightsUseCase.execute(any(SearchFlightsCommand.class))).thenReturn(Flux.just(flight));
        when(flightResponseMapper.toResponse(any(Flight.class))).thenReturn(flightResponse);

        StepVerifier.create(flightController.searchFlights(searchRequest))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().size() == 1 &&
                    response.getData().get(0).getFlightNumber().equals("AV101")
                )
                .verifyComplete();

        verify(searchFlightsUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay vuelos")
    void testSearchFlights_WhenNoFlights_ShouldReturnEmptyList() {
        when(flightRestMapper.toSearchCommand(any(FlightSearchRequestDTO.class))).thenReturn(searchCommand);
        when(searchFlightsUseCase.execute(any(SearchFlightsCommand.class))).thenReturn(Flux.empty());

        StepVerifier.create(flightController.searchFlights(searchRequest))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().isEmpty()
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar múltiples vuelos")
    void testSearchFlights_WhenMultipleFlights_ShouldReturnAll() {
        Flight flight2 = Flight.builder()
                .id(new FlightId(2L))
                .flightNumber(new FlightNumber("AV102"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(30)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(300000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        FlightResponseDTO flightResponse2 = FlightResponseDTO.builder()
                .flightId(2L)
                .flightNumber("AV102")
                .origin("BOG")
                .destination("MDE")
                .availableSeats(30)
                .price(BigDecimal.valueOf(300000))
                .airline("Avianca")
                .status("ACTIVE")
                .build();

        when(flightRestMapper.toSearchCommand(any(FlightSearchRequestDTO.class))).thenReturn(searchCommand);
        when(searchFlightsUseCase.execute(any(SearchFlightsCommand.class))).thenReturn(Flux.just(flight, flight2));
        when(flightResponseMapper.toResponse(flight)).thenReturn(flightResponse);
        when(flightResponseMapper.toResponse(flight2)).thenReturn(flightResponse2);

        StepVerifier.create(flightController.searchFlights(searchRequest))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().size() == 2
                )
                .verifyComplete();
    }
}


package org.example.application.usecase;

import org.example.application.command.SearchFlightsCommand;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.domain.model.Flight;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.valueobject.*;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para SearchFlightsUseCaseImpl - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class SearchFlightsUseCaseImplTest {

    @Mock
    private FlightRepositoryPort flightRepository;

    @InjectMocks
    private SearchFlightsUseCaseImpl searchFlightsUseCase;

    private Flight bookableFlight;
    private Flight nonBookableFlight;
    private Flight cancelledFlight;

    @BeforeEach
    void setUp() {
        bookableFlight = Flight.builder()
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
                .createdAt(LocalDateTime.now())
                .build();

        nonBookableFlight = Flight.builder()
                .id(new FlightId(2L))
                .flightNumber(new FlightNumber("AV102"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(0)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        cancelledFlight = Flight.builder()
                .id(new FlightId(3L))
                .flightNumber(new FlightNumber("AV103"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(50)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.CANCELLED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe retornar solo vuelos reservables cuando existen vuelos")
    void testExecute_WhenFlightsExist_ShouldReturnBookableFlights() {
        SearchFlightsCommand command = new SearchFlightsCommand(
                "BOG", "MDE", LocalDateTime.now().plusDays(1), 2
        );

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Flux.just(bookableFlight, nonBookableFlight));

        StepVerifier.create(searchFlightsUseCase.execute(command))
                .expectNext(bookableFlight)
                .verifyComplete();

        verify(flightRepository, times(1)).searchFlights(anyString(), anyString(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Debe retornar vacío cuando no existen vuelos")
    void testExecute_WhenNoFlightsExist_ShouldReturnEmpty() {
        SearchFlightsCommand command = new SearchFlightsCommand(
                "BOG", "CTG", LocalDateTime.now().plusDays(1), 1
        );

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Flux.empty());

        StepVerifier.create(searchFlightsUseCase.execute(command))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar todos los vuelos reservables cuando passengers es null")
    void testExecute_WhenPassengersNotProvided_ShouldNotFilterBySeats() {
        SearchFlightsCommand command = new SearchFlightsCommand(
                "BOG", "MDE", LocalDateTime.now().plusDays(1), null
        );

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Flux.just(bookableFlight));

        StepVerifier.create(searchFlightsUseCase.execute(command))
                .expectNext(bookableFlight)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe filtrar vuelos cancelados")
    void testExecute_WhenCancelledFlights_ShouldFilter() {
        SearchFlightsCommand command = new SearchFlightsCommand(
                "BOG", "MDE", LocalDateTime.now().plusDays(1), 1
        );

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Flux.just(bookableFlight, cancelledFlight));

        StepVerifier.create(searchFlightsUseCase.execute(command))
                .expectNext(bookableFlight)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe filtrar vuelos sin suficientes asientos para pasajeros solicitados")
    void testExecute_WhenNotEnoughSeats_ShouldFilter() {
        SearchFlightsCommand command = new SearchFlightsCommand(
                "BOG", "MDE", LocalDateTime.now().plusDays(1), 100
        );

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Flux.just(bookableFlight));

        StepVerifier.create(searchFlightsUseCase.execute(command))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe buscar con fecha null")
    void testExecute_WhenDateIsNull_ShouldSearchWithoutDate() {
        SearchFlightsCommand command = new SearchFlightsCommand(
                "BOG", "MDE", null, 1
        );

        when(flightRepository.searchFlights(eq("BOG"), eq("MDE"), isNull()))
                .thenReturn(Flux.just(bookableFlight));

        StepVerifier.create(searchFlightsUseCase.execute(command))
                .expectNext(bookableFlight)
                .verifyComplete();

        verify(flightRepository).searchFlights(eq("BOG"), eq("MDE"), isNull());
    }

    @Test
    @DisplayName("Debe manejar error del repositorio")
    void testExecute_WhenRepositoryError_ShouldPropagateError() {
        SearchFlightsCommand command = new SearchFlightsCommand(
                "BOG", "MDE", LocalDateTime.now().plusDays(1), 1
        );

        when(flightRepository.searchFlights(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(Flux.error(new RuntimeException("Database error")));

        StepVerifier.create(searchFlightsUseCase.execute(command))
                .expectError(RuntimeException.class)
                .verify();
    }
}

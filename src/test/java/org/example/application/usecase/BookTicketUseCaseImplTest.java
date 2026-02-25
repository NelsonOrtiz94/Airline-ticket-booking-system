package org.example.application.usecase;

import org.example.application.command.BookTicketCommand;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.exception.FlightNotFoundException;
import org.example.domain.exception.NoSeatsAvailableException;
import org.example.domain.exception.SeatAlreadyTakenException;
import org.example.domain.model.Flight;
import org.example.domain.model.Reservation;
import org.example.domain.model.Ticket;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.model.enums.TicketClass;
import org.example.domain.model.enums.TicketStatus;
import org.example.domain.service.PriceCalculationService;
import org.example.domain.service.ReservationDomainService;
import org.example.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para BookTicketUseCaseImpl - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class BookTicketUseCaseImplTest {

    @Mock
    private FlightRepositoryPort flightRepository;

    @Mock
    private TicketRepositoryPort ticketRepository;

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @Mock
    private ReservationDomainService reservationDomainService;

    @Mock
    private PriceCalculationService priceCalculationService;

    @InjectMocks
    private BookTicketUseCaseImpl bookTicketUseCase;

    private Flight flight;
    private Ticket savedTicket;
    private Reservation savedReservation;

    @BeforeEach
    void setUp() {
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
                .createdAt(LocalDateTime.now())
                .build();

        savedTicket = Ticket.builder()
                .id(new TicketId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .passengerName("John Doe")
                .seatNumber(new SeatNumber("12A"))
                .price(new Price(BigDecimal.valueOf(250000)))
                .ticketClass(TicketClass.ECONOMY)
                .status(TicketStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();

        // Reservación CON ID para simular respuesta del repositorio
        savedReservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe crear reservación exitosamente cuando todos los datos son válidos")
    void testExecute_WhenValidBooking_ShouldCreateReservation() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 1L, "John Doe", "12A", "ECONOMY"
        );

        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(false));
        when(priceCalculationService.calculatePrice(any(Price.class), any(TicketClass.class)))
                .thenReturn(flight.getPrice());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Mono.just(savedTicket));
        when(flightRepository.update(any(Flight.class))).thenReturn(Mono.just(flight));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(Mono.just(savedReservation));

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectNextMatches(res ->
                        res.getId() != null &&
                                res.getUserId().equals(new UserId(1L)) &&
                                res.getFlightId().equals(new FlightId(1L))
                )
                .verifyComplete();

        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(flightRepository, times(1)).update(any(Flight.class));
        verify(reservationDomainService, times(1)).validateReservation(any(Flight.class), eq(1));
    }

    @Test
    @DisplayName("Debe lanzar FlightNotFoundException cuando el vuelo no existe")
    void testExecute_WhenFlightNotFound_ShouldThrowException() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 999L, "John Doe", "12A", "ECONOMY"
        );

        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.empty());

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectError(FlightNotFoundException.class)
                .verify();

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Debe lanzar SeatAlreadyTakenException cuando el asiento está ocupado")
    void testExecute_WhenSeatAlreadyTaken_ShouldThrowException() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 1L, "John Doe", "12A", "ECONOMY"
        );

        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(true));

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectError(SeatAlreadyTakenException.class)
                .verify();

        verify(ticketRepository, never()).save(any(Ticket.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Debe lanzar NoSeatsAvailableException cuando no hay asientos disponibles")
    void testExecute_WhenNoSeatsAvailable_ShouldThrowException() {
        Flight fullFlight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
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

        BookTicketCommand command = new BookTicketCommand(
                1L, 1L, "John Doe", "12A", "ECONOMY"
        );

        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(fullFlight));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(false));

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectError(NoSeatsAvailableException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe calcular precio correctamente para clase BUSINESS")
    void testExecute_WhenBusinessClass_ShouldCalculateCorrectPrice() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 1L, "John Doe", "1A", "BUSINESS"
        );

        Price businessPrice = new Price(BigDecimal.valueOf(500000));

        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(false));
        when(priceCalculationService.calculatePrice(any(Price.class), eq(TicketClass.BUSINESS)))
                .thenReturn(businessPrice);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Mono.just(savedTicket));
        when(flightRepository.update(any(Flight.class))).thenReturn(Mono.just(flight));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(Mono.just(savedReservation));

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(priceCalculationService).calculatePrice(any(Price.class), eq(TicketClass.BUSINESS));
    }

    @Test
    @DisplayName("Debe calcular precio correctamente para clase FIRST_CLASS")
    void testExecute_WhenFirstClass_ShouldCalculateCorrectPrice() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 1L, "John Doe", "1A", "FIRST_CLASS"
        );

        Price firstClassPrice = new Price(BigDecimal.valueOf(750000));

        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(false));
        when(priceCalculationService.calculatePrice(any(Price.class), eq(TicketClass.FIRST_CLASS)))
                .thenReturn(firstClassPrice);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Mono.just(savedTicket));
        when(flightRepository.update(any(Flight.class))).thenReturn(Mono.just(flight));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(Mono.just(savedReservation));

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(priceCalculationService).calculatePrice(any(Price.class), eq(TicketClass.FIRST_CLASS));
    }
}

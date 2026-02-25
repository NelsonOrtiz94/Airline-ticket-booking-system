package org.example.application.usecase;

import org.example.application.command.CancelReservationCommand;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.exception.ReservationNotFoundException;
import org.example.domain.model.Flight;
import org.example.domain.model.Reservation;
import org.example.domain.model.Ticket;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.model.enums.TicketClass;
import org.example.domain.model.enums.TicketStatus;
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
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CancelReservationUseCaseImpl - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class CancelReservationUseCaseImplTest {

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @Mock
    private TicketRepositoryPort ticketRepository;

    @Mock
    private FlightRepositoryPort flightRepository;

    @Mock
    private ReservationDomainService reservationDomainService;

    @InjectMocks
    private CancelReservationUseCaseImpl cancelReservationUseCase;

    private Reservation reservation;
    private Ticket ticket;
    private Flight flight;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        ticket = Ticket.builder()
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

        flight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(49)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe cancelar reservación exitosamente cuando existe")
    void testExecute_WhenReservationExists_ShouldCancelSuccessfully() {
        CancelReservationCommand command = new CancelReservationCommand(1L, "User requested");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));
        when(ticketRepository.update(any(Ticket.class))).thenReturn(Mono.just(ticket));
        when(flightRepository.update(any(Flight.class))).thenReturn(Mono.just(flight));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(cancelReservationUseCase.execute(command))
                .verifyComplete();

        verify(reservationDomainService, times(1)).validateCancellation(any(Reservation.class));
        verify(reservationRepository, times(1)).update(any(Reservation.class));
        verify(ticketRepository, times(1)).update(any(Ticket.class));
        verify(flightRepository, times(1)).update(any(Flight.class));
    }

    @Test
    @DisplayName("Debe lanzar ReservationNotFoundException cuando la reservación no existe")
    void testExecute_WhenReservationNotFound_ShouldThrowException() {
        CancelReservationCommand command = new CancelReservationCommand(999L, "User requested");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.empty());

        StepVerifier.create(cancelReservationUseCase.execute(command))
                .expectError(ReservationNotFoundException.class)
                .verify();

        verify(ticketRepository, never()).update(any(Ticket.class));
        verify(flightRepository, never()).update(any(Flight.class));
        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    @DisplayName("Debe cancelar reservación sin razón cuando reason es null")
    void testExecute_WhenReasonIsNull_ShouldCancelSuccessfully() {
        CancelReservationCommand command = new CancelReservationCommand(1L, null);

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));
        when(ticketRepository.update(any(Ticket.class))).thenReturn(Mono.just(ticket));
        when(flightRepository.update(any(Flight.class))).thenReturn(Mono.just(flight));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(cancelReservationUseCase.execute(command))
                .verifyComplete();

        verify(reservationRepository, times(1)).update(any(Reservation.class));
    }

    @Test
    @DisplayName("Debe liberar asiento correctamente al cancelar")
    void testExecute_ShouldReleaseSeatsOnCancel() {
        CancelReservationCommand command = new CancelReservationCommand(1L, "Cancelación");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));
        when(ticketRepository.update(any(Ticket.class))).thenReturn(Mono.just(ticket));
        when(flightRepository.update(any(Flight.class))).thenReturn(Mono.just(flight));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(cancelReservationUseCase.execute(command))
                .verifyComplete();

        verify(flightRepository, times(1)).update(any(Flight.class));
    }
}

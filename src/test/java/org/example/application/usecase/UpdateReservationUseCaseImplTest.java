package org.example.application.usecase;

import org.example.application.command.UpdateReservationCommand;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.exception.ReservationNotFoundException;
import org.example.domain.exception.SeatAlreadyTakenException;
import org.example.domain.model.Reservation;
import org.example.domain.model.Ticket;
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
 * Tests unitarios para UpdateReservationUseCaseImpl
 */
@ExtendWith(MockitoExtension.class)
class UpdateReservationUseCaseImplTest {

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @Mock
    private TicketRepositoryPort ticketRepository;

    @Mock
    private ReservationDomainService reservationDomainService;

    @InjectMocks
    private UpdateReservationUseCaseImpl updateReservationUseCase;

    private Reservation reservation;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .observations("Initial observation")
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        ticket = Ticket.builder()
                .id(new TicketId(1L))
                .flightId(new FlightId(1L))
                .userId(new UserId(1L))
                .passengerName("John Doe")
                .seatNumber(new SeatNumber("12A"))
                .price(new Price(BigDecimal.valueOf(100.00)))
                .ticketClass(TicketClass.ECONOMY)
                .status(TicketStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe actualizar observaciones exitosamente")
    void testExecute_WhenValidUpdate_ShouldUpdateReservation() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, null, "New observations");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(reservationDomainService, times(1)).validateUpdate(any(Reservation.class));
        verify(reservationRepository, times(1)).update(any(Reservation.class));
    }

    @Test
    @DisplayName("Debe actualizar asiento exitosamente")
    void testExecute_WhenValidSeatUpdate_ShouldUpdateTicketSeat() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, "15B", null);

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(false));
        when(ticketRepository.update(any(Ticket.class))).thenReturn(Mono.just(ticket));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(ticketRepository, times(1)).isSeatTaken(any(FlightId.class), any(SeatNumber.class));
        verify(ticketRepository, times(1)).update(any(Ticket.class));
    }

    @Test
    @DisplayName("Debe lanzar SeatAlreadyTakenException cuando el asiento está ocupado")
    void testExecute_WhenSeatTaken_ShouldThrowException() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, "15B", null);

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(true));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectError(SeatAlreadyTakenException.class)
                .verify();

        verify(ticketRepository, never()).update(any(Ticket.class));
    }

    @Test
    @DisplayName("Debe actualizar asiento y observaciones simultáneamente")
    void testExecute_WhenUpdateBothSeatAndObservations_ShouldUpdateBoth() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, "20C", "Updated observations");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class))).thenReturn(Mono.just(false));
        when(ticketRepository.update(any(Ticket.class))).thenReturn(Mono.just(ticket));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(ticketRepository, times(1)).update(any(Ticket.class));
        verify(reservationRepository, times(1)).update(any(Reservation.class));
    }

    @Test
    @DisplayName("No debe verificar disponibilidad si el asiento es el mismo")
    void testExecute_WhenSameSeat_ShouldNotCheckAvailability() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, "12A", null);

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(ticketRepository, never()).isSeatTaken(any(FlightId.class), any(SeatNumber.class));
    }

    @Test
    @DisplayName("Debe lanzar ReservationNotFoundException cuando no existe")
    void testExecute_WhenReservationNotFound_ShouldThrowException() {
        UpdateReservationCommand command = new UpdateReservationCommand(999L, null, "New observations");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.empty());

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectError(ReservationNotFoundException.class)
                .verify();

        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    @DisplayName("Debe actualizar sin cambiar observaciones cuando es null")
    void testExecute_WhenObservationsNull_ShouldNotUpdateObservations() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, null, null);

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(reservationRepository, times(1)).update(any(Reservation.class));
    }

    @Test
    @DisplayName("Debe actualizar con observaciones vacías")
    void testExecute_WhenEmptyObservations_ShouldUpdateWithEmpty() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, null, "");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextCount(1)
                .verifyComplete();

        verify(reservationRepository, times(1)).update(any(Reservation.class));
    }

    @Test
    @DisplayName("Debe validar con servicio de dominio antes de actualizar")
    void testExecute_ShouldValidateWithDomainService() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, null, "Test");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextCount(1)
                .verifyComplete();

        verify(reservationDomainService, times(1)).validateUpdate(reservation);
    }

    @Test
    @DisplayName("Debe manejar seatNumber vacío sin actualizar asiento")
    void testExecute_WhenSeatNumberBlank_ShouldNotUpdateSeat() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, "  ", "New observations");

        when(reservationRepository.findById(any(ReservationId.class))).thenReturn(Mono.just(reservation));
        when(reservationRepository.update(any(Reservation.class))).thenReturn(Mono.just(reservation));

        StepVerifier.create(updateReservationUseCase.execute(command))
                .expectNextMatches(res -> res.getId() != null)
                .verifyComplete();

        verify(ticketRepository, never()).findById(any(TicketId.class));
    }
}


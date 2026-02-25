package org.example.infrastructure.entrypoints.rest;

import org.example.application.command.BookTicketCommand;
import org.example.application.command.CancelReservationCommand;
import org.example.application.command.UpdateReservationCommand;
import org.example.application.port.in.BookTicketUseCase;
import org.example.application.port.in.CancelReservationUseCase;
import org.example.application.port.in.GetUserReservationsUseCase;
import org.example.application.port.in.UpdateReservationUseCase;
import org.example.domain.model.Reservation;
import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.entrypoints.rest.dto.request.BookingRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.request.UpdateReservationRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.ReservationResponseDTO;
import org.example.infrastructure.entrypoints.rest.mapper.ReservationResponseMapper;
import org.example.infrastructure.entrypoints.rest.mapper.ReservationRestMapper;
import org.example.infrastructure.entrypoints.rest.mapper.TicketRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ReservationController - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private BookTicketUseCase bookTicketUseCase;

    @Mock
    private UpdateReservationUseCase updateReservationUseCase;

    @Mock
    private CancelReservationUseCase cancelReservationUseCase;

    @Mock
    private GetUserReservationsUseCase getUserReservationsUseCase;

    @Mock
    private TicketRestMapper ticketRestMapper;

    @Mock
    private ReservationRestMapper reservationRestMapper;

    @Mock
    private ReservationResponseMapper reservationResponseMapper;

    @InjectMocks
    private ReservationController reservationController;

    private BookingRequestDTO bookingRequest;
    private UpdateReservationRequestDTO updateRequest;
    private Reservation reservation;
    private ReservationResponseDTO reservationResponse;

    @BeforeEach
    void setUp() {
        bookingRequest = BookingRequestDTO.builder()
                .userId(1L)
                .flightId(1L)
                .passengerName("John Doe")
                .seatNumber("12A")
                .ticketClass("ECONOMY")
                .build();

        updateRequest = UpdateReservationRequestDTO.builder()
                .reservationId(1L)
                .observations("Updated observations")
                .build();

        reservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        reservationResponse = ReservationResponseDTO.builder()
                .reservationId(1L)
                .userId(1L)
                .flightId(1L)
                .ticketId(1L)
                .status("CONFIRMED")
                .build();
    }

    @Test
    @DisplayName("Debe crear reservación exitosamente")
    void testCreateReservation_WhenValid_ShouldCreateReservation() {
        BookTicketCommand command = new BookTicketCommand(1L, 1L, "John Doe", "12A", "ECONOMY");

        when(ticketRestMapper.toCommand(any(BookingRequestDTO.class))).thenReturn(command);
        when(bookTicketUseCase.execute(any(BookTicketCommand.class))).thenReturn(Mono.just(reservation));
        when(reservationResponseMapper.toResponseWithDetails(any(Reservation.class)))
                .thenReturn(Mono.just(reservationResponse));

        StepVerifier.create(reservationController.createReservation(bookingRequest))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().getReservationId().equals(1L)
                )
                .verifyComplete();

        verify(bookTicketUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("Debe actualizar reservación exitosamente")
    void testUpdateReservation_WhenValid_ShouldUpdateReservation() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, null, "Updated observations");

        when(reservationRestMapper.toUpdateCommand(any(UpdateReservationRequestDTO.class))).thenReturn(command);
        when(updateReservationUseCase.execute(any(UpdateReservationCommand.class))).thenReturn(Mono.just(reservation));
        when(reservationResponseMapper.toResponseWithDetails(any(Reservation.class)))
                .thenReturn(Mono.just(reservationResponse));

        StepVerifier.create(reservationController.updateReservation(updateRequest))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().getReservationId().equals(1L)
                )
                .verifyComplete();

        verify(updateReservationUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("Debe cancelar reservación exitosamente")
    void testCancelReservation_WhenValid_ShouldCancelReservation() {
        CancelReservationCommand command = new CancelReservationCommand(1L, "User requested");

        when(reservationRestMapper.toCancelCommand(1L, "User requested")).thenReturn(command);
        when(cancelReservationUseCase.execute(any(CancelReservationCommand.class))).thenReturn(Mono.empty());

        StepVerifier.create(reservationController.cancelReservation(1L, "User requested"))
                .expectNextMatches(response ->
                    response.getMessage() != null
                )
                .verifyComplete();

        verify(cancelReservationUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("Debe cancelar reservación sin razón")
    void testCancelReservation_WhenNoReason_ShouldCancelReservation() {
        CancelReservationCommand command = new CancelReservationCommand(1L, null);

        when(reservationRestMapper.toCancelCommand(1L, null)).thenReturn(command);
        when(cancelReservationUseCase.execute(any(CancelReservationCommand.class))).thenReturn(Mono.empty());

        StepVerifier.create(reservationController.cancelReservation(1L, null))
                .expectNextMatches(response ->
                    response.getMessage() != null
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener reservaciones del usuario")
    void testGetUserReservations_WhenReservationsExist_ShouldReturnReservations() {
        when(getUserReservationsUseCase.execute(any(UserId.class))).thenReturn(Flux.just(reservation));
        when(reservationResponseMapper.toResponseWithDetails(any(Reservation.class)))
                .thenReturn(Mono.just(reservationResponse));

        StepVerifier.create(reservationController.getUserReservations(1L))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().size() == 1
                )
                .verifyComplete();

        verify(getUserReservationsUseCase, times(1)).execute(any(UserId.class));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando usuario no tiene reservaciones")
    void testGetUserReservations_WhenNoReservations_ShouldReturnEmptyList() {
        when(getUserReservationsUseCase.execute(any(UserId.class))).thenReturn(Flux.empty());

        StepVerifier.create(reservationController.getUserReservations(999L))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().isEmpty()
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener múltiples reservaciones del usuario")
    void testGetUserReservations_WhenMultipleReservations_ShouldReturnAll() {
        Reservation reservation2 = Reservation.builder()
                .id(new ReservationId(2L))
                .userId(new UserId(1L))
                .flightId(new FlightId(2L))
                .ticketId(new TicketId(2L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        ReservationResponseDTO reservationResponse2 = ReservationResponseDTO.builder()
                .reservationId(2L)
                .userId(1L)
                .flightId(2L)
                .ticketId(2L)
                .status("CONFIRMED")
                .build();

        when(getUserReservationsUseCase.execute(any(UserId.class))).thenReturn(Flux.just(reservation, reservation2));
        when(reservationResponseMapper.toResponseWithDetails(reservation)).thenReturn(Mono.just(reservationResponse));
        when(reservationResponseMapper.toResponseWithDetails(reservation2)).thenReturn(Mono.just(reservationResponse2));

        StepVerifier.create(reservationController.getUserReservations(1L))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    response.getData().size() == 2
                )
                .verifyComplete();
    }
}


package org.example.application.usecase;

import org.example.application.port.out.ReservationRepositoryPort;
import org.example.domain.model.Reservation;
import org.example.domain.model.enums.ReservationStatus;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para GetUserReservationsUseCaseImpl - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class GetUserReservationsUseCaseImplTest {

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @InjectMocks
    private GetUserReservationsUseCaseImpl getUserReservationsUseCase;

    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        reservation1 = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        reservation2 = Reservation.builder()
                .id(new ReservationId(2L))
                .userId(new UserId(1L))
                .flightId(new FlightId(2L))
                .ticketId(new TicketId(2L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe retornar reservaciones del usuario cuando existen")
    void testExecute_WhenReservationsExist_ShouldReturnReservations() {
        UserId userId = new UserId(1L);

        when(reservationRepository.findByUserId(any(UserId.class)))
                .thenReturn(Flux.just(reservation1, reservation2));

        StepVerifier.create(getUserReservationsUseCase.execute(userId))
                .expectNext(reservation1)
                .expectNext(reservation2)
                .verifyComplete();

        verify(reservationRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Debe retornar vacío cuando el usuario no tiene reservaciones")
    void testExecute_WhenNoReservations_ShouldReturnEmpty() {
        UserId userId = new UserId(999L);

        when(reservationRepository.findByUserId(any(UserId.class)))
                .thenReturn(Flux.empty());

        StepVerifier.create(getUserReservationsUseCase.execute(userId))
                .verifyComplete();

        verify(reservationRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Debe retornar una sola reservación cuando solo hay una")
    void testExecute_WhenOneReservation_ShouldReturnOne() {
        UserId userId = new UserId(1L);

        when(reservationRepository.findByUserId(any(UserId.class)))
                .thenReturn(Flux.just(reservation1));

        StepVerifier.create(getUserReservationsUseCase.execute(userId))
                .expectNext(reservation1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar error del repositorio")
    void testExecute_WhenRepositoryError_ShouldPropagateError() {
        UserId userId = new UserId(1L);

        when(reservationRepository.findByUserId(any(UserId.class)))
                .thenReturn(Flux.error(new RuntimeException("Database error")));

        StepVerifier.create(getUserReservationsUseCase.execute(userId))
                .expectError(RuntimeException.class)
                .verify();
    }
}


package org.example.domain.model;

import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.valueobject.FlightId;
import org.example.domain.valueobject.ReservationId;
import org.example.domain.valueobject.TicketId;
import org.example.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Reservation - 100% Coverage
 */
class ReservationTest {

    private Reservation confirmedReservation;
    private Reservation pendingReservation;
    private Reservation cancelledReservation;

    @BeforeEach
    void setUp() {
        confirmedReservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        pendingReservation = Reservation.builder()
                .id(new ReservationId(2L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.PENDING)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        cancelledReservation = Reservation.builder()
                .id(new ReservationId(3L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CANCELLED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe crear reservación con status CONFIRMED")
    void testCreate_ShouldCreateReservationWithConfirmedStatus() {
        Reservation newReservation = Reservation.create(
                new UserId(1L),
                new FlightId(1L),
                new TicketId(1L)
        );

        assertNotNull(newReservation);
        assertTrue(newReservation.isConfirmed());
        assertNotNull(newReservation.getReservationDate());
        assertNotNull(newReservation.getCreatedAt());
    }

    @Test
    @DisplayName("Debe confirmar reservación pendiente")
    void testConfirm_WhenPending_ShouldSetStatusToConfirmed() {
        pendingReservation.confirm();
        assertTrue(pendingReservation.isConfirmed());
        assertNotNull(pendingReservation.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe lanzar excepción al confirmar reservación cancelada")
    void testConfirm_WhenCancelled_ShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> cancelledReservation.confirm());
    }

    @Test
    @DisplayName("Debe confirmar reservación ya confirmada sin error")
    void testConfirm_WhenAlreadyConfirmed_ShouldNotThrowException() {
        assertDoesNotThrow(() -> confirmedReservation.confirm());
        assertTrue(confirmedReservation.isConfirmed());
    }

    @Test
    @DisplayName("Debe cancelar reservación con razón")
    void testCancel_ShouldSetStatusToCancelled() {
        confirmedReservation.cancel("User requested cancellation");
        assertTrue(confirmedReservation.isCancelled());
        assertEquals("User requested cancellation", confirmedReservation.getObservations());
        assertNotNull(confirmedReservation.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe lanzar excepción al cancelar reservación ya cancelada")
    void testCancel_WhenAlreadyCancelled_ShouldThrowException() {
        assertThrows(IllegalStateException.class,
                () -> cancelledReservation.cancel("Second cancellation"));
    }

    @Test
    @DisplayName("Debe cancelar con razón null")
    void testCancel_WithNullReason_ShouldCancelWithNullObservations() {
        confirmedReservation.cancel(null);
        assertTrue(confirmedReservation.isCancelled());
        assertNull(confirmedReservation.getObservations());
    }

    @Test
    @DisplayName("Debe actualizar observaciones correctamente")
    void testUpdateObservations_ShouldUpdateObservations() {
        confirmedReservation.updateObservations("New observations");
        assertEquals("New observations", confirmedReservation.getObservations());
        assertNotNull(confirmedReservation.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe retornar true cuando está confirmada")
    void testIsConfirmed_WhenConfirmed_ShouldReturnTrue() {
        assertTrue(confirmedReservation.isConfirmed());
    }

    @Test
    @DisplayName("Debe retornar false cuando no está confirmada")
    void testIsConfirmed_WhenNotConfirmed_ShouldReturnFalse() {
        assertFalse(pendingReservation.isConfirmed());
        assertFalse(cancelledReservation.isConfirmed());
    }

    @Test
    @DisplayName("Debe retornar true cuando está cancelada")
    void testIsCancelled_WhenCancelled_ShouldReturnTrue() {
        assertTrue(cancelledReservation.isCancelled());
    }

    @Test
    @DisplayName("Debe retornar false cuando no está cancelada")
    void testIsCancelled_WhenNotCancelled_ShouldReturnFalse() {
        assertFalse(confirmedReservation.isCancelled());
        assertFalse(pendingReservation.isCancelled());
    }

    @Test
    @DisplayName("Debe retornar true cuando está pendiente")
    void testIsPending_WhenPending_ShouldReturnTrue() {
        assertTrue(pendingReservation.isPending());
    }

    @Test
    @DisplayName("Debe retornar false cuando no está pendiente")
    void testIsPending_WhenNotPending_ShouldReturnFalse() {
        assertFalse(confirmedReservation.isPending());
        assertFalse(cancelledReservation.isPending());
    }

    @Test
    @DisplayName("Debe verificar todos los getters de la reservación")
    void testAllGetters_ShouldReturnCorrectValues() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDate = now.minusDays(1);
        LocalDateTime updated = now.plusHours(1);

        Reservation completeReservation = Reservation.builder()
                .id(new ReservationId(99L))
                .userId(new UserId(5L))
                .flightId(new FlightId(10L))
                .ticketId(new TicketId(20L))
                .status(ReservationStatus.CONFIRMED)
                .observations("Test observations")
                .reservationDate(reservationDate)
                .createdAt(now)
                .updatedAt(updated)
                .build();

        assertEquals(new ReservationId(99L), completeReservation.getId());
        assertEquals(new UserId(5L), completeReservation.getUserId());
        assertEquals(new FlightId(10L), completeReservation.getFlightId());
        assertEquals(new TicketId(20L), completeReservation.getTicketId());
        assertEquals(ReservationStatus.CONFIRMED, completeReservation.getStatus());
        assertEquals("Test observations", completeReservation.getObservations());
        assertEquals(reservationDate, completeReservation.getReservationDate());
        assertEquals(now, completeReservation.getCreatedAt());
        assertEquals(updated, completeReservation.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe retornar null para updatedAt cuando no se ha actualizado")
    void testGetUpdatedAt_WhenNotUpdated_ShouldReturnNull() {
        Reservation newReservation = Reservation.create(
                new UserId(1L),
                new FlightId(1L),
                new TicketId(1L)
        );
        assertNull(newReservation.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe retornar null para observations cuando no están configuradas")
    void testGetObservations_WhenNotSet_ShouldReturnNull() {
        Reservation newReservation = Reservation.create(
                new UserId(1L),
                new FlightId(1L),
                new TicketId(1L)
        );
        assertNull(newReservation.getObservations());
    }

    @Test
    @DisplayName("Debe actualizar observaciones a valor vacío")
    void testUpdateObservations_WithEmptyString_ShouldUpdate() {
        confirmedReservation.updateObservations("");
        assertEquals("", confirmedReservation.getObservations());
    }

    @Test
    @DisplayName("Debe actualizar observaciones a null")
    void testUpdateObservations_WithNull_ShouldUpdate() {
        confirmedReservation.updateObservations("Initial");
        confirmedReservation.updateObservations(null);
        assertNull(confirmedReservation.getObservations());
    }
}

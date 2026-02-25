package org.example.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para todas las excepciones de dominio - 100% Coverage
 */
class DomainExceptionsTest {

    // === DomainException Tests ===
    @Test
    @DisplayName("DomainException - Debe crear con mensaje")
    void testDomainException_WithMessage() {
        DomainException exception = new DomainException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    @DisplayName("DomainException - Debe crear con mensaje y causa")
    void testDomainException_WithMessageAndCause() {
        Throwable cause = new RuntimeException("Cause");
        DomainException exception = new DomainException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    // === FlightNotFoundException Tests ===
    @Test
    @DisplayName("FlightNotFoundException - Debe crear con ID")
    void testFlightNotFoundException_WithId() {
        FlightNotFoundException exception = new FlightNotFoundException(123L);
        assertTrue(exception.getMessage().contains("123"));
    }

    @Test
    @DisplayName("FlightNotFoundException - Debe crear con mensaje")
    void testFlightNotFoundException_WithMessage() {
        FlightNotFoundException exception = new FlightNotFoundException("Flight not found");
        assertEquals("Flight not found", exception.getMessage());
    }

    // === ReservationNotFoundException Tests ===
    @Test
    @DisplayName("ReservationNotFoundException - Debe crear con ID")
    void testReservationNotFoundException_WithId() {
        ReservationNotFoundException exception = new ReservationNotFoundException(456L);
        assertTrue(exception.getMessage().contains("456"));
    }

    @Test
    @DisplayName("ReservationNotFoundException - Debe crear con mensaje")
    void testReservationNotFoundException_WithMessage() {
        ReservationNotFoundException exception = new ReservationNotFoundException("Reservation not found");
        assertEquals("Reservation not found", exception.getMessage());
    }

    // === UserNotFoundException Tests ===
    @Test
    @DisplayName("UserNotFoundException - Debe crear con ID")
    void testUserNotFoundException_WithId() {
        UserNotFoundException exception = new UserNotFoundException(789L);
        assertTrue(exception.getMessage().contains("789"));
    }

    @Test
    @DisplayName("UserNotFoundException - Debe crear con mensaje")
    void testUserNotFoundException_WithMessage() {
        UserNotFoundException exception = new UserNotFoundException("User not found");
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("UserNotFoundException - Debe crear con username")
    void testUserNotFoundException_WithUsername() {
        UserNotFoundException exception = new UserNotFoundException("testuser", true);
        assertTrue(exception.getMessage().contains("testuser"));
    }

    // === NoSeatsAvailableException Tests ===
    @Test
    @DisplayName("NoSeatsAvailableException - Debe crear con mensaje")
    void testNoSeatsAvailableException_WithMessage() {
        NoSeatsAvailableException exception = new NoSeatsAvailableException("No seats");
        assertEquals("No seats", exception.getMessage());
    }

    // === SeatAlreadyTakenException Tests ===
    @Test
    @DisplayName("SeatAlreadyTakenException - Debe crear con asiento")
    void testSeatAlreadyTakenException_WithSeat() {
        SeatAlreadyTakenException exception = new SeatAlreadyTakenException("12A");
        assertTrue(exception.getMessage().contains("12A"));
    }

    // === InvalidBookingException Tests ===
    @Test
    @DisplayName("InvalidBookingException - Debe crear con mensaje")
    void testInvalidBookingException_WithMessage() {
        InvalidBookingException exception = new InvalidBookingException("Invalid booking");
        assertEquals("Invalid booking", exception.getMessage());
    }

    // === AuthenticationException Tests ===
    @Test
    @DisplayName("AuthenticationException - Debe crear con mensaje")
    void testAuthenticationException_WithMessage() {
        AuthenticationException exception = new AuthenticationException("Auth failed");
        assertEquals("Auth failed", exception.getMessage());
    }
}



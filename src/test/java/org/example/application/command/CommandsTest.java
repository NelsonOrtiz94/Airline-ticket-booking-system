package org.example.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para todos los Commands - 100% Coverage
 */
class CommandsTest {

    // === BookTicketCommand Tests ===
    @Test
    @DisplayName("BookTicketCommand - Debe crear con todos los parámetros")
    void testBookTicketCommand_ShouldCreate() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 2L, "John Doe", "12A", "ECONOMY"
        );

        assertEquals(1L, command.userId());
        assertEquals(2L, command.flightId());
        assertEquals("John Doe", command.passengerName());
        assertEquals("12A", command.seatNumber());
        assertEquals("ECONOMY", command.ticketClass());
    }

    @Test
    @DisplayName("BookTicketCommand - equals y hashCode deben funcionar")
    void testBookTicketCommand_EqualsAndHashCode() {
        BookTicketCommand command1 = new BookTicketCommand(1L, 2L, "John", "12A", "ECONOMY");
        BookTicketCommand command2 = new BookTicketCommand(1L, 2L, "John", "12A", "ECONOMY");
        BookTicketCommand command3 = new BookTicketCommand(1L, 3L, "John", "12A", "ECONOMY");

        assertEquals(command1, command2);
        assertNotEquals(command1, command3);
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    @DisplayName("BookTicketCommand - toString debe funcionar")
    void testBookTicketCommand_ToString() {
        BookTicketCommand command = new BookTicketCommand(1L, 2L, "John", "12A", "ECONOMY");
        String str = command.toString();
        assertNotNull(str);
        assertTrue(str.contains("John"));
    }

    // === CancelReservationCommand Tests ===
    @Test
    @DisplayName("CancelReservationCommand - Debe crear con todos los parámetros")
    void testCancelReservationCommand_ShouldCreate() {
        CancelReservationCommand command = new CancelReservationCommand(1L, "User requested");

        assertEquals(1L, command.reservationId());
        assertEquals("User requested", command.reason());
    }

    @Test
    @DisplayName("CancelReservationCommand - Debe crear con reason null")
    void testCancelReservationCommand_WithNullReason() {
        CancelReservationCommand command = new CancelReservationCommand(1L, null);

        assertEquals(1L, command.reservationId());
        assertNull(command.reason());
    }

    @Test
    @DisplayName("CancelReservationCommand - equals y hashCode deben funcionar")
    void testCancelReservationCommand_EqualsAndHashCode() {
        CancelReservationCommand command1 = new CancelReservationCommand(1L, "reason");
        CancelReservationCommand command2 = new CancelReservationCommand(1L, "reason");

        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    // === UpdateReservationCommand Tests ===
    @Test
    @DisplayName("UpdateReservationCommand - Debe crear con todos los parámetros")
    void testUpdateReservationCommand_ShouldCreate() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, "15A", "New observations");

        assertEquals(1L, command.reservationId());
        assertEquals("15A", command.seatNumber());
        assertEquals("New observations", command.observations());
    }

    @Test
    @DisplayName("UpdateReservationCommand - Debe crear con observations null")
    void testUpdateReservationCommand_WithNullObservations() {
        UpdateReservationCommand command = new UpdateReservationCommand(1L, null, null);

        assertEquals(1L, command.reservationId());
        assertNull(command.seatNumber());
        assertNull(command.observations());
    }

    // === AuthenticateUserCommand Tests ===
    @Test
    @DisplayName("AuthenticateUserCommand - Debe crear con todos los parámetros")
    void testAuthenticateUserCommand_ShouldCreate() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("admin", "password123");

        assertEquals("admin", command.username());
        assertEquals("password123", command.password());
    }

    @Test
    @DisplayName("AuthenticateUserCommand - equals y hashCode deben funcionar")
    void testAuthenticateUserCommand_EqualsAndHashCode() {
        AuthenticateUserCommand command1 = new AuthenticateUserCommand("admin", "pass");
        AuthenticateUserCommand command2 = new AuthenticateUserCommand("admin", "pass");

        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    // === SearchFlightsCommand Tests ===
    @Test
    @DisplayName("SearchFlightsCommand - Debe crear con todos los parámetros")
    void testSearchFlightsCommand_ShouldCreate() {
        LocalDateTime date = LocalDateTime.now();
        SearchFlightsCommand command = new SearchFlightsCommand("BOG", "MDE", date, 2);

        assertEquals("BOG", command.origin());
        assertEquals("MDE", command.destination());
        assertEquals(date, command.departureDate());
        assertEquals(2, command.passengers());
    }

    @Test
    @DisplayName("SearchFlightsCommand - Debe crear con passengers null")
    void testSearchFlightsCommand_WithNullPassengers() {
        LocalDateTime date = LocalDateTime.now();
        SearchFlightsCommand command = new SearchFlightsCommand("BOG", "MDE", date, null);

        assertEquals("BOG", command.origin());
        assertEquals("MDE", command.destination());
        assertNull(command.passengers());
    }

    @Test
    @DisplayName("SearchFlightsCommand - Debe crear con date null")
    void testSearchFlightsCommand_WithNullDate() {
        SearchFlightsCommand command = new SearchFlightsCommand("BOG", "MDE", null, 1);

        assertEquals("BOG", command.origin());
        assertEquals("MDE", command.destination());
        assertNull(command.departureDate());
    }

    @Test
    @DisplayName("SearchFlightsCommand - equals y hashCode deben funcionar")
    void testSearchFlightsCommand_EqualsAndHashCode() {
        LocalDateTime date = LocalDateTime.now();
        SearchFlightsCommand command1 = new SearchFlightsCommand("BOG", "MDE", date, 2);
        SearchFlightsCommand command2 = new SearchFlightsCommand("BOG", "MDE", date, 2);

        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
    }
}


package org.example.domain.model;

import org.example.domain.model.enums.TicketClass;
import org.example.domain.model.enums.TicketStatus;
import org.example.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Ticket - 100% Coverage
 */
class TicketTest {

    private Ticket confirmedTicket;
    private Ticket cancelledTicket;

    @BeforeEach
    void setUp() {
        confirmedTicket = Ticket.builder()
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

        cancelledTicket = Ticket.builder()
                .id(new TicketId(2L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .passengerName("Jane Doe")
                .seatNumber(new SeatNumber("12B"))
                .price(new Price(BigDecimal.valueOf(250000)))
                .ticketClass(TicketClass.ECONOMY)
                .status(TicketStatus.CANCELLED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe crear ticket con status CONFIRMED")
    void testCreate_ShouldCreateTicketWithConfirmedStatus() {
        Ticket newTicket = Ticket.create(
                new UserId(1L),
                new FlightId(1L),
                "John Doe",
                new SeatNumber("1A"),
                new Price(BigDecimal.valueOf(100000)),
                TicketClass.BUSINESS
        );

        assertNotNull(newTicket);
        assertTrue(newTicket.isActive());
        assertEquals("John Doe", newTicket.getPassengerName());
        assertEquals(TicketClass.BUSINESS, newTicket.getTicketClass());
        assertNotNull(newTicket.getCreatedAt());
    }

    @Test
    @DisplayName("Debe cancelar ticket correctamente")
    void testCancel_ShouldSetStatusToCancelled() {
        confirmedTicket.cancel();

        assertTrue(confirmedTicket.isCancelled());
        assertFalse(confirmedTicket.isActive());
        assertNotNull(confirmedTicket.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe lanzar excepción al cancelar ticket ya cancelado")
    void testCancel_WhenAlreadyCancelled_ShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> cancelledTicket.cancel());
    }

    @Test
    @DisplayName("Debe retornar true cuando está activo")
    void testIsActive_WhenConfirmed_ShouldReturnTrue() {
        assertTrue(confirmedTicket.isActive());
    }

    @Test
    @DisplayName("Debe retornar false cuando no está activo")
    void testIsActive_WhenCancelled_ShouldReturnFalse() {
        assertFalse(cancelledTicket.isActive());
    }

    @Test
    @DisplayName("Debe retornar true cuando está cancelado")
    void testIsCancelled_WhenCancelled_ShouldReturnTrue() {
        assertTrue(cancelledTicket.isCancelled());
    }

    @Test
    @DisplayName("Debe retornar false cuando no está cancelado")
    void testIsCancelled_WhenConfirmed_ShouldReturnFalse() {
        assertFalse(confirmedTicket.isCancelled());
    }

    @Test
    @DisplayName("Debe crear ticket con diferentes clases")
    void testCreate_WithDifferentClasses_ShouldWork() {
        Ticket economyTicket = Ticket.create(
                new UserId(1L), new FlightId(1L), "Test", new SeatNumber("1A"),
                new Price(BigDecimal.valueOf(100000)), TicketClass.ECONOMY
        );
        assertEquals(TicketClass.ECONOMY, economyTicket.getTicketClass());

        Ticket businessTicket = Ticket.create(
                new UserId(1L), new FlightId(1L), "Test", new SeatNumber("1B"),
                new Price(BigDecimal.valueOf(200000)), TicketClass.BUSINESS
        );
        assertEquals(TicketClass.BUSINESS, businessTicket.getTicketClass());

        Ticket firstTicket = Ticket.create(
                new UserId(1L), new FlightId(1L), "Test", new SeatNumber("1C"),
                new Price(BigDecimal.valueOf(300000)), TicketClass.FIRST_CLASS
        );
        assertEquals(TicketClass.FIRST_CLASS, firstTicket.getTicketClass());
    }

    @Test
    @DisplayName("Debe mantener el precio correctamente")
    void testCreate_ShouldMaintainPrice() {
        Price expectedPrice = new Price(BigDecimal.valueOf(500000), "USD");

        Ticket ticket = Ticket.create(
                new UserId(1L),
                new FlightId(1L),
                "Test",
                new SeatNumber("1A"),
                expectedPrice,
                TicketClass.BUSINESS
        );

        assertEquals(expectedPrice, ticket.getPrice());
    }

    @Test
    @DisplayName("Debe retornar null para updatedAt cuando no se ha actualizado")
    void testGetUpdatedAt_WhenNotUpdated_ShouldReturnNull() {
        Ticket ticket = Ticket.create(
                new UserId(1L),
                new FlightId(1L),
                "Test",
                new SeatNumber("1A"),
                new Price(BigDecimal.valueOf(100000)),
                TicketClass.ECONOMY
        );

        assertNull(ticket.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe retornar null para id cuando se crea con factory method")
    void testCreate_ShouldHaveNullId() {
        Ticket ticket = Ticket.create(
                new UserId(1L),
                new FlightId(1L),
                "Test",
                new SeatNumber("1A"),
                new Price(BigDecimal.valueOf(100000)),
                TicketClass.ECONOMY
        );

        assertNull(ticket.getId());
    }

    @Test
    @DisplayName("Debe verificar todos los getters del ticket construido con builder")
    void testBuilder_ShouldSetAllFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updated = now.plusHours(1);

        Ticket ticket = Ticket.builder()
                .id(new TicketId(99L))
                .userId(new UserId(5L))
                .flightId(new FlightId(10L))
                .passengerName("Complete Test")
                .seatNumber(new SeatNumber("99F"))
                .price(new Price(BigDecimal.valueOf(999999)))
                .ticketClass(TicketClass.FIRST_CLASS)
                .status(TicketStatus.CONFIRMED)
                .createdAt(now)
                .updatedAt(updated)
                .build();

        assertEquals(new TicketId(99L), ticket.getId());
        assertEquals(new UserId(5L), ticket.getUserId());
        assertEquals(new FlightId(10L), ticket.getFlightId());
        assertEquals("Complete Test", ticket.getPassengerName());
        assertEquals(new SeatNumber("99F"), ticket.getSeatNumber());
        assertEquals(new Price(BigDecimal.valueOf(999999)), ticket.getPrice());
        assertEquals(TicketClass.FIRST_CLASS, ticket.getTicketClass());
        assertEquals(TicketStatus.CONFIRMED, ticket.getStatus());
        assertEquals(now, ticket.getCreatedAt());
        assertEquals(updated, ticket.getUpdatedAt());
    }

    @Test
    @DisplayName("Debe verificar getStatus del ticket")
    void testGetStatus_ShouldReturnCorrectStatus() {
        assertEquals(TicketStatus.CONFIRMED, confirmedTicket.getStatus());
        assertEquals(TicketStatus.CANCELLED, cancelledTicket.getStatus());
    }

    @Test
    @DisplayName("Debe verificar getUserId del ticket")
    void testGetUserId_ShouldReturnCorrectUserId() {
        assertEquals(new UserId(1L), confirmedTicket.getUserId());
    }

    @Test
    @DisplayName("Debe verificar getFlightId del ticket")
    void testGetFlightId_ShouldReturnCorrectFlightId() {
        assertEquals(new FlightId(1L), confirmedTicket.getFlightId());
    }

    @Test
    @DisplayName("Debe verificar getSeatNumber del ticket")
    void testGetSeatNumber_ShouldReturnCorrectSeatNumber() {
        assertEquals(new SeatNumber("12A"), confirmedTicket.getSeatNumber());
    }

    @Test
    @DisplayName("Debe crear ticket con clase PREMIUM_ECONOMY")
    void testCreate_WithPremiumEconomy_ShouldWork() {
        Ticket premiumTicket = Ticket.create(
                new UserId(1L), new FlightId(1L), "Test", new SeatNumber("1D"),
                new Price(BigDecimal.valueOf(150000)), TicketClass.PREMIUM_ECONOMY
        );
        assertEquals(TicketClass.PREMIUM_ECONOMY, premiumTicket.getTicketClass());
    }
}

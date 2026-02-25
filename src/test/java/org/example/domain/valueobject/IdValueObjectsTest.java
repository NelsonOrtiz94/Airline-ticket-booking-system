package org.example.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para todos los Value Objects de ID - 100% Coverage
 */
class IdValueObjectsTest {

    // === FlightId Tests ===
    @Test
    @DisplayName("FlightId - Debe crear FlightId válido")
    void testFlightId_ValidId_ShouldCreate() {
        FlightId flightId = new FlightId(1L);
        assertEquals(1L, flightId.value());
    }

    @Test
    @DisplayName("FlightId - Debe lanzar excepción para ID null")
    void testFlightId_NullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FlightId(null));
    }

    @Test
    @DisplayName("FlightId - Debe lanzar excepción para ID negativo")
    void testFlightId_NegativeId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FlightId(-1L));
    }

    @Test
    @DisplayName("FlightId - Debe lanzar excepción para ID cero")
    void testFlightId_ZeroId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FlightId(0L));
    }

    @Test
    @DisplayName("FlightId - equals y hashCode deben funcionar correctamente")
    void testFlightId_EqualsAndHashCode() {
        FlightId id1 = new FlightId(1L);
        FlightId id2 = new FlightId(1L);
        FlightId id3 = new FlightId(2L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    // === TicketId Tests ===
    @Test
    @DisplayName("TicketId - Debe crear TicketId válido")
    void testTicketId_ValidId_ShouldCreate() {
        TicketId ticketId = new TicketId(1L);
        assertEquals(1L, ticketId.value());
    }

    @Test
    @DisplayName("TicketId - Debe lanzar excepción para ID null")
    void testTicketId_NullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TicketId(null));
    }

    @Test
    @DisplayName("TicketId - Debe lanzar excepción para ID negativo")
    void testTicketId_NegativeId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TicketId(-1L));
    }

    @Test
    @DisplayName("TicketId - Debe lanzar excepción para ID cero")
    void testTicketId_ZeroId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TicketId(0L));
    }

    // === ReservationId Tests ===
    @Test
    @DisplayName("ReservationId - Debe crear ReservationId válido")
    void testReservationId_ValidId_ShouldCreate() {
        ReservationId reservationId = new ReservationId(1L);
        assertEquals(1L, reservationId.value());
    }

    @Test
    @DisplayName("ReservationId - Debe lanzar excepción para ID null")
    void testReservationId_NullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new ReservationId(null));
    }

    @Test
    @DisplayName("ReservationId - Debe lanzar excepción para ID negativo")
    void testReservationId_NegativeId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new ReservationId(-1L));
    }

    // === UserId Tests ===
    @Test
    @DisplayName("UserId - Debe crear UserId válido")
    void testUserId_ValidId_ShouldCreate() {
        UserId userId = new UserId(1L);
        assertEquals(1L, userId.value());
    }

    @Test
    @DisplayName("UserId - Debe lanzar excepción para ID null")
    void testUserId_NullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new UserId(null));
    }

    @Test
    @DisplayName("UserId - Debe lanzar excepción para ID negativo")
    void testUserId_NegativeId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new UserId(-1L));
    }

    // === FlightNumber Tests ===
    @Test
    @DisplayName("FlightNumber - Debe crear FlightNumber válido")
    void testFlightNumber_ValidNumber_ShouldCreate() {
        FlightNumber flightNumber = new FlightNumber("AV101");
        assertEquals("AV101", flightNumber.value());
    }

    @Test
    @DisplayName("FlightNumber - Debe lanzar excepción para null")
    void testFlightNumber_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FlightNumber(null));
    }

    @Test
    @DisplayName("FlightNumber - Debe lanzar excepción para vacío")
    void testFlightNumber_Empty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FlightNumber(""));
    }

    @Test
    @DisplayName("FlightNumber - Debe lanzar excepción para solo espacios")
    void testFlightNumber_Blank_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FlightNumber("   "));
    }

    // === Location Tests ===
    @Test
    @DisplayName("Location - Debe crear Location válido")
    void testLocation_ValidLocation_ShouldCreate() {
        Location location = new Location("BOG");
        assertEquals("BOG", location.value());
    }

    @Test
    @DisplayName("Location - Debe lanzar excepción para null")
    void testLocation_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(null));
    }

    @Test
    @DisplayName("Location - Debe lanzar excepción para vacío")
    void testLocation_Empty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Location(""));
    }

    @Test
    @DisplayName("Location - Debe lanzar excepción para solo espacios")
    void testLocation_Blank_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Location("   "));
    }

    // === Airline Tests ===
    @Test
    @DisplayName("Airline - Debe crear Airline válido")
    void testAirline_ValidAirline_ShouldCreate() {
        Airline airline = new Airline("Avianca");
        assertEquals("Avianca", airline.name());
    }

    @Test
    @DisplayName("Airline - Debe lanzar excepción para null")
    void testAirline_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Airline(null));
    }

    @Test
    @DisplayName("Airline - Debe lanzar excepción para vacío")
    void testAirline_Empty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Airline(""));
    }

    @Test
    @DisplayName("Airline - Debe lanzar excepción para solo espacios")
    void testAirline_Blank_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Airline("   "));
    }

    // === Username Tests ===
    @Test
    @DisplayName("Username - Debe crear Username válido")
    void testUsername_ValidUsername_ShouldCreate() {
        Username username = new Username("admin");
        assertEquals("admin", username.value());
    }

    @Test
    @DisplayName("Username - Debe lanzar excepción para null")
    void testUsername_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Username(null));
    }

    @Test
    @DisplayName("Username - Debe lanzar excepción para vacío")
    void testUsername_Empty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Username(""));
    }

    @Test
    @DisplayName("Username - Debe lanzar excepción para solo espacios")
    void testUsername_Blank_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Username("   "));
    }

    // === SeatNumber Tests ===
    @Test
    @DisplayName("SeatNumber - Debe crear SeatNumber válido")
    void testSeatNumber_ValidSeatNumber_ShouldCreate() {
        SeatNumber seatNumber = new SeatNumber("12A");
        assertEquals("12A", seatNumber.value());
    }

    @Test
    @DisplayName("SeatNumber - Debe crear SeatNumber con 1 dígito")
    void testSeatNumber_SingleDigit_ShouldCreate() {
        SeatNumber seatNumber = new SeatNumber("1B");
        assertEquals("1B", seatNumber.value());
    }

    @Test
    @DisplayName("SeatNumber - Debe lanzar excepción para null")
    void testSeatNumber_Null_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber(null));
    }

    @Test
    @DisplayName("SeatNumber - Debe lanzar excepción para vacío")
    void testSeatNumber_Empty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber(""));
    }

    @Test
    @DisplayName("SeatNumber - Debe lanzar excepción para letra fuera de rango A-F")
    void testSeatNumber_InvalidLetter_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber("12Z"));
    }

    @Test
    @DisplayName("SeatNumber - Debe lanzar excepción para formato inválido sin número")
    void testSeatNumber_NoNumber_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber("A"));
    }

    @Test
    @DisplayName("SeatNumber - Debe lanzar excepción para formato inválido con 3 dígitos")
    void testSeatNumber_ThreeDigits_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber("123A"));
    }

    @Test
    @DisplayName("SeatNumber - Debe lanzar excepción para solo espacios")
    void testSeatNumber_Blank_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber("   "));
    }

    // === FullName Tests ===
    @Test
    @DisplayName("FullName - Debe crear FullName válido")
    void testFullName_ValidFullName_ShouldCreate() {
        FullName fullName = new FullName("John", "Doe");
        assertEquals("John", fullName.firstName());
        assertEquals("Doe", fullName.lastName());
        assertEquals("John Doe", fullName.fullName());
    }

    @Test
    @DisplayName("FullName - Debe lanzar excepción para firstName null")
    void testFullName_NullFirstName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FullName(null, "Doe"));
    }

    @Test
    @DisplayName("FullName - Debe lanzar excepción para lastName null")
    void testFullName_NullLastName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FullName("John", null));
    }

    @Test
    @DisplayName("FullName - Debe lanzar excepción para firstName vacío")
    void testFullName_EmptyFirstName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FullName("", "Doe"));
    }

    @Test
    @DisplayName("FullName - Debe lanzar excepción para lastName vacío")
    void testFullName_EmptyLastName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new FullName("John", ""));
    }
}



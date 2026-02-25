package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.port.out.FlightRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.model.Flight;
import org.example.domain.model.Reservation;
import org.example.domain.model.Ticket;
import org.example.domain.model.enums.FlightStatus;
import org.example.domain.model.enums.ReservationStatus;
import org.example.domain.model.enums.TicketClass;
import org.example.domain.model.enums.TicketStatus;
import org.example.domain.valueobject.*;
import org.example.infrastructure.entrypoints.rest.dto.response.ReservationResponseDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para ReservationResponseMapper - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class ReservationResponseMapperTest {

    @Mock
    private FlightRepositoryPort flightRepository;

    @Mock
    private TicketRepositoryPort ticketRepository;

    @InjectMocks
    private ReservationResponseMapper mapper;

    private Reservation reservation;
    private Flight flight;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .observations("Test observation")
                .reservationDate(LocalDateTime.of(2026, 2, 16, 10, 0))
                .createdAt(LocalDateTime.now())
                .build();

        flight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.of(2026, 2, 20, 10, 0))
                .arrivalTime(LocalDateTime.of(2026, 2, 20, 12, 0))
                .availableSeats(50)
                .totalSeats(100)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
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
    }

    @Test
    @DisplayName("Debe convertir Reservation a ReservationResponseDTO con detalles")
    void testToResponseWithDetails_ShouldMapAllFields() {
        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Mono.just(ticket));
        when(flightRepository.findById(any(FlightId.class))).thenReturn(Mono.just(flight));

        StepVerifier.create(mapper.toResponseWithDetails(reservation))
                .expectNextMatches(response -> {
                    assertEquals(1L, response.getReservationId());
                    assertEquals(1L, response.getUserId());
                    assertEquals(1L, response.getFlightId());
                    assertEquals(1L, response.getTicketId());
                    assertEquals("CONFIRMED", response.getStatus());
                    assertEquals("Test observation", response.getObservations());
                    assertEquals("AV101", response.getFlightNumber());
                    assertEquals("BOG", response.getOrigin());
                    assertEquals("MDE", response.getDestination());
                    assertEquals("John Doe", response.getPassengerName());
                    assertEquals("12A", response.getSeatNumber());
                    assertEquals(BigDecimal.valueOf(250000), response.getPrice());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar Mono vacío cuando Reservation es null")
    void testToResponseWithDetails_WhenReservationIsNull_ShouldReturnEmpty() {
        StepVerifier.create(mapper.toResponseWithDetails(null))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe convertir a respuesta básica correctamente")
    void testToBasicResponse_ShouldMapBasicFields() {
        ReservationResponseDTO response = mapper.toBasicResponse(reservation);

        assertNotNull(response);
        assertEquals(1L, response.getReservationId());
        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getFlightId());
        assertEquals(1L, response.getTicketId());
        assertEquals("CONFIRMED", response.getStatus());
        assertEquals("Test observation", response.getObservations());
        assertEquals(LocalDateTime.of(2026, 2, 16, 10, 0), response.getReservationDate());
    }

    @Test
    @DisplayName("Debe retornar null cuando Reservation es null en respuesta básica")
    void testToBasicResponse_WhenReservationIsNull_ShouldReturnNull() {
        ReservationResponseDTO response = mapper.toBasicResponse(null);

        assertNull(response);
    }

    @Test
    @DisplayName("Debe manejar reservación cancelada")
    void testToBasicResponse_WithCancelledStatus_ShouldMapCorrectly() {
        Reservation cancelledReservation = Reservation.builder()
                .id(new ReservationId(2L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CANCELLED)
                .observations("Cancelled by user")
                .reservationDate(LocalDateTime.now())
                .build();

        ReservationResponseDTO response = mapper.toBasicResponse(cancelledReservation);

        assertEquals("CANCELLED", response.getStatus());
        assertEquals("Cancelled by user", response.getObservations());
    }
}


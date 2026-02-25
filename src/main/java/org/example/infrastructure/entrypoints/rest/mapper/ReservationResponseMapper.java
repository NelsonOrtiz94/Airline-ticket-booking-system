package org.example.infrastructure.entrypoints.rest.mapper;

import lombok.RequiredArgsConstructor;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.model.Reservation;
import org.example.infrastructure.entrypoints.rest.dto.response.ReservationResponseDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Mapper para convertir modelos de dominio Reservation a DTOs de respuesta
 */
@Component
@RequiredArgsConstructor
public class ReservationResponseMapper {

    private final FlightRepositoryPort flightRepository;
    private final TicketRepositoryPort ticketRepository;

    /**
     * Convierte una Reservation a ReservationResponse con detalles completos
     */
    public Mono<ReservationResponseDTO> toResponseWithDetails(Reservation reservation) {
        if (reservation == null) {
            return Mono.empty();
        }

        return Mono.zip(
            ticketRepository.findById(reservation.getTicketId()),
            flightRepository.findById(reservation.getFlightId())
        ).map(tuple -> {
            var ticket = tuple.getT1();
            var flight = tuple.getT2();

            return ReservationResponseDTO.builder()
                    .reservationId(reservation.getId().value())
                    .userId(reservation.getUserId().value())
                    .ticketId(reservation.getTicketId().value())
                    .flightId(reservation.getFlightId().value())
                    .status(reservation.getStatus().name())
                    .observations(reservation.getObservations())
                    .reservationDate(reservation.getReservationDate())
                    .flightNumber(flight.getFlightNumber().value())
                    .origin(flight.getOrigin().value())
                    .destination(flight.getDestination().value())
                    .departureTime(flight.getDepartureTime())
                    .passengerName(ticket.getPassengerName())
                    .seatNumber(ticket.getSeatNumber().value())
                    .price(ticket.getPrice().amount())
                    .build();
        });
    }

    /**
     * Convierte una Reservation a ReservationResponse básico (sin detalles)
     */
    public ReservationResponseDTO toBasicResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return ReservationResponseDTO.builder()
                .reservationId(reservation.getId().value())
                .userId(reservation.getUserId().value())
                .ticketId(reservation.getTicketId().value())
                .flightId(reservation.getFlightId().value())
                .status(reservation.getStatus().name())
                .observations(reservation.getObservations())
                .reservationDate(reservation.getReservationDate())
                .build();
    }
}


package org.example.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.command.UpdateReservationCommand;
import org.example.application.port.in.UpdateReservationUseCase;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.exception.ReservationNotFoundException;
import org.example.domain.exception.SeatAlreadyTakenException;
import org.example.domain.model.Reservation;
import org.example.domain.model.Ticket;
import org.example.domain.service.ReservationDomainService;
import org.example.domain.valueobject.ReservationId;
import org.example.domain.valueobject.SeatNumber;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementación del caso de uso para actualizar reservaciones
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateReservationUseCaseImpl implements UpdateReservationUseCase {

    private final ReservationRepositoryPort reservationRepository;
    private final TicketRepositoryPort ticketRepository;
    private final ReservationDomainService reservationDomainService;

    @Override
    public Mono<Reservation> execute(UpdateReservationCommand command) {
        log.info("Actualizando reservación ID: {}", command.reservationId());

        ReservationId reservationId = new ReservationId(command.reservationId());

        return reservationRepository.findById(reservationId)
                .switchIfEmpty(Mono.error(
                    new ReservationNotFoundException(command.reservationId())
                ))
                .flatMap(reservation -> {
                    // Validar con servicio de dominio
                    reservationDomainService.validateUpdate(reservation);

                    // Si se proporciona un nuevo número de asiento, actualizarlo
                    if (command.seatNumber() != null && !command.seatNumber().isBlank()) {
                        return updateTicketSeat(reservation, command.seatNumber())
                                .flatMap(ticket -> updateReservationData(reservation, command));
                    } else {
                        return updateReservationData(reservation, command);
                    }
                })
                .doOnSuccess(r ->
                    log.info("Reservación actualizada exitosamente: {}", r.getId().value())
                )
                .doOnError(error ->
                    log.error("Error al actualizar reservación: {}", error.getMessage())
                );
    }

    private Mono<Ticket> updateTicketSeat(Reservation reservation, String newSeatNumber) {
        SeatNumber seatNumber = new SeatNumber(newSeatNumber);

        return ticketRepository.findById(reservation.getTicketId())
                .flatMap(ticket -> {
                    // Verificar si el asiento ya está ocupado (solo si es diferente al actual)
                    if (!ticket.getSeatNumber().equals(seatNumber)) {
                        return ticketRepository.isSeatTaken(ticket.getFlightId(), seatNumber)
                                .flatMap(isTaken -> {
                                    if (Boolean.TRUE.equals(isTaken)) {
                                        return Mono.error(new SeatAlreadyTakenException(newSeatNumber));
                                    }
                                    // Actualizar el asiento
                                    ticket.updateSeatNumber(seatNumber);
                                    return ticketRepository.update(ticket);
                                });
                    }
                    return Mono.just(ticket);
                });
    }

    private Mono<Reservation> updateReservationData(Reservation reservation, UpdateReservationCommand command) {
        // Actualizar observaciones
        if (command.observations() != null) {
            reservation.updateObservations(command.observations());
        }

        return reservationRepository.update(reservation);
    }
}


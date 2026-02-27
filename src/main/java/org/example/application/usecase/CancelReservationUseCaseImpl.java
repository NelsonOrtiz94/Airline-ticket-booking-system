package org.example.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.command.CancelReservationCommand;
import org.example.application.port.in.CancelReservationUseCase;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.exception.ReservationNotFoundException;
import org.example.domain.service.ReservationDomainService;
import org.example.domain.valueobject.ReservationId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementación del caso de uso para cancelar reservaciones
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CancelReservationUseCaseImpl implements CancelReservationUseCase {

    private final ReservationRepositoryPort reservationRepository;
    private final TicketRepositoryPort ticketRepository;
    private final FlightRepositoryPort flightRepository;
    private final ReservationDomainService reservationDomainService;

    @Override
    public Mono<Void> execute(CancelReservationCommand command) {
        log.info("Cancelando reservación ID: {}", command.reservationId());

        ReservationId reservationId = new ReservationId(command.reservationId());

        return reservationRepository.findById(reservationId)
                .switchIfEmpty(Mono.error(
                    new ReservationNotFoundException(command.reservationId())
                ))
                .flatMap(reservation -> {
                    // Validar con servicio de dominio
                    reservationDomainService.validateCancellation(reservation);

                    // Cancelar reservación
                    reservation.cancel(command.reason());

                    // Cancelar ticket y liberar asiento
                    return ticketRepository.findById(reservation.getTicketId())
                        .flatMap(ticket -> {
                            ticket.cancel();
                            return ticketRepository.update(ticket)
                                .then(flightRepository.findById(reservation.getFlightId()))
                                .flatMap(flight -> {
                                    flight.releaseSeats(1);
                                    return flightRepository.update(flight);
                                })
                                .then(reservationRepository.update(reservation));
                        })
                        .then();
                })
                .doOnSuccess(v ->
                    log.info("Reservación cancelada exitosamente: {}", command.reservationId())
                )
                .doOnError(error ->
                    log.error("Error al cancelar reservación: {}", error.getMessage())
                );
    }
}


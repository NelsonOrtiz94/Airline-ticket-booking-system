package org.example.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.command.BookTicketCommand;
import org.example.application.port.in.BookTicketUseCase;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.domain.exception.FlightNotFoundException;
import org.example.domain.exception.SeatAlreadyTakenException;
import org.example.domain.model.Flight;
import org.example.domain.model.Reservation;
import org.example.domain.model.Ticket;
import org.example.domain.service.PriceCalculationService;
import org.example.domain.service.ReservationDomainService;
import org.example.domain.model.enums.TicketClass;
import org.example.domain.valueobject.FlightId;
import org.example.domain.valueobject.Price;
import org.example.domain.valueobject.SeatNumber;
import org.example.domain.valueobject.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookTicketUseCaseImpl implements BookTicketUseCase {

    private final FlightRepositoryPort flightRepository;
    private final TicketRepositoryPort ticketRepository;
    private final ReservationRepositoryPort reservationRepository;
    private final ReservationDomainService reservationDomainService;
    private final PriceCalculationService priceCalculationService;

    @Override
    public Mono<Reservation> execute(BookTicketCommand command) {
        log.info("Procesando reserva para vuelo ID: {} y usuario ID: {}",
                command.flightId(), command.userId());

        FlightId flightId = new FlightId(command.flightId());
        SeatNumber seatNumber = new SeatNumber(command.seatNumber());

        return flightRepository.findById(flightId)
                .switchIfEmpty(Mono.error(new FlightNotFoundException(command.flightId())))
                .flatMap(flight -> validateSeatAvailability(flight, flightId, seatNumber)
                        .then(Mono.just(flight)))
                .flatMap(flight -> {
                    // Validar con el servicio de dominio
                    reservationDomainService.validateReservation(flight, 1);

                    // Reservar asiento
                    flight.reserveSeats(1);

                    // Calcular precio según clase
                    TicketClass ticketClass = TicketClass.valueOf(command.ticketClass());
                    Price finalPrice = priceCalculationService.calculatePrice(flight.getPrice(), ticketClass);

                    // Crear ticket
                    Ticket ticket = Ticket.create(
                        new UserId(command.userId()),
                        flightId,
                        command.passengerName(),
                        seatNumber,
                        finalPrice,
                        ticketClass
                    );

                    return ticketRepository.save(ticket)
                        .flatMap(savedTicket -> {
                            // Crear reservación
                            Reservation reservation = Reservation.create(
                                new UserId(command.userId()),
                                flightId,
                                savedTicket.getId()
                            );

                            return reservationRepository.save(reservation)
                                .flatMap(savedReservation ->
                                    flightRepository.update(flight)
                                        .thenReturn(savedReservation)
                                );
                        });
                })
                .doOnSuccess(reservation ->
                    log.info("Reserva creada exitosamente con ID: {}", reservation.getId().value())
                )
                .doOnError(error ->
                    log.error("Error al crear reserva: {}", error.getMessage())
                );
    }

    private Mono<Void> validateSeatAvailability(Flight flight, FlightId flightId, SeatNumber seatNumber) {
        return ticketRepository.isSeatTaken(flightId, seatNumber)
                .flatMap(isTaken -> {
                    if (isTaken) {
                        return Mono.error(new SeatAlreadyTakenException(seatNumber.value()));
                    }
                    return Mono.empty();
                });
    }
}


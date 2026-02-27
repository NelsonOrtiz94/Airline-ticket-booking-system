package org.example.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.port.in.GetUserReservationsUseCase;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.domain.model.Reservation;
import org.example.domain.valueobject.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserReservationsUseCaseImpl implements GetUserReservationsUseCase {

    private final ReservationRepositoryPort reservationRepository;

    @Override
    public Flux<Reservation> execute(UserId userId) {
        log.info("Obteniendo reservaciones del usuario ID: {}", userId.value());

        return reservationRepository.findByUserId(userId)
                .doOnComplete(() ->
                    log.info("Reservaciones obtenidas exitosamente para usuario: {}", userId.value())
                )
                .doOnError(error ->
                    log.error("Error al obtener reservaciones: {}", error.getMessage())
                );
    }
}


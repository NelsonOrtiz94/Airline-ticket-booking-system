package org.example.application.port.in;

import org.example.domain.model.Reservation;
import org.example.domain.valueobject.UserId;
import reactor.core.publisher.Flux;

/**
 * Puerto de entrada para obtener reservaciones de un usuario
 */
public interface GetUserReservationsUseCase {
    Flux<Reservation> execute(UserId userId);
}


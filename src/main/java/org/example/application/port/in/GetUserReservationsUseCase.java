package org.example.application.port.in;

import org.example.domain.model.Reservation;
import org.example.domain.valueobject.UserId;
import reactor.core.publisher.Flux;


public interface GetUserReservationsUseCase {
    Flux<Reservation> execute(UserId userId);
}


package org.example.application.port.out;

import org.example.domain.model.User;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserRepositoryPort {
    Mono<User> findById(UserId userId);
    Mono<User> findByUsername(Username username);
    Mono<User> save(User user);
    Mono<Void> deleteById(UserId userId);
    Flux<User> findAll();
}


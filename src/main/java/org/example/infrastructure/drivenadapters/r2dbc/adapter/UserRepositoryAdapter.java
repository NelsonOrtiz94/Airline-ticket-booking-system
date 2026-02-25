package org.example.infrastructure.drivenadapters.r2dbc.adapter;

import lombok.RequiredArgsConstructor;
import org.example.application.port.out.UserRepositoryPort;
import org.example.domain.model.User;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;
import org.example.infrastructure.drivenadapters.r2dbc.mapper.UserPersistenceMapper;
import org.example.infrastructure.drivenadapters.r2dbc.repository.UserR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador que implementa UserRepositoryPort usando R2DBC
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserR2dbcRepository r2dbcRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public Mono<User> findById(UserId userId) {
        return r2dbcRepository.findById(userId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByUsername(Username username) {
        return r2dbcRepository.findByUsername(username.value())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<User> save(User user) {
        return r2dbcRepository.save(mapper.toEntity(user))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UserId userId) {
        return r2dbcRepository.deleteById(userId.value());
    }

    @Override
    public Flux<User> findAll() {
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }
}


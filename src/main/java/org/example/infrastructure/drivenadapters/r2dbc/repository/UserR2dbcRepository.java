package org.example.infrastructure.drivenadapters.r2dbc.repository;

import org.example.infrastructure.drivenadapters.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para UserEntity
 */
@Repository
public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, Long> {

    @Query("SELECT * FROM users WHERE username = :username")
    Mono<UserEntity> findByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email")
    Mono<UserEntity> findByEmail(String email);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    Mono<Boolean> existsByUsername(String username);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    Mono<Boolean> existsByEmail(String email);
}

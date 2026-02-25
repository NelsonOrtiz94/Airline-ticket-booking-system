package org.example.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.command.AuthenticateUserCommand;
import org.example.application.port.in.AuthenticateUserUseCase;
import org.example.application.port.out.UserRepositoryPort;
import org.example.domain.exception.AuthenticationException;
import org.example.domain.model.User;
import org.example.domain.valueobject.Username;
import org.example.infrastructure.config.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementación del caso de uso de autenticación
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<AuthenticationResult> execute(AuthenticateUserCommand command) {
        log.info("Intento de login para usuario: {}", command.username());

        return userRepository.findByUsername(new Username(command.username()))
                .switchIfEmpty(Mono.error(
                    new AuthenticationException("Credenciales inválidas")
                ))
                .filter(user -> validatePassword(user, command.password()))
                .switchIfEmpty(Mono.error(
                    new AuthenticationException("Credenciales inválidas")
                ))
                .map(user -> {
                    String token = jwtService.generateToken(user.getUsername().value());
                    log.info("Login exitoso para usuario: {}", user.getUsername().value());
                    return new AuthenticationResult(
                        token,
                        user.getUsername().value(),
                        user.getRole().name()
                    );
                });
    }

    private boolean validatePassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }
}


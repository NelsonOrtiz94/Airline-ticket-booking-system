package org.example.application.port.in;

import org.example.application.command.AuthenticateUserCommand;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada para autenticación de usuarios
 */
public interface AuthenticateUserUseCase {
    Mono<AuthenticationResult> execute(AuthenticateUserCommand command);

    /**
     * Resultado de la autenticación
     */
    record AuthenticationResult(String token, String username, String role) {}
}


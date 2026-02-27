package org.example.application.port.in;

import org.example.application.command.AuthenticateUserCommand;
import reactor.core.publisher.Mono;


public interface AuthenticateUserUseCase {
    Mono<AuthenticationResult> execute(AuthenticateUserCommand command);


    record AuthenticationResult(String token, String username, String role) {}
}


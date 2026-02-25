package org.example.application.command;

/**
 * Comando para autenticar un usuario
 */
public record AuthenticateUserCommand(
    String username,
    String password
) {
    public AuthenticateUserCommand {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
}


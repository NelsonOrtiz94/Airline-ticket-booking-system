package org.example.application.command;


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


package org.example.domain.exception;

/**
 * Excepción de dominio cuando un usuario no es encontrado
 */
public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }

    public UserNotFoundException(String username, boolean byUsername) {
        super("User not found with username: " + username);
    }
}


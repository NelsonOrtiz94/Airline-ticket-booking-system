package org.example.domain.exception;

/**
 * Excepción de dominio para errores de autenticación
 */
public class AuthenticationException extends DomainException {
    public AuthenticationException(String message) {
        super(message);
    }
}


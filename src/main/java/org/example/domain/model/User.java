package org.example.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.example.domain.valueobject.*;
import org.example.domain.model.enums.UserRole;
import org.example.domain.valueobject.Email;
import org.example.domain.valueobject.FullName;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;

import java.time.LocalDateTime;

/**
 * Modelo de dominio puro de User (sin dependencias de frameworks)
 */
@Getter
@Builder
public class User {
    private UserId id;
    private Username username;
    private String passwordHash;
    private FullName fullName;
    private Email email;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Verifica si el usuario tiene un rol específico
     */
    public boolean hasRole(UserRole requiredRole) {
        return this.role == requiredRole;
    }

    /**
     * Verifica si el usuario es administrador
     */
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    /**
     * Obtiene el nombre completo formateado
     */
    public String getFullNameString() {
        return fullName.fullName();
    }
}


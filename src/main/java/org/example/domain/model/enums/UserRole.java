package org.example.domain.model.enums;

/**
 * Roles de usuario en el sistema.
 * Ubicado en la capa de dominio porque representa reglas de negocio core.
 */
public enum UserRole {
    ADMIN("ADMIN", "Administrador"),
    USER("USER", "Usuario"),
    AGENT("AGENT", "Agente de ventas");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol de usuario inválido: " + code);
    }
}


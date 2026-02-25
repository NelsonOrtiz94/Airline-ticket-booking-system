package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.AuthenticateUserCommand;
import org.example.application.port.in.AuthenticateUserUseCase.AuthenticationResult;
import org.example.domain.model.User;
import org.example.domain.model.enums.UserRole;
import org.example.domain.valueobject.Email;
import org.example.domain.valueobject.FullName;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;
import org.example.infrastructure.entrypoints.rest.dto.request.LoginRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.LoginResponseDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para UserRestMapper - 100% Coverage
 */
class UserRestMapperTest {

    private UserRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserRestMapper();
    }

    @Test
    @DisplayName("Debe convertir LoginRequestDTO a AuthenticateUserCommand")
    void testToCommand_ShouldMapAllFields() {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .username("admin")
                .password("password123")
                .build();

        AuthenticateUserCommand command = mapper.toCommand(dto);

        assertNotNull(command);
        assertEquals("admin", command.username());
        assertEquals("password123", command.password());
    }

    @Test
    @DisplayName("Debe convertir AuthenticationResult a LoginResponseDTO")
    void testToLoginResponse_ShouldMapAllFields() {
        AuthenticationResult result = new AuthenticationResult("jwt-token", "admin", "ADMIN");

        LoginResponseDTO response = mapper.toLoginResponse(result);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("admin", response.getUsername());
        assertEquals("ADMIN", response.getRole());
    }

    @Test
    @DisplayName("Debe convertir User a UserResponseDTO")
    void testToResponse_ShouldMapAllFields() {
        User user = User.builder()
                .id(new UserId(1L))
                .username(new Username("testuser"))
                .passwordHash("hashedPassword")
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john.doe@example.com"))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        UserResponseDTO response = mapper.toResponse(user);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    @DisplayName("Debe retornar null cuando User es null")
    void testToResponse_WhenUserIsNull_ShouldReturnNull() {
        UserResponseDTO response = mapper.toResponse(null);

        assertNull(response);
    }

    @Test
    @DisplayName("Debe manejar usuario con rol ADMIN")
    void testToResponse_WithAdminRole_ShouldMapCorrectly() {
        User admin = User.builder()
                .id(new UserId(1L))
                .username(new Username("admin"))
                .passwordHash("hashedPassword")
                .fullName(new FullName("Admin", "User"))
                .email(new Email("admin@example.com"))
                .role(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        UserResponseDTO response = mapper.toResponse(admin);

        assertEquals("ADMIN", response.getRole());
    }
}


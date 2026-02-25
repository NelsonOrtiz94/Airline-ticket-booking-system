package org.example.application.usecase;

import org.example.application.command.AuthenticateUserCommand;
import org.example.application.port.in.AuthenticateUserUseCase.AuthenticationResult;
import org.example.application.port.out.UserRepositoryPort;
import org.example.domain.exception.AuthenticationException;
import org.example.domain.model.User;
import org.example.domain.model.enums.UserRole;
import org.example.domain.valueobject.Email;
import org.example.domain.valueobject.FullName;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;
import org.example.infrastructure.config.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AuthenticateUserUseCaseImpl - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticateUserUseCaseImpl authenticateUserUseCase;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .id(new UserId(1L))
                .username(new Username("admin"))
                .passwordHash("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .fullName(new FullName("Admin", "System"))
                .email(new Email("admin@airline.com"))
                .role(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        regularUser = User.builder()
                .id(new UserId(2L))
                .username(new Username("user"))
                .passwordHash("hashedPassword")
                .fullName(new FullName("John", "Doe"))
                .email(new Email("user@airline.com"))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe retornar token cuando las credenciales son válidas")
    void testExecute_WhenCredentialsValid_ShouldReturnToken() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("admin", "password");

        when(userRepository.findByUsername(any(Username.class))).thenReturn(Mono.just(adminUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("jwt-token-123");

        StepVerifier.create(authenticateUserUseCase.execute(command))
                .expectNextMatches(result ->
                        result != null &&
                                "jwt-token-123".equals(result.token()) &&
                                "admin".equals(result.username()) &&
                                "ADMIN".equals(result.role())
                )
                .verifyComplete();

        verify(jwtService, times(1)).generateToken("admin");
    }

    @Test
    @DisplayName("Debe lanzar AuthenticationException cuando el usuario no existe")
    void testExecute_WhenUserNotFound_ShouldThrowException() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("invalid", "password");

        when(userRepository.findByUsername(any(Username.class))).thenReturn(Mono.empty());

        StepVerifier.create(authenticateUserUseCase.execute(command))
                .expectError(AuthenticationException.class)
                .verify();

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Debe lanzar AuthenticationException cuando la contraseña es incorrecta")
    void testExecute_WhenPasswordInvalid_ShouldThrowException() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("admin", "wrongpassword");

        when(userRepository.findByUsername(any(Username.class))).thenReturn(Mono.just(adminUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        StepVerifier.create(authenticateUserUseCase.execute(command))
                .expectError(AuthenticationException.class)
                .verify();

        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Debe retornar rol USER para usuario regular")
    void testExecute_WhenRegularUser_ShouldReturnUserRole() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("user", "password");

        when(userRepository.findByUsername(any(Username.class))).thenReturn(Mono.just(regularUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("user-token");

        StepVerifier.create(authenticateUserUseCase.execute(command))
                .expectNextMatches(result ->
                        "USER".equals(result.role()) &&
                                "user".equals(result.username())
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe validar correctamente password con el hash almacenado")
    void testExecute_ShouldValidatePasswordWithStoredHash() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("admin", "password123");

        when(userRepository.findByUsername(any(Username.class))).thenReturn(Mono.just(adminUser));
        when(passwordEncoder.matches("password123", adminUser.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("token");

        StepVerifier.create(authenticateUserUseCase.execute(command))
                .expectNextCount(1)
                .verifyComplete();

        verify(passwordEncoder).matches("password123", adminUser.getPasswordHash());
    }
}

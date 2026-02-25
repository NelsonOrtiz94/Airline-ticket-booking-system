package org.example.infrastructure.entrypoints.rest;

import org.example.application.command.AuthenticateUserCommand;
import org.example.application.port.in.AuthenticateUserUseCase;
import org.example.application.port.in.AuthenticateUserUseCase.AuthenticationResult;
import org.example.infrastructure.config.JwtService;
import org.example.infrastructure.entrypoints.rest.dto.request.LoginRequestDTO;
import org.example.infrastructure.entrypoints.rest.mapper.UserRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AuthController - 100% Coverage
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock
    private UserRestMapper userRestMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private LoginRequestDTO loginRequest;
    private AuthenticateUserCommand command;
    private AuthenticationResult authResult;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequestDTO.builder()
                .username("admin")
                .password("password123")
                .build();

        command = new AuthenticateUserCommand("admin", "password123");
        authResult = new AuthenticationResult("jwt-token-123", "admin", "ADMIN");
    }

    @Test
    @DisplayName("Debe retornar token cuando login es exitoso")
    void testLogin_WhenValidCredentials_ShouldReturnToken() {
        when(userRestMapper.toCommand(any(LoginRequestDTO.class))).thenReturn(command);
        when(authenticateUserUseCase.execute(any(AuthenticateUserCommand.class)))
                .thenReturn(Mono.just(authResult));
        when(userRestMapper.toLoginResponse(any(AuthenticationResult.class)))
                .thenReturn(org.example.infrastructure.entrypoints.rest.dto.response.LoginResponseDTO.builder()
                        .token("jwt-token-123")
                        .username("admin")
                        .role("ADMIN")
                        .build());

        StepVerifier.create(authController.login(loginRequest))
                .expectNextMatches(response ->
                    response.getData() != null &&
                    "jwt-token-123".equals(response.getData().getToken())
                )
                .verifyComplete();

        verify(authenticateUserUseCase, times(1)).execute(any());
    }

    @Test
    @DisplayName("Debe retornar éxito cuando token es válido")
    void testVerifyToken_WhenValidToken_ShouldReturnSuccess() {
        String validToken = "valid-jwt-token";
        String authHeader = "Bearer " + validToken;

        when(jwtService.extractUsername(validToken)).thenReturn("admin");
        when(jwtService.validateToken(validToken, "admin")).thenReturn(true);

        StepVerifier.create(authController.verifyToken(authHeader))
                .expectNextMatches(response ->
                    "admin".equals(response.getData()) &&
                    response.getMessage().contains("válido")
                )
                .verifyComplete();

        verify(jwtService, times(1)).extractUsername(validToken);
        verify(jwtService, times(1)).validateToken(validToken, "admin");
    }

    @Test
    @DisplayName("Debe retornar error cuando token es inválido")
    void testVerifyToken_WhenInvalidToken_ShouldReturnError() {
        String invalidToken = "invalid-jwt-token";
        String authHeader = "Bearer " + invalidToken;

        when(jwtService.extractUsername(invalidToken)).thenReturn("admin");
        when(jwtService.validateToken(invalidToken, "admin")).thenReturn(false);

        StepVerifier.create(authController.verifyToken(authHeader))
                .expectNextMatches(response ->
                    response.getMessage().contains("inválido")
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar error cuando header no tiene Bearer")
    void testVerifyToken_WhenNoBearer_ShouldReturnError() {
        String authHeader = "invalid-header";

        StepVerifier.create(authController.verifyToken(authHeader))
                .expectNextMatches(response ->
                    response.getMessage().contains("inválido")
                )
                .verifyComplete();

        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("Debe retornar error cuando header es null")
    void testVerifyToken_WhenNullHeader_ShouldReturnError() {
        StepVerifier.create(authController.verifyToken(null))
                .expectNextMatches(response ->
                    response.getMessage().contains("inválido")
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar excepción en validación de token")
    void testVerifyToken_WhenException_ShouldReturnError() {
        String token = "bad-token";
        String authHeader = "Bearer " + token;

        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Token parsing error"));

        StepVerifier.create(authController.verifyToken(authHeader))
                .expectNextMatches(response ->
                    response.getMessage().contains("inválido")
                )
                .verifyComplete();
    }
}


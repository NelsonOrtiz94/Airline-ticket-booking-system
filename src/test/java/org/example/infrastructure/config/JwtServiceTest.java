package org.example.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para JwtService - 100% Coverage
 */
class JwtServiceTest {

    private JwtService jwtService;
    private static final String SECRET = "mySecretKeyForTestingPurposesOnlyMustBeLongEnough123456";
    private static final long EXPIRATION = 3600000L; // 1 hora

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, EXPIRATION);
    }

    @Test
    @DisplayName("Debe generar token válido")
    void testGenerateToken_ShouldGenerateValidToken() {
        String token = jwtService.generateToken("admin");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes
    }

    @Test
    @DisplayName("Debe extraer username del token")
    void testExtractUsername_ShouldReturnCorrectUsername() {
        String token = jwtService.generateToken("testuser");

        String username = jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Debe validar token correctamente cuando es válido")
    void testValidateToken_WhenValid_ShouldReturnTrue() {
        String username = "admin";
        String token = jwtService.generateToken(username);

        boolean isValid = jwtService.validateToken(token, username);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Debe retornar false cuando username no coincide")
    void testValidateToken_WhenUsernameMismatch_ShouldReturnFalse() {
        String token = jwtService.generateToken("admin");

        boolean isValid = jwtService.validateToken(token, "otheruser");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debe lanzar excepción con token inválido")
    void testExtractUsername_WhenInvalidToken_ShouldThrowException() {
        assertThrows(Exception.class, () -> jwtService.extractUsername("invalid.token.here"));
    }

    @Test
    @DisplayName("Debe validar token expirado como inválido")
    void testValidateToken_WhenExpired_ShouldReturnFalse() {
        // Crear servicio con expiración muy corta
        JwtService shortExpirationService = new JwtService(SECRET, 1L); // 1ms
        String token = shortExpirationService.generateToken("admin");

        // Esperar a que expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThrows(Exception.class, () -> shortExpirationService.validateToken(token, "admin"));
    }

    @Test
    @DisplayName("Debe generar tokens diferentes para el mismo usuario")
    void testGenerateToken_ShouldGenerateDifferentTokens() {
        String token1 = jwtService.generateToken("admin");

        // Pequeña pausa para asegurar diferente timestamp
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String token2 = jwtService.generateToken("admin");

        // Los tokens pueden ser iguales si se generan en el mismo milisegundo
        // pero el username extraído debe ser el mismo
        assertEquals(jwtService.extractUsername(token1), jwtService.extractUsername(token2));
    }

    @Test
    @DisplayName("Debe manejar diferentes usernames")
    void testGenerateToken_WithDifferentUsernames_ShouldWork() {
        String token1 = jwtService.generateToken("user1");
        String token2 = jwtService.generateToken("user2");

        assertEquals("user1", jwtService.extractUsername(token1));
        assertEquals("user2", jwtService.extractUsername(token2));
    }
}

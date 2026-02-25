package org.example.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para Email value object
 */
class EmailTest {

    @Test
    void testEmail_WhenValid_ShouldCreate() {
        Email email = new Email("user@airline.com");
        assertEquals("user@airline.com", email.value());
    }

    @Test
    void testEmail_WhenNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }

    @Test
    void testEmail_WhenEmpty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
    }

    @Test
    void testEmail_WhenInvalidFormat_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email"));
        assertThrows(IllegalArgumentException.class, () -> new Email("invalid@"));
        assertThrows(IllegalArgumentException.class, () -> new Email("@invalid.com"));
    }

    @Test
    void testEmail_WhenValidWithDifferentDomains_ShouldCreate() {
        assertDoesNotThrow(() -> new Email("user@gmail.com"));
        assertDoesNotThrow(() -> new Email("admin@company.co"));
        assertDoesNotThrow(() -> new Email("test.user+tag@example.org"));
    }
}


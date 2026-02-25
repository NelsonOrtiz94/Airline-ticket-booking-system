package org.example.domain.model;

import org.example.domain.model.enums.UserRole;
import org.example.domain.valueobject.Email;
import org.example.domain.valueobject.FullName;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad User
 */
class UserTest {

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .id(new UserId(1L))
                .username(new Username("admin"))
                .passwordHash("hashedPassword")
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
    void testHasRole_WhenUserHasRole_ShouldReturnTrue() {
        assertTrue(adminUser.hasRole(UserRole.ADMIN));
        assertTrue(regularUser.hasRole(UserRole.USER));
    }

    @Test
    void testHasRole_WhenUserDoesNotHaveRole_ShouldReturnFalse() {
        assertFalse(adminUser.hasRole(UserRole.USER));
        assertFalse(regularUser.hasRole(UserRole.ADMIN));
    }

    @Test
    void testIsAdmin_WhenUserIsAdmin_ShouldReturnTrue() {
        assertTrue(adminUser.isAdmin());
    }

    @Test
    void testIsAdmin_WhenUserIsNotAdmin_ShouldReturnFalse() {
        assertFalse(regularUser.isAdmin());
    }

    @Test
    void testGetFullNameString_ShouldReturnFormattedName() {
        String fullName = adminUser.getFullNameString();
        assertNotNull(fullName);
        assertEquals("Admin System", fullName);
    }

    @Test
    void testAllGetters_ShouldReturnCorrectValues() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updated = now.plusHours(1);

        User completeUser = User.builder()
                .id(new UserId(99L))
                .username(new Username("testuser"))
                .passwordHash("hashedPwd123")
                .fullName(new FullName("Test", "User"))
                .email(new Email("test@example.com"))
                .role(UserRole.USER)
                .createdAt(now)
                .updatedAt(updated)
                .build();

        assertEquals(new UserId(99L), completeUser.getId());
        assertEquals(new Username("testuser"), completeUser.getUsername());
        assertEquals("hashedPwd123", completeUser.getPasswordHash());
        assertEquals(new FullName("Test", "User"), completeUser.getFullName());
        assertEquals(new Email("test@example.com"), completeUser.getEmail());
        assertEquals(UserRole.USER, completeUser.getRole());
        assertEquals(now, completeUser.getCreatedAt());
        assertEquals(updated, completeUser.getUpdatedAt());
    }

    @Test
    void testGetUpdatedAt_WhenNotSet_ShouldReturnNull() {
        assertNull(adminUser.getUpdatedAt());
    }

    @Test
    void testGetId_ShouldReturnCorrectId() {
        assertEquals(new UserId(1L), adminUser.getId());
        assertEquals(new UserId(2L), regularUser.getId());
    }

    @Test
    void testGetUsername_ShouldReturnCorrectUsername() {
        assertEquals(new Username("admin"), adminUser.getUsername());
        assertEquals(new Username("user"), regularUser.getUsername());
    }

    @Test
    void testGetPasswordHash_ShouldReturnCorrectHash() {
        assertEquals("hashedPassword", adminUser.getPasswordHash());
    }

    @Test
    void testGetEmail_ShouldReturnCorrectEmail() {
        assertEquals(new Email("admin@airline.com"), adminUser.getEmail());
        assertEquals(new Email("user@airline.com"), regularUser.getEmail());
    }

    @Test
    void testGetRole_ShouldReturnCorrectRole() {
        assertEquals(UserRole.ADMIN, adminUser.getRole());
        assertEquals(UserRole.USER, regularUser.getRole());
    }

    @Test
    void testGetCreatedAt_ShouldNotBeNull() {
        assertNotNull(adminUser.getCreatedAt());
        assertNotNull(regularUser.getCreatedAt());
    }

    @Test
    void testGetFullName_ShouldReturnCorrectFullName() {
        assertEquals(new FullName("Admin", "System"), adminUser.getFullName());
        assertEquals(new FullName("John", "Doe"), regularUser.getFullName());
    }
}


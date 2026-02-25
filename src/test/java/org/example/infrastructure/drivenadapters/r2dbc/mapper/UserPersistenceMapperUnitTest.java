package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.User;
import org.example.domain.model.enums.UserRole;
import org.example.domain.valueobject.Email;
import org.example.domain.valueobject.FullName;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;
import org.example.infrastructure.drivenadapters.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperUnitTest {

    private final UserPersistenceMapper mapper = new UserPersistenceMapper();

    @Test
    void toDomain_and_toEntity_should_map_correctly() {
        LocalDateTime now = LocalDateTime.now();

        UserEntity entity = UserEntity.builder()
                .userId(10L)
                .username("jdoe")
                .password("hashedPassword")
                .firstName("Juan")
                .lastName("Perez")
                .email("jdoe@example.com")
                .role(UserRole.ADMIN.name())
                .createdAt(now)
                .updatedAt(now)
                .build();

        User domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals(10L, domain.getId().value());
        assertEquals("jdoe", domain.getUsername().value());
        assertEquals("hashedPassword", domain.getPasswordHash());
        assertEquals("Juan", domain.getFullName().firstName());
        assertEquals("Perez", domain.getFullName().lastName());
        assertEquals("jdoe@example.com", domain.getEmail().value());
        assertEquals(UserRole.ADMIN, domain.getRole());
        assertEquals(now, domain.getCreatedAt());
        assertEquals(now, domain.getUpdatedAt());

        UserEntity mappedBack = mapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals(entity.getUserId(), mappedBack.getUserId());
        assertEquals(entity.getUsername(), mappedBack.getUsername());
        assertEquals(entity.getPassword(), mappedBack.getPassword());
        assertEquals(entity.getFirstName(), mappedBack.getFirstName());
        assertEquals(entity.getLastName(), mappedBack.getLastName());
        assertEquals(entity.getEmail(), mappedBack.getEmail());
        assertEquals(entity.getRole(), mappedBack.getRole());
        assertEquals(entity.getCreatedAt(), mappedBack.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), mappedBack.getUpdatedAt());
    }
}


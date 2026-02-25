package org.example.infrastructure.drivenadapters.r2dbc.mapper;

import org.example.domain.model.User;
import org.example.domain.valueobject.Email;
import org.example.domain.valueobject.FullName;
import org.example.domain.valueobject.UserId;
import org.example.domain.valueobject.Username;
import org.example.infrastructure.drivenadapters.r2dbc.entity.UserEntity;
import org.example.domain.model.enums.UserRole;
import org.springframework.stereotype.Component;

/**
 * Mapper entre User (dominio) y UserEntity (persistencia)
 */
@Component
public class UserPersistenceMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(new UserId(entity.getUserId()))
                .username(new Username(entity.getUsername()))
                .passwordHash(entity.getPassword())
                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                .email(new Email(entity.getEmail()))
                .role(UserRole.valueOf(entity.getRole()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        return UserEntity.builder()
                .userId(domain.getId() != null ? domain.getId().value() : null)
                .username(domain.getUsername().value())
                .password(domain.getPasswordHash())
                .firstName(domain.getFullName().firstName())
                .lastName(domain.getFullName().lastName())
                .email(domain.getEmail().value())
                .role(domain.getRole().name())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}


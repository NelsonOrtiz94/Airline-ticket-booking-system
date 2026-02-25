package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.AuthenticateUserCommand;
import org.example.application.port.in.AuthenticateUserUseCase.AuthenticationResult;
import org.example.domain.model.User;
import org.example.infrastructure.entrypoints.rest.dto.request.LoginRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.LoginResponseDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.UserResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DTOs REST y comandos/modelos de dominio de User
 */
@Component
public class UserRestMapper {

    /**
     * Convierte LoginRequestDTO a AuthenticateUserCommand
     */
    public AuthenticateUserCommand toCommand(LoginRequestDTO dto) {
        return new AuthenticateUserCommand(
            dto.getUsername(),
            dto.getPassword()
        );
    }

    /**
     * Convierte AuthenticationResult a LoginResponseDTO
     */
    public LoginResponseDTO toLoginResponse(AuthenticationResult result) {
        return LoginResponseDTO.builder()
                .token(result.token())
                .username(result.username())
                .role(result.role())
                .build();
    }

    /**
     * Convierte User a UserResponseDTO
     */
    public UserResponseDTO toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .userId(user.getId().value())
                .username(user.getUsername().value())
                .firstName(user.getFullName().firstName())
                .lastName(user.getFullName().lastName())
                .email(user.getEmail().value())
                .role(user.getRole().name())
                .build();
    }
}


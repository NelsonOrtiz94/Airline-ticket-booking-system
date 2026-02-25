package org.example.infrastructure.entrypoints.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.port.in.AuthenticateUserUseCase;
import org.example.infrastructure.config.JwtService;
import org.example.infrastructure.entrypoints.rest.dto.request.LoginRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.ApiResponse;
import org.example.infrastructure.entrypoints.rest.dto.response.LoginResponseDTO;
import org.example.infrastructure.entrypoints.rest.mapper.UserRestMapper;
import org.example.shared.constants.MessageConstants;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/airline/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserRestMapper userRestMapper;
    private final JwtService jwtService;

    @PostMapping("/login")
    public Mono<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        return authenticateUserUseCase.execute(userRestMapper.toCommand(request))
                .map(result -> ApiResponse.success(
                    userRestMapper.toLoginResponse(result),
                    MessageConstants.AUTH_SUCCESS
                ));
    }

    @GetMapping("/verify")
    public Mono<ApiResponse<String>> verifyToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String username = jwtService.extractUsername(token);

                if (jwtService.validateToken(token, username)) {
                    return Mono.just(ApiResponse.success(username,
                            "Token válido. Usuario autenticado: " + username));
                }
            } catch (Exception e) {
                log.error("Error verificando token: {}", e.getMessage());
            }
        }

        return Mono.just(ApiResponse.unauthorized(MessageConstants.AUTH_TOKEN_INVALID));
    }
}

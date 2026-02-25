package org.example.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shared.constants.SecurityConstants;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            SecurityConstants.AUTH_PATH,
            SecurityConstants.FLIGHTS_PATH,
            SecurityConstants.ACTUATOR_PATH
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        log.debug("Processing request to: {}", path);

        // Permitir rutas públicas sin token
        if (isPublicPath(path)) {
            log.debug("Public path, skipping JWT validation");
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            String token = authHeader.substring(SecurityConstants.BEARER_PREFIX_LENGTH);

            try {
                String username = jwtService.extractUsername(token);

                if (username != null && jwtService.validateToken(token, username)) {
                    log.debug("Token válido para usuario: {}", username);

                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                } else {
                    log.warn("Token inválido o expirado");
                }
            } catch (Exception e) {
                log.error("Error validando token JWT: {}", e.getMessage());
            }
        } else {
            log.warn("No se encontró header Authorization o no empieza con Bearer");
        }

        // Si no hay token válido, continuar sin autenticación
        return chain.filter(exchange);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}


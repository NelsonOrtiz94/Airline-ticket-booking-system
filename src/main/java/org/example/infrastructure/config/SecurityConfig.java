package org.example.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.example.shared.constants.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges
                        // Rutas públicas - no requieren token
                        .pathMatchers(SecurityConstants.AUTH_PATH).permitAll()
                        .pathMatchers(SecurityConstants.FLIGHTS_PATH).permitAll()
                        .pathMatchers(SecurityConstants.ACTUATOR_PATH).permitAll()

                        // Rutas protegidas - requieren token JWT
                        .pathMatchers(SecurityConstants.RESERVATIONS_PATH).authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyExchange().authenticated()
                )
                // Agregar el filtro JWT ANTES de la autenticación
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


package org.example.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Set;

/**
 * Filtro Web para loguear todas las peticiones HTTP
 * Registra método, URI, headers, tiempo de respuesta y status code
 */
@Slf4j
@Component
@Order(1) // Ejecutar primero en la cadena de filtros
public class RequestLoggingWebFilter implements WebFilter {

    // Paths que no se deben loguear para evitar ruido en logs
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/actuator/health",
            "/actuator/prometheus",
            "/favicon.ico"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Saltar logging para paths excluidos
        if (shouldSkipLogging(path)) {
            return chain.filter(exchange);
        }

        long startTime = System.currentTimeMillis();
        String method = exchange.getRequest().getMethod().name();
        String queryParams = exchange.getRequest().getURI().getQuery();
        String clientIp = getClientIp(exchange);

        // Log de entrada (solo una vez, evitando duplicación con controllers)
        log.info("==> [REQUEST] {} {}{} | IP: {}",
                method,
                path,
                queryParams != null ? "?" + queryParams : "",
                clientIp);

        // Log headers solo en modo DEBUG (comentar en producción si es sensible)
        if (log.isDebugEnabled()) {
            exchange.getRequest().getHeaders().forEach((name, values) ->
                log.debug("    Header: {} = {}", name, values)
            );
        }

        // Continuar con la cadena de filtros y loguear la respuesta
        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    long duration = System.currentTimeMillis() - startTime;
                    Integer statusCode = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : null;

                    log.info("<== [RESPONSE] {} {} | Status: {} | Duration: {}ms",
                            method,
                            path,
                            statusCode != null ? statusCode : "N/A",
                            duration);
                })
                .doOnError(error -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.error("<== [ERROR] {} {} | Duration: {}ms | Error: {}",
                            method,
                            path,
                            duration,
                            error.getMessage(),
                            error);
                });
    }

    /**
     * Determina si un path debe omitirse del logging
     */
    private boolean shouldSkipLogging(String path) {
        return EXCLUDED_PATHS.contains(path) || path.startsWith("/actuator");
    }

    /**
     * Obtiene la IP real del cliente considerando proxies y load balancers
     */
    private String getClientIp(ServerWebExchange exchange) {
        // Intentar obtener IP desde headers de proxy
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        // Fallback a la dirección remota directa
        if (exchange.getRequest().getRemoteAddress() != null &&
            exchange.getRequest().getRemoteAddress().getAddress() != null) {
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }

        return "unknown";
    }
}


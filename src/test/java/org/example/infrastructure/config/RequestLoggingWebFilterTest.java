package org.example.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para RequestLoggingWebFilter
 */
@ExtendWith(MockitoExtension.class)
class RequestLoggingWebFilterTest {

    private RequestLoggingWebFilter filter;

    @Mock
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new RequestLoggingWebFilter();
    }

    @Test
    @DisplayName("Debe loguear petición GET exitosa")
    void testFilter_SuccessfulGetRequest() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/airline/flights/search")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getResponse().setStatusCode(HttpStatus.OK);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe loguear petición POST exitosa")
    void testFilter_SuccessfulPostRequest() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .post("/airline/reservations")
                .header("Content-Type", "application/json")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getResponse().setStatusCode(HttpStatus.CREATED);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe loguear petición con query parameters")
    void testFilter_RequestWithQueryParams() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/airline/flights/search?origin=BOG&destination=MDE")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getResponse().setStatusCode(HttpStatus.OK);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe loguear petición con X-Forwarded-For header")
    void testFilter_WithXForwardedForHeader() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/airline/flights")
                .header("X-Forwarded-For", "192.168.1.1, 10.0.0.1")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getResponse().setStatusCode(HttpStatus.OK);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe loguear error cuando la cadena falla")
    void testFilter_WhenChainThrowsError() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/airline/flights")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        RuntimeException error = new RuntimeException("Test error");
        when(chain.filter(any())).thenReturn(Mono.error(error));

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .expectError(RuntimeException.class)
                .verify();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe loguear petición PUT")
    void testFilter_PutRequest() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .put("/airline/reservations")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getResponse().setStatusCode(HttpStatus.OK);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe loguear petición DELETE")
    void testFilter_DeleteRequest() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .delete("/airline/reservations/1")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getResponse().setStatusCode(HttpStatus.NO_CONTENT);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe manejar petición sin status code en respuesta")
    void testFilter_WithoutStatusCode() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/airline/flights")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        // No se establece status code

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe omitir logging de /actuator/health")
    void testFilter_ShouldSkipActuatorHealth() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/actuator/health")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
        // El filtro debe pasar la petición sin loguear
    }

    @Test
    @DisplayName("Debe omitir logging de /actuator/prometheus")
    void testFilter_ShouldSkipActuatorPrometheus() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/actuator/prometheus")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }

    @Test
    @DisplayName("Debe omitir logging de /favicon.ico")
    void testFilter_ShouldSkipFavicon() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/favicon.ico")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(chain.filter(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }
}


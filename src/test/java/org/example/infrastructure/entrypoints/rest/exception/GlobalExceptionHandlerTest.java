package org.example.infrastructure.entrypoints.rest.exception;

import org.example.domain.exception.*;
import org.example.shared.constants.MessageConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para GlobalExceptionHandler - 100% Coverage
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    // ============= NOT FOUND EXCEPTIONS =============

    @Test
    @DisplayName("Debe manejar FlightNotFoundException")
    void testHandleFlightNotFound() {
        FlightNotFoundException ex = new FlightNotFoundException(1L);

        StepVerifier.create(exceptionHandler.handleFlightNotFound(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.FLIGHT_NOT_FOUND, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar ReservationNotFoundException")
    void testHandleReservationNotFound() {
        ReservationNotFoundException ex = new ReservationNotFoundException(2L);

        StepVerifier.create(exceptionHandler.handleReservationNotFound(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.RESERVATION_NOT_FOUND, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar UserNotFoundException")
    void testHandleUserNotFound() {
        UserNotFoundException ex = new UserNotFoundException(3L);

        StepVerifier.create(exceptionHandler.handleUserNotFound(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.USER_NOT_FOUND, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    // ============= CONFLICT EXCEPTIONS =============

    @Test
    @DisplayName("Debe manejar NoSeatsAvailableException")
    void testHandleNoSeatsAvailable() {
        NoSeatsAvailableException ex = new NoSeatsAvailableException("No seats available");

        StepVerifier.create(exceptionHandler.handleNoSeatsAvailable(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.CONFLICT.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.NO_SEATS_AVAILABLE, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar SeatAlreadyTakenException")
    void testHandleSeatAlreadyTaken() {
        SeatAlreadyTakenException ex = new SeatAlreadyTakenException("12A");

        StepVerifier.create(exceptionHandler.handleSeatAlreadyTaken(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.CONFLICT.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.SEAT_ALREADY_TAKEN, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    // ============= BAD REQUEST EXCEPTIONS =============

    @Test
    @DisplayName("Debe manejar InvalidBookingException")
    void testHandleInvalidBooking() {
        InvalidBookingException ex = new InvalidBookingException("Invalid booking");

        StepVerifier.create(exceptionHandler.handleInvalidBooking(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.INVALID_BOOKING, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar IllegalArgumentException")
    void testHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        StepVerifier.create(exceptionHandler.handleIllegalArgument(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus().getCode());
                    assertTrue(response.getMessage().contains("Invalid argument"));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar WebExchangeBindException (errores de validación)")
    void testHandleValidationErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "error message");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(ex.getMessage()).thenReturn("Validation failed");

        StepVerifier.create(exceptionHandler.handleValidationErrors(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.VALIDATION_ERROR, response.getMessage());
                    assertNotNull(response.getData());
                    Map<String, String> errors = response.getData();
                    assertEquals("error message", errors.get("field"));
                    return true;
                })
                .verifyComplete();
    }

    // ============= UNAUTHORIZED EXCEPTIONS =============

    @Test
    @DisplayName("Debe manejar AuthenticationException")
    void testHandleAuthentication() {
        AuthenticationException ex = new AuthenticationException("Invalid credentials");

        StepVerifier.create(exceptionHandler.handleAuthentication(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.AUTH_FAILED, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    // ============= GENERIC EXCEPTIONS =============

    @Test
    @DisplayName("Debe manejar Exception genérica")
    void testHandleGenericException() {
        Exception ex = new RuntimeException("Unexpected error");

        StepVerifier.create(exceptionHandler.handleGenericException(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus().getCode());
                    assertEquals(MessageConstants.INTERNAL_SERVER_ERROR, response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar DomainException genérica")
    void testHandleDomainException() {
        DomainException ex = new DomainException("Domain error");

        StepVerifier.create(exceptionHandler.handleDomainException(ex))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus().getCode());
                    assertEquals("Domain error", response.getMessage());
                    return true;
                })
                .verifyComplete();
    }
}


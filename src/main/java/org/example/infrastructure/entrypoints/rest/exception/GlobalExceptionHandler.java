package org.example.infrastructure.entrypoints.rest.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.exception.*;
import org.example.infrastructure.entrypoints.rest.dto.response.ApiResponse;
import org.example.shared.constants.MessageConstants;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 * Traduce excepciones de dominio a respuestas HTTP apropiadas con mensajes
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============= EXCEPCIONES DE DOMINIO (404 - NOT FOUND) =============

    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiResponse<Void>> handleFlightNotFound(FlightNotFoundException ex) {
        log.error("Vuelo no encontrado: {}", ex.getMessage());
        return Mono.just(ApiResponse.notFound(MessageConstants.FLIGHT_NOT_FOUND));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiResponse<Void>> handleReservationNotFound(ReservationNotFoundException ex) {
        log.error("Reservación no encontrada: {}", ex.getMessage());
        return Mono.just(ApiResponse.notFound(MessageConstants.RESERVATION_NOT_FOUND));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        log.error("Usuario no encontrado: {}", ex.getMessage());
        return Mono.just(ApiResponse.notFound(MessageConstants.USER_NOT_FOUND));
    }

    // ============= EXCEPCIONES DE NEGOCIO (409 - CONFLICT) =============

    @ExceptionHandler(NoSeatsAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ApiResponse<Void>> handleNoSeatsAvailable(NoSeatsAvailableException ex) {
        log.warn("No hay asientos disponibles: {}", ex.getMessage());
        return Mono.just(ApiResponse.conflict(MessageConstants.NO_SEATS_AVAILABLE));
    }

    @ExceptionHandler(SeatAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ApiResponse<Void>> handleSeatAlreadyTaken(SeatAlreadyTakenException ex) {
        log.warn("Asiento ya ocupado: {}", ex.getMessage());
        return Mono.just(ApiResponse.conflict(MessageConstants.SEAT_ALREADY_TAKEN));
    }

    // ============= EXCEPCIONES DE VALIDACIÓN (400 - BAD REQUEST) =============

    @ExceptionHandler(InvalidBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<Void>> handleInvalidBooking(InvalidBookingException ex) {
        log.warn("Reserva inválida: {}", ex.getMessage());
        return Mono.just(ApiResponse.error(MessageConstants.INVALID_BOOKING));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        return Mono.just(ApiResponse.error(MessageConstants.INVALID_ARGUMENT + ": " + ex.getMessage()));
    }

    /**
     * Maneja errores de validación de @Valid
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<Map<String, String>>> handleValidationErrors(WebExchangeBindException ex) {
        log.warn("Errores de validación: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return Mono.just(ApiResponse.validationError(errors, MessageConstants.VALIDATION_ERROR));
    }

    // ============= EXCEPCIONES DE AUTENTICACIÓN (401 - UNAUTHORIZED) =============

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<ApiResponse<Void>> handleAuthentication(AuthenticationException ex) {
        log.warn("Error de autenticación: {}", ex.getMessage());
        return Mono.just(ApiResponse.unauthorized(MessageConstants.AUTH_FAILED));
    }

    // ============= EXCEPCIONES GENÉRICAS (500 - INTERNAL SERVER ERROR) =============

    /**
     * Maneja cualquier excepción no capturada específicamente
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);
        return Mono.just(ApiResponse.internalError(MessageConstants.INTERNAL_SERVER_ERROR));
    }

    /**
     * Maneja excepciones de dominio genéricas
     */
    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiResponse<Void>> handleDomainException(DomainException ex) {
        log.warn("Excepción de dominio: {}", ex.getMessage());
        return Mono.just(ApiResponse.error(ex.getMessage()));
    }
}

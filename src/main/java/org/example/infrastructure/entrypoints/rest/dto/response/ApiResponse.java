package org.example.infrastructure.entrypoints.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Respuesta estándar de la API
 * Formato: { data, status, message }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"data", "status", "message"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private T data;
    private StatusInfo status;
    private String message;

    /**
     * Información del estado HTTP
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusInfo {
        private int code;
        private String description;
    }

    // ============= MÉTODOS DE ÉXITO =============

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(StatusInfo.builder()
                        .code(HttpStatus.OK.value())
                        .description(HttpStatus.OK.getReasonPhrase())
                        .build())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .status(StatusInfo.builder()
                        .code(HttpStatus.CREATED.value())
                        .description(HttpStatus.CREATED.getReasonPhrase())
                        .build())
                .message(message)
                .build();
    }

    // ============= MÉTODOS DE ERROR =============

    public static <T> ApiResponse<T> error(String message, HttpStatus httpStatus) {
        return ApiResponse.<T>builder()
                .data(null)
                .status(StatusInfo.builder()
                        .code(httpStatus.value())
                        .description(httpStatus.getReasonPhrase())
                        .build())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return error(message, HttpStatus.CONFLICT);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResponse<T> validationError(T errors, String message) {
        return ApiResponse.<T>builder()
                .data(errors)
                .status(StatusInfo.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build())
                .message(message)
                .build();
    }
}


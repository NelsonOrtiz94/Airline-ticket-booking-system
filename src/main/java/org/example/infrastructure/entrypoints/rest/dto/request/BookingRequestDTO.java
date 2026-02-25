package org.example.infrastructure.entrypoints.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotBlank(message = "Passenger name is required")
    @Size(max = 30, message = "Passenger name must not exceed 30 characters")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Passenger name must contain only letters")
    private String passengerName;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @Builder.Default
    private String ticketClass = "ECONOMY";

    @Size(max = 100, message = "Observations must not exceed 100 characters")
    private String observations;
}


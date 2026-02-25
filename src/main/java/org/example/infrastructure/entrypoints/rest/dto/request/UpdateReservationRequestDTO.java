package org.example.infrastructure.entrypoints.rest.dto.request;

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
public class UpdateReservationRequestDTO {

    @NotNull(message = "Reservation ID is required")
    private Long reservationId;

    @Pattern(regexp = "^\\d{1,2}[A-F]$", message = "Seat number must be in format: 1-2 digits followed by letter A-F (e.g., 12A)")
    private String seatNumber;

    @Size(max = 100, message = "Observations must not exceed 100 characters")
    private String observations;
}


package org.example.infrastructure.entrypoints.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private Long reservationId;
    private Long userId;
    private Long flightId;
    private Long ticketId;
    private String status;
    private String observations;
    private LocalDateTime reservationDate;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private String passengerName;
    private String seatNumber;
    private BigDecimal price;
}


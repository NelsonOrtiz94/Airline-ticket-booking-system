package org.example.infrastructure.drivenadapters.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de persistencia para Flight (R2DBC)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("flights")
public class FlightEntity {

    @Id
    private Long flightId;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private Integer totalSeats;
    private BigDecimal price;
    private String airline;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


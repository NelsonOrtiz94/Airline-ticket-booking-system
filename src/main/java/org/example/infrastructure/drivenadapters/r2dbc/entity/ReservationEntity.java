package org.example.infrastructure.drivenadapters.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de persistencia para Reservation (R2DBC)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("reservations")
public class ReservationEntity {

    @Id
    private Long reservationId;
    private Long userId;
    private Long ticketId;
    private Long flightId;
    private String status;
    private String observations;
    private LocalDateTime reservationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


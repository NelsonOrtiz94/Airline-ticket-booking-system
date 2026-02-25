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
 * Entidad de persistencia para Ticket (R2DBC)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tickets")
public class TicketEntity {

    @Id
    private Long ticketId;
    private Long flightId;
    private Long userId;
    private String passengerName;
    private String seatNumber;
    private BigDecimal price;
    private String ticketClass;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


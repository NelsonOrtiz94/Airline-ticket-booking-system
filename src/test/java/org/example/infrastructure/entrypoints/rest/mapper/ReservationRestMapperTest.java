package org.example.infrastructure.entrypoints.rest.mapper;

import org.example.application.command.CancelReservationCommand;
import org.example.application.command.UpdateReservationCommand;
import org.example.infrastructure.entrypoints.rest.dto.request.UpdateReservationRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ReservationRestMapper - 100% Coverage
 */
class ReservationRestMapperTest {

    private ReservationRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReservationRestMapper();
    }

    @Test
    @DisplayName("Debe convertir UpdateReservationRequestDTO a UpdateReservationCommand")
    void testToUpdateCommand_ShouldMapAllFields() {
        UpdateReservationRequestDTO dto = UpdateReservationRequestDTO.builder()
                .reservationId(1L)
                .observations("Updated observations")
                .build();

        UpdateReservationCommand command = mapper.toUpdateCommand(dto);

        assertNotNull(command);
        assertEquals(1L, command.reservationId());
        assertEquals("Updated observations", command.observations());
    }

    @Test
    @DisplayName("Debe manejar observations null")
    void testToUpdateCommand_WithNullObservations_ShouldWork() {
        UpdateReservationRequestDTO dto = UpdateReservationRequestDTO.builder()
                .reservationId(1L)
                .observations(null)
                .build();

        UpdateReservationCommand command = mapper.toUpdateCommand(dto);

        assertNull(command.observations());
    }

    @Test
    @DisplayName("Debe crear CancelReservationCommand con razón")
    void testToCancelCommand_WithReason_ShouldMapCorrectly() {
        CancelReservationCommand command = mapper.toCancelCommand(1L, "User requested cancellation");

        assertNotNull(command);
        assertEquals(1L, command.reservationId());
        assertEquals("User requested cancellation", command.reason());
    }

    @Test
    @DisplayName("Debe crear CancelReservationCommand sin razón")
    void testToCancelCommand_WithNullReason_ShouldWork() {
        CancelReservationCommand command = mapper.toCancelCommand(2L, null);

        assertNotNull(command);
        assertEquals(2L, command.reservationId());
        assertNull(command.reason());
    }
}


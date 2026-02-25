package org.example.infrastructure.entrypoints.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.port.in.BookTicketUseCase;
import org.example.application.port.in.CancelReservationUseCase;
import org.example.application.port.in.GetUserReservationsUseCase;
import org.example.application.port.in.UpdateReservationUseCase;
import org.example.domain.valueobject.UserId;
import org.example.infrastructure.entrypoints.rest.dto.request.BookingRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.request.UpdateReservationRequestDTO;
import org.example.infrastructure.entrypoints.rest.dto.response.ApiResponse;
import org.example.infrastructure.entrypoints.rest.dto.response.ReservationResponseDTO;
import org.example.infrastructure.entrypoints.rest.mapper.ReservationResponseMapper;
import org.example.infrastructure.entrypoints.rest.mapper.ReservationRestMapper;
import org.example.infrastructure.entrypoints.rest.mapper.TicketRestMapper;
import org.example.shared.constants.MessageConstants;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/airline/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final BookTicketUseCase bookTicketUseCase;
    private final UpdateReservationUseCase updateReservationUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final GetUserReservationsUseCase getUserReservationsUseCase;
    private final TicketRestMapper ticketRestMapper;
    private final ReservationRestMapper reservationRestMapper;
    private final ReservationResponseMapper reservationResponseMapper;

    @PostMapping
    public Mono<ApiResponse<ReservationResponseDTO>> createReservation(
            @Valid @RequestBody BookingRequestDTO request) {

        log.info("Recibida solicitud de reserva para vuelo ID: {}", request.getFlightId());

        return bookTicketUseCase.execute(ticketRestMapper.toCommand(request))
                .flatMap(reservationResponseMapper::toResponseWithDetails)
                .map(response -> ApiResponse.created(response,
                        MessageConstants.RESERVATION_CREATED));
    }

    @PutMapping
    public Mono<ApiResponse<ReservationResponseDTO>> updateReservation(
            @Valid @RequestBody UpdateReservationRequestDTO request) {

        log.info("Recibida solicitud de actualización de reserva ID: {}",
                request.getReservationId());

        return updateReservationUseCase.execute(reservationRestMapper.toUpdateCommand(request))
                .flatMap(reservationResponseMapper::toResponseWithDetails)
                .map(response -> ApiResponse.success(response,
                        MessageConstants.RESERVATION_UPDATED));
    }

    @DeleteMapping("/{reservationId}")
    public Mono<ApiResponse<Void>> cancelReservation(
            @PathVariable Long reservationId,
            @RequestParam(required = false) String reason) {

        log.info("Recibida solicitud de cancelación de reserva ID: {}", reservationId);

        return cancelReservationUseCase.execute(
                reservationRestMapper.toCancelCommand(reservationId, reason))
                .then(Mono.just(ApiResponse.success(null, MessageConstants.RESERVATION_CANCELLED)));
    }

    @GetMapping("/user/{userId}")
    public Mono<ApiResponse<List<ReservationResponseDTO>>> getUserReservations(
            @PathVariable Long userId) {

        log.info("Recibida solicitud de reservas para usuario ID: {}", userId);

        return getUserReservationsUseCase.execute(new UserId(userId))
                .flatMap(reservationResponseMapper::toResponseWithDetails)
                .collectList()
                .map(reservations -> {
                    if (reservations.isEmpty()) {
                        return ApiResponse.success(reservations, MessageConstants.NO_RESERVATIONS_FOUND);
                    } else {
                        return ApiResponse.success(reservations,
                                String.format(MessageConstants.RESERVATIONS_FOUND, reservations.size()));
                    }
                })
                .defaultIfEmpty(ApiResponse.success(List.of(),
                        MessageConstants.NO_RESERVATIONS_FOUND));
    }
}

package org.example.infrastructure.config;

import org.example.domain.service.FlightAvailabilityService;
import org.example.domain.service.PriceCalculationService;
import org.example.domain.service.ReservationDomainService;
import org.example.application.port.in.*;
import org.example.application.port.out.FlightRepositoryPort;
import org.example.application.port.out.ReservationRepositoryPort;
import org.example.application.port.out.TicketRepositoryPort;
import org.example.application.port.out.UserRepositoryPort;
import org.example.application.usecase.*;
import org.example.infrastructure.config.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de beans para inyección de dependencias
 * Conecta la capa de aplicación con la capa de infraestructura
 */
@Configuration
public class BeanConfiguration {

    // ============ Domain Services ============

    @Bean
    public ReservationDomainService reservationDomainService() {
        return new ReservationDomainService();
    }

    @Bean
    public FlightAvailabilityService flightAvailabilityService() {
        return new FlightAvailabilityService();
    }

    @Bean
    public PriceCalculationService priceCalculationService() {
        return new PriceCalculationService();
    }

    // ============ Use Cases ============

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(
            UserRepositoryPort userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        return new AuthenticateUserUseCaseImpl(userRepository, jwtService, passwordEncoder);
    }

    @Bean
    public BookTicketUseCase bookTicketUseCase(
            FlightRepositoryPort flightRepository,
            TicketRepositoryPort ticketRepository,
            ReservationRepositoryPort reservationRepository,
            ReservationDomainService reservationDomainService,
            PriceCalculationService priceCalculationService
    ) {
        return new BookTicketUseCaseImpl(
                flightRepository,
                ticketRepository,
                reservationRepository,
                reservationDomainService,
                priceCalculationService
        );
    }

    @Bean
    public SearchFlightsUseCase searchFlightsUseCase(
            FlightRepositoryPort flightRepository
    ) {
        return new SearchFlightsUseCaseImpl(flightRepository);
    }

    @Bean
    public UpdateReservationUseCase updateReservationUseCase(
            ReservationRepositoryPort reservationRepository,
            TicketRepositoryPort ticketRepository,
            ReservationDomainService reservationDomainService
    ) {
        return new UpdateReservationUseCaseImpl(reservationRepository, ticketRepository, reservationDomainService);
    }

    @Bean
    public CancelReservationUseCase cancelReservationUseCase(
            ReservationRepositoryPort reservationRepository,
            TicketRepositoryPort ticketRepository,
            FlightRepositoryPort flightRepository,
            ReservationDomainService reservationDomainService
    ) {
        return new CancelReservationUseCaseImpl(
                reservationRepository,
                ticketRepository,
                flightRepository,
                reservationDomainService
        );
    }

    @Bean
    public GetUserReservationsUseCase getUserReservationsUseCase(
            ReservationRepositoryPort reservationRepository
    ) {
        return new GetUserReservationsUseCaseImpl(reservationRepository);
    }
}


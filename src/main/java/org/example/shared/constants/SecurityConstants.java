package org.example.shared.constants;

public class SecurityConstants {

    // JWT Configuration
    public static final String JWT_SECRET = "airlineBookingSecretKeyForJwtTokenGenerationAndValidation2024";
    public static final long JWT_EXPIRATION = 86400000; // 24 hours

    // Headers
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // API Paths
    public static final String AUTH_PATH = "/airline/auth/**";
    public static final String FLIGHTS_PATH = "/airline/flights/search";
    public static final String ACTUATOR_PATH = "/actuator/**";
    public static final String RESERVATIONS_PATH = "/airline/reservations/**";

    private SecurityConstants() {}
}


package org.example.shared.constants;

/**
 * Constantes de mensajes en español para las respuestas de la API
 */
public class MessageConstants {

    // ============= AUTENTICACIÓN =============
    public static final String AUTH_SUCCESS = "Inicio de sesión exitoso. Bienvenido al sistema";
    public static final String AUTH_FAILED = "Las credenciales proporcionadas son incorrectas. Verifique su usuario y contraseña";
    public static final String AUTH_TOKEN_INVALID = "El token de autenticación es inválido o ha expirado";
    public static final String AUTH_TOKEN_REQUIRED = "Se requiere un token de autenticación para acceder a este recurso";

    // ============= RESERVACIONES =============
    public static final String RESERVATION_CREATED = "Su reservación ha sido creada exitosamente";
    public static final String RESERVATION_UPDATED = "Su reservación ha sido actualizada correctamente";
    public static final String RESERVATION_CANCELLED = "Su reservación ha sido cancelada. Los asientos han sido liberados";
    public static final String RESERVATION_NOT_FOUND = "No se encontró la reservación con el ID especificado";
    public static final String RESERVATION_NOT_FOUND_WITH_ID = "No se encontró la reservación con ID: %d";
    public static final String RESERVATIONS_FOUND = "Se encontraron %d reservación(es) para el usuario";
    public static final String NO_RESERVATIONS_FOUND = "El usuario no tiene reservaciones registradas";
    public static final String RESERVATION_ALREADY_CANCELLED = "La reservación ya se encuentra cancelada";
    public static final String RESERVATION_CANNOT_UPDATE = "No es posible actualizar una reservación cancelada";

    // ============= VUELOS =============
    public static final String FLIGHT_NOT_FOUND = "No se encontró el vuelo solicitado";
    public static final String FLIGHT_NOT_FOUND_WITH_ID = "No se encontró el vuelo con ID: %d";
    public static final String FLIGHTS_FOUND = "Se encontraron %d vuelo(s) disponibles para su búsqueda";
    public static final String FLIGHTS_FOUND_SINGULAR = "Se encontró 1 vuelo disponible para su búsqueda";
    public static final String NO_FLIGHTS_FOUND = "No se encontraron vuelos para los criterios de búsqueda especificados";
    public static final String NO_FLIGHTS_AVAILABLE = "No hay vuelos disponibles en este momento";
    public static final String FLIGHT_NOT_BOOKABLE = "El vuelo seleccionado no está disponible para reservas";

    // ============= ASIENTOS =============
    public static final String NO_SEATS_AVAILABLE = "No hay asientos disponibles en el vuelo seleccionado";
    public static final String NO_SEATS_AVAILABLE_WITH_FLIGHT = "No hay asientos disponibles en el vuelo %s";
    public static final String SEAT_ALREADY_TAKEN = "El asiento seleccionado ya está ocupado. Por favor, elija otro asiento";
    public static final String SEAT_ALREADY_TAKEN_WITH_NUMBER = "El asiento %s ya está ocupado. Por favor, elija otro asiento";
    public static final String SEATS_RELEASED = "Los asientos han sido liberados correctamente";

    // ============= RESERVA / BOOKING =============
    public static final String INVALID_BOOKING = "Los datos de la reserva son inválidos. Verifique la información proporcionada";
    public static final String BOOKING_FLIGHT_INACTIVE = "El vuelo seleccionado no está activo para reservas";
    public static final String BOOKING_FLIGHT_DEPARTED = "No es posible reservar en un vuelo que ya ha partido";

    // ============= USUARIOS =============
    public static final String USER_NOT_FOUND = "No se encontró el usuario especificado";
    public static final String USER_NOT_FOUND_WITH_ID = "No se encontró el usuario con ID: %d";
    public static final String USER_NOT_FOUND_WITH_USERNAME = "No se encontró el usuario con nombre de usuario: %s";

    // ============= TICKETS =============
    public static final String TICKET_CREATED = "Su ticket ha sido generado exitosamente";
    public static final String TICKET_CANCELLED = "Su ticket ha sido cancelado";
    public static final String TICKET_NOT_FOUND = "No se encontró el ticket especificado";

    // ============= VALIDACIÓN =============
    public static final String VALIDATION_ERROR = "Error de validación en los datos proporcionados";
    public static final String INVALID_ARGUMENT = "El argumento proporcionado no es válido";
    public static final String REQUIRED_FIELD = "El campo '%s' es obligatorio";

    // ============= ERRORES GENERALES =============
    public static final String INTERNAL_SERVER_ERROR = "Ha ocurrido un error interno en el servidor. Por favor, intente más tarde";
    public static final String OPERATION_SUCCESS = "Operación realizada exitosamente";
    public static final String OPERATION_FAILED = "No se pudo completar la operación solicitada";

    private MessageConstants() {
        // Constructor privado para evitar instanciación
    }
}


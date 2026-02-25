package org.example.infrastructure.entrypoints.rest.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para FlightSearchRequestDTO
 * Valida el formato de fecha dd/MM/yyyy HH:mm:ss
 */
class FlightSearchRequestDTOTest {

    private ObjectMapper objectMapper;
    private DateTimeFormatter formatter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    @Test
    @DisplayName("Debe crear DTO con todos los campos")
    void testCreateDTO_WithAllFields() {
        LocalDateTime departureDate = LocalDateTime.of(2026, 3, 15, 8, 0, 0);

        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("BOG")
                .destination("MDE")
                .departureDate(departureDate)
                .passengers(2)
                .build();

        assertNotNull(dto);
        assertEquals("BOG", dto.getOrigin());
        assertEquals("MDE", dto.getDestination());
        assertEquals(departureDate, dto.getDepartureDate());
        assertEquals(2, dto.getPassengers());
    }

    @Test
    @DisplayName("Debe crear DTO sin fecha (búsqueda sin filtro de fecha)")
    void testCreateDTO_WithoutDate() {
        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("Bogotá")
                .destination("Cartagena")
                .build();

        assertNotNull(dto);
        assertEquals("Bogotá", dto.getOrigin());
        assertEquals("Cartagena", dto.getDestination());
        assertNull(dto.getDepartureDate());
        assertNull(dto.getPassengers());
    }

    @Test
    @DisplayName("Debe serializar fecha en formato correcto dd/MM/yyyy HH:mm:ss")
    void testSerializeDate_CorrectFormat() throws Exception {
        LocalDateTime departureDate = LocalDateTime.of(2026, 3, 15, 8, 30, 45);

        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("BOG")
                .destination("CTG")
                .departureDate(departureDate)
                .build();

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("\"departureDate\":\"15/03/2026 08:30:45\""));
    }

    @Test
    @DisplayName("Debe deserializar fecha desde formato dd/MM/yyyy HH:mm:ss")
    void testDeserializeDate_FromCorrectFormat() throws Exception {
        String json = "{\"origin\":\"BOG\",\"destination\":\"MDE\",\"departureDate\":\"15/03/2026 08:00:00\"}";

        FlightSearchRequestDTO dto = objectMapper.readValue(json, FlightSearchRequestDTO.class);

        assertNotNull(dto);
        assertEquals("BOG", dto.getOrigin());
        assertEquals("MDE", dto.getDestination());
        assertNotNull(dto.getDepartureDate());
        assertEquals(LocalDateTime.of(2026, 3, 15, 8, 0, 0), dto.getDepartureDate());
    }

    @Test
    @DisplayName("Debe manejar nombres con espacios y tildes")
    void testDTO_WithAccentsAndSpaces() {
        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("Bogotá")
                .destination("San Andrés")
                .departureDate(LocalDateTime.of(2026, 3, 22, 13, 0, 0))
                .passengers(3)
                .build();

        assertNotNull(dto);
        assertEquals("Bogotá", dto.getOrigin());
        assertEquals("San Andrés", dto.getDestination());
        assertEquals(3, dto.getPassengers());
    }

    @Test
    @DisplayName("Debe formatear correctamente la fecha para logs")
    void testDateFormatting_ForLogs() {
        LocalDateTime departureDate = LocalDateTime.of(2026, 3, 15, 8, 0, 0);

        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("BOG")
                .destination("MDE")
                .departureDate(departureDate)
                .build();

        String formattedDate = dto.getDepartureDate().format(formatter);

        assertEquals("15/03/2026 08:00:00", formattedDate);
    }

    @Test
    @DisplayName("Debe permitir búsqueda solo con ciudades (fecha null)")
    void testDTO_OnlyCities_NullDate() {
        FlightSearchRequestDTO dto = FlightSearchRequestDTO.builder()
                .origin("MDE")
                .destination("CLO")
                .build();

        assertNotNull(dto);
        assertNull(dto.getDepartureDate());
        // La búsqueda debe retornar todos los vuelos disponibles sin filtrar por fecha
    }
}


package org.example.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para CityCodeNormalizer
 */
class CityCodeNormalizerTest {

    @Test
    @DisplayName("Debe normalizar códigos IATA correctamente")
    void testNormalize_IATACodes() {
        assertEquals("BOG", CityCodeNormalizer.normalize("BOG"));
        assertEquals("MDE", CityCodeNormalizer.normalize("MDE"));
        assertEquals("CTG", CityCodeNormalizer.normalize("CTG"));
        assertEquals("CLO", CityCodeNormalizer.normalize("CLO"));
    }

    @Test
    @DisplayName("Debe normalizar nombres completos de ciudades")
    void testNormalize_FullNames() {
        assertEquals("BOG", CityCodeNormalizer.normalize("Bogotá"));
        assertEquals("BOG", CityCodeNormalizer.normalize("BOGOTA"));
        assertEquals("MDE", CityCodeNormalizer.normalize("Medellín"));
        assertEquals("MDE", CityCodeNormalizer.normalize("MEDELLIN"));
        assertEquals("CTG", CityCodeNormalizer.normalize("Cartagena"));
        assertEquals("CTG", CityCodeNormalizer.normalize("CARTAGENA"));
    }

    @Test
    @DisplayName("Debe manejar variaciones de mayúsculas/minúsculas")
    void testNormalize_CaseInsensitive() {
        assertEquals("BOG", CityCodeNormalizer.normalize("bog"));
        assertEquals("BOG", CityCodeNormalizer.normalize("BoGoTa"));
        assertEquals("MDE", CityCodeNormalizer.normalize("medellin"));
        assertEquals("CTG", CityCodeNormalizer.normalize("cartagena"));
    }

    @Test
    @DisplayName("Debe normalizar Santa Marta correctamente")
    void testNormalize_SantaMarta() {
        assertEquals("SMR", CityCodeNormalizer.normalize("SMR"));
        assertEquals("SMR", CityCodeNormalizer.normalize("Santa Marta"));
        assertEquals("SMR", CityCodeNormalizer.normalize("SANTAMARTA"));
    }

    @Test
    @DisplayName("Debe normalizar San Andrés correctamente")
    void testNormalize_SanAndres() {
        assertEquals("ADZ", CityCodeNormalizer.normalize("ADZ"));
        assertEquals("ADZ", CityCodeNormalizer.normalize("San Andrés"));
        assertEquals("ADZ", CityCodeNormalizer.normalize("SAN ANDRES"));
        assertEquals("ADZ", CityCodeNormalizer.normalize("SANANDRES"));
    }

    @Test
    @DisplayName("Debe manejar valores nulos o vacíos")
    void testNormalize_NullOrEmpty() {
        assertNull(CityCodeNormalizer.normalize(null));
        assertEquals("", CityCodeNormalizer.normalize(""));
        assertEquals("", CityCodeNormalizer.normalize(""));
    }

    @Test
    @DisplayName("Debe devolver el valor original si no hay mapeo")
    void testNormalize_UnknownCity() {
        assertEquals("XYZ", CityCodeNormalizer.normalize("XYZ"));
        assertEquals("CIUDAD DESCONOCIDA", CityCodeNormalizer.normalize("Ciudad Desconocida"));
    }

    @Test
    @DisplayName("Debe manejar espacios adicionales")
    void testNormalize_Trimming() {
        assertEquals("BOG", CityCodeNormalizer.normalize("  BOG  "));
        assertEquals("MDE", CityCodeNormalizer.normalize("  Medellín  "));
    }

    @Test
    @DisplayName("Debe normalizar todas las ciudades colombianas principales")
    void testNormalize_AllCities() {
        // Verificar todas las ciudades mapeadas
        assertEquals("BOG", CityCodeNormalizer.normalize("Bogotá"));
        assertEquals("MDE", CityCodeNormalizer.normalize("Medellín"));
        assertEquals("CTG", CityCodeNormalizer.normalize("Cartagena"));
        assertEquals("CLO", CityCodeNormalizer.normalize("Cali"));
        assertEquals("BAQ", CityCodeNormalizer.normalize("Barranquilla"));
        assertEquals("SMR", CityCodeNormalizer.normalize("Santa Marta"));
        assertEquals("PEI", CityCodeNormalizer.normalize("Pereira"));
        assertEquals("BGA", CityCodeNormalizer.normalize("Bucaramanga"));
        assertEquals("ADZ", CityCodeNormalizer.normalize("San Andrés"));
    }
}


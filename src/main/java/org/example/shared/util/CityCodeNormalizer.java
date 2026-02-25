package org.example.shared.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para normalizar nombres de ciudades a códigos IATA
 * Permite búsquedas flexibles con nombres completos, parciales o códigos
 */
public class CityCodeNormalizer {

    private static final Map<String, String> CITY_MAPPINGS = new HashMap<>();

    static {
        // Bogotá
        CITY_MAPPINGS.put("BOG", "BOG");
        CITY_MAPPINGS.put("BOGOTA", "BOG");
        CITY_MAPPINGS.put("BOGOTÁ", "BOG");

        // Medellín
        CITY_MAPPINGS.put("MDE", "MDE");
        CITY_MAPPINGS.put("MED", "MDE");
        CITY_MAPPINGS.put("MEDELLIN", "MDE");
        CITY_MAPPINGS.put("MEDELLÍN", "MDE");

        // Cartagena
        CITY_MAPPINGS.put("CTG", "CTG");
        CITY_MAPPINGS.put("CARTAGENA", "CTG");

        // Cali
        CITY_MAPPINGS.put("CLO", "CLO");
        CITY_MAPPINGS.put("CALI", "CLO");

        // Barranquilla
        CITY_MAPPINGS.put("BAQ", "BAQ");
        CITY_MAPPINGS.put("BARRANQUILLA", "BAQ");

        // Santa Marta
        CITY_MAPPINGS.put("SMR", "SMR");
        CITY_MAPPINGS.put("SANTA MARTA", "SMR");
        CITY_MAPPINGS.put("SANTAMARTA", "SMR");

        // Pereira
        CITY_MAPPINGS.put("PEI", "PEI");
        CITY_MAPPINGS.put("PEREIRA", "PEI");

        // Bucaramanga
        CITY_MAPPINGS.put("BGA", "BGA");
        CITY_MAPPINGS.put("BUCARAMANGA", "BGA");

        // San Andrés
        CITY_MAPPINGS.put("ADZ", "ADZ");
        CITY_MAPPINGS.put("SAN ANDRES", "ADZ");
        CITY_MAPPINGS.put("SAN ANDRÉS", "ADZ");
        CITY_MAPPINGS.put("SANANDRES", "ADZ");
    }

    /**
     * Normaliza el nombre de una ciudad a su código IATA
     * @param cityName Nombre de la ciudad (puede ser código IATA, nombre completo o parcial)
     * @return Código IATA normalizado o el valor original si no se encuentra mapeo
     */
    public static String normalize(String cityName) {
        if (cityName == null) {
            return null;
        }

        if (cityName.isBlank()) {
            return "";
        }

        String normalized = cityName.trim().toUpperCase();
        return CITY_MAPPINGS.getOrDefault(normalized, normalized);
    }
}


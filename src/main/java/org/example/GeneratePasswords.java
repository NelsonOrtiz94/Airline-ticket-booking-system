package org.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt de contraseñas.
 *
 * USO:
 * 1. Ejecuta esta clase: mvn exec:java -Dexec.mainClass="org.example.GeneratePasswords"
 * 2. Copia el SQL generado al archivo data.sql
 * 3. Guarda las credenciales en un lugar seguro (NO en el repositorio)
 *
 * IMPORTANTE:
 * - Los hashes BCrypt son diferentes cada vez (incluyen salt aleatorio)
 * - Esto es correcto y esperado - BCrypt.matches() los validará correctamente
 */
public class GeneratePasswords {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=".repeat(80));
        System.out.println("  GENERADOR DE HASHES BCRYPT PARA AIRLINE TICKET BOOKING API");
        System.out.println("=".repeat(80));

        // Contraseñas a hashear
        String adminPass = "password";      // Contraseña simple para demo
        String userPass = "password";       // Misma contraseña, hash diferente

        // Generar hashes
        String adminHash = encoder.encode(adminPass);
        String userHash = encoder.encode(userPass);

        System.out.println("\n CREDENCIALES GENERADAS:");
        System.out.println("-".repeat(80));
        System.out.println("Usuario Admin:");
        System.out.println("  - Username: admin");
        System.out.println("  - Password: " + adminPass);
        System.out.println("  - Hash:     " + adminHash);

        System.out.println("\nUsuario Normal:");
        System.out.println("  - Username: user");
        System.out.println("  - Password: " + userPass);
        System.out.println("  - Hash:     " + userHash);

        System.out.println("\n" + "=".repeat(80));
        System.out.println("  SQL PARA COPIAR A data.sql");
        System.out.println("=".repeat(80));

        System.out.println("\n-- Limpieza de datos existentes");
        System.out.println("DELETE FROM reservations;");
        System.out.println("DELETE FROM tickets;");
        System.out.println("DELETE FROM flights;");
        System.out.println("DELETE FROM users;");
        System.out.println("ALTER SEQUENCE users_user_id_seq RESTART WITH 1;");
        System.out.println("ALTER SEQUENCE flights_flight_id_seq RESTART WITH 1;");
        System.out.println("ALTER SEQUENCE tickets_ticket_id_seq RESTART WITH 1;");
        System.out.println("ALTER SEQUENCE reservations_reservation_id_seq RESTART WITH 1;");

        System.out.println("\n-- Insertar usuarios (password para ambos: '" + adminPass + "')");
        System.out.println("INSERT INTO users (user_id, username, password, first_name, last_name, email, role, created_at, updated_at) VALUES");
        System.out.println("(1, 'admin', '" + adminHash + "', 'Admin', 'System', 'admin@airline.com', 'ADMIN', NOW(), NOW()),");
        System.out.println("(2, 'user', '" + userHash + "', 'Usuario', 'Demo', 'user@airline.com', 'USER', NOW(), NOW());");

        System.out.println("\n-- Vuelos de ejemplo");
        System.out.println("INSERT INTO flights (flight_id, flight_number, origin, destination, departure_time, arrival_time, available_seats, total_seats, price, airline, status, created_at, updated_at) VALUES");
        System.out.println("(1, 'AV101', 'BOG', 'MDE', '2026-03-15 08:00:00', '2026-03-15 09:30:00', 50, 50, 250000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),");
        System.out.println("(2, 'AV102', 'BOG', 'CTG', '2026-03-16 10:00:00', '2026-03-16 11:30:00', 50, 50, 300000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),");
        System.out.println("(3, 'LA201', 'MDE', 'CTG', '2026-03-17 14:00:00', '2026-03-17 15:30:00', 45, 45, 280000.00, 'LATAM', 'ACTIVE', NOW(), NOW());");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("  INFORMACIÓN IMPORTANTE");
        System.out.println("=".repeat(80));
        System.out.println("\nLos hashes BCrypt son DIFERENTES cada vez que ejecutas este programa.");
        System.out.println("   Esto es NORMAL y CORRECTO - BCrypt incluye un salt aleatorio.");
        System.out.println("\nAmbos usuarios usan la misma contraseña ('" + adminPass + "') pero tienen hashes diferentes.");
        System.out.println("   Esto demuestra que BCrypt es seguro contra rainbow tables.");
        System.out.println("\nPara hacer login:");
        System.out.println("   POST /airline/auth/login");
        System.out.println("   { \"username\": \"admin\", \"password\": \"" + adminPass + "\" }");
        System.out.println("\n  GUARDA estas credenciales en un lugar SEGURO (no en Git)");
        System.out.println("   Sugerencia: Crea un archivo CREDENCIALES.md y añádelo al .gitignore");
        System.out.println("\n" + "=".repeat(80) + "\n");
    }
}


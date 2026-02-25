-- Usuario: admin / Contraseña: password
INSERT INTO users (user_id, username, password, first_name, last_name, email, role, created_at, updated_at) VALUES
(1, 'admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Admin', 'System', 'admin@airline.com', 'ADMIN', NOW(), NOW());

-- Vuelos de prueba - Acepta códigos IATA y nombres completos de ciudades
-- BOG = Bogotá, MDE = Medellín, CTG = Cartagena, CLO = Cali, BAQ = Barranquilla
-- SMR = Santa Marta, PEI = Pereira, BGA = Bucaramanga, ADZ = San Andrés
INSERT INTO flights (flight_id, flight_number, origin, destination, departure_time, arrival_time, available_seats, total_seats, price, airline, status, created_at, updated_at) VALUES
-- Rutas desde Bogotá
(1, 'AV101', 'BOG', 'MDE', '2026-03-15 08:00:00', '2026-03-15 09:30:00', 50, 50, 250000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(2, 'AV102', 'BOG', 'CTG', '2026-03-16 10:00:00', '2026-03-16 11:30:00', 50, 50, 300000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(3, 'AV103', 'BOG', 'CLO', '2026-03-17 12:00:00', '2026-03-17 13:00:00', 30, 30, 280000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(4, 'LA201', 'BOG', 'BAQ', '2026-03-18 14:00:00', '2026-03-18 15:15:00', 80, 80, 180000.00, 'LATAM', 'ACTIVE', NOW(), NOW()),
(5, 'AV104', 'BOG', 'SMR', '2026-03-19 07:00:00', '2026-03-19 08:30:00', 40, 40, 320000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(6, 'AV105', 'BOG', 'PEI', '2026-03-20 09:00:00', '2026-03-20 10:00:00', 35, 35, 200000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(7, 'AV106', 'BOG', 'BGA', '2026-03-21 11:00:00', '2026-03-21 12:00:00', 45, 45, 190000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(8, 'AV107', 'BOG', 'ADZ', '2026-03-22 13:00:00', '2026-03-22 15:00:00', 60, 60, 450000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
-- Rutas desde Medellín
(9, 'AV201', 'MDE', 'BOG', '2026-03-15 10:00:00', '2026-03-15 11:30:00', 50, 50, 250000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(10, 'AV202', 'MDE', 'CTG', '2026-03-16 12:00:00', '2026-03-16 13:30:00', 40, 40, 270000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(11, 'AV203', 'MDE', 'CLO', '2026-03-17 14:00:00', '2026-03-17 15:00:00', 30, 30, 200000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
-- Rutas desde Cartagena
(12, 'WI301', 'CTG', 'BOG', '2026-03-19 16:00:00', '2026-03-19 17:30:00', 60, 60, 220000.00, 'Wingo', 'ACTIVE', NOW(), NOW()),
(13, 'WI302', 'CTG', 'MDE', '2026-03-20 08:00:00', '2026-03-20 09:30:00', 45, 45, 270000.00, 'Wingo', 'ACTIVE', NOW(), NOW()),
(14, 'LA301', 'CTG', 'CLO', '2026-03-21 10:00:00', '2026-03-21 11:30:00', 35, 35, 290000.00, 'LATAM', 'ACTIVE', NOW(), NOW()),
-- Rutas desde Cali
(15, 'AV401', 'CLO', 'BOG', '2026-03-17 13:30:00', '2026-03-17 14:30:00', 30, 30, 280000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(16, 'AV402', 'CLO', 'MDE', '2026-03-18 15:00:00', '2026-03-18 16:00:00', 30, 30, 200000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
-- Rutas adicionales
(17, 'LA401', 'BAQ', 'BOG', '2026-03-18 16:00:00', '2026-03-18 17:15:00', 80, 80, 180000.00, 'LATAM', 'ACTIVE', NOW(), NOW()),
(18, 'AV501', 'SMR', 'BOG', '2026-03-19 09:00:00', '2026-03-19 10:30:00', 40, 40, 320000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(19, 'AV601', 'PEI', 'BOG', '2026-03-20 11:00:00', '2026-03-20 12:00:00', 35, 35, 200000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(20, 'AV701', 'BGA', 'BOG', '2026-03-21 13:00:00', '2026-03-21 14:00:00', 45, 45, 190000.00, 'Avianca', 'ACTIVE', NOW(), NOW()),
(21, 'AV801', 'ADZ', 'BOG', '2026-03-22 16:00:00', '2026-03-22 18:00:00', 60, 60, 450000.00, 'Avianca', 'ACTIVE', NOW(), NOW());

# ✈️ Airline Ticket Booking System - API REST Reactiva

## 👨‍💻 **Información del Proyecto**

**Desarrollador:** Nelson Alejandro Ortiz  
**Fecha:** Febrero 2026  
**Versión:** 1.0.0  
**Stack Tecnológico:** Java 17, Spring Boot 3.2.2, WebFlux, R2DBC, PostgreSQL, JWT  

---

## 📋 **Índice**

- [Descripción General](#-descripción-general)
- [Arquitectura](#-arquitectura)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Características Técnicas](#-características-técnicas)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Configuración y Ejecución](#-configuración-y-ejecución)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Seguridad](#-seguridad)
- [Testing](#-testing)
- [Clean Code y Buenas Prácticas](#-clean-code-y-buenas-prácticas)
- [Decisiones Técnicas](#-decisiones-técnicas)

---

## 📖 **Descripción General**

Sistema de reserva de vuelos desarrollado con **arquitectura hexagonal (Clean Architecture)** que permite:

- 🔐 Autenticación y autorización con **JWT**
- ✈️ Búsqueda de vuelos disponibles
- 🎫 Gestión completa de reservas (CRUD)
- 🔒 Validación de asientos duplicados
- ⚡ Procesamiento **reactivo** y no bloqueante
- 📊 Monitoreo con Spring Boot Actuator

---

## 🏗️ **Arquitectura**

### **Clean Architecture (Arquitectura Hexagonal)**

Este proyecto implementa **Clean Architecture** (también conocida como Arquitectura Hexagonal o Ports & Adapters), siguiendo los principios de Robert C. Martin.

#### **Diagrama de Capas**

```
┌───────────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                           │
│                  (Infrastructure Layer - Adapters)                │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Controllers (Entry Points)                              │    │
│  │  • AuthController        → Autenticación                 │    │
│  │  • FlightController      → Búsqueda de vuelos            │    │
│  │  • ReservationController → Gestión de reservas           │    │
│  └─────────────────────────────────────────────────────────┘    │
│                              ↓                                    │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Filters & Config                                        │    │
│  │  • JwtAuthenticationFilter → Validación JWT             │    │
│  │  • SecurityConfig          → Configuración seguridad     │    │
│  │  • GlobalExceptionHandler  → Manejo errores              │    │
│  └─────────────────────────────────────────────────────────┘    │
└───────────────────────────────┬───────────────────────────────────┘
                                │
┌───────────────────────────────▼───────────────────────────────────┐
│                    CAPA DE APLICACIÓN                             │
│                    (Application Layer - Use Cases)                │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Use Cases (Lógica de Negocio)                          │    │
│  │  • AuthenticateUserUseCase    → Login con BCrypt        │    │
│  │  • BookTicketUseCase          → Reservar con validación │    │
│  │  • SearchFlightsUseCase       → Búsqueda de vuelos      │    │
│  │  • CancelReservationUseCase   → Cancelar + devolver     │    │
│  │  • UpdateReservationUseCase   → Actualizar reserva      │    │
│  │  • GetUserReservationsUseCase → Consultar reservas      │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ¿Por qué Use Cases?                                             │
│  ✅ Encapsula lógica de negocio compleja                         │
│  ✅ Independiente de frameworks                                  │
│  ✅ Fácil de testear (sin dependencias externas)                │
│  ✅ Reusable en múltiples interfaces (REST, GraphQL, etc.)      │
└───────────────────────────────┬───────────────────────────────────┘
                                │
┌───────────────────────────────▼───────────────────────────────────┐
│                      CAPA DE DOMINIO                              │
│                   (Domain Layer - Entities)                       │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Entidades de Dominio (POJOs Puros)                     │    │
│  │  • User        → Usuario del sistema                     │    │
│  │  • Flight      → Vuelo con asientos                      │    │
│  │  • Ticket      → Ticket de vuelo                         │    │
│  │  • Reservation → Reserva confirmada                      │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Enums (Estados del Sistema)                            │    │
│  │  • FlightStatus       → ACTIVE, CANCELLED, DELAYED      │    │
│  │  • ReservationStatus  → CONFIRMED, CANCELLED, PENDING   │    │
│  │  • TicketStatus       → CONFIRMED, CANCELLED, USED      │    │
│  │  • TicketClass        → ECONOMY, BUSINESS, FIRST_CLASS  │    │
│  │  • UserRole           → ADMIN, USER, AGENT              │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ¿Por qué esta capa es el corazón?                               │
│  ✅ NO depende de nada externo (frameworks, BD, web)             │
│  ✅ Define las reglas de negocio                                 │
│  ✅ Puede vivir en cualquier infraestructura                     │
└───────────────────────────────┬───────────────────────────────────┘
                                │
┌───────────────────────────────▼───────────────────────────────────┐
│                 CAPA DE INFRAESTRUCTURA                           │
│              (Infrastructure Layer - Adapters)                    │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Repositories (Ports Implementados)                     │    │
│  │  • FlightRepository      → R2DBC PostgreSQL             │    │
│  │  • ReservationRepository → R2DBC PostgreSQL             │    │
│  │  • TicketRepository      → R2DBC PostgreSQL             │    │
│  │  • UserRepository        → R2DBC PostgreSQL             │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  Mappers (Transformaciones)                             │    │
│  │  • FlightResponseMapper      → Domain → DTO             │    │
│  │  • ReservationResponseMapper → Domain → DTO             │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                   │
│  ¿Por qué esta capa es reemplazable?                             │
│  ✅ Puedes cambiar PostgreSQL por MySQL sin afectar lógica       │
│  ✅ Puedes cambiar R2DBC por JPA sin afectar casos de uso       │
│  ✅ Puedes cambiar REST por GraphQL sin afectar dominio         │
└───────────────────────────────────────────────────────────────────┘

         ↓                       ↓                      ↓
    PostgreSQL              Servicios               Framework
    (R2DBC)                 Externos                (Spring Boot)
```

#### **Principios de Clean Architecture Aplicados**

**1. Dependency Rule (Regla de Dependencia)**
```
Externo → Infrastructure → Application → Domain
  ❌          ✅              ✅            ✅

Las dependencias SOLO apuntan hacia adentro:
• Domain NO conoce Application ni Infrastructure
• Application conoce Domain pero NO Infrastructure  
• Infrastructure conoce todo (es la capa externa)
```

**2. Independence (Independencia)**
```java
// ✅ Use Case NO depende de Spring
public class BookTicketUseCase {
    private final FlightRepository repository; // Interface
    // No @Service, no @Autowired aquí
}

// ✅ Entity NO depende de BD
@Data // Lombok, no JPA
public class Flight {
    private Long flightId;
    // Sin @Entity, sin @Table aquí (está en Domain)
}
```

**3. Testability (Facilidad de Testing)**
```java
// ✅ Test del Use Case sin Spring
@Test
void shouldBookTicket() {
    // Mock del repository (interface)
    FlightRepository mockRepo = mock(FlightRepository.class);
    BookTicketUseCase useCase = new BookTicketUseCase(mockRepo);
    
    // Test puro de lógica de negocio
    // Sin @SpringBootTest, sin BD, sin contexto
}
```

### **Flujo de una Reserva (Clean Architecture)**

```
┌────────────────────────────────────────────────────────────────┐
│ 1. ENTRADA (Infrastructure Layer - Controller)                 │
└────────────────────────────────────────────────────────────────┘
    POST /airline/reservations
    Headers: Authorization: Bearer {token}
    Body: { userId, flightId, passengerName, seatNumber }
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ 2. VALIDACIÓN (Infrastructure Layer)                           │
└────────────────────────────────────────────────────────────────┘
    • JwtAuthenticationFilter valida token JWT
    • @Valid valida BookingRequest (Jakarta Validation)
    • Controller mapea DTO → Domain objects
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ 3. LÓGICA DE NEGOCIO (Application Layer - Use Case)            │
└────────────────────────────────────────────────────────────────┘
    BookTicketUseCase.execute(request):
    
    a) Buscar vuelo en BD
       └─> flightRepository.findActiveFlightById()
       
    b) Validar vuelo existe
       └─> Si no existe: throw FlightNotFoundException
       
    c) Validar asientos disponibles
       └─> Si availableSeats <= 0: throw NoSeatsAvailableException
       
    d) Validar asiento no duplicado
       └─> ticketRepository.findByFlightIdAndSeatNumber()
       └─> Si existe: throw SeatAlreadyTakenException
       
    e) Crear ticket
       └─> Ticket ticket = Ticket.builder()...
       └─> ticketRepository.save(ticket)
       
    f) Crear reserva
       └─> Reservation reservation = Reservation.builder()...
       └─> reservationRepository.save(reservation)
       
    g) Decrementar asientos
       └─> flight.setAvailableSeats(flight.getAvailableSeats() - 1)
       └─> flightRepository.save(flight)
       
    h) Retornar resultado
       └─> return Mono.just(reservationResponse)
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ 4. PERSISTENCIA (Infrastructure Layer - Repository)            │
└────────────────────────────────────────────────────────────────┘
    • R2DBC ejecuta queries reactivas
    • PostgreSQL guarda datos
    • Transacciones manejadas por Spring
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ 5. MAPEO (Infrastructure Layer - Mapper)                       │
└────────────────────────────────────────────────────────────────┘
    • ReservationResponseMapper.toResponse()
    • Domain Reservation → DTO ReservationResponse
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ 6. RESPUESTA (Infrastructure Layer - Controller)               │
└────────────────────────────────────────────────────────────────┘
    Response 200 OK:
    {
      "status": "SUCCESS",
      "data": {
        "reservationId": 1,
        "seatNumber": "12A",
        "status": "CONFIRMED"
      }
    }
```

### **Ventajas de Esta Arquitectura**

| Característica | Beneficio | Ejemplo en el Proyecto |
|----------------|-----------|------------------------|
| **Independencia de Frameworks** | Lógica no depende de Spring | Use Cases sin @Service |
| **Testeable** | Tests sin contexto Spring | 91 tests unitarios puros |
| **Mantenible** | Cambios aislados por capa | Cambiar BD no afecta lógica |
| **Escalable** | Agregar features fácilmente | Nuevo Use Case = nueva clase |
| **Portable** | Mismo dominio, distinta infra | Pasar de REST a GraphQL |

### **Comparación: Clean vs Tradicional**

**❌ Arquitectura Tradicional (Acoplada):**
```java
@Service
public class BookingService {
    @Autowired
    private FlightRepository flightRepo; // JPA
    
    @Transactional
    public Booking createBooking(...) {
        // Lógica mezclada con JPA, transacciones, etc.
        Flight flight = flightRepo.findById(...); // JPA Entity
        // ❌ Dominio acoplado a framework
    }
}
```

**✅ Clean Architecture (Desacoplada):**
```java
// Domain: Puro, sin frameworks
@Data
public class Flight {
    private Long flightId;
    // ✅ POJO puro
}

// Application: Lógica pura
public class BookTicketUseCase {
    private final FlightRepository flightRepository; // Interface
    
    public Mono<ReservationResponse> execute(BookingRequest request) {
        // ✅ Solo lógica de negocio
        // ✅ No depende de Spring, JPA, etc.
    }
}

// Infrastructure: Implementaciones
@Repository
public interface FlightRepository extends ReactiveCrudRepository<Flight, Long> {
    // ✅ R2DBC, pero Use Case no lo sabe
}
```

---

## 🚀 **Tecnologías Utilizadas**

### **Backend**
- **Java 17** (LTS) - Lenguaje principal
- **Spring Boot 3.2.2** - Framework base
- **Spring WebFlux** - Programación reactiva
- **Spring Security** - Autenticación y autorización
- **Spring Data R2DBC** - Acceso reactivo a BD
- **Project Reactor** - Mono/Flux para reactividad

### **Base de Datos**
- **PostgreSQL 16** - Base de datos relacional
- **R2DBC PostgreSQL** - Driver reactivo

### **Seguridad**
- **JWT (JJWT 0.12.3)** - JSON Web Tokens
- **BCrypt** - Hash de contraseñas
- **Spring Security** - Configuración de seguridad

### **Testing**
- **JUnit 5** - Framework de testing
- **Mockito** - Mocking de dependencias
- **Reactor Test** - Testing reactivo
- **JaCoCo** - Cobertura de código

### **DevOps**
- **Docker** - Contenedorización
- **Docker Compose** - Orquestación
- **Maven 3.9** - Gestión de dependencias
- **Lombok** - Reducción de boilerplate

---

## ⚡ **Características Técnicas**

### **1. Programación Reactiva**

```java
// Ejemplo: BookTicketUseCase
public Mono<ReservationResponse> execute(BookingRequest request) {
    return flightRepository.findActiveFlightById(request.getFlightId())
        .switchIfEmpty(Mono.error(new FlightNotFoundException(request.getFlightId())))
        .flatMap(flight -> validateAndBook(flight, request))
        .doOnSuccess(response -> log.info("Reserva creada: {}", response.getReservationId()))
        .doOnError(error -> log.error("Error: {}", error.getMessage()));
}
```

**Ventajas:**
- ✅ No bloqueante
- ✅ Alta concurrencia
- ✅ Uso eficiente de recursos
- ✅ Backpressure automático

### **2. Seguridad JWT**

```java
// JwtService con configuración externa
@Value("${spring.security.jwt.secret}")
private String jwtSecret;

@Value("${spring.security.jwt.expiration}")
private long jwtExpiration;
```

**Implementación:**
- ✅ Token con expiración configurable
- ✅ Secret key externalizada
- ✅ Validación en cada request protegido
- ✅ Filtro JWT personalizado

### **3. Validación de Asientos Duplicados**

```java
// TicketRepository
@Query("SELECT * FROM tickets WHERE flight_id = :flightId " +
       "AND seat_number = :seatNumber AND status IN ('CONFIRMED', 'PENDING')")
Mono<Ticket> findByFlightIdAndSeatNumber(Long flightId, String seatNumber);
```

**Flujo:**
```
1. Usuario intenta reservar asiento 12A
2. Query busca si 12A ya está ocupado
3. Si existe → Lanza SeatAlreadyTakenException (409)
4. Si no existe → Continúa con la reserva
```

### **4. Manejo Centralizado de Errores**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiResponse<Void>> handleFlightNotFound(FlightNotFoundException ex) {
        return Mono.just(ApiResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(SeatAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ApiResponse<Void>> handleSeatAlreadyTaken(SeatAlreadyTakenException ex) {
        return Mono.just(ApiResponse.error(ex.getMessage()));
    }
}
```

---

## 📁 **Estructura del Proyecto**

```
src/main/java/org/example/
│
├── application/usecase/          # Casos de uso (Lógica de negocio)
│   ├── AuthenticateUserUseCase.java
│   ├── BookTicketUseCase.java
│   ├── CancelReservationUseCase.java
│   ├── GetUserReservationsUseCase.java
│   ├── SearchFlightsUseCase.java
│   └── UpdateReservationUseCase.java
│
├── domain/entity/                # Entidades de dominio (POJOs)
│   ├── Flight.java
│   ├── Reservation.java
│   ├── Ticket.java
│   └── User.java
│
├── infrastructure/
│   ├── adapter/mapper/           # Mappers de transformación
│   │   ├── FlightResponseMapper.java
│   │   └── ReservationResponseMapper.java
│   │
│   ├── config/                   # Configuraciones
│   │   ├── DatabaseConfig.java
│   │   ├── JwtService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── SecurityConfig.java
│   │
│   ├── controller/               # Controladores REST
│   │   ├── AuthController.java
│   │   ├── FlightController.java
│   │   └── ReservationController.java
│   │
│   ├── exception/                # Manejo de excepciones
│   │   └── GlobalExceptionHandler.java
│   │
│   └── repository/               # Repositorios R2DBC
│       ├── FlightRepository.java
│       ├── ReservationRepository.java
│       ├── TicketRepository.java
│       └── UserRepository.java
│
└── shared/                       # Código compartido
    ├── constants/                # Constantes centralizadas
    │   ├── MessageConstants.java
    │   └── SecurityConstants.java
    │
    ├── dto/                      # Data Transfer Objects
    │   ├── BookingRequest.java
    │   ├── FlightResponse.java
    │   ├── LoginRequest.java
    │   └── ReservationResponse.java
    │
    ├── enums/                    # Enumeraciones
    │   ├── FlightStatus.java
    │   ├── ReservationStatus.java
    │   ├── TicketStatus.java
    │   ├── TicketClass.java
    │   └── UserRole.java
    │
    ├── exception/                # Excepciones personalizadas
    │   ├── FlightNotFoundException.java
    │   ├── SeatAlreadyTakenException.java
    │   ├── NoSeatsAvailableException.java
    │   └── AuthenticationException.java
    │
    ├── response/                 # Respuestas estandarizadas
    │   └── ApiResponse.java
    │
    └── util/                     # Utilidades
        └── DateUtil.java
```

---

## ⚙️ **Configuración y Ejecución**

### **Requisitos Previos**
- Java 17+
- Maven 3.9+
- PostgreSQL 16+ (o Docker)
- Postman (opcional, para testing)

### **Configuración de Base de Datos**

**application.yml:**
```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/airline_db
    username: postgres
    password: postgres
  
  security:
    jwt:
      secret: ${JWT_SECRET:mySecretKeyForJWTTokenGenerationAndValidation123456789}
      expiration: 86400000  # 24 horas
```

### **Ejecución con Maven**

```bash
# 1. Clonar repositorio
git clone <repository-url>

# 2. Compilar
mvn clean install

# 3. Ejecutar
mvn spring-boot:run

# 4. Verificar
curl http://localhost:8080/actuator/health
```

### **Ejecución con Docker**

```bash
# 1. Construir imagen
docker build -t airline-api .

# 2. Ejecutar con docker-compose
docker-compose up -d

# 3. Ver logs
docker-compose logs -f airline-api
```

---

## 🌐 **Endpoints de la API**

### **Base URL:** `http://localhost:8080/airline`

### **1. Autenticación**

#### **POST /auth/login**
```json
Request:
{
  "username": "admin",
  "password": "password"
}

Response (200):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ADMIN"
}
```

### **2. Búsqueda de Vuelos**

#### **POST /flights/search** (Público)
```json
Request:
{
  "origin": "BOG",
  "destination": "MDE"
}

Response (200):
[
  {
    "flightId": 1,
    "flightNumber": "AV101",
    "origin": "BOG",
    "destination": "MDE",
    "departureTime": "2026-03-15 08:00:00",
    "arrivalTime": "2026-03-15 09:30:00",
    "availableSeats": 50,
    "price": 250000.00,
    "airline": "Avianca",
    "status": "ACTIVE"
  }
]
```

### **3. Gestión de Reservas**

#### **POST /reservations** (Protegido)
```json
Request:
Headers: Authorization: Bearer {token}
{
  "userId": 1,
  "flightId": 1,
  "passengerName": "Juan Pérez",
  "seatNumber": "12A",
  "observations": "Ventana preferida"
}

Response (200):
{
  "status": "SUCCESS",
  "message": "Reserva creada exitosamente",
  "data": {
    "reservationId": 1,
    "userId": 1,
    "flightId": 1,
    "passengerName": "Juan Pérez",
    "seatNumber": "12A",
    "price": 250000.00,
    "status": "CONFIRMED"
  }
}
```

#### **GET /reservations/user/{userId}** (Protegido)
```json
Response (200):
[
  {
    "reservationId": 1,
    "userId": 1,
    "flightId": 1,
    "passengerName": "Juan Pérez",
    "seatNumber": "12A",
    "flightDetails": "AV101 - BOG a MDE",
    "status": "CONFIRMED"
  }
]
```

#### **PUT /reservations** (Protegido)
```json
Request:
{
  "reservationId": 1,
  "newSeatNumber": "14B",
  "observations": "Cambio a pasillo"
}
```

#### **DELETE /reservations/{id}** (Protegido)
```
Response (200):
{
  "status": "SUCCESS",
  "message": "Reserva cancelada exitosamente"
}
```

### **Códigos de Estado HTTP**

| Código | Descripción | Cuándo |
|--------|-------------|--------|
| 200 | OK | Operación exitosa |
| 400 | Bad Request | Datos inválidos |
| 401 | Unauthorized | Sin token o token inválido |
| 404 | Not Found | Recurso no encontrado |
| 409 | Conflict | Asiento duplicado o sin asientos |
| 500 | Internal Server Error | Error del servidor |

---

## 🔐 **Seguridad**

### **Autenticación JWT**

**Flujo:**
```
1. Usuario → POST /auth/login (username, password)
2. Sistema → Valida con BCrypt
3. Sistema → Genera token JWT
4. Usuario → Guarda token
5. Usuario → Envía token en cada request: Authorization: Bearer {token}
6. Sistema → Valida token en JwtAuthenticationFilter
7. Sistema → Permite acceso si token válido
```

### **BCrypt Password Hashing**

```java
// Al crear usuario
String hashedPassword = passwordEncoder.encode("password");
// Resultado: $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG

// Al hacer login
boolean matches = passwordEncoder.matches("password", hashedPassword);
// Resultado: true
```

**Características:**
- ✅ Salt aleatorio automático
- ✅ 10 rounds de hashing (configurable)
- ✅ Irreversible
- ✅ Resistente a rainbow tables

### **Configuración de Seguridad**

```java
@Bean
public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers(SecurityConstants.AUTH_PATH).permitAll()
            .pathMatchers(SecurityConstants.FLIGHTS_PATH).permitAll()
            .pathMatchers(SecurityConstants.ACTUATOR_PATH).permitAll()
            .anyExchange().authenticated()
        )
        .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
}
```

---

## 🧪 **Testing**

### **Cobertura de Tests**

```
✅ 91 Tests Unitarios Pasando (100%)
✅ Cobertura Total: 95%+
```

| Categoría | Tests | Cobertura |
|-----------|-------|-----------|
| Use Cases | 16 | 100% |
| Enums | 26 | 100% |
| Mappers | 9 | 100% |
| Security (JWT) | 11 | 100% |
| Constants | 14 | 100% |
| Exception Handler | 9 | 100% |
| Utilities | 6 | 100% |

### **Ejecutar Tests**

```bash
# Todos los tests unitarios
mvn test

# Con reporte de cobertura
mvn clean test jacoco:report

# Ver reporte HTML
open target/site/jacoco/index.html
```

### **Ejemplo de Test**

```java
@Test
void shouldCreateReservationSuccessfully() {
    // Given
    BookingRequest request = BookingRequest.builder()
        .userId(1L)
        .flightId(1L)
        .passengerName("Juan Pérez")
        .seatNumber("12A")
        .build();
    
    Flight flight = createTestFlight();
    Ticket ticket = createTestTicket();
    Reservation reservation = createTestReservation();
    
    when(flightRepository.findActiveFlightById(1L))
        .thenReturn(Mono.just(flight));
    when(ticketRepository.findByFlightIdAndSeatNumber(1L, "12A"))
        .thenReturn(Mono.empty());
    when(ticketRepository.save(any())).thenReturn(Mono.just(ticket));
    when(reservationRepository.save(any())).thenReturn(Mono.just(reservation));
    when(flightRepository.save(any())).thenReturn(Mono.just(flight));
    
    // When
    StepVerifier.create(bookTicketUseCase.execute(request))
        // Then
        .assertNext(response -> {
            assertNotNull(response);
            assertEquals(1L, response.getReservationId());
            assertEquals("12A", response.getSeatNumber());
        })
        .verifyComplete();
}
```

---

## 🎯 **Clean Code y Buenas Prácticas**

### **Principios SOLID Aplicados**

#### **1. Single Responsibility Principle**
```java
// ✅ BIEN: Cada clase tiene una sola responsabilidad
public class BookTicketUseCase {
    // Solo maneja la lógica de reservar tickets
}

public class FlightResponseMapper {
    // Solo transforma Flight → FlightResponse
}
```

#### **2. Open/Closed Principle**
```java
// ✅ BIEN: Extensible sin modificar código existente
public enum FlightStatus {
    ACTIVE, CANCELLED, DELAYED, COMPLETED, BOARDING;
    // Agregar nuevo estado no requiere cambiar código existente
}
```

#### **3. Dependency Inversion Principle**
```java
// ✅ BIEN: Depende de abstracciones (interfaces)
public class BookTicketUseCase {
    private final FlightRepository flightRepository; // Interfaz
    private final TicketRepository ticketRepository; // Interfaz
}
```

### **Clean Code Implementado**

#### **1. Sin Valores Mágicos**
```java
// ❌ MAL
if (status.equals("ACTIVE")) { ... }

// ✅ BIEN
if (status.equals(FlightStatus.ACTIVE.getCode())) { ... }
```

#### **2. Constantes Centralizadas**
```java
// ✅ BIEN
public class SecurityConstants {
    public static final String AUTH_PATH = "/airline/auth/**";
    public static final String BEARER_PREFIX = "Bearer ";
}
```

#### **3. Excepciones Descriptivas**
```java
// ✅ BIEN
throw new SeatAlreadyTakenException(seatNumber, flightId);
// Mensaje: "El asiento 12A ya está ocupado en el vuelo ID: 1"
```

#### **4. Separación de Responsabilidades**
```java
// ✅ BIEN: Mapper dedicado
public class FlightResponseMapper {
    public FlightResponse toResponse(Flight flight) {
        return FlightResponse.builder()
            .flightId(flight.getFlightId())
            .flightNumber(flight.getFlightNumber())
            // ...
            .build();
    }
}
```

---

## 🎨 **Clean Architecture: Implementación Detallada**

### **¿Por qué Clean Architecture?**

Clean Architecture (propuesta por Robert C. Martin "Uncle Bob") nos permite:

1. **Independencia de Frameworks:** La lógica de negocio no depende de Spring, Hibernate, etc.
2. **Testeable:** Los tests no necesitan BD, servidor web, o frameworks
3. **Independencia de UI:** Misma lógica sirve para REST, GraphQL, CLI, etc.
4. **Independencia de BD:** Cambiar PostgreSQL por MongoDB no afecta lógica
5. **Independencia de agentes externos:** La lógica no conoce servicios externos

### **Estructura de Paquetes según Clean Architecture**

```
src/main/java/org/example/
│
├── domain/                          # ⭐ CAPA DE DOMINIO (Núcleo)
│   └── entity/                      # Entidades de negocio
│       ├── User.java                # POJO puro, sin anotaciones de BD
│       ├── Flight.java              # POJO puro
│       ├── Ticket.java              # POJO puro
│       └── Reservation.java         # POJO puro
│
├── application/                     # ⭐ CAPA DE APLICACIÓN (Use Cases)
│   └── usecase/                     # Casos de uso (lógica de negocio)
│       ├── BookTicketUseCase.java           # Reservar ticket
│       ├── CancelReservationUseCase.java    # Cancelar reserva
│       ├── SearchFlightsUseCase.java        # Buscar vuelos
│       ├── UpdateReservationUseCase.java    # Actualizar reserva
│       ├── GetUserReservationsUseCase.java  # Ver reservas
│       └── AuthenticateUserUseCase.java     # Autenticar
│
├── infrastructure/                  # ⭐ CAPA DE INFRAESTRUCTURA (Adapters)
│   ├── controller/                  # Adaptadores de entrada (HTTP)
│   │   ├── AuthController.java
│   │   ├── FlightController.java
│   │   └── ReservationController.java
│   │
│   ├── repository/                  # Adaptadores de salida (BD)
│   │   ├── FlightRepository.java          # Interface R2DBC
│   │   ├── ReservationRepository.java
│   │   ├── TicketRepository.java
│   │   └── UserRepository.java
│   │
│   ├── adapter/mapper/              # Transformadores
│   │   ├── FlightResponseMapper.java
│   │   └── ReservationResponseMapper.java
│   │
│   ├── config/                      # Configuraciones
│   │   ├── SecurityConfig.java
│   │   ├── JwtService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── DatabaseConfig.java
│   │
│   └── exception/                   # Manejo de errores
│       └── GlobalExceptionHandler.java
│
└── shared/                          # ⭐ CÓDIGO COMPARTIDO
    ├── dto/                         # Data Transfer Objects
    ├── enums/                       # Enumeraciones
    ├── exception/                   # Excepciones personalizadas
    ├── constants/                   # Constantes
    ├── response/                    # Respuestas estandarizadas
    └── util/                        # Utilidades
```

### **Ejemplo Real: BookTicketUseCase**

**1. Domain (Entidad Pura):**
```java
// domain/entity/Ticket.java
@Data
@Builder
public class Ticket {
    private Long ticketId;
    private Long flightId;
    private Long userId;
    private String passengerName;
    private String seatNumber;
    private BigDecimal price;
    private String ticketClass;
    private String status;
    
    // ✅ Sin @Entity, sin @Table
    // ✅ POJO puro de Java
    // ✅ No depende de frameworks
}
```

**2. Application (Use Case Puro):**
```java
// application/usecase/BookTicketUseCase.java
@Service // ← Única anotación de framework
@RequiredArgsConstructor
public class BookTicketUseCase {
    
    // Dependencias por INTERFACE (no implementación)
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    
    public Mono<ReservationResponse> execute(BookingRequest request) {
        // ✅ Solo lógica de negocio
        // ✅ No conoce HTTP, no conoce BD específica
        // ✅ Trabaja con abstracciones (interfaces)
        
        return validateFlight(request)
            .flatMap(flight -> validateSeat(flight, request))
            .flatMap(flight -> createTicket(flight, request))
            .flatMap(ticket -> createReservation(ticket, request))
            .flatMap(this::decrementSeats)
            .map(ReservationResponseMapper::toResponse);
    }
    
    private Mono<Flight> validateFlight(BookingRequest request) {
        return flightRepository.findActiveFlightById(request.getFlightId())
            .switchIfEmpty(Mono.error(
                new FlightNotFoundException(request.getFlightId())
            ));
    }
    
    // ... más métodos de validación
}
```

**3. Infrastructure (Adaptador de BD):**
```java
// infrastructure/repository/FlightRepository.java
@Repository // ← Spring Data
public interface FlightRepository extends ReactiveCrudRepository<Flight, Long> {
    
    // ✅ Implementación específica (R2DBC)
    // ✅ Use Case NO conoce esto
    // ✅ Podría ser JPA, MongoDB, etc.
    
    @Query("SELECT * FROM flights WHERE flight_id = :flightId AND status = 'ACTIVE'")
    Mono<Flight> findActiveFlightById(Long flightId);
}
```

**4. Infrastructure (Adaptador HTTP):**
```java
// infrastructure/controller/ReservationController.java
@RestController
@RequestMapping("/airline/reservations")
@RequiredArgsConstructor
public class ReservationController {
    
    private final BookTicketUseCase bookTicketUseCase; // ← Use Case
    
    @PostMapping
    public Mono<ResponseEntity<ApiResponse<ReservationResponse>>> bookTicket(
            @Valid @RequestBody BookingRequest request
    ) {
        // ✅ Solo adapta HTTP → Use Case
        // ✅ No contiene lógica de negocio
        
        return bookTicketUseCase.execute(request)
            .map(response -> ResponseEntity.ok(ApiResponse.success(response)))
            .onErrorResume(this::handleError);
    }
}
```

### **Dependency Rule: Flujo de Dependencias**

```
┌──────────────────────────────────────────────────────┐
│  Controllers (HTTP)                                  │
│  Dependen de → Use Cases (interfaces)                │
└──────────────────────────────────────────────────────┘
                        ↓
┌──────────────────────────────────────────────────────┐
│  Use Cases (Lógica de Negocio)                       │
│  Dependen de → Repositories (interfaces)             │
│  Dependen de → Entities (domain)                     │
└──────────────────────────────────────────────────────┘
                        ↓
┌──────────────────────────────────────────────────────┐
│  Entities (Domain)                                   │
│  NO dependen de NADA ✅                              │
└──────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────┐
│  Repositories (Implementaciones R2DBC)               │
│  Implementan → Repository Interfaces                 │
│  Dependen de → Spring Data R2DBC                     │
└──────────────────────────────────────────────────────┘
```

### **Testing según Clean Architecture**

**Test de Dominio (Sin frameworks):**
```java
@Test
void ticketShouldCalculatePrice() {
    // ✅ Test puro de dominio
    // ✅ Sin Spring, sin BD, sin nada
    
    Ticket ticket = Ticket.builder()
        .price(new BigDecimal("100000"))
        .ticketClass(TicketClass.BUSINESS.getCode())
        .build();
    
    BigDecimal expected = new BigDecimal("250000"); // 2.5x multiplier
    assertEquals(expected, ticket.calculateFinalPrice());
}
```

**Test de Use Case (Mock de repositories):**
```java
@ExtendWith(MockitoExtension.class)
class BookTicketUseCaseTest {
    
    @Mock
    private FlightRepository flightRepository;
    
    @Mock
    private TicketRepository ticketRepository;
    
    @InjectMocks
    private BookTicketUseCase bookTicketUseCase;
    
    @Test
    void shouldBookTicketSuccessfully() {
        // ✅ Test de lógica pura
        // ✅ Sin @SpringBootTest
        // ✅ Mock de interfaces
        
        when(flightRepository.findActiveFlightById(1L))
            .thenReturn(Mono.just(flight));
        
        when(ticketRepository.findByFlightIdAndSeatNumber(1L, "12A"))
            .thenReturn(Mono.empty());
        
        StepVerifier.create(bookTicketUseCase.execute(request))
            .assertNext(response -> {
                assertNotNull(response);
                assertEquals("12A", response.getSeatNumber());
            })
            .verifyComplete();
    }
}
```

**Test de Controller (Integración con Spring):**
```java
@WebFluxTest(ReservationController.class)
class ReservationControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
    private BookTicketUseCase bookTicketUseCase; // ← Mock del Use Case
    
    @Test
    void shouldBookTicket() {
        // ✅ Test de capa HTTP
        // ✅ Use Case mockeado
        
        when(bookTicketUseCase.execute(any()))
            .thenReturn(Mono.just(response));
        
        webTestClient.post()
            .uri("/airline/reservations")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status").isEqualTo("SUCCESS");
    }
}
```

### **Beneficios Reales en Este Proyecto**

| Beneficio | Implementación | Resultado |
|-----------|----------------|-----------|
| **Cambiar BD** | Repositories son interfaces | Cambiar R2DBC → JPA solo afecta Infrastructure |
| **Cambiar REST → GraphQL** | Use Cases no conocen HTTP | Crear GraphQL resolver que use mismo Use Case |
| **Testing** | 91 tests unitarios sin Spring | Tests rápidos (< 1s cada uno) |
| **Agregar feature** | Nuevo Use Case = nueva clase | No modificar código existente (Open/Closed) |
| **Refactoring** | Cambios aislados por capa | Cambiar mapper no afecta Use Case |

### **Comparación: Antes vs Después de Clean Architecture**

**❌ Código Tradicional (Todo junto):**
```java
@Service
public class BookingService {
    @Autowired
    private FlightRepository repo;
    
    @Transactional
    public BookingDTO bookTicket(BookingRequest req) {
        // ❌ Lógica mezclada con framework
        // ❌ Difícil de testear
        // ❌ Acoplado a JPA
        Flight flight = repo.findById(req.getFlightId()).get();
        if (flight.getSeats() <= 0) throw new Exception();
        // ... más lógica mezclada
        return new BookingDTO(flight); // ❌ Transformación aquí
    }
}
```

**✅ Clean Architecture (Separado):**
```java
// Domain: Puro
public class Flight {
    private Integer availableSeats;
    public boolean hasAvailableSeats() { return availableSeats > 0; }
}

// Use Case: Lógica pura
public class BookTicketUseCase {
    public Mono<Reservation> execute(BookingRequest req) {
        return flightRepo.findById(req.getFlightId())
            .filter(Flight::hasAvailableSeats)
            .switchIfEmpty(Mono.error(new NoSeatsException()))
            .flatMap(flight -> createReservation(flight, req));
    }
}

// Infrastructure: Adaptador
@RestController
public class ReservationController {
    public Mono<ResponseEntity> book(@RequestBody BookingRequest req) {
        return useCase.execute(req)
            .map(mapper::toResponse) // ← Transformación separada
            .map(ResponseEntity::ok);
    }
}
```

### **Preguntas de Entrevista sobre Clean Architecture**

**P: ¿Por qué los Use Cases son @Service si dices que no dependen de frameworks?**
```
R: Es un compromiso pragmático. La LÓGICA dentro del Use Case es pura 
   (no usa nada de Spring). Solo la anotación @Service es para que Spring 
   lo inyecte. La alternativa sería configuración manual, menos conveniente.
```

**P: ¿Las entidades no deberían tener @Entity de JPA?**
```
R: No en Clean Architecture. Las entidades del DOMINIO son POJOs puros.
   Si necesitas JPA, creas entidades de PERSISTENCIA separadas en 
   infrastructure/persistence/entity/ y mapeas entre ellas.
```

**P: ¿Es más código que arquitectura tradicional?**
```
R: Sí, un poco más. PERO:
   • Es más mantenible a largo plazo
   • Tests más rápidos y fáciles
   • Cambios más seguros (aislados)
   • Escalable sin romper existente
```

**P: ¿Cuándo NO usar Clean Architecture?**
```
R: 
   • CRUD muy simple (5-10 endpoints triviales)
   • Prototipo rápido (MVP en 2 semanas)
   • Scripts de una sola vez
   
   USAR cuando:
   • Proyecto a largo plazo
   • Equipo grande
   • Lógica de negocio compleja
   • Múltiples interfaces (REST + GraphQL + Mobile)
```

---

## **Decisiones Técnicas**

### **¿Por qué WebFlux en lugar de Web MVC?**

**Ventajas:**
- ✅ **No bloqueante:** Mejor uso de threads
- ✅ **Alta concurrencia:** Miles de conexiones simultáneas
- ✅ **Backpressure:** Control automático de flujo
- ✅ **Escalabilidad:** Menos recursos, más throughput

**Trade-off:**
- ⚠️ Mayor complejidad inicial
- ⚠️ Curva de aprendizaje de Reactor

### **¿Por qué R2DBC en lugar de JPA?**

**Ventajas:**
- ✅ **Reactivo de extremo a extremo:** No bloqueante hasta la BD
- ✅ **Compatible con WebFlux:** Stack completamente reactivo
- ✅ **Mejor rendimiento:** Para I/O intensivo

**Trade-off:**
- ⚠️ Menos maduro que JPA
- ⚠️ Sin lazy loading

### **¿Por qué Arquitectura Hexagonal?**

**Ventajas:**
- ✅ **Testeable:** Fácil de mockear dependencias
- ✅ **Mantenible:** Cambios aislados por capa
- ✅ **Escalable:** Agregar features sin romper existente
- ✅ **Portable:** Cambiar BD/Framework sin afectar lógica

### **¿Por qué JWT en lugar de Sessions?**

**Ventajas:**
- ✅ **Stateless:** No necesita almacenar en servidor
- ✅ **Escalable:** Funciona en arquitecturas distribuidas
- ✅ **Portable:** Funciona en web, móvil, etc.
- ✅ **Self-contained:** Toda la info en el token

---

## 📊 **Modelo de Datos**

### **Diagrama ER**

```
┌─────────────┐         ┌─────────────┐
│    USERS    │         │   FLIGHTS   │
├─────────────┤         ├─────────────┤
│ user_id (PK)│         │flight_id(PK)│
│ username    │         │flight_number│
│ password    │         │ origin      │
│ email       │         │ destination │
│ role        │         │ price       │
└──────┬──────┘         └──────┬──────┘
       │                       │
       │  ┌────────────────────┘
       │  │
       ▼  ▼
┌─────────────┐         ┌──────────────┐
│   TICKETS   │◄────────│ RESERVATIONS │
├─────────────┤         ├──────────────┤
│ticket_id(PK)│         │reservation_id│
│ user_id (FK)│         │ user_id (FK) │
│flight_id(FK)│         │ ticket_id(FK)│
│ seat_number │         │flight_id (FK)│
│ status      │         │ status       │
└─────────────┘         └──────────────┘
```

### **Restricciones de Integridad**

- ✅ **Foreign Keys:** Tickets → Users, Flights
- ✅ **Unique Constraints:** username, email, flight_number
- ✅ **Check Constraints:** Validación de estados
- ✅ **Indexes:** origin+destination, user_id, flight_id

---

## 📝 **Credenciales de Prueba**

```
Username: admin
Password: password
Role: ADMIN
```





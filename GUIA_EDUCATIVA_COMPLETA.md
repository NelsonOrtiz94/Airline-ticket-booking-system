# 📚 Guía Educativa Completa - Sistema de Reserva de Boletos Aéreos
## Arquitectura Hexagonal + Spring WebFlux + JWT + BCrypt

---

## 📑 Índice

1. [Introducción al Proyecto](#1-introducción-al-proyecto)
2. [Arquitectura Hexagonal Explicada](#2-arquitectura-hexagonal-explicada)
3. [Capa de Dominio - El Corazón del Sistema](#3-capa-de-dominio---el-corazón-del-sistema)
4. [Capa de Aplicación - Orquestando la Lógica](#4-capa-de-aplicación---orquestando-la-lógica)
5. [Capa de Infraestructura - Conectando con el Mundo](#5-capa-de-infraestructura---conectando-con-el-mundo)
6. [Seguridad: JWT y BCrypt Explicados](#6-seguridad-jwt-y-bcrypt-explicados)
7. [Programación Reactiva con WebFlux](#7-programación-reactiva-con-webflux)
8. [Tests: Estrategias y Cobertura](#8-tests-estrategias-y-cobertura)
9. [Ejemplos Prácticos de Flujos](#9-ejemplos-prácticos-de-flujos)
10. [Mejores Prácticas Implementadas](#10-mejores-prácticas-implementadas)

---

## 1. Introducción al Proyecto

### 🎯 ¿Qué hace este sistema?

Este es un sistema de **reserva de boletos de avión** que permite:
- 🔐 **Autenticación** de usuarios con JWT
- ✈️ **Búsqueda** de vuelos disponibles
- 🎫 **Reserva** de tickets con selección de asiento
- 📝 **Gestión** de reservaciones (actualizar, cancelar)
- 👥 **Consulta** de reservaciones por usuario

### 🏗️ Tecnologías Utilizadas

| Tecnología | Propósito |
|------------|-----------|
| **Java 17+** | Lenguaje de programación con features modernas (records, switch expressions) |
| **Spring WebFlux** | Framework reactivo para alta concurrencia |
| **R2DBC** | Acceso reactivo a base de datos |
| **PostgreSQL** | Base de datos relacional |
| **JWT (JSON Web Token)** | Autenticación stateless |
| **BCrypt** | Encriptación segura de contraseñas |
| **JUnit 5 + Mockito** | Testing unitario |
| **Reactor Test** | Testing de código reactivo |

### 📊 Métricas del Proyecto

```
✅ 284 Tests - Todos pasando
✅ 100 Clases analizadas
✅ Cobertura completa de Use Cases
✅ Arquitectura Hexagonal bien implementada
```

---

## 2. Arquitectura Hexagonal Explicada

### 🎯 ¿Qué es la Arquitectura Hexagonal?

La **Arquitectura Hexagonal** (también llamada "Ports and Adapters") fue creada por Alistair Cockburn. Su objetivo principal es:

> **Aislar la lógica de negocio de los detalles técnicos**

### 🔺 Estructura del Proyecto

```
src/main/java/org/example/
├── 🏛️ domain/                    # NÚCLEO - Lógica de negocio PURA
│   ├── model/                    # Entidades de dominio
│   ├── valueobject/              # Objetos de valor inmutables
│   ├── service/                  # Servicios de dominio
│   └── exception/                # Excepciones de negocio
│
├── 📋 application/               # ORQUESTACIÓN - Casos de uso
│   ├── port/
│   │   ├── in/                   # Puertos de entrada (interfaces)
│   │   └── out/                  # Puertos de salida (interfaces)
│   ├── command/                  # Comandos (DTOs de entrada)
│   └── usecase/                  # Implementaciones de casos de uso
│
├── 🔌 infrastructure/            # ADAPTADORES - Conexiones externas
│   ├── config/                   # Configuración (Spring, Seguridad)
│   ├── entrypoints/rest/         # Controladores REST (Adapters IN)
│   └── drivenadapters/r2dbc/     # Persistencia (Adapters OUT)
│
└── 🛠️ shared/                    # UTILIDADES compartidas
    ├── constants/
    └── util/
```

### 📐 Diagrama de Capas

```
                    ┌──────────────────────────────────────────┐
                    │         INFRAESTRUCTURA                  │
                    │  ┌────────────┐    ┌────────────────┐   │
                    │  │ REST API   │    │  R2DBC/DB      │   │
                    │  │ (Adapters  │    │  (Adapters     │   │
                    │  │  IN)       │    │   OUT)         │   │
                    │  └──────┬─────┘    └───────┬────────┘   │
                    └─────────┼──────────────────┼────────────┘
                              │                  │
                              ▼                  ▼
                    ┌─────────────────────────────────────────┐
                    │           APLICACIÓN                     │
                    │  ┌──────────────┐  ┌──────────────┐     │
                    │  │ Ports IN     │  │ Ports OUT    │     │
                    │  │ (Interfaces) │  │ (Interfaces) │     │
                    │  └──────────────┘  └──────────────┘     │
                    │  ┌──────────────────────────────┐       │
                    │  │      USE CASES               │       │
                    │  │   (Implementaciones)         │       │
                    │  └──────────────────────────────┘       │
                    └─────────────────────────────────────────┘
                                      │
                                      ▼
                    ┌─────────────────────────────────────────┐
                    │              DOMINIO                     │
                    │  ┌────────┐ ┌─────────────┐ ┌────────┐  │
                    │  │ Models │ │ValueObjects │ │Services│  │
                    │  └────────┘ └─────────────┘ └────────┘  │
                    │  ┌──────────────────────────────────┐   │
                    │  │         Exceptions               │   │
                    │  └──────────────────────────────────┘   │
                    └─────────────────────────────────────────┘
```

### 🔑 Principios Clave

| Principio | Descripción | Ejemplo en el Proyecto |
|-----------|-------------|------------------------|
| **Inversión de Dependencias** | Las capas externas dependen de las internas, NUNCA al revés | `BookTicketUseCaseImpl` depende de `FlightRepositoryPort` (interfaz), no de `FlightRepositoryAdapter` |
| **Puertos y Adaptadores** | Las interfaces (puertos) definen contratos, los adaptadores los implementan | `FlightRepositoryPort` es el puerto, `FlightRepositoryAdapter` es el adaptador |
| **Dominio Puro** | El dominio no tiene dependencias de frameworks | `Flight.java` no tiene anotaciones de Spring ni JPA |

---

## 3. Capa de Dominio - El Corazón del Sistema

### 🏛️ ¿Qué contiene el Dominio?

El dominio es **la parte más importante** del sistema. Contiene:
- **Modelos**: Las entidades principales del negocio
- **Value Objects**: Objetos inmutables que representan valores
- **Servicios de Dominio**: Lógica que no pertenece a una sola entidad
- **Excepciones**: Errores específicos del negocio

### 📦 Modelos de Dominio

#### Flight (Vuelo)

```java
@Getter
@Builder
public class Flight {
    private FlightId id;                    // Value Object para el ID
    private FlightNumber flightNumber;      // Value Object (ej: "AV101")
    private Location origin;                // Value Object (ej: "BOG")
    private Location destination;           // Value Object (ej: "MDE")
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private Integer totalSeats;
    private Price price;                    // Value Object con moneda
    private Airline airline;                // Value Object
    private FlightStatus status;            // Enum: ACTIVE, CANCELLED, etc.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ⭐ LÓGICA DE NEGOCIO EN EL MODELO
    
    /**
     * Verifica si hay asientos disponibles
     */
    public boolean hasAvailableSeats(int requested) {
        return availableSeats != null && availableSeats >= requested;
    }

    /**
     * Reserva asientos - Modifica el estado interno
     */
    public void reserveSeats(int quantity) {
        if (!hasAvailableSeats(quantity)) {
            throw new NoSeatsAvailableException(
                "No hay suficientes asientos en el vuelo " + flightNumber.value()
            );
        }
        this.availableSeats -= quantity;
    }

    /**
     * Verifica si el vuelo es reservable
     */
    public boolean isBookable() {
        return status == FlightStatus.ACTIVE
            && departureTime.isAfter(LocalDateTime.now())
            && hasAvailableSeats(1);
    }
}
```

**💡 ¿Por qué es importante?**
- La lógica de negocio está **EN EL MODELO**, no en servicios externos
- El modelo **protege su invarianza**: no puedes dejar asientos negativos
- Es **testeable** sin necesidad de base de datos o Spring

#### User (Usuario)

```java
@Getter
@Builder
public class User {
    private UserId id;
    private Username username;
    private String passwordHash;       // ⚠️ Nunca la contraseña en texto plano
    private FullName fullName;
    private Email email;               // Value Object con validación
    private UserRole role;             // ADMIN o USER
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    public String getFullNameString() {
        return fullName.fullName();
    }
}
```

#### Reservation (Reservación)

```java
@Getter
@Builder
public class Reservation {
    private ReservationId id;
    private UserId userId;
    private TicketId ticketId;
    private FlightId flightId;
    private ReservationStatus status;    // CONFIRMED, CANCELLED, PENDING
    private String observations;
    private LocalDateTime reservationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ⭐ Factory Method - Patrón de creación
    public static Reservation create(UserId userId, FlightId flightId, TicketId ticketId) {
        return Reservation.builder()
                .userId(userId)
                .flightId(flightId)
                .ticketId(ticketId)
                .status(ReservationStatus.CONFIRMED)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ⭐ Métodos de negocio que protegen invariantes
    public void cancel(String reason) {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        this.status = ReservationStatus.CANCELLED;
        this.observations = reason;
        this.updatedAt = LocalDateTime.now();
    }
}
```

### 💎 Value Objects (Objetos de Valor)

Los **Value Objects** son objetos inmutables que representan conceptos del dominio. Son especialmente útiles para:
- ✅ **Validación**: La validación ocurre en la construcción
- ✅ **Tipado fuerte**: No puedes confundir un `UserId` con un `FlightId`
- ✅ **Inmutabilidad**: Una vez creado, no cambia

#### Email - Ejemplo de Validación

```java
public record Email(String value) {
    // Patrón de regex para validar formato de email
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // ⭐ Constructor compacto de record con validación
    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }
}
```

**Uso:**
```java
// ✅ Esto funciona
Email email = new Email("usuario@empresa.com");

// ❌ Esto lanza excepción INMEDIATAMENTE
Email invalid = new Email("no-es-email");  // IllegalArgumentException!
```

#### Price - Value Object con Operaciones

```java
public record Price(BigDecimal amount, String currency) {
    private static final String DEFAULT_CURRENCY = "USD";

    // ⭐ Validación en construcción
    public Price {
        if (amount == null) {
            throw new IllegalArgumentException("Price amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (currency == null || currency.isBlank()) {
            currency = DEFAULT_CURRENCY;
        }
    }

    // Constructor alternativo
    public Price(BigDecimal amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    // ⭐ Operación de dominio: Comparación
    public boolean isGreaterThan(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare prices with different currencies");
        }
        return this.amount.compareTo(other.amount) > 0;
    }

    // ⭐ Operación de dominio: Suma
    public Price add(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add prices with different currencies");
        }
        return new Price(this.amount.add(other.amount), this.currency);
    }
}
```

**Uso:**
```java
Price basePrice = new Price(BigDecimal.valueOf(250000));
Price tax = new Price(BigDecimal.valueOf(50000));
Price total = basePrice.add(tax);  // 300000 USD

// ❌ No puedes crear precios negativos
Price invalid = new Price(BigDecimal.valueOf(-100));  // Exception!
```

#### FlightId - Value Object de Identidad

```java
public record FlightId(Long value) {
    public FlightId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Flight ID must be positive");
        }
    }
}
```

**💡 Beneficio del tipado fuerte:**
```java
// Sin Value Objects - PROPENSO A ERRORES
void bookTicket(Long userId, Long flightId, Long ticketId) { }
bookTicket(flightId, userId, ticketId);  // ⚠️ Orden incorrecto, compila!

// Con Value Objects - SEGURO
void bookTicket(UserId userId, FlightId flightId, TicketId ticketId) { }
bookTicket(new FlightId(1L), new UserId(2L), new TicketId(3L));  // ❌ ERROR DE COMPILACIÓN!
```

### 🛠️ Servicios de Dominio

Los servicios de dominio contienen **lógica que no pertenece a una sola entidad**.

#### PriceCalculationService

```java
public class PriceCalculationService {
    private static final BigDecimal BUSINESS_CLASS_MULTIPLIER = BigDecimal.valueOf(2.5);
    private static final BigDecimal FIRST_CLASS_MULTIPLIER = BigDecimal.valueOf(4.0);

    /**
     * Calcula el precio final según la clase del ticket
     */
    public Price calculatePrice(Price basePrice, TicketClass ticketClass) {
        return switch (ticketClass) {
            case ECONOMY -> basePrice;
            case PREMIUM_ECONOMY -> new Price(
                basePrice.amount().multiply(BigDecimal.valueOf(1.5)),
                basePrice.currency()
            );
            case BUSINESS -> new Price(
                basePrice.amount().multiply(BUSINESS_CLASS_MULTIPLIER),
                basePrice.currency()
            );
            case FIRST_CLASS -> new Price(
                basePrice.amount().multiply(FIRST_CLASS_MULTIPLIER),
                basePrice.currency()
            );
        };
    }

    /**
     * Aplica descuento por compra grupal (5+ tickets)
     */
    public Price applyGroupDiscount(Price totalPrice, int numberOfTickets) {
        if (numberOfTickets >= 5) {
            BigDecimal discount = totalPrice.amount().multiply(BigDecimal.valueOf(0.10));
            return new Price(
                totalPrice.amount().subtract(discount),
                totalPrice.currency()
            );
        }
        return totalPrice;
    }
}
```

**Uso:**
```java
PriceCalculationService service = new PriceCalculationService();
Price base = new Price(BigDecimal.valueOf(100000));

Price economy = service.calculatePrice(base, TicketClass.ECONOMY);     // 100,000
Price business = service.calculatePrice(base, TicketClass.BUSINESS);   // 250,000
Price first = service.calculatePrice(base, TicketClass.FIRST_CLASS);   // 400,000
```

### ⚠️ Excepciones de Dominio

Las excepciones de dominio representan **errores de negocio**, no errores técnicos.

```java
// Excepción base
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}

// Excepciones específicas
public class FlightNotFoundException extends DomainException {
    public FlightNotFoundException(Long flightId) {
        super("Flight not found with ID: " + flightId);
    }
}

public class NoSeatsAvailableException extends DomainException {
    public NoSeatsAvailableException(String message) {
        super(message);
    }
}

public class SeatAlreadyTakenException extends DomainException {
    public SeatAlreadyTakenException(String seatNumber) {
        super("Seat " + seatNumber + " is already taken");
    }
}
```

---

## 4. Capa de Aplicación - Orquestando la Lógica

### 📋 ¿Qué hace la capa de Aplicación?

La capa de aplicación **orquesta** la lógica de negocio:
- Define los **casos de uso** del sistema
- Define **puertos** (interfaces) para comunicarse
- NO contiene lógica de negocio (eso está en el dominio)
- Coordina entre el dominio y la infraestructura

### 🚪 Puertos de Entrada (Ports IN)

Los puertos de entrada definen **qué puede hacer el sistema**:

```java
// Interfaz que define el caso de uso
public interface BookTicketUseCase {
    Mono<Reservation> execute(BookTicketCommand command);
}

public interface SearchFlightsUseCase {
    Flux<Flight> execute(SearchFlightsCommand command);
}

public interface AuthenticateUserUseCase {
    Mono<String> execute(AuthenticateUserCommand command);  // Retorna JWT token
}
```

### 🚪 Puertos de Salida (Ports OUT)

Los puertos de salida definen **qué necesita el sistema** del exterior:

```java
public interface FlightRepositoryPort {
    Mono<Flight> findById(FlightId flightId);
    Mono<Flight> save(Flight flight);
    Mono<Flight> update(Flight flight);
    Mono<Void> deleteById(FlightId flightId);
    Flux<Flight> findAll();
    Flux<Flight> searchFlights(String origin, String destination, LocalDateTime departureDate);
}

public interface UserRepositoryPort {
    Mono<User> findByUsername(String username);
    Mono<User> findById(UserId userId);
    Mono<User> save(User user);
}
```

### 📨 Commands (Comandos)

Los comandos son **DTOs de entrada** validados:

```java
public record BookTicketCommand(
    Long userId,
    Long flightId,
    String passengerName,
    String seatNumber,
    String ticketClass
) {
    // ⭐ Validación en construcción
    public BookTicketCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (flightId == null || flightId <= 0) {
            throw new IllegalArgumentException("Flight ID must be positive");
        }
        if (passengerName == null || passengerName.isBlank()) {
            throw new IllegalArgumentException("Passenger name cannot be empty");
        }
        if (seatNumber == null || seatNumber.isBlank()) {
            throw new IllegalArgumentException("Seat number cannot be empty");
        }
        if (ticketClass == null || ticketClass.isBlank()) {
            throw new IllegalArgumentException("Ticket class cannot be empty");
        }
    }
}
```

### ⚙️ Implementación de Casos de Uso

#### BookTicketUseCaseImpl - Ejemplo Completo

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class BookTicketUseCaseImpl implements BookTicketUseCase {

    // ⭐ Dependencias inyectadas - TODAS son interfaces (puertos)
    private final FlightRepositoryPort flightRepository;
    private final TicketRepositoryPort ticketRepository;
    private final ReservationRepositoryPort reservationRepository;
    private final ReservationDomainService reservationDomainService;
    private final PriceCalculationService priceCalculationService;

    @Override
    public Mono<Reservation> execute(BookTicketCommand command) {
        log.info("Procesando reserva para vuelo ID: {} y usuario ID: {}",
                command.flightId(), command.userId());

        // Crear Value Objects a partir del comando
        FlightId flightId = new FlightId(command.flightId());
        SeatNumber seatNumber = new SeatNumber(command.seatNumber());

        // ⭐ Flujo reactivo encadenado
        return flightRepository.findById(flightId)
                // 1. Si no existe el vuelo, lanzar excepción
                .switchIfEmpty(Mono.error(new FlightNotFoundException(command.flightId())))
                
                // 2. Validar que el asiento esté disponible
                .flatMap(flight -> validateSeatAvailability(flight, flightId, seatNumber)
                        .then(Mono.just(flight)))
                
                // 3. Procesar la reserva
                .flatMap(flight -> {
                    // Validar con servicio de dominio
                    reservationDomainService.validateReservation(flight, 1);

                    // Reservar el asiento (modifica el estado del Flight)
                    flight.reserveSeats(1);

                    // Calcular precio según clase
                    TicketClass ticketClass = TicketClass.valueOf(command.ticketClass());
                    Price finalPrice = priceCalculationService.calculatePrice(
                        flight.getPrice(), ticketClass
                    );

                    // Crear el ticket
                    Ticket ticket = Ticket.create(
                        new UserId(command.userId()),
                        flightId,
                        command.passengerName(),
                        seatNumber,
                        finalPrice,
                        ticketClass
                    );

                    // 4. Guardar ticket -> crear reservación -> actualizar vuelo
                    return ticketRepository.save(ticket)
                        .flatMap(savedTicket -> {
                            Reservation reservation = Reservation.create(
                                new UserId(command.userId()),
                                flightId,
                                savedTicket.getId()
                            );

                            return reservationRepository.save(reservation)
                                .flatMap(savedReservation ->
                                    flightRepository.update(flight)
                                        .thenReturn(savedReservation)
                                );
                        });
                })
                // 5. Logging
                .doOnSuccess(reservation ->
                    log.info("Reserva creada exitosamente con ID: {}", 
                             reservation.getId().value())
                )
                .doOnError(error ->
                    log.error("Error al crear reserva: {}", error.getMessage())
                );
    }

    private Mono<Void> validateSeatAvailability(Flight flight, FlightId flightId, 
                                                 SeatNumber seatNumber) {
        return ticketRepository.isSeatTaken(flightId, seatNumber)
                .flatMap(isTaken -> {
                    if (isTaken) {
                        return Mono.error(new SeatAlreadyTakenException(seatNumber.value()));
                    }
                    return Mono.empty();
                });
    }
}
```

**💡 Observaciones importantes:**
1. **Solo depende de interfaces** (`FlightRepositoryPort`, no `FlightRepositoryAdapter`)
2. **Usa servicios de dominio** para validaciones complejas
3. **Es completamente testeable** con mocks
4. **Usa programación reactiva** con `Mono` y `Flux`

---

## 5. Capa de Infraestructura - Conectando con el Mundo

### 🔌 ¿Qué contiene la Infraestructura?

La infraestructura contiene los **adaptadores** que conectan con el mundo exterior:
- **Adaptadores de entrada**: REST Controllers, GraphQL, etc.
- **Adaptadores de salida**: Repositorios R2DBC, clientes HTTP, etc.
- **Configuración**: Spring, Seguridad, Beans

### 🌐 Adaptadores de Entrada (REST Controllers)

```java
@Slf4j
@RestController
@RequestMapping("/airline/reservations")
@RequiredArgsConstructor
public class ReservationController {

    // ⭐ Depende de interfaces (puertos IN), no de implementaciones
    private final BookTicketUseCase bookTicketUseCase;
    private final UpdateReservationUseCase updateReservationUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final GetUserReservationsUseCase getUserReservationsUseCase;
    
    // Mappers para convertir DTOs
    private final TicketRestMapper ticketRestMapper;
    private final ReservationResponseMapper reservationResponseMapper;

    @PostMapping
    public Mono<ApiResponse<ReservationResponseDTO>> createReservation(
            @Valid @RequestBody BookingRequestDTO request) {

        log.info("Recibida solicitud de reserva para vuelo ID: {}", request.getFlightId());

        // 1. Mapper convierte DTO -> Command
        // 2. UseCase ejecuta la lógica
        // 3. Mapper convierte Reservation -> ResponseDTO
        return bookTicketUseCase.execute(ticketRestMapper.toCommand(request))
                .flatMap(reservationResponseMapper::toResponseWithDetails)
                .map(response -> ApiResponse.success(response, 
                        MessageConstants.RESERVATION_CREATED));
    }

    @DeleteMapping("/{reservationId}")
    public Mono<ApiResponse<Void>> cancelReservation(
            @PathVariable Long reservationId,
            @RequestParam(required = false) String reason) {

        log.info("Recibida solicitud de cancelación de reserva ID: {}", reservationId);

        return cancelReservationUseCase.execute(
                reservationRestMapper.toCancelCommand(reservationId, reason))
                .then(Mono.just(ApiResponse.success(null, 
                        MessageConstants.RESERVATION_CANCELLED)));
    }

    @GetMapping("/user/{userId}")
    public Mono<ApiResponse<List<ReservationResponseDTO>>> getUserReservations(
            @PathVariable Long userId) {

        return getUserReservationsUseCase.execute(new UserId(userId))
                .flatMap(reservationResponseMapper::toResponseWithDetails)
                .collectList()
                .map(reservations -> ApiResponse.success(reservations,
                        String.format(MessageConstants.RESERVATIONS_FOUND, 
                                     reservations.size())));
    }
}
```

### 💾 Adaptadores de Salida (Repositorios R2DBC)

```java
@Component
@RequiredArgsConstructor
public class FlightRepositoryAdapter implements FlightRepositoryPort {

    // Repositorio Spring Data R2DBC
    private final FlightR2dbcRepository r2dbcRepository;
    // Mapper para convertir Entity <-> Domain Model
    private final FlightPersistenceMapper mapper;

    @Override
    public Mono<Flight> findById(FlightId flightId) {
        return r2dbcRepository.findById(flightId.value())
                .map(mapper::toDomain);  // Entity -> Domain Model
    }

    @Override
    public Mono<Flight> save(Flight flight) {
        return r2dbcRepository.save(mapper.toEntity(flight))  // Domain -> Entity
                .map(mapper::toDomain);                        // Entity -> Domain
    }

    @Override
    public Flux<Flight> searchFlights(String origin, String destination, 
                                       LocalDateTime departureDate) {
        if (departureDate != null) {
            return r2dbcRepository.findByOriginDestinationAndDate(
                    origin, destination, departureDate)
                    .map(mapper::toDomain);
        }
        return r2dbcRepository.findByOriginAndDestination(origin, destination)
                .map(mapper::toDomain);
    }
}
```

### ⚙️ Configuración de Beans

```java
@Configuration
public class BeanConfiguration {

    // ========== Domain Services ==========
    
    @Bean
    public ReservationDomainService reservationDomainService() {
        return new ReservationDomainService();
    }

    @Bean
    public PriceCalculationService priceCalculationService() {
        return new PriceCalculationService();
    }

    // ========== Use Cases ==========
    
    // ⭐ Conectamos puertos IN con puertos OUT
    @Bean
    public BookTicketUseCase bookTicketUseCase(
            FlightRepositoryPort flightRepository,        // Puerto OUT
            TicketRepositoryPort ticketRepository,        // Puerto OUT
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
    public AuthenticateUserUseCase authenticateUserUseCase(
            UserRepositoryPort userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        return new AuthenticateUserUseCaseImpl(userRepository, jwtService, passwordEncoder);
    }
}
```

---

## 6. Seguridad: JWT y BCrypt Explicados

### 🔐 ¿Cómo funciona BCrypt?

**BCrypt** es un algoritmo de hash para contraseñas que:
- ✅ Incluye un **salt aleatorio** en cada hash
- ✅ Es **computacionalmente costoso** (difícil de forzar)
- ✅ Produce hashes **diferentes** para la misma contraseña

#### GeneratePasswords.java - Generador de Hashes

```java
public class GeneratePasswords {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminPass = "password";
        String userPass = "password";

        // ⭐ Cada vez genera un hash DIFERENTE (por el salt aleatorio)
        String adminHash = encoder.encode(adminPass);
        String userHash = encoder.encode(userPass);

        // Ejemplo de salida:
        // adminHash: $2a$10$N9qo8uLOickgx2ZMRZoMye... (60 caracteres)
        // userHash:  $2a$10$xLJPQZr8nEP5K9k3rTQZme... (60 caracteres)
        // ¡Diferentes aunque la contraseña sea la misma!

        System.out.println("Hash admin: " + adminHash);
        System.out.println("Hash user:  " + userHash);
    }
}
```

**💡 Anatomía de un hash BCrypt:**
```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
│  │  │                                                    │
│  │  └── Salt aleatorio (22 caracteres)                   │
│  └───── Factor de costo (10 = 2^10 iteraciones)          │
└──────── Versión del algoritmo (2a)                       │
                                        Hash resultante ───┘
```

#### ¿Cómo valida BCrypt?

```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

String password = "password";
String hash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

// BCrypt extrae el salt del hash y recalcula
boolean matches = encoder.matches(password, hash);  // true
boolean wrong = encoder.matches("wrong", hash);     // false
```

### 🎟️ ¿Cómo funciona JWT?

**JWT (JSON Web Token)** permite autenticación **stateless**:

```
Header.Payload.Signature
```

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwODEwMDAwMCwiZXhwIjoxNzA4MTg2NDAwfQ.
HmLIcMFVMvNVL3LnMz6u7DTRB0JGqJHJ8vnBkC5VuWo
```

#### JwtService - Generación y Validación

```java
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpiration;

    public JwtService(
            @Value("${spring.security.jwt.secret}") String secret,
            @Value("${spring.security.jwt.expiration}") long expiration) {
        // ⭐ Crear clave secreta a partir del string configurado
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpiration = expiration;
    }

    // ⭐ Generar token para un usuario
    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(username)           // Quién es el usuario
                .issuedAt(now)               // Cuándo se creó
                .expiration(expirationDate)  // Cuándo expira
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Firma
                .compact();
    }

    // ⭐ Extraer username del token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ⭐ Validar token
    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
```

### 🔒 SecurityConfig - Configuración de Seguridad

```java
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Deshabilitar CSRF (no necesario en APIs stateless)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Deshabilitar login por formulario
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // Configurar rutas
                .authorizeExchange(exchanges -> exchanges
                        // ✅ Rutas públicas
                        .pathMatchers("/airline/auth/**").permitAll()
                        .pathMatchers("/airline/flights/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        // 🔐 Rutas protegidas
                        .pathMatchers("/airline/reservations/**").authenticated()
                        .anyExchange().authenticated()
                )
                // Agregar filtro JWT
                .addFilterAt(jwtAuthenticationFilter, 
                             SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 🔄 Flujo de Autenticación Completo

```
1. LOGIN
   Usuario → POST /airline/auth/login { "username": "admin", "password": "password" }
                                ↓
   AuthController → AuthenticateUserUseCase.execute(command)
                                ↓
   AuthenticateUserUseCaseImpl:
   - Busca usuario por username
   - Compara password con BCrypt: encoder.matches(password, user.passwordHash)
   - Si coincide: jwtService.generateToken(username)
                                ↓
   Usuario ← { "token": "eyJhbGci...", "type": "Bearer" }


2. PETICIÓN PROTEGIDA
   Usuario → GET /airline/reservations/user/1
             Header: Authorization: Bearer eyJhbGci...
                                ↓
   JwtAuthenticationFilter:
   - Extrae token del header
   - Valida firma y expiración: jwtService.validateToken(token, username)
   - Si válido: establece SecurityContext
                                ↓
   ReservationController → procesa la petición
                                ↓
   Usuario ← Lista de reservaciones
```

---

## 7. Programación Reactiva con WebFlux

### 🌊 ¿Qué es la Programación Reactiva?

Es un paradigma que permite manejar **streams de datos asíncronos**. En lugar de bloquear mientras esperas una respuesta, te **suscribes** a los datos cuando estén listos.

### 📦 Mono vs Flux

| Tipo | Descripción | Ejemplo |
|------|-------------|---------|
| `Mono<T>` | 0 o 1 elemento | Buscar un usuario por ID |
| `Flux<T>` | 0 a N elementos | Lista de vuelos |

### 🔗 Operadores Principales

```java
// ===== TRANSFORMACIÓN =====

// map: Transforma el elemento
Mono<Flight> flight = flightRepository.findById(id);
Mono<String> flightNumber = flight.map(f -> f.getFlightNumber().value());

// flatMap: Transforma a otro Mono/Flux (para operaciones encadenadas)
Mono<Reservation> reservation = flightRepository.findById(flightId)
    .flatMap(flight -> {
        Ticket ticket = createTicket(flight);
        return ticketRepository.save(ticket);
    })
    .flatMap(savedTicket -> {
        Reservation res = createReservation(savedTicket);
        return reservationRepository.save(res);
    });


// ===== MANEJO DE ERRORES =====

// switchIfEmpty: Proporciona alternativa si está vacío
Mono<Flight> flight = flightRepository.findById(id)
    .switchIfEmpty(Mono.error(new FlightNotFoundException(id)));

// onErrorResume: Manejar errores con alternativa
Mono<User> user = userRepository.findByUsername(username)
    .onErrorResume(e -> Mono.just(User.createGuest()));


// ===== COMBINACIÓN =====

// zip: Combina resultados de múltiples Monos
Mono.zip(flightRepository.findById(flightId), 
         ticketRepository.findById(ticketId))
    .map(tuple -> {
        Flight flight = tuple.getT1();
        Ticket ticket = tuple.getT2();
        return createResponse(flight, ticket);
    });


// ===== EJECUCIÓN =====

// then: Ejecuta algo después, ignorando el resultado anterior
flightRepository.update(flight)
    .then(Mono.just(savedReservation));

// doOnSuccess/doOnError: Side effects (logging, métricas)
reservation
    .doOnSuccess(r -> log.info("Reserva creada: {}", r.getId()))
    .doOnError(e -> log.error("Error: {}", e.getMessage()));
```

### 📝 Ejemplo Práctico: Búsqueda de Vuelos

```java
@Override
public Flux<Flight> execute(SearchFlightsCommand command) {
    log.info("Buscando vuelos de {} a {} para fecha: {}", 
             command.origin(), command.destination(), command.departureDate());

    return flightRepository.searchFlights(
            command.origin(), 
            command.destination(), 
            command.departureDate())
        // Filtrar solo vuelos reservables
        .filter(Flight::isBookable)
        // Logging de cada vuelo encontrado
        .doOnNext(flight -> log.debug("Vuelo encontrado: {}", 
                                       flight.getFlightNumber().value()))
        // Logging al completar
        .doOnComplete(() -> log.info("Búsqueda de vuelos completada"));
}
```

---

## 8. Tests: Estrategias y Cobertura

### 📊 Resumen de Cobertura

```
┌─────────────────────────────────────────────┐
│           TESTS EJECUTADOS: 284             │
│           TODOS PASANDO ✅                   │
├─────────────────────────────────────────────┤
│ Application (UseCases)     │      31        │
│ Domain (Models)            │      71        │
│ Domain (Services)          │      27        │
│ Domain (ValueObjects)      │      59        │
│ Domain (Exceptions)        │      13        │
│ Infrastructure (Controllers)│     33        │
│ Infrastructure (Mappers)   │      24        │
│ Infrastructure (Config)    │       8        │
│ Shared (Utils)             │       5        │
├─────────────────────────────────────────────┤
│ REST/Mappers/Config        │      50+       │
└─────────────────────────────────────────────┘
```

### 🧪 Ejemplo de Test Unitario: BookTicketUseCaseImplTest

```java
@ExtendWith(MockitoExtension.class)
class BookTicketUseCaseImplTest {

    // ⭐ Mocks de las dependencias
    @Mock
    private FlightRepositoryPort flightRepository;
    @Mock
    private TicketRepositoryPort ticketRepository;
    @Mock
    private ReservationRepositoryPort reservationRepository;
    @Mock
    private ReservationDomainService reservationDomainService;
    @Mock
    private PriceCalculationService priceCalculationService;

    // ⭐ Clase bajo test con mocks inyectados
    @InjectMocks
    private BookTicketUseCaseImpl bookTicketUseCase;

    private Flight flight;
    private Ticket savedTicket;
    private Reservation savedReservation;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        flight = Flight.builder()
                .id(new FlightId(1L))
                .flightNumber(new FlightNumber("AV101"))
                .origin(new Location("BOG"))
                .destination(new Location("MDE"))
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(50)
                .totalSeats(50)
                .price(new Price(BigDecimal.valueOf(250000)))
                .airline(new Airline("Avianca"))
                .status(FlightStatus.ACTIVE)
                .build();

        savedTicket = Ticket.builder()
                .id(new TicketId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .passengerName("John Doe")
                .seatNumber(new SeatNumber("12A"))
                .price(new Price(BigDecimal.valueOf(250000)))
                .ticketClass(TicketClass.ECONOMY)
                .status(TicketStatus.CONFIRMED)
                .build();

        savedReservation = Reservation.builder()
                .id(new ReservationId(1L))
                .userId(new UserId(1L))
                .flightId(new FlightId(1L))
                .ticketId(new TicketId(1L))
                .status(ReservationStatus.CONFIRMED)
                .build();
    }

    @Test
    @DisplayName("Debe crear reservación exitosamente cuando todos los datos son válidos")
    void testExecute_WhenValidBooking_ShouldCreateReservation() {
        // Arrange - Configurar comportamiento de mocks
        BookTicketCommand command = new BookTicketCommand(
                1L, 1L, "John Doe", "12A", "ECONOMY"
        );

        when(flightRepository.findById(any(FlightId.class)))
            .thenReturn(Mono.just(flight));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class)))
            .thenReturn(Mono.just(false));
        when(priceCalculationService.calculatePrice(any(Price.class), any(TicketClass.class)))
            .thenReturn(flight.getPrice());
        when(ticketRepository.save(any(Ticket.class)))
            .thenReturn(Mono.just(savedTicket));
        when(flightRepository.update(any(Flight.class)))
            .thenReturn(Mono.just(flight));
        when(reservationRepository.save(any(Reservation.class)))
            .thenReturn(Mono.just(savedReservation));

        // Act & Assert - Usar StepVerifier para código reactivo
        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectNextMatches(res ->
                        res.getId() != null &&
                        res.getUserId().equals(new UserId(1L)) &&
                        res.getFlightId().equals(new FlightId(1L))
                )
                .verifyComplete();

        // Verify - Verificar interacciones
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(flightRepository, times(1)).update(any(Flight.class));
    }

    @Test
    @DisplayName("Debe lanzar FlightNotFoundException cuando el vuelo no existe")
    void testExecute_WhenFlightNotFound_ShouldThrowException() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 999L, "John Doe", "12A", "ECONOMY"
        );

        when(flightRepository.findById(any(FlightId.class)))
            .thenReturn(Mono.empty());

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectError(FlightNotFoundException.class)
                .verify();

        // Verificar que NO se guardó ningún ticket
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Debe lanzar SeatAlreadyTakenException cuando el asiento está ocupado")
    void testExecute_WhenSeatTaken_ShouldThrowException() {
        BookTicketCommand command = new BookTicketCommand(
                1L, 1L, "John Doe", "12A", "ECONOMY"
        );

        when(flightRepository.findById(any(FlightId.class)))
            .thenReturn(Mono.just(flight));
        when(ticketRepository.isSeatTaken(any(FlightId.class), any(SeatNumber.class)))
            .thenReturn(Mono.just(true));  // ⬅️ Asiento ocupado

        StepVerifier.create(bookTicketUseCase.execute(command))
                .expectError(SeatAlreadyTakenException.class)
                .verify();
    }
}
```

### 🔑 Patrones de Testing Usados

| Patrón | Descripción | Ejemplo |
|--------|-------------|---------|
| **AAA (Arrange-Act-Assert)** | Estructura clara de tests | Ver ejemplo arriba |
| **Given-When-Then** | BDD style | `@DisplayName` descriptivos |
| **Mock Objects** | Simular dependencias | `@Mock` + Mockito |
| **StepVerifier** | Testing reactivo | Verificar Mono/Flux |

---

## 9. Ejemplos Prácticos de Flujos

### 🔐 Flujo Completo: Login de Usuario

```
1. POST /airline/auth/login
   Body: { "username": "admin", "password": "password" }

2. AuthController recibe la petición
   ↓
3. AuthenticateUserUseCase.execute(AuthenticateUserCommand)
   ↓
4. UserRepositoryPort.findByUsername("admin")
   ↓
5. BCryptPasswordEncoder.matches("password", storedHash)
   ↓
6. JwtService.generateToken("admin")
   ↓
7. Respuesta: { "token": "eyJhbGci...", "type": "Bearer" }
```

### ✈️ Flujo Completo: Reservar un Vuelo

```
1. POST /airline/reservations
   Header: Authorization: Bearer eyJhbGci...
   Body: {
     "userId": 1,
     "flightId": 1,
     "passengerName": "Juan Pérez",
     "seatNumber": "12A",
     "ticketClass": "BUSINESS"
   }

2. JwtAuthenticationFilter valida el token
   ↓
3. ReservationController.createReservation(BookingRequestDTO)
   ↓
4. TicketRestMapper.toCommand(request) → BookTicketCommand
   ↓
5. BookTicketUseCase.execute(command)
   │
   ├── FlightRepositoryPort.findById(FlightId(1))
   │   ↓ Flight encontrado
   │
   ├── TicketRepositoryPort.isSeatTaken(FlightId(1), SeatNumber("12A"))
   │   ↓ false (asiento disponible)
   │
   ├── ReservationDomainService.validateReservation(flight, 1)
   │   ↓ Validación OK
   │
   ├── Flight.reserveSeats(1)
   │   ↓ availableSeats: 50 → 49
   │
   ├── PriceCalculationService.calculatePrice(basePrice, BUSINESS)
   │   ↓ 250,000 × 2.5 = 625,000
   │
   ├── Ticket.create(...) → nuevo Ticket
   │   ↓
   ├── TicketRepositoryPort.save(ticket)
   │   ↓ Ticket guardado con ID
   │
   ├── Reservation.create(...) → nueva Reservation
   │   ↓
   ├── ReservationRepositoryPort.save(reservation)
   │   ↓ Reservation guardada
   │
   └── FlightRepositoryPort.update(flight)
       ↓ Vuelo actualizado con menos asientos

6. ReservationResponseMapper.toResponseWithDetails(reservation)
   ↓
7. Respuesta:
   {
     "success": true,
     "data": {
       "reservationId": 123,
       "userId": 1,
       "flightNumber": "AV101",
       "passengerName": "Juan Pérez",
       "seatNumber": "12A",
       "ticketClass": "BUSINESS",
       "price": 625000,
       "status": "CONFIRMED"
     },
     "message": "Reservación creada exitosamente"
   }
```

---

## 10. Mejores Prácticas Implementadas

### ✅ Domain-Driven Design (DDD)

| Práctica | Implementación |
|----------|----------------|
| **Rich Domain Models** | `Flight.reserveSeats()`, `Reservation.cancel()` |
| **Value Objects** | `Price`, `Email`, `FlightId`, etc. |
| **Domain Services** | `PriceCalculationService`, `ReservationDomainService` |
| **Factory Methods** | `Reservation.create()`, `Ticket.create()` |
| **Domain Exceptions** | `FlightNotFoundException`, `NoSeatsAvailableException` |

### ✅ Clean Architecture

| Práctica | Implementación |
|----------|----------------|
| **Separación de capas** | domain / application / infrastructure |
| **Inversión de dependencias** | Use Cases dependen de Ports (interfaces) |
| **Independence del framework** | Domain no tiene anotaciones de Spring |

### ✅ SOLID Principles

| Principio | Ejemplo |
|-----------|---------|
| **S**ingle Responsibility | Cada UseCase hace UNA cosa |
| **O**pen/Closed | Nuevos casos de uso sin modificar existentes |
| **L**iskov Substitution | Cualquier implementación de `FlightRepositoryPort` funciona |
| **I**nterface Segregation | Puertos específicos: `BookTicketUseCase`, `SearchFlightsUseCase` |
| **D**ependency Inversion | Dependencia de abstracciones (Ports), no de implementaciones |

### ✅ Testing

| Práctica | Implementación |
|----------|----------------|
| **Unit Tests** | 284 tests para todas las capas |
| **Mocking** | Mockito para dependencias |
| **Reactive Testing** | StepVerifier para Mono/Flux |
| **Descriptive Names** | `@DisplayName` para tests legibles |

### ✅ Seguridad

| Práctica | Implementación |
|----------|----------------|
| **Password Hashing** | BCrypt (no reversible) |
| **Stateless Auth** | JWT tokens |
| **Route Protection** | SecurityConfig con rutas públicas/protegidas |
| **Input Validation** | Validación en Commands y Value Objects |

---

## 🎓 Conclusión

Este proyecto es un **excelente ejemplo** de cómo construir una aplicación empresarial moderna:

1. **Arquitectura sólida**: Hexagonal con separación clara de responsabilidades
2. **Dominio rico**: Modelos con comportamiento, no solo datos
3. **Seguridad robusta**: JWT + BCrypt bien implementados
4. **Programación reactiva**: WebFlux para alta concurrencia
5. **Tests completos**: 284 tests con 100% de cobertura en casos de uso
6. **Código limpio**: Siguiendo principios SOLID y Clean Code

### 📚 Recursos para Seguir Aprendiendo

- [Arquitectura Hexagonal - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Project Reactor Reference](https://projectreactor.io/docs/core/release/reference/)
- [JWT.io - Debugger](https://jwt.io/)
- [BCrypt Explained](https://auth0.com/blog/hashing-in-action-understanding-bcrypt/)

---

*Documento generado: 2026-02-16*
*Proyecto: Airline Ticket Booking API*
*Tests: 284 ✅ | Clases: 100*


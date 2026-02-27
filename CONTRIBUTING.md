# 🤝 Guía de Contribución

## Bienvenido

Gracias por tu interés en contribuir al sistema de reserva de tickets de avión. Este documento proporciona las directrices para contribuir al proyecto.

---

## 📋 Tabla de Contenidos

- [Código de Conducta](#código-de-conducta)
- [¿Cómo puedo contribuir?](#cómo-puedo-contribuir)
- [Proceso de Desarrollo](#proceso-de-desarrollo)
- [Estándares de Código](#estándares-de-código)
- [Commits y Pull Requests](#commits-y-pull-requests)
- [Testing](#testing)

---

## 🤝 Código de Conducta

Este proyecto se adhiere a un código de conducta profesional:

- ✅ Sé respetuoso y constructivo
- ✅ Acepta críticas constructivas
- ✅ Enfócate en lo mejor para la comunidad
- ❌ No toleramos acoso ni lenguaje ofensivo

---

## 🚀 ¿Cómo puedo contribuir?

### Reportar Bugs

Antes de crear un issue:
1. Verifica que no exista un issue similar
2. Incluye información detallada:
   - Pasos para reproducir
   - Comportamiento esperado vs actual
   - Logs relevantes
   - Versión del proyecto

**Template de Bug:**
```markdown
**Descripción del Bug:**
[Descripción clara y concisa]

**Pasos para Reproducir:**
1. Paso 1
2. Paso 2
3. ...

**Comportamiento Esperado:**
[Qué debería pasar]

**Comportamiento Actual:**
[Qué está pasando]

**Logs:**
```
[Logs aquí]
```

**Entorno:**
- OS: [Windows/Linux/Mac]
- Java Version: [17]
- PostgreSQL Version: [15]
```

### Sugerir Mejoras

Las sugerencias de mejoras son bienvenidas:
- Nuevas características
- Mejoras de rendimiento
- Mejoras de documentación

---

## 🔧 Proceso de Desarrollo

### 1. Fork y Clone

```bash
# Fork el repositorio en GitHub
# Luego clona tu fork
git clone https://github.com/TU_USUARIO/AirplaneTicketBooking.git
cd AirplaneTicketBooking

# Agrega el repositorio original como upstream
git remote add upstream https://github.com/ORIGINAL_USUARIO/AirplaneTicketBooking.git
```

### 2. Crear una Rama

```bash
# Actualiza tu main
git checkout main
git pull upstream main

# Crea una rama descriptiva
git checkout -b feature/nombre-feature
# o
git checkout -b fix/nombre-bug
# o
git checkout -b refactor/nombre-refactor
```

**Convención de Nombres de Ramas:**
- `feature/` - Nueva funcionalidad
- `fix/` - Corrección de bug
- `refactor/` - Refactorización de código
- `docs/` - Cambios en documentación
- `test/` - Agregar o modificar tests

### 3. Hacer Cambios

```bash
# Haz tus cambios
# Ejecuta tests
./mvnw test

# Verifica cobertura (mínimo 50%)
./mvnw jacoco:report

# Verifica que compila
./mvnw clean install
```

### 4. Commit

```bash
git add .
git commit -m "tipo: descripción corta"
```

### 5. Push y Pull Request

```bash
git push origin feature/nombre-feature

# Crea PR en GitHub con descripción detallada
```

---

## 📝 Estándares de Código

### Clean Architecture

Este proyecto sigue **Clean Architecture**. Respeta la separación de capas:

```
Domain (Núcleo)
   ↑
Application (Use Cases)
   ↑
Infrastructure (Adapters)
```

**❌ No hagas:**
```java
// Domain NO debe depender de Infrastructure
public class Flight {
    @Entity // ❌ Anotación de JPA en Domain
    private Long id;
}
```

**✅ Haz:**
```java
// Domain puro
@Data
public class Flight {
    private Long flightId; // ✅ POJO puro
}

// Entidad de BD en Infrastructure
@Table("flights")
public class FlightEntity {
    @Id
    private Long id;
}
```

### Principios SOLID

- **S**ingle Responsibility Principle
- **O**pen/Closed Principle
- **L**iskov Substitution Principle
- **I**nterface Segregation Principle
- **D**ependency Inversion Principle

### Programación Funcional

Usa programación funcional cuando sea posible:

```java
// ✅ Bueno - Funcional
return flights.stream()
    .filter(Flight::isActive)
    .map(this::toResponse)
    .collect(Collectors.toList());

// ❌ Evitar - Imperativo
List<FlightResponse> result = new ArrayList<>();
for (Flight flight : flights) {
    if (flight.isActive()) {
        result.add(toResponse(flight));
    }
}
return result;
```

### Programación Reactiva

Usa Reactor correctamente:

```java
// ✅ Bueno
return flightRepository.findById(id)
    .switchIfEmpty(Mono.error(new FlightNotFoundException()))
    .flatMap(this::processBooking);

// ❌ Evitar - Bloquear flujo reactivo
Flight flight = flightRepository.findById(id).block(); // ❌
```

### Manejo de Excepciones

```java
// ✅ Excepciones personalizadas
throw new FlightNotFoundException("Vuelo no encontrado: " + id);

// ❌ Excepciones genéricas
throw new Exception("Error"); // ❌
```

### Logging

```java
// ✅ Usar SLF4J con niveles apropiados
log.info("Buscando vuelo con ID: {}", flightId);
log.error("Error al procesar reserva", exception);

// ❌ No usar System.out
System.out.println("Log"); // ❌
```

### Validaciones

```java
// ✅ Validaciones Jakarta
public record BookingRequest(
    @NotNull(message = "User ID requerido")
    Long userId,
    
    @Pattern(regexp = "^[A-Z0-9]{1,4}$")
    String seatNumber
) {}
```

### Formato de Código

- **Indentación:** 4 espacios (no tabs)
- **Línea máxima:** 120 caracteres
- **Imports:** Organizar y eliminar no usados
- **Naming:**
  - Clases: `PascalCase`
  - Métodos: `camelCase`
  - Constantes: `UPPER_SNAKE_CASE`
  - Paquetes: `lowercase`

---

## 📝 Commits y Pull Requests

### Formato de Commits

Usamos **Conventional Commits**:

```
tipo(alcance): descripción corta

Descripción detallada (opcional)

Fixes #123
```

**Tipos:**
- `feat`: Nueva característica
- `fix`: Corrección de bug
- `refactor`: Refactorización
- `test`: Agregar/modificar tests
- `docs`: Documentación
- `chore`: Tareas de mantenimiento
- `perf`: Mejoras de rendimiento

**Ejemplos:**
```bash
feat(reservations): agregar validación de asientos duplicados

fix(flights): corregir búsqueda por ciudad

refactor(domain): mejorar estructura de Flight entity

test(use-cases): agregar tests para BookTicketUseCase

docs(readme): actualizar guía de instalación

chore(deps): actualizar Spring Boot a 3.2.3
```

### Pull Request Template

```markdown
## Descripción
[Descripción clara de los cambios]

## Tipo de Cambio
- [ ] Bug fix
- [ ] Nueva feature
- [ ] Breaking change
- [ ] Documentación

## ¿Cómo se ha probado?
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Pruebas manuales

## Checklist
- [ ] El código sigue los estándares del proyecto
- [ ] Los tests pasan (mvn test)
- [ ] Cobertura >= 50%
- [ ] Documentación actualizada
- [ ] Sin warnings de compilación
- [ ] Commits siguen Conventional Commits

## Capturas (si aplica)
[Screenshots o logs]

## Issues Relacionados
Fixes #[issue_number]
```

---

## 🧪 Testing

### Cobertura Mínima

- **Requerido:** 50% de cobertura
- **Objetivo:** 80%+ en lógica de negocio

### Ejecutar Tests

```bash
# Todos los tests
./mvnw test

# Con reporte de cobertura
./mvnw clean test jacoco:report

# Ver reporte
# Abrir: target/site/jacoco/index.html
```

### Estructura de Tests

```java
@Test
@DisplayName("Debe lanzar excepción cuando no hay asientos")
void shouldThrowExceptionWhenNoSeats() {
    // Given (Arrange)
    Flight flight = Flight.builder()
        .availableSeats(0)
        .build();
    
    // When (Act)
    StepVerifier.create(useCase.execute(command))
        // Then (Assert)
        .expectError(NoSeatsAvailableException.class)
        .verify();
}
```

### Tipos de Tests Requeridos

1. **Tests Unitarios** (Domain y Application)
   - Sin dependencias externas
   - Usar Mocks para repositories

2. **Tests de Integración** (Infrastructure)
   - Con base de datos en memoria (si es necesario)
   - Con contexto de Spring

3. **Tests de Contratos** (Controllers)
   - Validar DTOs
   - Validar responses

---

## 🔒 Seguridad

### No Commitear:

- ❌ Credenciales o tokens
- ❌ Claves privadas
- ❌ Datos sensibles en logs
- ❌ Archivos de configuración local

### Usar:

- ✅ Variables de entorno
- ✅ application-local.yml en .gitignore
- ✅ Secrets management

---

## 📞 Contacto

Si tienes preguntas:
- Crea un issue con la etiqueta `question`
- Revisa la documentación en el README
- Revisa issues cerrados

---

## 📄 Licencia

Al contribuir, aceptas que tus contribuciones se licenciarán bajo la misma licencia del proyecto.

---

**¡Gracias por contribuir! 🚀**


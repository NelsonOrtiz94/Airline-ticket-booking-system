## 📝 Descripción

<!-- Describe tus cambios en detalle -->

## 🔗 Issue Relacionado

<!-- Si existe un issue, refiérelo aquí -->
Fixes #(issue)

## 🎯 Tipo de Cambio

<!-- Marca con una X las opciones que apliquen -->

- [ ] 🐛 Bug fix (cambio que corrige un problema)
- [ ] ✨ Nueva feature (cambio que agrega funcionalidad)
- [ ] 💥 Breaking change (fix o feature que causaría que funcionalidad existente no funcione como antes)
- [ ] 📝 Documentación
- [ ] ♻️ Refactorización
- [ ] 🧪 Tests
- [ ] 🔧 Configuración

## 🧪 ¿Cómo se ha probado?

<!-- Describe las pruebas que realizaste -->

- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Pruebas manuales con Postman
- [ ] Pruebas con Docker

**Casos de prueba:**
1. Caso 1: ...
2. Caso 2: ...

## 📋 Checklist

<!-- Marca con una X cuando hayas completado cada item -->

### Code Quality
- [ ] El código sigue los estándares del proyecto (Clean Architecture)
- [ ] He realizado una auto-revisión de mi código
- [ ] He comentado mi código, especialmente en áreas complejas
- [ ] Los cambios no generan nuevos warnings
- [ ] He eliminado código comentado y console.logs innecesarios

### Testing
- [ ] He agregado tests que prueban mi fix/feature
- [ ] Los tests nuevos y existentes pasan localmente (`mvn test`)
- [ ] La cobertura de código es >= 50% (`mvn jacoco:report`)

### Documentation
- [ ] He actualizado la documentación relacionada
- [ ] He actualizado el README si es necesario
- [ ] He agregado JavaDoc a métodos públicos

### Clean Architecture
- [ ] Las capas están correctamente separadas
- [ ] Domain NO depende de Infrastructure
- [ ] Use Cases están en Application layer
- [ ] Controllers están en Infrastructure layer

### Reactive Programming
- [ ] No uso `.block()` en el flujo reactivo
- [ ] Manejo correctamente los `Mono` y `Flux`
- [ ] Uso operadores reactivos apropiados

### Security
- [ ] No commiteo credenciales o secretos
- [ ] Valido correctamente los inputs
- [ ] Manejo apropiadamente las excepciones

### Database
- [ ] Las queries usan parámetros (proteción contra SQL Injection)
- [ ] He agregado/actualizado scripts de migración si es necesario
- [ ] He verificado que no hay N+1 queries

## 📸 Capturas de Pantalla (si aplica)

<!-- Agrega capturas de pantalla de la funcionalidad o del resultado de tests -->

## 🔍 Logs de Ejecución

```bash
# Pegar logs relevantes aquí
```

## 📊 Métricas

**Cobertura de Tests:**
- Anterior: XX%
- Nueva: XX%

**Performance (si aplica):**
- Tiempo de respuesta antes: XXms
- Tiempo de respuesta después: XXms

## 🚀 Deployment Notes

<!-- ¿Hay algo especial que deba considerarse al deployar? -->

- [ ] Requiere migración de BD
- [ ] Requiere variables de entorno nuevas
- [ ] Requiere actualización de documentación de API

## 📝 Notas Adicionales

<!-- Cualquier información adicional que los revisores deban saber -->

---

## ✅ Revisión Final

Confirmo que:
- [ ] He leído y seguido la [Guía de Contribución](../CONTRIBUTING.md)
- [ ] Mi código sigue el estilo del proyecto
- [ ] He probado exhaustivamente mis cambios
- [ ] Estoy listo para el code review

---

**Reviewers:** @username1 @username2


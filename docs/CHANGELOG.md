# Changelog

## [Unreleased]

### Added
- **[BACKEND]** Implementación completa del **CRUD de Auditoría** en Spring Boot.
  - Commit: `f7a6023`
  - Autor: jairoramos18
  - Fecha: 2025-06-07
  - Nuevos archivos:
    - `CrudAuditoriaController.java` – Controlador REST para operaciones CRUD.
    - `CrudAuditoriaRepository.java` – Repositorio JPA para Auditoría.
    - `CrudAuditoriaService.java` – Lógica de negocio del CRUD.
    - `CrudAuditoria.java` – Entidad de base de datos para auditorías.

### Frontend
- **[FRONTEND]** Creación de interfaz Angular para el CRUD de Auditoría.
  - Commit: `14e1c20`
  - Autor: jairoramos18
  - Fecha: 2025-06-08
  - Cambios relevantes:
    - Agregado componente `crud-auditoria` y su configuración en rutas.
    - Se integró el servicio HTTP `crud-auditoria.service.ts` para consumir el backend.
    - Ajustes menores en archivos de configuración del proyecto (`app.config.ts`, rutas, y componentes).

### Fixed
- Duplicado de entradas en configuración de compilador (`.idea/compiler.xml`) fue eliminado.

---

## Notas técnicas

- El backend ahora ofrece endpoints REST para:
  - Obtener todas las auditorías
  - Obtener auditorías por fecha
  - Crear, actualizar y eliminar auditorías
- El frontend implementa una tabla de auditorías y formularios para agregar/modificar registros.

# Sistema de Gestión de un Café-Bar - PDN

## Documentación del Backend

### Entorno y Herramientas Utilizadas

Para el desarrollo del backend se configuró el entorno teniendo en cuenta los siguientes requisitos:

- **Java 17**
- **Spring Boot** (Framework para el desarrollo backend)
- **IntelliJ IDEA** (IDE)
- **Maven** (Gestor de dependencias)
- **PostgreSQL** (Base de datos utilizada, gestionada mediante **PgAdmin**)

### Lenguajes y Frameworks

- **Java**: Lenguaje principal del backend.
- **Spring Boot**: Framework para desarrollo rápido y sencillo del backend.
- **PostgreSQL**: Base de datos relacional utilizada.
- **PgAdmin**: Herramienta para la administración y gestión de la base de datos PostgreSQL.

---

### Creación del Proyecto en Spring Boot

Se siguieron los siguientes pasos para la creación del proyecto:

1. Se utilizó **Spring Initializr** desde el siguiente enlace: [https://start.spring.io/](https://start.spring.io/)
2. Parámetros seleccionados:
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.4.3
   - **Java**: 17
   - **Dependencies**: Recomendadas por el profesor

---

### Estructura del Proyecto

Una vez generado el proyecto y abierto con **IntelliJ IDEA**, la estructura de carpetas es la siguiente:


demo/
├── src/
│ ├── main/
│ │ ├── java/com/example/app/
│ │ │com.example.demo/  
 ├── controller/ # Controladores REST
│ │ │ ├── DemoApplication/ # Clase principal de Spring Boot
│ │ ├── resources/
│ │ │ ├── application.properties # Configuración de Spring Boot
│ ├── test/ # Pruebas unitarias y de integración
├── pom.xml # Archivo de configuración de Maven

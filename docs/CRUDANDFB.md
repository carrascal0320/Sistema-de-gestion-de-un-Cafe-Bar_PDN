
### Funcionalidades Implementadas

### 1. CRUD de Entidades Clave

Se implementaron operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para las siguientes entidades:

- **Usuarios:** Administración de cuentas de usuarios.

### 2. Integración de Inicio de Sesión con Facebook mediante Firebase y OAuth2

Se integró el inicio de sesión utilizando cuentas de Facebook, aprovechando Firebase Authentication y el protocolo OAuth2.

**Pasos realizados:**

2. **Configuración en Facebook Developers:**
   - Creación de una nueva aplicación en [Facebook for Developers](https://developers.facebook.com/).
   - Configuración de la URI de redirección proporcionada por Firebase.

3. **Implementación en el Frontend:**
   - Utilización del SDK de Firebase para manejar el flujo de autenticación.
   - Manejo de tokens de acceso y almacenamiento de sesiones.

### 3. Restablecimiento de Contraseña mediante la API de Gmail

Se implementó la funcionalidad de restablecimiento de contraseña, enviando correos electrónicos a través de la API de Gmail.

**Pasos realizados:**

1. **Configuración de la API de Gmail:**
   - Activación de la API de Gmail en Google Cloud Console.
   - Creación de credenciales OAuth 2.0 para el servidor.

2. **Implementación del Flujo de Restablecimiento:**
   - Generación de tokens de restablecimiento y almacenamiento temporal.
   - Envío de correos electrónicos con enlaces de restablecimiento utilizando la API de Gmail.
   - Validación de tokens y actualización de contraseñas.


### 4. Vinculación con GitHub

Se configuró la integración del proyecto con GitHub para facilitar la colaboración y el control de versiones.

**Pasos realizados:**

1. **Repositorio en GitHub:**
   - Creación del repositorio en GitHub: [Sistema-de-gestion-de-un-Cafe-Bar_PDN](https://github.com/carrascal0320/Sistema-de-gestion-de-un-Cafe-Bar_PDN)

2. **Configuración de GitHub Actions:**
   - Implementación de flujos de trabajo para automatizar pruebas y despliegues.
   - Uso de secretos para manejar credenciales de forma segura.

- **Archivos de Configuración:**
  - Se utilizaron archivos `.env` para manejar variables de entorno sensibles, como claves de API y credenciales. Estos archivos no están incluidos en el repositorio por razones de seguridad.

- **Seguridad:**
  - Se implementaron medidas para proteger las rutas de la API y garantizar que solo usuarios autenticados puedan acceder a ciertas funcionalidades.

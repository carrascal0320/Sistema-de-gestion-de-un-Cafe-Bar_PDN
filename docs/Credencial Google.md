# Configuración de Credenciales para Autenticación con Google

Este procedimiento permite autenticar usuarios mediante Google usando OAuth 2.0. A continuación, los pasos para configurar las credenciales en Google Cloud Console:

---

## 1️⃣ Crear un Proyecto en Google Cloud Console

1. Accede a [Google Cloud Console](https://cloud.google.com/cloud-console/).
2. Inicia sesión con tu cuenta de Google.
3. Haz clic en **"Seleccionar proyecto"** → **"Nuevo Proyecto"**.
4. Asigna un nombre al proyecto.
5. Haz clic en **Crear**.

---

## 2️⃣ Habilitar la API de Autenticación

1. En el menú lateral, ve a:  
   **APIs y servicios** → **Habilitar APIs y servicios**.
2. Busca **Google Identity Platform**.
3. Haz clic en **Habilitar**.

---

## 3️⃣ Crear Credenciales OAuth 2.0

1. Ir a:  
   **APIs y servicios** → **Credenciales**.
2. Haz clic en **Crear credenciales** → **ID de cliente de OAuth**.

   ![Crear credenciales](/demo/demo/1.png)

3. Selecciona **Aplicación web** como tipo de aplicación.
4. Configura lo siguiente:

   - **Orígenes autorizados de JavaScript**:
     - `http://localhost:4200` (Angular)

   - **URIs de redirección autorizados**:
     - `http://localhost:8080/login/oauth2/code/google`

5. Haz clic en **Crear**.

   ![Configuración de OAuth](/demo/demo/2.png)

6. Copia el **Client ID** y el **Client Secret** en tu archivo de configuración de Spring Boot `application.properties` o `application.yml`.

---

# 🔐 Integración con Google OAuth en Spring Boot

Este proyecto permite a los usuarios autenticarse con su cuenta de Google (o GitHub) mediante el protocolo OAuth 2.0. A continuación, se explican las partes clave del código que implementan esta funcionalidad.

---

## 📍 Endpoint: `/api/auth/user`

Este endpoint es invocado automáticamente después de una autenticación exitosa con Google o GitHub. Verifica si el usuario autenticado ya está registrado y, si no lo está, lo registra automáticamente en la base de datos.

### 📦 Ubicación:
📂 `AuthController.java`

```java
@GetMapping("/user")
public ResponseEntity<?> getAuthenticatedUser(Principal principal) {
    if (principal == null) {
        return ResponseEntity.status(401).body(Map.of("message", "User not authenticated"));
    }

    String email = principal.getName(); // Se obtiene el correo electrónico del usuario autenticado por Google
    Optional<User> existingUser = userService.findByEmail(email);

    if (existingUser.isPresent()) {
        return ResponseEntity.ok(existingUser.get());
    }

    // Si el usuario no existe, se crea uno nuevo a partir del objeto OAuth2User
    OAuth2User oAuth2User = (OAuth2User) principal;
    String name = oAuth2User.getAttribute("name");

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setName(name);
    newUser.setPassword("OAuthUser");
    newUser.setPhone("N/A");

    User savedUser = userService.registerUser(newUser);
    return ResponseEntity.ok(savedUser);
}
```

Luego nos dirigimos a la carpeta `services` donde tenemos `UserService` y hacemos una modificación:

```java
   (GitHub/Google)
   public User(String email, String name) {
    this.email = email;
     this.name = name;
     this.password = "OAuthUser";
     this.phone = null;
   }
 
```

---

# 🔐 Integración de GitHub OAuth en Spring Security  

Este documento explica cómo se configuró la autenticación con GitHub en el proyecto utilizando **Spring Security** y OAuth2.

---

## 📌 1. Configuración General de Seguridad  
En la clase `SecurityConfig.java`, se establece la configuración de seguridad del backend, permitiendo la autenticación con GitHub.

### 🔷 Deshabilitación de CSRF y Configuración de CORS  
```java
.csrf(csrf -> csrf.disable())
.cors(cors -> cors.configurationSource(corsConfigurationSource()))
```

---

## 📌 2. Configuración de OAuth2 con Google  
Este paso se realiza en `SecurityConfig.java`:

```java
.oauth2Login(oauth2 -> oauth2
    .successHandler(oAuthSuccessHandler()) // Manejador de éxito que guarda el usuario en BD
    .failureUrl("http://localhost:4200/login?error=true")
)
```

✅ **Explicación paso a paso:**

1. Obtiene el usuario autenticado de GitHub (OAuth2User).
2. Extrae los datos del usuario:
   - **email** → Se obtiene el correo electrónico desde el perfil de GitHub.
   - **name** → Nombre del usuario en GitHub.
3. Si GitHub no proporciona un email (por razones de privacidad), se crea uno falso basado en el login del usuario (`username@github.com`).
4. Llama a `userService.processOAuthUser(email, name)`, que almacena el usuario en la base de datos si no existe.
5. Redirige al frontend (`http://localhost:4200/dashboard`) con el `email` y el `name` en la URL.

---

## 📌 4. Configuración de Logout  

```java
.logout(logout -> logout
    .logoutUrl("/api/auth/logout")
    .logoutSuccessHandler((request, response, authentication) -> {
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("http://localhost:4200/login");
    })
)
```

✅ **Explicación:**

- Define la ruta `/api/auth/logout` para cerrar sesión.
- Cuando el usuario cierra sesión, es redirigido al login del frontend.

---

## Configuración de la Credencial de Google en `auth.service.ts` (Angular)

En este archivo, se configura la autenticación con Google OAuth2 redirigiendo al backend de Spring Boot.

```typescript
loginGoogle(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
}

## Configuración de Inicio de Sesión en `login.component.ts` (Angular)

Este componente maneja el evento de inicio de sesión con Google y llama al servicio de autenticación.

 Iniciar sesión con Google
loginGoogle() {
    this.authService.loginGoogle();
}
 
---
 ## luego con este codigo 
 <button (click)="loginGoogle()">Iniciar sesión con Google</button> garantizamos la funcionalidad de traer la funcion del logeo de google a nuestro proyecto
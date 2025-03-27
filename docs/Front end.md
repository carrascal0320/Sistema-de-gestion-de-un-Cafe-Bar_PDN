# Documentación del Frontend

## Configuración del Entorno

Para ejecutar el frontend del proyecto con Angular, sigue estos pasos:

### Requisitos Previos
Antes de crear un proyecto con Angular, es necesario contar con Node.js instalado en el sistema.

- Tener instalado Node.js (versión recomendada: 18.x o superior).
- Luego instalar Angular CLI globalmente con el siguiente comando:
  ```sh
  npm install -g @angular/cli
  ```
- Usar un editor de código recomendado: VS Code, Sublime Text, etc.

### Instalación y Ejecución
Para instalar Angular, dirigirse a la página oficial de Angular, donde se encuentran los comandos y pasos a seguir para la instalación. Luego de instalar Angular CLI con:
```sh
npm install -g @angular/cli
```
Abrir la carpeta generada con el proyecto Angular, ejecutar `code .` en la terminal y ya se tendrá todo el entorno listo.

### Instalar dependencias
Para instalar las dependencias faltantes del proyecto, ejecutar:
```sh
npm install
```

### Ejecutar el servidor de desarrollo
Para iniciar el servidor de Angular, ejecutar:
```sh
ng serve
```

## Tecnologías Utilizadas
El frontend está desarrollado con las siguientes tecnologías:

- **Angular** (Framework principal)
- **TypeScript** (Lenguaje de programación)
- **HTML5** (Estructura del frontend)
- **CSS3** (Estilos y diseño visual)
- **Bootstrap** (Para el diseño responsivo y moderno)

## Estructura de Carpetas
La estructura del frontend está organizada de la siguiente manera:

![Estructura de carpeta](/demo/demo/Captura%20de%20pantalla%202025-03-13%20161708.png "Estructura de carpeta")

/demo
├── .mvn/
├── node_modules/
├── src/
│   ├── app/
│   │   ├── login/
│   │   │   ├── login.component.html
│   │   │   ├── login.component.css
│   │   │   ├── login.component.ts
│   │   │   └── login.component.spec.ts
│   │   ├── home/
│   │   │   ├── home.component.html
│   │   │   ├── home.component.css
│   │   │   ├── home.component.ts
│   │   │   └── home.component.spec.ts
│   │   ├── register/
│   │   │   ├── register.component.html
│   │   │   ├── register.component.css
│   │   │   ├── register.component.ts
│   │   │   └── register.component.spec.ts
│   │   ├── app.component.html
│   │   ├── app.component.css
│   │   ├── app.component.ts
│   │   ├── app.component.spec.ts
│   │   ├── app.config.ts
│   │   └── app.routes.ts
│   ├── main/
│   ├── test/
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── target/
├── .editorconfig
├── .gitattributes
├── .gitignore
├── angular.json
├── Docs/
│   ├── Frontend.md
│   ├── Backend.md
│   └── README.md
└── README.md

## Creación del Componente de Login
En el desarrollo del frontend, se creó un componente llamado `Login` para manejar la autenticación de usuarios.

### Creación del Componente
El componente se generó con el siguiente comando:
```sh
ng generate component login
```
Esto creó los archivos:
- `login.component.ts` (Lógica del componente)
- `login.component.html` (Vista del formulario de login)
- `login.component.css` (Estilos del login)

## Conclusión
Esta documentación describe la configuración del frontend, las tecnologías utilizadas y la estructura del proyecto. Además, se explicó la creación del componente Login, que permite a los usuarios autenticarse en el sistema.

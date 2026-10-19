# Cacharrito backend

## Requisitos

- Java 17
- MySQL accesible desde el equipo o servicio que ejecuta Spring Boot

## Configuración local en Windows PowerShell
Configure la conexión a la base de datos mediante las variables de entorno DB_URL, DB_USERNAME y DB_PASSWORD. No guardes contraseñas ni cadenas de conexión privadas en este archivo.
Antes de iniciar la aplicación, define estas variables en la terminal actual. Copia los valores actuales desde el panel de Aiven; no los guardes en este README ni en Git:

```powershell
$env:DB_URL = 'jdbc:mysql://<HOST>:<PORT>/<DATABASE>?<PARAMETERS_FROM_AIVEN>'
$env:DB_USERNAME = '<AIVEN_USERNAME>'
$env:DB_PASSWORD = '<NEW_AIVEN_PASSWORD>'
```

Para habilitar el correo, configura también:

```powershell
$env:MAIL_USERNAME = '<EMAIL_ACCOUNT>'
$env:MAIL_PASSWORD = '<EMAIL_PROVIDER_APP_PASSWORD>'
$env:MAIL_FROM = '<SENDER_ADDRESS>'
$env:FRONTEND_URL = 'http://localhost:4200'
```

Después, desde esta carpeta, inicia Spring Boot con `./mvnw.cmd spring-boot:run`. Las variables solo estarán disponibles en esa ventana de PowerShell. En Render, agrega los mismos nombres desde la sección **Environment** del servicio.

El correo es opcional para las funciones básicas. Sin esas variables, los mensajes de aprobación no podrán enviarse.

Las contraseñas existentes en texto plano se convierten a BCrypt cuando el usuario inicia sesión correctamente. Las nuevas contraseñas se guardan con BCrypt y no se incluyen en las respuestas JSON.

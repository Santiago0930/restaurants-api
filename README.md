📌 Restaurants API

API REST desarrollada en Spring Boot que permite:

- Registro de usuarios
- Login de usuario
- Consulta de restaurantes cercanos (por ciudad o coordenadas)
- Consulta del historial de acciones
- Logout
  
🚀 Tecnologías utilizadas:
- Java 17
- Spring Boot
- Spring Security (JWT)
- MySQL
- Docker & Docker Compose
- Geoapify API
  
⚙️ Ejecución con Docker
1. Construir el proyecto
mvn clean package -DskipTests
2. Levantar los contenedores
docker compose up --build
3. Acceso a la API mediante Postman por la Url: http://localhost:8080

🛑 Detener la aplicación

- docker compose down

🔐 Autenticación

La API utiliza JWT y para acceder a endpoints protegidos debe enviar:
- Authorization: Bearer TOKEN_GENERADO_AL_HACER_LOGIN

📌 Endpoints principales

1. registerUser: Registra un usuario en el sistema.
   
```POST http://localhost:8080/user/registerUser```

```json
{
  "firstName": "Santiago",
  "lastName": "Guerrero",
  "age": 23,
  "email": "Santiago@test.com",
  "password": "123456"
}
```
2. Login: Permite al usuario autenticarse en el sistema.
   
```POST http://localhost:8080/auth/login```
```json
{
  "email": "Santiago@test.com",
  "password": "123456"
}
```

3. Consultar restaurantes; Permite al usuario saber que restaurantes hay cerca de una ciudad o de unas coordenadas.
- Por ciudad
```GET http://localhost:8080/restaurant/nearby?city=Cartagena```
- Por coordenadas
```GET http://localhost:8080/restaurant/nearby?lat=4.6533817&lon=-74.0836331```

4. Historial de transacciones: Permite al usuario ver el historial de acciones que ha realizado en la app (LOGIN, SIGN_UP, LOGOUT, CONSULTAR_RESTAURANTES)
```GET http://localhost:8080/transactions/me```

5. Logout: Registra la acción de logout del usuario.
```POST http://localhost:8080/auth/logout```

🧠 Registro de acciones
La aplicación registra automáticamente las siguientes acciones:

- Registro de usuario (SIGN_UP)
- Inicio de sesión (LOGIN)
- Cierre de sesión (LOGOUT)
- Consulta de restaurantes
  
📦 Variables de entorno
Configuradas en docker-compose.yml:

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- JWT_SECRET
- JWT_EXPIRATION
- GEOAPIFY_API_KEY
  
🧪 Flujo sugerido para probar la app

- Registrar Usuario
- Login
- Consultar restaurantes por ciudad
- Consultar restaurantes por coordenadas
- Logout
- Consultar historial de transacciones
  
🧠 Notas técnicas

- Se utilizo arquitectura por capas (Controller - Service - Repository)
- Se implemento inyección por constructor
- Se maneja autenticación con JWT sin estado (stateless)

👨‍💻 Autor

- Santiago Guerrero

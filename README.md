# Proyecto Spring Boot con Autenticación JWT

Este proyecto es una API REST desarrollada con Spring Boot que implementa autenticación JWT (JSON Web Tokens) para la gestión de usuarios.
Tambien implementa una serie de validaciones y condiciones que deben cumplir los campos de registro del usuario, como el largo del nombre, que exista un formato especifico para la creacion del email, el manejo personalizado de Excepciones con Spring y la anotacion @RestControllerAdvice.

## Características principales

- Registro y autenticación de usuarios
- Generación y validación de tokens JWT
- Endpoints protegidos por roles/autorizaciones
- Base de datos H2 embebida (para desarrollo)
- Configuración y Validación de Excepciones con Response personalizadas 
- Configuración de seguridad con Spring Security
- Mapeo automático DTO <-> Entidad

## Diagrama de Secuencia

El siguiente diagrama muestra los flujos principales de la aplicación:

```mermaid
sequenceDiagram
    participant Client
    participant Controller as UserController
    participant Service as UserService
    participant Repository as UserRepository
    participant Database as H2 Database
    participant Security as SecurityConfig
    participant JWT as JwtUtil
    participant UserEntity as UserEntity
    participant Filter as JwtAuthFilter

    Note over Client, Filter: User Registration Flow
    Client->>Controller: POST /api/auth/register
    Controller->>Service: createUser(CreateUserRequest)
    Service->>Repository: existsByUsername()
    Repository->>Database: SELECT * FROM users WHERE username=?
    Database-->>Repository: false
    Repository-->>Service: false
    Service->>Repository: existsByEmail()
    Repository->>Database: SELECT * FROM users WHERE email=?
    Database-->>Repository: false
    Repository-->>Service: false
    Service->>Service: passwordEncoder.encode(password)
    Service->>UserEntity: Utils.generateRandomUUID()
    UserEntity->>UserEntity: Utils.formatDateTime(LocalDateTime.now())
    UserEntity-->>Service: New UserEntity
    Service->>JWT: generateToken(username)
    JWT->>JWT: Create JWT with secret + expiration
    JWT-->>Service: JWT token
    Service->>Repository: save(user)
    Repository->>Database: INSERT INTO users...
    Database-->>Repository: User entity
    Repository-->>Service: User entity
    Service-->>Controller: UserResponse
    Controller-->>Client: 200 OK + UserResponse

    Note over Client, Filter: User Login Flow
    Client->>Controller: POST /api/auth/login
    Controller->>Service: authenticate(LoginRequest)
    Service->>Repository: findByUsername()
    Repository->>Database: SELECT * FROM users WHERE username=?
    Database-->>Repository: User entity
    Repository-->>Service: User entity
    Service->>Service: passwordEncoder.matches()
    Service->>JWT: generateToken(username)
    JWT->>JWT: Create JWT with secret + expiration
    JWT-->>Service: JWT token
    Service-->>Controller: JwtResponse(token, user)
    Controller-->>Client: 200 OK + JWT token

    Note over Client, Filter: Protected Endpoint Access
    Client->>Filter: GET /api/users (with Bearer token)
    Filter->>JWT: validateToken(token)
    JWT-->>Filter: true
    Filter->>JWT: getUsernameFromToken()
    JWT-->>Filter: username
    Filter->>Security: Set authentication context
    Filter->>Controller: Forward request
    Controller->>Service: getAllUsers()
    Service->>Repository: findAll()
    Repository->>Database: SELECT * FROM users
    Database-->>Repository: List<User>
    Repository-->>Service: List<User>
    Service->>Service: mapToUserResponse()
    Service-->>Controller: List<UserResponse>
    Controller-->>Client: 200 OK + List<UserResponse>
```

## Diagrama de Estructura (Arquitectura en Capas)

![diagrama-de-clases](https://github.com/user-attachments/assets/86ae653d-0130-4483-baa0-0313bb887d5a)


## Reporte de Porcentaje de Coverage Test del Proyecto

El reporte completo se puede obtener corriendo el siguiente comando: ./gradlew clean test

![image](https://github.com/user-attachments/assets/a145556f-20ee-44a1-9681-080195be4e82)



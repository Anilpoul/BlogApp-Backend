Here’s a clean and structured `README.md` checklist for your **Spring Boot + Keycloak + React Blog Application** setup:

---

# ✅ Blog Application – Keycloak Integration Checklist

This project integrates **Spring Boot (backend)** with **Keycloak** for authentication and **React (frontend)** for the UI. Below is a checklist of what has been implemented for secure user registration, login, role-based access, and backend protection via JWT.

---

## 🔐 1. Keycloak Setup

- [x] Created a **realm** named `blogrealm`.
- [x] Created **client** with:
  - Client ID: `blog-client`
  - Access Type: `confidential`
  - Valid redirect URIs: `http://localhost:3000/*`
  - Enabled `Service Accounts`
- [x] Created **roles**: `USER`, `ADMIN`.
- [x] Created **user groups** or assigned roles to users manually in the admin console.

---

## 📦 2. Backend – Spring Boot Configuration

### ✅ application.properties

```properties
spring.application.name=BlogApplication
server.port=8081

# PostgreSQL DB
spring.datasource.url=jdbc:postgresql://localhost:5432/blogapplication
spring.datasource.username=postgres
spring.datasource.password=anil
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Multipart upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
project.image=images/

# Logging
logging.level.org.springframework.security=DEBUG

# Thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
spring.web.resources.static-locations=classpath:/static/

# 🔐 Keycloak JWT config
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/blogrealm
```

---

## ⚙️ 3. Role Enum

```java
public enum Role {
    USER,
    ADMIN
}
```

---

## 🔧 4. KeycloakService

Handles:
- Creating a user in Keycloak using Admin REST API.
- Assigning roles to the user in Keycloak.
- Fetching Keycloak user ID using email.

```java
String userId = getKeycloakUserId(user.getEmail());
assignRolesToUser(userId, user.getRoles());
```

---

## 👥 5. UserService

- Registers users using the `KeycloakService`.
- Stores Keycloak user ID in the database:
```java
user.setKeycloakUserId(keycloakUserId);
```

---

## 🔐 6. SecurityConfig

- Enables JWT-based stateless security.
- Public endpoints: Register, Login, View Posts.
- Protected endpoints for authenticated users and admins.

```java
.oauth2ResourceServer(oauth2 -> oauth2.jwt())
.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
```

---

## 🌐 7. CorsConfig

Allows React frontend to communicate with backend:

```java
.allowedOrigins("http://localhost:3000")
.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
.allowCredentials(true)
```

---

## 🧪 8. Sample Role-based Access

You can annotate controller methods with:
```java
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasRole('USER')")
```

Or define rules in `SecurityFilterChain`.

---

## 🖼️ 9. Frontend Integration Notes

- Store access token in localStorage after login.
- Pass token in `Authorization` header:
  ```
  Authorization: Bearer <token>
  ```
- Use Keycloak JS adapter or fetch tokens via `Keycloak Admin REST` API.

---

## ✅ Done

You now have a fully working backend with:

- 🔐 Keycloak-secured user management.
- ✅ Role-based access control.
- ⚙️ Admin-level and User-level permissions.
- 💾 Users persisted in both Keycloak and your DB.

---

Let me know if you’d like this saved as a file or want a frontend checklist added too!

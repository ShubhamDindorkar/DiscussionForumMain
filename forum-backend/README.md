# Forum Backend

Java 21 Spring Boot 3.3 REST + WebSocket backend for Discussion Forum.

## Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL running with database `forum`

## Environment
- `DB_URL` (default: `jdbc:postgresql://localhost:5432/forum`)
- `DB_USER` (default: `postgres`)
- `DB_PASSWORD` (default: `postgres`)
- `ALLOWED_EMAIL_DOMAIN` (default: `your-college-domain.tld`)
- `GOOGLE_OAUTH_CLIENT_ID` / `GOOGLE_OAUTH_CLIENT_SECRET` (optional for OAuth)
- `JWT_KEYPAIR` (provide PEM files below or use defaults)

Place RSA keys in `src/main/resources/keys`:
- `jwt.pub` (PEM: BEGIN PUBLIC KEY)
- `jwt.key` (PEM: BEGIN PRIVATE KEY)

## Build
```bash
mvn -q -DskipTests package
```

## Run
```bash
mvn spring-boot:run
```

## Endpoints
- `GET /api/health` → `{"status":"ok"}` (no auth)
- `GET /actuator/health`

## Notes
- JWT resource server (RS256). CORS defaults to `http://localhost:3000`.
- RBAC roles: ADMIN, FACULTY, STUDENT.


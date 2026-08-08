# RBAC system with JWT auth and immutable audit logging

A full-stack app demonstrating role-based access control, secure
authentication, and tamper-resistant audit logging.

- **Backend**: Spring Boot (Java 17) + PostgreSQL
- **Frontend**: React (Vite) + React Router + Axios

## Status: Phase 0-1 complete (scaffolding + data model)

- [x] Backend project scaffolding (Maven, Spring Boot, Spring Security, JPA)
- [x] Frontend project scaffolding (Vite + React, react-router-dom, axios)
- [x] Docker Compose for local Postgres
- [x] Git repo initialized
- [x] Entities: `User`, `Role`, `Permission`, `AuditLog`
- [x] Repositories (with `AuditLog` intentionally restricted to insert/read only)
- [x] Startup data seeder (3 demo users across 3 roles)
- [ ] Phase 2: JWT authentication (login issues token, filter validates it)
- [ ] Phase 3: Method-level RBAC authorization (`@PreAuthorize`)
- [ ] Phase 4: Audit logging wired into real actions + DB-level insert-only grant
- [ ] Phase 5: React login page, role-gated dashboard, audit log viewer
- [ ] Phase 6: Tests, full-stack Docker Compose, deployment

## Project structure

```
rbac-audit-system/
├── backend/          Spring Boot API
│   ├── pom.xml
│   └── src/main/java/com/rbacaudit/
│       ├── model/         User, Role, Permission, AuditLog entities
│       ├── repository/    Spring Data repositories
│       └── config/        DataSeeder (bootstraps demo roles/users)
├── frontend/          React app (Vite)
│   └── src/
│       ├── api/client.js  Axios instance pointed at the backend
│       └── pages/         Route-level components
└── docker-compose.yml  Local Postgres
```

## Running it locally

**1. Start Postgres:**
```bash
docker compose up -d
```

**2. Run the backend** (from `backend/`):
```bash
cd backend
mvn spring-boot:run
```
This boots the API on `localhost:8080` and seeds 3 demo accounts:

| username | password        | role   |
|----------|-----------------|--------|
| admin    | AdminPass123!   | ADMIN  |
| editor   | EditorPass123!  | EDITOR |
| viewer   | ViewerPass123!  | VIEWER |

(Demo passwords only - never do this in a real deployment.)

**3. Run the frontend** (from `frontend/`):
```bash
cd frontend
npm install
npm run dev
```
This starts the Vite dev server, by default on `localhost:5173`.

## Data model

- **User** - implements Spring Security's `UserDetails` directly, so it plugs
  straight into the security filter chain with no adapter class.
- **Role** - a named bundle of permissions (e.g. `ADMIN`, `EDITOR`, `VIEWER`).
- **Permission** - the smallest unit of "what you can do" (e.g. `DOCUMENT_DELETE`).
  Kept as its own entity rather than a hardcoded string on `Role`, so you can
  reassign what a role can do without touching Java code.
- **AuditLog** - append-only by design. No setters, no update path in Java,
  and (from Phase 4 onward) the database grant itself will revoke UPDATE/DELETE
  on this table for the application's DB user.

## Why these security choices

- **bcrypt over plain hashing**: bcrypt is deliberately slow and includes a
  salt automatically, which makes brute-force and rainbow-table attacks
  impractical compared to a fast hash like SHA-256.
- **JWT with short expiration**: keeps the server stateless (no session
  store) while limiting the damage window if a token is ever stolen.
- **Audit log is append-only**: a security log you can edit isn't a security
  log - it's just a suggestion. Enforcing this at both the Java and database
  layer means even a compromised app can't rewrite its own history.

## Next steps

Phase 2 - JWT issuance/validation on the backend, then wiring a login form
on the frontend that stores the token and attaches it to API requests.

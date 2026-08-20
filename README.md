# RBAC system with JWT auth and immutable audit logging

I built this project to get a hands-on experience with some security patterns that 
might come up in backend job postings: role-based access control, JWT auth, and tamper-proof logging. Spring Boot backend, React frontend, Postgres for storage. 

What I used:
- **Backend**: Spring Boot (Java 17) + PostgreSQL
- **Frontend**: React (Vite) + React Router + Axios

## Current: Phase 0-1 complete (scaffolding + data model)

Currently at Phase 1. Data model and project scaffolding are done and it runs. JWT auth, 
RBAC enforcement, and the actual audit logging are next. 

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

## How it's structured

```
backend/    Spring Boot API
frontend/   React app (Vite)
docker-compose.yml   local Postgres
```

## Running it 

```bash
docker compose up -d
cd backend && mvn spring-boot:run
cd frontend && npm install && npm run dev
```
Demo accounts (seeded on startup): 

| username | password        | role   |
|----------|-----------------|--------|
| admin    | AdminPass123!   | ADMIN  |
| editor   | EditorPass123!  | EDITOR |
| viewer   | ViewerPass123!  | VIEWER |

(Demo passwords only)

## Data model

Permissions are their own table instead of hardcoded strings. More setup than 
just using an enum, but it means I can change what each role can do by editing
a database row instead of redeploying code. Roles are just the named bundles of 
these permissions, and users get assigned roles, not permissions.

`User` implements Spring Security's
`UserDetails` directly instead of going through a separate adapter class. 

## Why these security choices

- **bcrypt over plain hashing**: bcrypt is deliberately slow and includes a
  salt automatically, which makes brute-force and rainbow-table attacks
  impractical compared to a fast hash like SHA-256.
- **JWT with short expiration**: keeps the server stateless (no session
  store) while limiting the damage window if a token is ever stolen.
- **Audit log is append-only**: a security log you can edit isn't really secure. 
  Enforcing this at both the Java and database 
  layer means even a compromised app can't rewrite its own history.

## Next step

Phase 2 - JWT issuance/validation on the backend, then wiring a login form
on the frontend that stores the token and attaches it to API requests.

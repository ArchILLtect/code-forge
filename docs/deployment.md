# Deployment Runbook — Tomcat 9 + Hibernate auto‑DDL (MVP)

This project runs as a WAR on Tomcat 9 (Jakarta Servlet API). The persistence layer uses Hibernate directly (no Spring). For MVP, schema is managed by Hibernate auto‑DDL; Flyway is a post‑MVP task.

## Environments
- Dev/Test: PostgreSQL (local or Docker)
- Prod: Neon (serverless PostgreSQL)

## Required runtime configuration
- Cognito secret: provided by environment or JVM property
  - `COGNITO_CLIENT_SECRET` (environment variable), or
  - `-DCOGNITO_CLIENT_SECRET=...` (system property)
- Non‑secret Cognito config lives in `src/main/resources/cognito.properties`
- QuoteService config lives in `src/main/resources/application.properties` (required by the service):
  - `quote.api.url` and `quote.timeout.seconds`

## Database configuration
### Dev/Test (default)
- `src/main/resources/hibernate.cfg.xml` points to H2 and sets `hbm2ddl.auto=update`.

### Production (Neon PostgreSQL)
Use environment variables or system properties:
- `DB_HOST`: Neon PostgreSQL endpoint (e.g., `ep-xyz.us-east-1.aws.neon.tech`)
- `DB_PORT`: `5432` (default PostgreSQL port)
- `DB_NAME`: Database name
- `DB_USER`: Database username
- `DB_PASS`: Database password

Connection string format:
```
jdbc:postgresql://<host>:5432/<db>?ssl=true&sslmode=require
```

## Balanced Approach (MVP)
- Phase 1 (first deploy): allow Hibernate to create/update tables with `hbm2ddl.auto=update`
- Verify schema and app functionality
- Phase 2: change to `hbm2ddl.auto=validate` to lock structure
- Post‑MVP: adopt Flyway with `V1__init_schema.sql` and disable Hibernate DDL (validate only)

## Build & Deploy
### Build WAR (Windows cmd)
```
cmd /c 'cd /d "C:\Users\nickh\Documents\My Projects\Java\code-forge" && mvn -q -DskipTests=false clean package'
```

### Deploy to Tomcat 9
- Copy `target/codeforge.war` to Tomcat `webapps/` (rename to `codeforge.war` if desired)
- Ensure environment/Java opts provide the Cognito secret
  - Example (set once for user):
    ```
    setx COGNITO_CLIENT_SECRET "your_secret"
    ```
  - Or add to Tomcat service/`setenv` as `-DCOGNITO_CLIENT_SECRET=your_secret`
- Start Tomcat and open `http://localhost:8080/codeforge/`

## RDS/Neon specifics
- Neon provides serverless PostgreSQL with automatic scaling
- Security: Neon requires SSL (`ssl=true&sslmode=require`)
- JDBC URL example:
  - Postgres/Neon: `jdbc:postgresql://<host>:5432/<db>?ssl=true&sslmode=require`
- On first deploy, Hibernate can auto-create schema with `hbm2ddl.auto=update`; after verifying tables (`challenges`, `submissions`, `drill_items`), consider switching to `validate`

## QuoteService notes
- `QuoteService` loads `application.properties` from the classpath and uses `HttpClient` for outbound calls
- If outbound HTTP is blocked, service returns a fallback string; logs the error

## Troubleshooting
- 401/403 on Drill/Admin routes → ensure login/session (AuthGuardFilter)
- 500 during `/auth` callback → verify `COGNITO_CLIENT_SECRET` and `cognito.properties` URLs/IDs
- No Drill items → ensure seed data or create by visiting Drill Mode (service ensures DrillItem on first submit)
- DB schema mismatch in prod → temporarily set DDL to `update` to bootstrap, then flip back to `validate`
# Deployment Guide — AWS RDS + Hibernate auto-DDL (MVP)

This guide documents the Balanced Approach for MVP deployment:
- Let Hibernate create/update the schema on first deploy (ddl-auto=update).
- After verifying the schema, switch to ddl-auto=validate to lock structure.
- Adopt Flyway post‑MVP for versioned migrations.

From `src/main/resources/application.yml` (dev defaults):
- `spring.jpa.hibernate.ddl-auto: update` (dev/test; uses in‑memory H2)
- Server port is already externalized: `server.port: ${PORT:5000}`

For production (AWS RDS), override datasource and JPA properties with environment variables.

---

## 1) Provision Neon Database

- Create a new project in Neon (https://neon.tech)
- Note the connection details: host, database name, username, password
- Neon automatically provides SSL-enabled PostgreSQL endpoints

## 2) Configure application (Render deployment)

Set the following environment variables in Render:

- `DB_HOST`: Neon endpoint (e.g., `ep-xyz.us-east-1.aws.neon.tech`)
- `DB_PORT`: `5432`
- `DB_NAME`: Database name
- `DB_USER`: Database username  
- `DB_PASS`: Database password

The application will build the JDBC URL automatically with SSL enabled:
```
jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}?ssl=true&sslmode=require
```

## 3) Deploy and verify

- Deploy the app to Render with the environment variables above.
- Hibernate will auto-create the schema on first startup (using entity annotations).
- Verify schema:
  - Connect to Neon and check that tables exist: `challenges`, `submissions`, `drill_items`, including `drill_items.version` (added via `@Version`).
- Smoke test the app (e.g., run a Drill flow to insert data).

## 4) Schema management notes

- Hibernate uses entity annotations to create/validate schema
- First deploy: Hibernate will auto-create tables based on `@Entity` classes
- After verification: you can set `hibernate.hbm2ddl.auto=validate` to prevent auto-changes
- Future: consider Flyway for versioned migrations (post-MVP)

## 5) Post‑MVP: Adopt Flyway migrations

After MVP deployment stabilizes:
- Export verified schema from RDS as `V1__init_schema.sql`.
- Add Flyway dependency and enable Flyway in your build.
- Configure Spring JPA:
  - `spring.jpa.hibernate.ddl-auto=validate` (keep schema immutable)
  - `spring.flyway.enabled=true`
- Place `V1__init_schema.sql` in `src/main/resources/db/migration/`.
- For future schema changes, add new migration scripts `V2__...sql`, `V3__...sql`, etc.

## 6) Troubleshooting

- `relation/table not found` at startup:
  - Ensure Hibernate can create tables (check DB user permissions).
- Driver/URL errors:
  - Confirm PostgreSQL JDBC driver is in dependencies and JDBC URL is correct.
- Connection timeouts:
  - Verify DB_HOST, DB_PORT, and that Neon endpoint is accessible from Render.
  - Check that SSL is enabled (`ssl=true&sslmode=require`).

## 7) Migration from AWS RDS MySQL

If migrating from existing MySQL database:
1. Export data from MySQL using `mysqldump` or similar
2. Convert MySQL syntax to PostgreSQL (AUTO_INCREMENT → SERIAL, ENUM → VARCHAR/CHECK, etc.)
3. Import into Neon using `psql` or database client
4. Update Render environment variables to point to Neon
5. Deploy and verify
6. After successful verification, delete AWS RDS instance


# Deployment Runbook — Tomcat 9 + Hibernate auto‑DDL (MVP)

This project runs as a WAR on Tomcat 9 (Jakarta Servlet API). The persistence layer uses Hibernate directly (no Spring). For MVP, schema is managed by Hibernate auto‑DDL; Flyway is a post‑MVP task.

## Environments
- Dev/Test: H2 in‑memory DB via `src/main/resources/hibernate.cfg.xml`
- Prod (Week 8): AWS RDS (PostgreSQL or MySQL)

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

### Production (RDS)
Use either approach:
1) Tomcat JNDI DataSource (recommended)
   - Define a JNDI resource in Tomcat `conf/context.xml` or the webapp context `META-INF/context.xml`:
     ```xml
     <Resource name="jdbc/codeforge" auth="Container"
               type="javax.sql.DataSource" maxTotal="20" maxIdle="5"
               username="db_user" password="db_pass"
               driverClassName="org.postgresql.Driver"
               url="jdbc:postgresql://host:5432/dbname"/>
     ```
   - Update your Hibernate configuration to use JNDI lookup (optional enhancement), or provide a prod `hibernate.cfg.xml` packaged for deployment.

2) Prod `hibernate.cfg.xml`
   - Build a production variant of `hibernate.cfg.xml` with your RDS JDBC URL, user, and password.
   - First deploy: set `<property name="hibernate.hbm2ddl.auto">update</property>`
   - After verification: change to `validate` and redeploy

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

## RDS specifics
- Create DB (PostgreSQL/MySQL), note endpoint, user, password
- Security groups: allow inbound from your app host
- JDBC URL examples:
  - Postgres: `jdbc:postgresql://<host>:5432/<db>`
  - MySQL: `jdbc:mysql://<host>:3306/<db>?useSSL=true&requireSSL=true`
- On first deploy, keep DDL `update`; after verifying tables (`challenges`, `submissions`, `drill_items`), flip to `validate`

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

## 1) Provision AWS RDS

- Choose engine/version (e.g., PostgreSQL 15.x or MySQL 8.x).
- Create database (DB name, username, password).
- Configure security group rules to allow inbound from your app host (e.g., Elastic Beanstalk instance SG).
- Note the JDBC endpoint: `host:port/dbname`.

## 2) Configure application (first deploy)

Set the following environment variables in your hosting environment (Elastic Beanstalk, ECS, EC2, etc.), or for an one‑off local test run:

- `SPRING_DATASOURCE_URL`
  - PostgreSQL: `jdbc:postgresql://<host>:<port>/<db>?sslmode=require`
  - MySQL: `jdbc:mysql://<host>:<port>/<db>?allowPublicKeyRetrieval=true&useSSL=true&requireSSL=true&useUnicode=true&characterEncoding=utf8`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update` (first deploy only — lets Hibernate create missing tables/columns)

Example one‑shot local run on Windows cmd:

```bat
cmd /c 'set "SPRING_DATASOURCE_URL=jdbc:postgresql://mydb.abcdefg.us-east-1.rds.amazonaws.com:5432/codeforge?sslmode=require" && set "SPRING_DATASOURCE_USERNAME=appuser" && set "SPRING_DATASOURCE_PASSWORD=secret" && set "SPRING_JPA_HIBERNATE_DDL_AUTO=update" && mvn -q spring-boot:run'
```

Notes
- The app already externalizes server port via `server.port=${PORT:5000}`.
- Keep test/dev profiles pointed at H2; only prod uses RDS overrides.

## 3) Deploy and verify

- Deploy the app to your AWS environment with the env vars above.
- Verify health:
  - `GET /actuator/health` → `UP`
- Verify schema:
  - Check RDS tables exist: `challenges`, `submissions`, `drill_items`, including `drill_items.version` (added in Issue 38 via `@Version`).
- Smoke test the app (e.g., run a Drill flow to insert data).

## 4) Flip ddl-auto to validate

Once the schema looks correct in RDS:
- Change environment variable: `SPRING_JPA_HIBERNATE_DDL_AUTO=validate`
- Redeploy the app. Hibernate will now validate the schema at startup and fail fast if it drifts, but it will no longer apply changes automatically.

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
  - You likely deployed with `ddl-auto=validate` before the schema existed; switch back to `update` for the first run.
- Permission errors creating tables:
  - Ensure the RDS user has `CREATE`/`ALTER` permissions.
- Driver/URL errors:
  - Confirm the correct JDBC driver is on the classpath (Spring Boot starters include both Postgres/MySQL when added) and that the JDBC URL is correct for your engine.
- Connection timeouts:
  - Check RDS SG rules, VPC networking, and that your app host can reach the RDS endpoint.

## 7) Quick rollback plan

- Keep `ddl-auto` in an env var so you can temporarily switch back to `update` if a small non‑destructive patch is needed.
- Prefer to schedule structural changes; adopt Flyway promptly post‑MVP for safer, versioned changes.


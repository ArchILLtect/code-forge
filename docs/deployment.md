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


# Deployment Runbook - Render + Docker + Neon PostgreSQL

This runbook documents the current production deployment path for CodeForge.

## Architecture
- App runtime: Tomcat 10.1 in Docker container
- Build artifact: WAR (`target/codeforge.war`)
- Hosting: Render Web Service (Docker)
- Database: Neon PostgreSQL
- Auth: AWS Cognito Hosted UI

## Required environment variables
Set these in Render:
- `APP_ENV=prod`
- `COGNITO_CLIENT_SECRET=<your_cognito_client_secret>`
- `DB_HOST=<your_neon_host>`
- `DB_PORT=5432`
- `DB_NAME=<your_database_name>`
- `DB_USER=<your_database_user>`
- `DB_PASS=<your_database_password>`

Optional local/dev overrides can also be provided as JVM properties.

## Database behavior
The app uses Hibernate with PostgreSQL and builds JDBC connection settings from env vars.

Expected JDBC format:

```text
jdbc:postgresql://<host>:5432/<db>?ssl=true&sslmode=require
```

Neon requires SSL. Keep `ssl=true&sslmode=require`.

## Build
```bash
mvn -q -DskipTests=false clean package
```

## Render deployment steps
1. Create or open the Render web service for this repo.
2. Ensure Docker deployment is enabled (uses repo `Dockerfile`).
3. Configure the environment variables listed above.
4. Deploy latest commit.
5. Verify app routes load (`/home`, `/challenges`, `/logIn`).

## Verification checklist
- App boots without Hibernate connection errors.
- Login flow redirects to Cognito and callback returns to app.
- Challenge list loads from Neon.
- Drill submission writes rows to `submissions` and updates `drill_items`.

## Troubleshooting
- Auth callback fails (`/auth`): verify `COGNITO_CLIENT_SECRET` and `cognito.properties` values.
- DB connection failure: verify `DB_HOST/DB_PORT/DB_NAME/DB_USER/DB_PASS` and Neon network/SSL settings.
- Empty drill queue: verify challenge seed data or enroll by visiting drill flow.

## Migration note
Older docs referenced legacy hosting platform, legacy managed database, and previous SQL backend. Those are no longer the active deployment/database targets for this project.


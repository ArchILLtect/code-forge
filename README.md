## CodeForge README

CodeForge is a Java 21 coding-practice web app built with Servlets/JSP + Hibernate.

Current production stack:
- Hosting: Render (Docker)
- Database: Neon PostgreSQL
- Auth: AWS Cognito Hosted UI

## Features
- Practice Mode: public challenge solving with inline feedback (no persistence).
- Drill Mode: authenticated spaced-repetition queue with persisted outcomes.
- Challenge Library: browse challenge list and details.
- Admin CRUD: create/edit/delete challenge routes (auth-protected).

## Tech Stack
- Java 21
- Servlets + JSP (Tomcat runtime inside Docker image)
- Hibernate 6
- PostgreSQL (Neon in prod)
- Maven (WAR build)
- Log4j2
- JUnit 5 + Mockito

## Project Docs
- Problem statement: `docs/problem-statement.md`
- User stories: `docs/user-stories.md`
- Project plan: `docs/project-plan.md`
- Deployment runbook: `docs/deployment.md`
- Reflections: `docs/reflections/WeeklyJournal.md`

## Runtime configuration
Required secret:
- `COGNITO_CLIENT_SECRET`

Non-secret Cognito settings are in:
- `src/main/resources/cognito.properties`

Database settings are resolved from env vars/system properties/local config:
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASS`

## Local test database setup (PostgreSQL)
Create a local test database:

```sql
CREATE DATABASE cf_test_db;
```

Create `src/test/resources/test-db.properties` (untracked) using this template:
- `src/test/resources/test-db.properties.example`

Tests reset the DB using `src/test/resources/cleandb.sql` before test classes.

Run tests:

```bash
mvn -q -DskipTests=false test
```

## Build
Build the WAR:

```bash
mvn -q -DskipTests=false clean package
```

## Deploy (Render + Docker)
1. Connect this repo to Render as a Docker web service.
2. Set environment variables in Render:
   - `APP_ENV=prod`
   - `COGNITO_CLIENT_SECRET=<secret>`
   - `DB_HOST=<neon-host>`
   - `DB_PORT=5432`
   - `DB_NAME=<db>`
   - `DB_USER=<user>`
   - `DB_PASS=<password>`
3. Deploy.

Neon/Postgres JDBC format used by the app:

```text
jdbc:postgresql://<host>:5432/<db>?ssl=true&sslmode=require
```

## Notes
- This repository previously documented older hosting/database workflows during earlier class milestones.
- The active and supported deployment/database path is now Render + Docker + Neon PostgreSQL.

## License
MIT


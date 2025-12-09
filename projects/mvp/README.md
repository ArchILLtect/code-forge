# CodeForge MVP – Quick Start

This guide summarizes how to run MVP features (Practice + Drill), configure flags, and run tests.

## Prerequisites
- Java 21 (Temurin recommended)
- MySQL 8.x running locally (cf_test_db)
- Maven 3.9+

## Config
- Edit `src/main/resources/application.properties` for feature flags:
  - `features.practice.enabled=true`
  - `features.drill.enabled=true`
- Database settings are loaded via environment variables or `hibernate.cfg.xml`.

## Run (Tomcat)
- Build WAR:
```powershell
mvn -f "C:\Users\nickh\Documents\My Projects\Java\code-forge\pom.xml" -DskipTests=true package
```
- Deploy `target/*.war` to Tomcat webapps and start Tomcat.

## Routes
- Practice (public):
  - `GET /practice/{id}` – render solve form.
  - `POST /practice/{id}/submit` – evaluate and show inline feedback; no persistence.
- Drill (auth required):
  - `GET /drill` – due queue + enrollment banner.
  - `GET /drill/next` – redirect to next due.
  - `GET /drill/{id}` – solve page with streak/seen/next due.
  - `POST /drill/{id}/submit` – evaluate, persist outcome, flash message → queue.

## Evaluator
- Basic evaluator compares submission to `Challenge.expectedAnswer`.
- Outcomes: CORRECT / ACCEPTABLE / INCORRECT / SKIPPED.
- Guard: very long or blank submissions → SKIPPED.

## Telemetry
- Rolling file appender at `logs/telemetry.log`.
- Emits: `mode, challengeId, outcome, durationMs` for each run.

## Tests
- Run all tests:
```powershell
mvn -f "C:\Users\nickh\Documents\My Projects\Java\code-forge\pom.xml" -DskipTests=false test
```
- Notable tests:
  - Evaluator unit tests (correct/acceptable/incorrect/missing/guard).
  - Drill auto-enrollment unit test.
  - Drill persistence test (timesSeen/nextDueAt).

## Troubleshooting
- If Practice/Drill are disabled, servlets respond with 404.
- If DB isn’t reachable, tests may fail; ensure `DB_PASSWORD` and JDBC URL are set.
- Check `logs/telemetry.log` for evaluator run entries.


# Historical Note
This file originated during MVP planning. Use README.md and docs/deployment.md for current runtime/deployment instructions.

# CodeForge MVP Release Project

This GitHub Project tracks the minimum set of features, quality gates, and deployment steps required to ship the CodeForge MVP.

## Project Name
CodeForge – MVP Release

## Project Description
Deliver a working MVP of CodeForge that supports:
- Cognito-based login (OIDC)
- Client-side pagination via jQuery CDN
- Drill submission flow wired to scheduling logic (stub runner)
- Docker deployment readiness for Render
- Documentation updates and basic test coverage

The project board groups all issues labeled `project:mvp` into a single execution view. Use this label to include new work items in the MVP scope.

## Goals (Done = MVP)
- AuthN: Cognito login works locally and in Render
- AuthZ: Public vs protected routes enforced; admin pages limited
- Drill: Users can submit, persist outcomes, and advance in queue
- Pagination: Challenges list paginates client-side (no Pageable)
- Deployability: legacy platform-bundle.zip produced by CI; /actuator/health = UP
- Documentation: README, plan, and journal reflect Week 6+ changes
- Tests: Core DAO/service/web tests cover MVP flows

## Non-Goals (after MVP)
- Real code execution sandbox
- Advanced RBAC and profile management
- Structured JSON logging and dashboards
- Production DB migration and multi-tenant support

## Board Columns (suggested)
- Backlog
- Ready
- In Progress
- In Review
- Done

## Labels
- Required: `project:mvp`
- Common: `area:security`, `area:web`, `area:service`, `area:ci-cd`, `area:deployment`, `area:logging`, `feature:cognito-auth`, `feature:pagination-jquery`, `feature:drill-mode`, `area:documentation`

## Issue Templates to Use
- Feature: Cognito, route protection, client-side pagination, drill submission
- Test coverage: Drill scheduling & submission
- Tooling/CI: legacy platform bundle and health readiness
- Documentation: Week 6 updates

## Milestone Suggestions
- MVP Ready (target date): set according to your course schedule
- Post-MVP hardening and polish

## Links
- Week 6 Plan: `docs/week-6-plan.md`
- Issue seeds: `docs/week-6-issues.md`
- Labels: `docs/labels.md`
- CI workflow: `.github/workflows/build.yml`
- Health endpoint: `/actuator/health`

## How to Add Work to This Project
- Apply the label `project:mvp` to any issue/PR that should appear on the MVP board.
- Set up a Project workflow rule in GitHub Projects:
  1. Open the project → Settings → Workflows
  2. Add a rule: "When issue is added to the repository and label = project:mvp → Add to project"
  3. Optionally auto-set status to "Ready"

# CodeForge MVP – Quick Start

This guide summarizes how to run MVP features (Practice + Drill), configure flags, and run tests.

## Prerequisites
- Java 21 (Temurin recommended)
- PostgreSQL running locally (cf_test_db)
- Maven 3.9+

## Config
- Edit `src/main/resources/application.properties` for feature flags:
  - `features.practice.enabled=true`
  - `features.drill.enabled=true`
- Database settings are loaded via environment variables or `hibernate.cfg.xml`.

## Run (Tomcat)
- Build WAR:
```powershell
mvn -f "[project-root]\pom.xml" -DskipTests=true package
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
mvn -f "[project-root]\pom.xml" -DskipTests=false test
```
- Notable tests:
  - Evaluator unit tests (correct/acceptable/incorrect/missing/guard).
  - Drill auto-enrollment unit test.
  - Drill persistence test (timesSeen/nextDueAt).

## Troubleshooting
- If Practice/Drill are disabled, servlets respond with 404.
- If DB isn’t reachable, tests may fail; ensure `DB_PASSWORD` and JDBC URL are set.
- Check `logs/telemetry.log` for evaluator run entries.



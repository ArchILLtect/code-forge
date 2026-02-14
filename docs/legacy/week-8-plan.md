# Week 8 Plan - Deployment and External Services (Updated)

Source inputs: `docs/project-plan.md`, `docs/deployment.md`, `QuoteService`, Week 7 carry-overs.

Objectives (Done when)
- App is deployed to Render and connected to Neon PostgreSQL.
- DB env vars are configured (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`).
- Deployment logs show successful DB connectivity and startup.
- External API requirement is met with QuoteService plus fallback behavior.
- Docs reflect Render + Docker + Neon (no legacy managed database/previous SQL backend guidance).

Scope
- Infra: Render web service + Neon Postgres.
- App config: env-driven DB and Cognito secrets.
- Deployment: Docker-based deployment from this repo.
- External API: QuoteService reachable in app flow.

Out of scope
- Flyway/Liquibase migrations (post-MVP).
- Advanced observability overhaul.

Plan
1) Provision Neon
- Create project/database.
- Capture host, db name, user, password.
- Confirm SSL-required Postgres endpoint.

2) Configure Render environment
- Set:
  - `APP_ENV=prod`
  - `COGNITO_CLIENT_SECRET`
  - `DB_HOST`
  - `DB_PORT=5432`
  - `DB_NAME`
  - `DB_USER`
  - `DB_PASS`

3) Deploy and verify
- Deploy latest commit.
- Validate app boot and core routes.
- Verify DB tables and write paths (drill submissions).

4) External API validation
- Confirm quote retrieval and fallback behavior in home flow.

5) Documentation pass
- Keep `docs/deployment.md` as canonical deployment runbook.
- Ensure README references Render + Neon only.

Acceptance criteria
- [ ] Render deployment is live.
- [ ] Neon connectivity verified in logs and app behavior.
- [ ] QuoteService behavior verified.
- [ ] No active docs imply legacy hosting platform/legacy managed database/previous SQL backend as current path.


# Week 8 Plan — Deployment and External Services (Oct 20–26)

Source inputs: project-plan.md (Week 8), deployment.md (Balanced Approach), QuoteService.java (external API), Week 7 carry-overs.

Objectives (Done when)
- App is deployed to AWS and connected to an AWS RDS database.
- First deploy uses Hibernate auto-DDL to create/update schema (ddl-auto=update); after verification, flipped to ddl-auto=validate.
- Health endpoint is UP in the deployed environment; logs show successful DB connectivity and no schema errors.
- External API requirement met using QuoteService (reachable endpoint or flow invoking it) with fallback handling.
- Docs updated with environment and flip-over steps; secrets are externalized.

Scope
- Infra: AWS RDS instance (PostgreSQL or MySQL), security groups/VPC settings.
- App config: environment variable overrides for datasource and JPA.
- Deployment: deploy the Spring Boot app and verify health/logs; no Flyway during MVP.
- External API: surface QuoteService in a simple flow or endpoint for demo.

Out of scope (Week 8)
- Flyway/Liquibase migrations (post‑MVP baseline). See TODO.md → Post MVP.
- Deep logging/metrics rework; structured logging is a stretch goal.

Plan & Tasks
1) Provision RDS
- Choose engine/version (PostgreSQL 15.x or MySQL 8.x), instance class, and storage.
- Create DB (name, user, password). Save JDBC endpoint (host:port/db).
- Security groups: allow inbound from your app environment (e.g., Elastic Beanstalk instance SG).

2) Configure application for first deploy (auto-DDL create/update)
- Set environment variables in your app host:
  - SPRING_DATASOURCE_URL (e.g., jdbc:postgresql://<host>:<port>/<db>?sslmode=require)
  - SPRING_DATASOURCE_USERNAME
  - SPRING_DATASOURCE_PASSWORD
  - SPRING_JPA_HIBERNATE_DDL_AUTO=update
- Confirm application.yml defaults: dev/test uses H2 and already has jpa.hibernate.ddl-auto: update; prod overrides come from env.

3) Deploy and verify
- Deploy the app (Elastic Beanstalk Tomcat/Java platform or another host).
- Verify:
  - GET /actuator/health → UP
  - Check RDS tables exist: challenges, submissions, drill_items, including drill_items.version.
  - Trigger a simple write path (e.g., Drill flow) to ensure DB updates work.

4) Flip to validate
- After schema verification, change SPRING_JPA_HIBERNATE_DDL_AUTO=validate.
- Redeploy; app should validate schema at startup and not modify it.

5) External API: QuoteService
- Existing QuoteService fetches a quote from https://zenquotes.io/api/random with fallback on error.
- For Week 8 demo:
  - Option A (no code change): invoke QuoteService from an existing page/controller if already wired.
  - Option B (tiny addition): add a controller endpoint /quote to display getRandomQuote() (if needed for demo visibility).
- Config: Optional override via app.quote.apiUrl; defaults to zenquotes.

6) Documentation
- Use docs/deployment.md as the live runbook for RDS + auto-DDL flip.
- Update README and Week 8 evidence with deployed URL and screenshots.

Acceptance criteria
- [ ] App deployed and reachable; health endpoint returns UP in AWS.
- [ ] RDS schema present and correct; drill_items.version column exists.
- [ ] ddl-auto is set to validate in the deployed environment after verification.
- [ ] A page or endpoint demonstrates the external API call (QuoteService) with graceful fallback.
- [ ] No secrets in repo; all credentials via environment.
- [ ] Documentation (deployment.md + README) updated with env vars and flip steps.

Risks & mitigations
- Network/Security: RDS not reachable → verify SG/VPC peering, correct JDBC URL/port.
- Permissions: insufficient CREATE/ALTER on first run → grant privileges or pre-create schema.
- Drift: leaving ddl-auto=update in prod → flip to validate once verified; adopt Flyway post‑MVP.
- API availability: external API failures → QuoteService already logs and returns a fallback string.

Evidence to capture
- Screenshot: /actuator/health = UP (deployed env)
- Screenshot: RDS table list (drill_items with version column)
- Log snippet: successful DB connection and Hibernate validation
- Screenshot: page/endpoint showing a fetched quote (or fallback) from QuoteService

Appendix: Quick local smoke (Windows cmd)
- Example one‑shot run against RDS using env vars:

```bat
cmd /c 'set "SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db>?sslmode=require" ^
&& set "SPRING_DATASOURCE_USERNAME=<user>" ^
&& set "SPRING_DATASOURCE_PASSWORD=<pass>" ^
&& set "SPRING_JPA_HIBERNATE_DDL_AUTO=update" ^
&& mvn -q spring-boot:run'
```

After verification, switch SPRING_JPA_HIBERNATE_DDL_AUTO to validate for deployment.



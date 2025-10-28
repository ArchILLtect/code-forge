# Rubric Mapping — CodeForge (Week 7)

Last updated: 2025-10-28

This document explicitly maps the repo state to rubric items for DB, DAO, logging, and tests.

## Database (Entities + Schema)
- Entities:
  - Challenge — `src/main/java/me/nickhanson/codeforge/entity/Challenge.java`
  - Submission — `src/main/java/me/nickhanson/codeforge/entity/Submission.java`
  - DrillItem — `src/main/java/me/nickhanson/codeforge/entity/DrillItem.java`
- Seed data:
  - `src/main/resources/data.sql` seeds CHALLENGES, SUBMISSIONS, and DRILL_ITEMS (IDs 1–3), sufficient for local dev/test.

## DAO (CRUD) + Transactions
- DAOs (manual JPA via EntityManager):
  - ChallengeDao — `src/main/java/me/nickhanson/codeforge/persistence/ChallengeDao.java`
  - SubmissionDao — `src/main/java/me/nickhanson/codeforge/persistence/SubmissionDao.java`
  - DrillItemDao — `src/main/java/me/nickhanson/codeforge/persistence/DrillItemDao.java`
- Transaction boundaries:
  - DAOs are annotated `@Transactional(readOnly = true)` by default with write methods (`save`, `deleteById`) annotated `@Transactional`.
  - Services (`DrillService`, `ChallengeService`) apply `@Transactional` at method level (readOnly for queries, write for mutations).

## Logging Framework
- Log4J2 configured at `src/main/resources/log4j2.properties`.
- No `System.out.println`/`printStackTrace` found in main sources (validated via grep in workspace; occurrences only in docs/tests snippets).

## Tests (Unit/Integration)
- DAO Tests (JUnit 5):
  - DrillItemDaoTest — `src/test/java/me/nickhanson/codeforge/persistence/DrillItemDaoTest.java`
  - SubmissionDaoTest — `src/test/java/me/nickhanson/codeforge/persistence/SubmissionDaoTest.java`
- Service/Web Tests (selected):
  - DrillServiceTest — `src/test/java/me/nickhanson/codeforge/service/DrillServiceTest.java`
  - AuthGuardFilterTest — `src/test/java/me/nickhanson/codeforge/web/AuthGuardFilterTest.java`
- Evidence (Surefire reports indicate all green):
  - `target/surefire-reports/me.nickhanson.codeforge.persistence.DrillItemDaoTest.txt`
  - `target/surefire-reports/me.nickhanson.codeforge.persistence.SubmissionDaoTest.txt`
  - `target/surefire-reports/TEST-me.nickhanson.codeforge.service.DrillServiceTest.xml`
  - `target/surefire-reports/me.nickhanson.codeforge.web.AuthGuardFilterTest.txt`

## Summary vs. Rubric (DB, DAO, Logging, Tests)
- DB: Entities present and seeded → Meets
- DAO: CRUD present and unit-tested → Meets
- Logging: Log4J2 in place; no System.out/printStackTrace → Meets
- Tests: Unit/integration tests present and green (coverage thresholds TBD) → Meets for Week 7 scope

## Optional Hardening (tracked separately)
- Add optimistic locking (`@Version`) for DrillItem to prevent lost updates.
- Add unique constraint on `drill_items.challenge_id` to prevent duplicates; handle concurrently-created rows gracefully in `getOrCreateDrillItem`.
- Prefer `@Transactional` only in service/DAO layers (remove from controllers) to clarify boundaries.


# Week 7 – Project Checkpoint 2 (Copy/Paste Issue Content)

Use this content with the "Project Checkpoint" issue template. Copy the Title into the title field, then paste the rest into the body/fields.

---

- Title
  - checkpoint: 2 – Database/DAO/Tests/Logging (Week 7)

- Checkpoint number
  - 2

- Due week/date
  - Week 7 (Oct 13–19, 2025)

- Scope per rubric / plan
  - Database designed and created
  - At least one DAO with full CRUD using Hibernate/JPA
  - DAO is fully unit tested
  - Log4J is implemented (no System.out.println)

- Checkpoint 2 – requirements (check these once verified)
  - [ ] Database designed and created (JPA config and tables present)
  - [ ] At least one DAO with full CRUD implemented using Hibernate/JPA
  - [ ] DAO unit/integration tests passing
  - [ ] Log4J implemented (no System.out.println)

- Evidence (links, screenshots)
  - H2 console screenshot showing CHALLENGE table and row count
  - Test summary (CI or local): `mvn -B -DskipTests=false test`
  - Surefire report(s):
    - `target/surefire-reports/TEST-me.nickhanson.codeforge.persistence.ChallengeDaoTest.xml`
  - Log4J config file reference: `src/main/resources/log4j2-spring.xml`
  - Health endpoint screenshot: `GET /actuator/health` returns `UP`

- References
  - Plan: `docs/project-plan.md` (Week 7 – Checkpoint 2)
  - Issue seeds: `docs/week-6-issues.md`
  - README (logging + stack notes): `README.md`

- Demo plan (live)
  1) Start app: `mvn spring-boot:run`
  2) Show `/actuator/health` → status `UP`
  3) Open H2 console → run `SELECT COUNT(*) FROM CHALLENGE;`
  4) Run tests: `mvn -B -DskipTests=false test` → show green summary
  5) Show logging output on startup (Log4J console appender)

- Approval checklist
  - [ ] All checkpoint requirements checked above
  - [ ] Evidence attached (screenshots, links)
  - [ ] References added (plan, PRs/CI)
  - [ ] Ready for review

- Suggested labels (auto-applied by templates/automation in this repo)
  - `checkpoint`, `status:triage`, `project:mvp`, `priority:P1-high`, `area:persistence`, `area:logging`


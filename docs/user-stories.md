<h2 align="center">Code Forge - User Stories</h2>

**Format:** As a [role], I want [capability], so that [benefit]. Each story has acceptance criteria (AC). MVP indicates Minimum Viable Product scope.

<div style="text-align: center;">
  <img src="https://img.shields.io/badge/-MVP%20🏆-gold?style=for-the-badge" alt="MVP Badge"/>
</div>

### Learner:

As a learner, I can view a list of challenges so that I can pick one to practice.

AC: Challenge cards show title, difficulty, short blurb, status (Not Started/In Progress/Passed).

As a learner, I can open a challenge detail to read the prompt, examples, and constraints.

AC: Detail page shows description, input/output specs, sample cases.

As a learner, I can submit Java code and see pass/fail results against tests.

AC: Server compiles/runs code in a sandbox; returns test results list with messages and timing.

As a learner, I can view my submission history per challenge to learn from attempts.

AC: Table lists datetime, status, and link to diff of last change (basic plain‑text okay).

As a learner, I can mark a challenge “Ready to Drill” after I’ve attempted it, so I can add it to my drill queue.

AC: Button toggles drill flag; appears in Drill queue if not passed 2x consecutively.

As a learner, I can run a Drill session that serves 3–5 resurfaced challenges I missed before.

AC: Drill session pulls from learner’s weak set, records score, and updates next‑review time.

As a learner, I can sign in locally to persist my progress.

AC: Basic username/password; session; ability to sign out.

### System/Technical:

As the system, I persist learners, challenges, attempts, and drill schedule via JPA/Hibernate.

AC: Entities mapped; CRUD DAO for at least one core entity; test DB seeded for demos.

As the system, I log important events (auth, submission run, DAO ops) using SLF4J+Log4j.

AC: No System.out; logs at INFO/WARN/ERROR; separate dev/test appenders.

As the system, I run DAO tests so that data access is reliable.

AC: JUnit tests cover happy paths and key edge cases; use test DB + rollback or truncation.

---

<div style="text-align: center;">
  <img src="https://img.shields.io/badge/-MVP%20🏆-gold?style=for-the-badge" alt="MVP Badge"/>
</div>

### Admin:

As an admin, I can create/edit challenges with title, prompt, tags, difficulty, and reference tests.

AC: Form validates fields; saves to DB; versioning optional.

As an admin, I can view basic analytics (top attempted, average pass rate).

AC: Read‑only dashboard with simple charts or tables.

---

<h2 align="center">Summary</h2>

### MVP (Minimum Viable Product):
Stories 1-10.

### Stretch (post‑MVP):

Stories 11 & 12.
Plus leaderboard, tags/search, OAuth2 login, richer code diff, rate limiting, CI/CD.

---
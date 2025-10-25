# Journal – CodeForge Project

## Week 3 – Fall 2025

**9/21/25**

This week I focused on getting the *CodeForge* project organized and aligned with the Enterprise Java class requirements. I created and refined several key project artifacts:

- Converted the **project rubric, overview, and course calendar** into clean Markdown files so they can live inside the repository (`/docs/`). This makes them easy to reference and ensures my project documentation matches the class expectations.
- Drafted a polished **README.md** for CodeForge that clearly explains the project purpose, roadmap, and tech stack.
- Designed a detailed **ProjectPlan.md** starting from Week 4 (current week) through Week 14. The plan separates *Class* tasks and *Indie* (CodeForge) tasks so I can track both at once.
- Compared the plan against my CodeForge vision (Practice Mode, Drill Mode, Progress Tracking, Admin interface, etc.) and then revised it to ensure nothing unique to the project was missing. This added items like **Drill Mode persistence and cycling**, **Admin challenge management**, a **Progress Tracking Dashboard**, and even a **CI/CD pipeline step** for testing and deployment hardening.
- Troubleshot IntelliJ IDEA’s *Non-Project Files Protection* warning when I moved files into `/docs/`. After some head-scratching, I discovered the IDE needed a full restart to re-index and properly recognize the moved files. Good reminder that IDEA sometimes caches aggressively!

Overall, I feel much more confident about the direction of the project. I now have a clear roadmap that aligns with the class rubric and also delivers on my original CodeForge vision: a simpler, friendlier coding practice platform with both Practice and Drill Modes.

### Reflection
This week was heavily documentation- and planning-focused, but that work was essential. The plan now shows a clear path from Week 4 to Week 14 that should both meet the Enterprise Java rubric and result in a working MVP of CodeForge. My biggest snag was IntelliJ’s project file indexing, which cost some time until I realized a restart solved it. Next week, I’ll move into actual Hibernate work and database schema setup, per the plan.

---

## Week 4 – Fall 2025

**9/28/25**

This week I focused on Project Checkpoint 1 for the Indie project and Hibernate for the class. Key activities included:
- Completion of Project 1 Checkpoint, which meant aligning the CodeForge repo with the course rubric and ensuring my initial documentation (overview, plan, rubric, calendar) was in place.
- Drafting a polished README.md and project documentation set, so the project has a professional landing page and clear supporting files.
- Designing a detailed pre-commit guardrail and CI workflow setup, making sure important files like backend-ci.yml can’t be accidentally modified without explicit overrides.
- Comparing the branching strategies between CodeForge and SameBoat, and deciding how to consistently handle main, dev, and feature branches across both projects.
- Troubleshooting IntelliJ indexing issues, Copilot auto-generating duplicate CI workflows, and GitHub rejecting pushes to main due to repository rules, which forced me to dig deeper into Git fundamentals and pre-commit hooks.

Overall, I feel much more confident in my ability to manage a structured Git workflow, protect critical project files, and debug tricky tool issues. Although it was frustrating at times, I walked away with a stronger understanding of branches, PRs, and workflow enforcement, which will be essential as CodeForge grows.

>### Note on Vulnerabilities:
>Vulnerabilities were flagged in transitive dependencies of Spring Boot and related libraries. Since this project is running in a local development/test environment (not exposed to the public), they are not exploitable in this context. I am acknowledging them here, but for course scope I am not remediating or overriding versions at this time. In a production scenario, I would upgrade to patched versions or apply dependency overrides as recommended.

---

### Reflection Points
Week 4 Reflection – Checkpoint 1

**Wins:** Boot app runs, H2 console enabled, Challenge entity mapped, Spring Data repository with CRUD, JPA tests passing, logging via SLF4J (no `System.out`).

**Blockers:** POM cleanup (legacy deps), config gotcha (edited `application-test.yml` instead of `application.yml`) and jar lock on Windows—resolved.

**Decisions:** Base package `me.nickhanson.codeforge`; H2 for dev/test; enums for Difficulty; Boot + JPA + JSP/JSTL stack (updated from Thymeleaf to align with course requirement for JSP and reduce learning curve).

**Next:** Add Submission/DrillItem DAOs, wire simple controllers/views, API spike.

### Cross-Course Reflection
Working on both Enterprise Java and CodeForge this week highlighted some strong parallels. In Ent Java, I learned how Hibernate manages entities, abstracts SQL, and enforces safe practices like separating test and dev databases. In CodeForge, I set up guardrails in Git (branch protections, pre-commit hooks, CI workflow checks) to keep the repo stable. Both experiences emphasized the importance of separating environments, anticipating errors, and building in safety nets so that testing doesn’t accidentally corrupt production.

Another similarity was the challenge of working with complex tooling. Hibernate’s configuration and silent warnings reminded me of Git’s quirks with branches, merges, and IntelliJ indexing issues in CodeForge. In both cases, I had to slow down, experiment, and build a mental model of how the system works. The key takeaway is that whether I’m dealing with data persistence or source control, success comes from the same habits: isolating states, protecting critical paths, and carefully troubleshooting when things don’t behave as expected.

---

## Week 5 – Fall 2025

**10/05/25**

This week I wrapped up the core Challenge management and error-handling work and aligned the docs/tests with the current app state.

Highlights
- Challenge admin CRUD completed (create, edit, delete) with validation (@Valid), unique title checks, friendly field errors, and flash messages.
- Challenges list/detail delivered with sorting/filtering and initial server-side pagination; added simple UI indicators (▲/▼) and page-size selector.
- Centralized error handling via @ControllerAdvice with JSP error pages for 404 and 500 (correct HTTP statuses).
- Logging: Log4J2 console appender configured; clean INFO defaults across the app.
- Service layer: Introduced ChallengeService to keep controllers thin and consolidate business logic.
- Data layer: Added Submission and DrillItem DAOs (repos) plus tests; expanded data.sql to seed realistic dev data.
- Fixes: H2 console 404 resolved; JSTL taglib issue fixed by adding Jakarta JSTL and correcting JSP taglib usage; clarified test profile behavior (create-drop, no dev seed load).

Testing and docs
- WebMvc tests cover list, 404, new, create (ok/duplicate), update (ok/duplicate), delete (ok/not-found), and 500 handler.
- JPA tests for Challenge, Submission, and DrillItem are green.
- Updated README and docs (project-plan, week-5-plan) to reflect delivered features and current expectations.

Pushed to Week 6
- Drill logic service layer: scheduling/nextDueAt updates, streak/timesSeen handling, and drill queue orchestration.
- Submission UI/flow and a basic run/execute stub to record a Submission and update a DrillItem.
- CI cleanup and switching from server-side pagination to jQuery-based pagination via CDN per course requirement.

Reflection
- Win: Admin CRUD and error pages feel solid; tests provide good coverage; seed data makes the UI useful.
- Adjustment: Pagination approach will move to jQuery/CDN in Week 6 to match class goals.
- Next: Implement DrillService basics, wire a minimal submit flow, and align CI/pagination changes.

---

## Week 6 – Fall 2025

**10/12/25**

This week I wrapped up authentication (MVP), protected key routes, finished client-side pagination, got CI artifacts building, and deployed the WAR to Elastic Beanstalk Tomcat.

Highlights
- Auth (MVP): Cognito Hosted UI login via servlet flow, ID token verification, user stored in session; `/me` displays user; `/logout` clears session.
- Authorization: Added `AuthGuardFilter` to protect challenge create/edit/delete and edit form; added JSP guards to hide Create/Edit/Delete for logged-out users.
- Bugfix: Fixed a JSP EL typo on the challenges list page (`pageContext.rbequest` → `pageContext.request`) that caused a 500.
- Pagination: Switched to client-side pagination with jQuery DataTables on `/challenges`.
- CI: GitHub Actions workflow (`.github/workflows/ci.yml`) builds and uploads the WAR; latest run is green.
- Deploy: Deployed the WAR to EB Tomcat (Corretto 17); `/actuator/health` returns `UP`.
- Secrets: Kept `COGNITO_CLIENT_SECRET` in EB environment variables for now; secret-hygiene improvements tracked in a separate issue.

Reflection
- Guardrails in two layers help: server filter prevents unauthorized access; UI guard avoids confusing the user. Tests around the filter gave confidence when tightening routes.
- Small typos can break JSP pages; I’ll keep leaning on tests and quick greps to catch these quickly.
- Shipping the WAR via CI and proving it live on EB felt like a good end-to-end checkpoint for Week 6.

Evidence (screenshots)
- `docs/screenshots/week6-auth-login.png`
- `docs/screenshots/week6-challenges-list.png`
- `docs/screenshots/week6-eb-health-up.png`

Next
- Week 7: Wire the Drill submission UI/flow to create Submissions and update DrillItem; add service/web tests for outcomes and queue advance.

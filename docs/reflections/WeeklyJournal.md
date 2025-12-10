# Weekly Journal (Weeks 1–16)

---

## **Week 1 (Sept 2–Sept 7)**
**A late start and a scramble to catch the train.**  
I didn’t actually begin the course on Day 1 because I was still getting back into town. When I finally logged into Blackboard and realized how much tooling we were expected to have ready, the week became a crash course in “bootstrapping my entire environment before I fall behind.”

### Highlights
- Installed **JDK, IntelliJ, Tomcat, SQL tools**, and verified all system paths.
- Set up my initial project workspace, Git config, and tested a bare-minimum servlet.
- Tried to recreate the standard “Hello World” servlet… and somehow crashed IntelliJ twice. Good start.

### Reflection
I felt behind right out of the gate, but getting the tools working gave me enough confidence to breathe. The class still felt abstract at this point — like I was standing on the edge of a very tall pool waiting to jump in.

---

## **Week 2 (Sept 8–Sept 14)**
Still playing catch-up, but by midweek I finally felt like I was in the same galaxy as everyone else. This week was about grounding myself in the fundamentals the rest of the semester would rely on.

### What Went Well
- Revisiting **JDBC basics** with fresh eyes; it made more sense now than when I learned it the first time.
- Using the **IntelliJ debugger** to actually watch variables change in real time — something I avoided in earlier classes.
- Relearning Git flow so I didn’t embarrass myself when commits started piling up.

### What Was Rough
- Some lectures referenced concepts I hadn’t reviewed yet because of the late start.
- I kept misconfiguring Tomcat ports — foreshadowing, apparently, for future “port already in use” nightmares.

### Reflection
This week was more foundational than inspirational, but necessary. I finally felt caught up enough to begin real project work.

---

## **Week 3 (Sept 15–Sept 21)**

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

## **Week 4 (Sept 22–Sept 28)**

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

## **Week 5 (Sept 29–Oct 5)**

This week I wrapped up the core Challenge management and error-handling work and aligned the docs/tests with the current app state.

Highlights
- Challenge admin CRUD completed (create, edit, delete) with validation (@Valid), unique title checks, friendly field errors, and flash messages.
- Challenges list/detail delivered with sorting/filtering and initial server-side pagination; added simple UI indicators (▲/▼) and page-size selector.
- Centralized error handling via @ControllerAdvice with JSP error pages for 404 and 500 (correct HTTP statuses).
- Logging: Log4J2 console appender configured; clean INFO defaults across the app.
- Service layer: Introduced ChallengeService to keep controllers thin and consolidate business logic.
- Data layer: Added Submission and DrillItem DAOs (repos) plus tests; expanded cleandb.sql to seed realistic dev data.
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

## **Week 6 (Oct 6–Oct 12)**

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

---

## **Week 7 (Oct 13–Oct 19)**

This week I delivered the Drill submission flow and tightened security around Drill routes.

Highlights
- Implemented Drill flow end-to-end: queue page → next redirect → solve page → submit.
- Submission now persists outcomes and updates scheduling via `DrillService` and `ChallengeRunService` (Issue 27).
- Protected all Drill routes with `AuthGuardFilter` (GET `/drill`, `/drill/*`, POST `/drill/*/submit`); added tests for unauthenticated redirects and authenticated pass-through (Issue 28).
- Verified the full test suite is green locally.

Reflection
- Wiring the submit flow uncovered a few edge cases (e.g., streak resets on `INCORRECT`, null `nextDueAt` ordering). Dedicated service tests helped lock these down.
- The filter-based guard plus JSP visibility checks continue to provide a good UX and secure defaults.
- Next step is to expand Drill Mode persistence/metrics and start prepping for the Week 9 deployment criteria.

Evidence
- Test reports show green for DrillItem/Submission DAOs and services:
  - `target/surefire-reports/me.nickhanson.codeforge.persistence.DrillItemDaoTest.txt`
  - `target/surefire-reports/me.nickhanson.codeforge.persistence.SubmissionDaoTest.txt`
  - `target/surefire-reports/TEST-me.nickhanson.codeforge.service.DrillServiceTest.xml`
  - `target/surefire-reports/me.nickhanson.codeforge.web.AuthGuardFilterTest.txt`

## **Week 8 (Oct 20–Oct 26)**
This week was all about tightening up Drill Mode after finally getting the full end-to-end flow working. Even though the main pieces were in place last week, the system still felt fragile — especially around how Drill Items advanced from queue → solve → submit. I spent most of my energy polishing that flow and hunting down edge cases (because of course there were edge cases). The AuthGuardFilter also got stricter, which immediately revealed a couple of places where I was implicitly trusting the session. Not anymore.

On the Ent Java side, Web Services Intro was a nice break from the nonstop CodeForge grind. It honestly helped connect some dots, seeing how what we were building mapped onto real REST concepts. Overall a productive, steady week.

### Real Challenges This Week
- The **AuthGuardFilter** was not consistently enforcing rules across ALL drill endpoints. Fixing that meant auditing every servlet.
- Ran into a weird issue where the browser cached responses so aggressively that it looked like my logic was broken… until I hard-refreshed.

### Ent Java Connection
The Web Services Intro helped reframe my thinking: “What CONTRACT does a Drill submission represent?” Suddenly the responsibilities made more sense.

---

## **Week 9 (Oct 27–Nov 2)**
A UI week, whether I planned it or not. The solve/queue screens were functional, but not intuitive — and it bothered me more than I expected. It felt like every time I smoothed out a UI corner, a backend bug poked its head up and asked to be fixed. But by the end of the week, the solve/queue UI finally matched the mental model I’ve been shooting for since the start — intuitive, minimal, and not fighting the user.

Ent Java Week 9 content overlapped nicely with CodeForge. RESTful services in class + RESTful services in my app = chef’s kiss. Being able to apply course concepts directly to my project definitely helped the material stick.

### Highlights
- Fixed weird CSS rules that were overriding each other (including one case where inline styles were winning over IDs and I wanted to scream).
- Cleaned up the Drill UI so users wouldn’t constantly wonder where to click next.
- Documentation passes — just paying it forward so Week-13-me isn’t crying later.

### Ent Java tie-in
REST concepts from Week 9 synced perfectly with the architecture choices I had already made. It felt like the class and my project were finally walking in sync.

---

## **Week 10 (Nov 3–Nov 9)**

### **The infamous “Spring Purge.”**

I deleted Spring Boot from the entire project and replaced it with hand-wired servlets. Most developers don’t willingly do this. I did. On purpose. This week deserves the award for “Most Chaotic but Somehow Productive Week.”  
I ripped out **all of Spring Boot** and migrated the project to **pure servlets**. It felt like open-heart surgery, except I was the patient, the surgeon, and the guy holding the flashlight. But surprisingly, it worked — AppBootstrap booted cleanly, ChallengeServlet and DrillServlet were functional, and the app felt lighter without all the Spring scaffolding.

On the course side, this was a “work week,” which was honestly perfect timing. I needed every ounce of that buffer. The Team Project also kicked into higher gear, and I ended up doing a ton of repo setup work. Easily one of the heaviest weeks so far, but also one of the most rewarding.

### Why I Tore It All Out
This one’s on me. I treated the Indie Project like a modern greenfield build, so I defaulted to Spring Boot. But this is *Enterprise Java* — the whole point is learning what frameworks normally hide. Boot was doing so much behind the scenes that I wasn’t actually learning servlets, filters, DI, or manual routing. I realized I had built a wall between myself and the fundamentals.

So I did the only thing that made sense: tear down the wall and start fresh.

### Chaos Log
- AppBootstrap started working only AFTER I stopped thinking about why it wasn’t working.
- I rewrote routing, DI-ish logic, and controller dispatch in one very long night.
- Accidentally broke my WAR packaging, then fixed it by sheer stubbornness.

### Team Project
This week also kicked off serious group work. Repo setup, environment alignment, and expectations-setting took more time than expected.

### Reflection
Exhausting week. But I learned more here than in any “clean tutorial” ever written.

---

## **Week 11 (Nov 10–Nov 16)**
If last week was surgery, this week was physical therapy. I cleaned up the lingering issues from ripping out Spring, redesigned large portions of the UI, and rebuilt logic in more servlet-friendly ways. Practice Mode finally started to look real, not just like a placeholder idea taped onto the navbar.

The Team Project absolutely dominated my schedule this week — testing suites, meetings, endpoint integration, you name it. For a while it felt like I was switching between two worlds: CodeForge (my “solo child”) and the group project (where everything required coordination). But both moved forward significantly.

Ent Java videos this week were also a good reset — Hibernate review + project work aligned nicely with what I was already doing.


### What Got Fixed
- Residual Spring references that quietly sabotaged servlet behavior.
- UI layouts that had drifted after earlier refactors.
- Early Practice Mode started to take shape.

### Team Project Drama (the good kind)
- We hit **recursive serialization** issues and solved them using `@JsonIgnore`.
- Paula asked the famous question:  
  *“But how are we supposed to get the schedule for Truck 1?”*  
  …which revealed a misunderstanding in our data-model assumptions.

### Personal Note
This was the week I finally felt comfortable “living” in the CodeForge codebase.

---

## **Week 12 (Nov 17–Nov 23)**
This week was calmer, but only by comparison. I spent time refining QuoteService, stabilizing performance, and updating CodeForge documentation so things wouldn't fall apart later. The team project presentation prep also took a surprising amount of brainpower — turning technical work into a coherent narrative isn’t easy, but it does force you to reflect on what you’ve learned.

Ent Java Week 12 focused on presentations and Hibernate/JDBC review. Honestly, it was nice to slow down and look at the bigger picture for a bit. I somehow managed to take what started as an over-complex 33-minute watch and condense it into a tight, clean 12-minute narrative. Definitely a skill I need to keep practicing.


### Wins
- Standardized timestamps and schema rules in CodeForge.
- Documentation updates so thorough that future me might weep in gratitude.
- Presentation scripting helped me see how far the Team Project had come.

### Ent Java
Reviewing Hibernate and JDBC again brought clarity to some design choices I had made subconsciously.

---

## **Week 13 (Nov 24–Nov 30)**
Thanksgiving week… but CodeForge did not take a holiday.  
This was a massive testing and cleanup push: migrating fully to MySQL, standardizing the schema, resetting seeds, cleaning up timestamps, and improving test isolation. It felt like preparing a battlefield for the final sprint.

The UI also got some nice quality-of-life improvements — smoother layouts, better date formatting, consistent navigation. The app is finally starting to feel “real.”

Ent Java Week 13 was about asynchronous messaging — challenging but interesting, especially considering what large-scale evaluation systems would require in the real world.


### Major Work
- Complete migration to **MySQL-only** testing.
- Cleaned up seeding logic and ensured test isolation.
- Fixed seeds that unexpectedly mutated state between tests.
- UI polish and nav consistency.

### Real-Life Debug Moment
At one point, all drill IDs suddenly came back as `null`.  
Spoiler: I forgot a form field name.  
Resolution time: 45 minutes.  
Emotional journey: 3 hours.

---

## **Week 14 (Dec 1–Dec 7)**
This week was pure momentum. More MySQL test migrations, DaoTestBase setup, workflow cleanups, and a surprisingly large set of UI fixes. LocalConfig + SessionFactoryProvider also fell into place, which unclogged several configuration headaches I’d been dealing with.

Ent Java Week 14 focused on code reviews and peer feedback. Fortunately, one of my teammates was available for me to team up with, so we did a deep dive into each other’s codebases. It was enlightening to see how others approached similar problems.


### Work Completed
- Full cleanup of CI pipelines.
- H2 removed entirely, replaced with MySQL test infra.
- SessionFactoryProvider became a first-class citizen instead of a gremlin.
- UI smoother than ever before.

### Ent Java
Peer review week — reading others’ code helped highlight how far I’d come since September.

---

## **Week 15 (Dec 8–Dec 14)**
This was probably the single biggest feature-delivery week of the semester. A nonstop wave of CodeForge improvements: user_id propagation, expected-answer logic, evaluator pipeline, telemetry logging, Practice Mode v2, and Monaco editor integration. It felt like the whole app leveled up several versions at once.

Even though the semester is wrapping up, this week made the project feel alive — like something I would actually continue developing beyondclass — and that’s a great feeling. I now know for sure that I will continue refining and expanding CodeForge long after the semester ends.

### What Shipped
- user_id propagation
- expected answer support
- evaluator pipeline
- Normalizer v1
- telemetry logging
- Practice Mode v2 redesign
- Monaco editor integration

### Favorite Moment
I tested Drill Mode after wiring the evaluator and shouted:  
**“HOLY SHIT IT WORKED!!”**  
Because I had expected to have to do a bunch of work debugging and additional building. It was the first moment the system *felt alive.*

---

## **Week 16 (Dec 15–Dec 21)**
The final week carried big “tie up loose ends” energy. Security polishing, UTF-8 fixes, logout cleanup, final UI refinements, documentation passes, and deployment checks. All the little tasks that don’t look impressive in a commit history but absolutely matter when shipping a real product.

Ent Java final reflections and wrap-up gave me a chance to think about how much ground I covered — from servlets and JDBC to Hibernate, AWS, CI pipelines, and real multi-page web app development. Easily one of the most demanding courses I’ve taken, but also one of the most rewarding.


### Finishing Touches
- UTF-8 filter fix
- logout flow correction
- model consistency updates
- deployment checks
- reflection writing
- documentation cleanup

### Emotional Arc
By this week, the project felt real — not like a homework assignment, but something I *built*.

---

## ⭐ Looking Back: What I’d Do Differently ⭐

If I could restart this semester from the beginning — knowing everything I learned the hard way — there are a few things I would absolutely change. Not because I regret how things turned out (honestly, the chaos made me better — and anyone who **_knows_** me knows that I am a staunch believer in the positive gained from 'trauma'), but because I wasted time fighting problems I could have sidestepped with a clearer head.

### 1. I would start with servlets on Day One instead of Spring Boot.

Spring Boot was a security blanket at first — familiar, modern, powerful.
The problem was that it hid everything this course actually wanted me to understand:

- request/response lifecycle
- servlet behavior
- filters
- manual dependency wiring
- configuration scope

By the time I ripped Boot out, I basically had to relearn the entire web stack “from scratch,” which meant rewiring large portions of CodeForge under pressure. If I could redo things, I’d embrace “bare metal” servlets from the start and let the clarity sink in early.

### 2. I’d give myself more breathing room on deployment and AWS setup.

AWS has a knack for behaving perfectly or breaking in ways that feel personal. Elastic Beanstalk in particular seems to wait until I'm confident… then throws a new error format at me. At this point, I’ve simply accepted this as part of the AWS experience.

Things like:

- UTF-8 encoding mysteriously breaking
- EB health checks failing without logs
- Cognito login propagation delays
- SSL certificate embedding weirdness
- Tomcat deciding which MIME types it likes that day

None of these were impossible — but all were time-consuming.
Next time: start AWS earlier, even if the app is still ugly.

### 3. I’d keep better notes on debugging patterns.

Some bugs were “fix once, never forget.”
Others were “fix ten times, forget every time.”

Notable examples:

- JSTL EL not updating because I used the wrong scoped attribute
- DataTables binding twice because of duplicate event listeners
- Drill Mode quietly moving forward because the evaluator always returned “correct”
- That one form field name typo that cost me 45 minutes of life I will never see again

If I had kept a log of these patterns earlier, I could have recognized new bugs faster — instead of rediscovering the same categories of mistakes multiple times.

### 4. I’d trust myself earlier.

This is the non-technical one — but arguably the most important.

There were entire weeks where I assumed I **“wasn’t ready”** to do something, only to eventually do it and realize:

> I was ready — I just didn’t **_know_** I was ready.

If I gave myself that confidence sooner, the middle of the semester might have been less stressful.

### 5. I’d preserve more small wins instead of jumping straight to the next task.

I had so many “holy shit it works!” breakthroughs, but I often moved on instantly because there was always something else waiting. Those moments are part of what make programming worthwhile and honestly, that’s probably 90% of why I love programming in the first place — I want to get better at pausing long enough to enjoy them.

---

# **Final Thoughts**
Over the span of 16 weeks, I went from late-start chaos to deploying a full multi-servlet application on AWS using real CI, real authentication, real data persistence, and a custom-built evaluation engine.

I learned more from what broke than from what worked — and honestly, that’s the best part.

This was one of the most demanding courses I’ve taken, but also one of the most rewarding.  
And I’m proud of the work I put in.

### — Nick Hanson (ArchILLtect)

---


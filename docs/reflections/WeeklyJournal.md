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

This week I focused on [.........]

- Completion project 1 checkpoint.
- Drafting a polished ___ (placeholder for what you’ll fill in).
- Designing a detailed ___ (placeholder).
- Comparing the ___ (placeholder).
- Troubleshooting ___ (placeholder).

Overall, I feel ___ (placeholder).

>### Note on Vulnerabilities:
>Vulnerabilities were flagged in transitive dependencies of Spring Boot and related libraries. Since this project is running in a local development/test environment (not exposed to the public), they are not exploitable in this context. I am acknowledging them here, but for course scope I am not remediating or overriding versions at this time. In a production scenario, I would upgrade to patched versions or apply dependency overrides as recommended.

---

### Reflection
Week 4 Reflection – Checkpoint 1

**Wins:** Boot app runs, H2 console enabled, Challenge entity mapped, Spring Data repository with CRUD, JPA tests passing, logging via SLF4J (no `System.out`).

**Blockers:** POM cleanup (legacy deps), config gotcha (edited `application-test.yml` instead of `application.yml`) and jar lock on Windows—resolved.

**Decisions:** Base package `me.nickhanson.codeforge`; H2 for dev/test; enums for Difficulty; Boot + JPA + Thymeleaf stack.

**Next:** Add Submission/DrillItem DAOs, wire simple controllers/views, API spike.

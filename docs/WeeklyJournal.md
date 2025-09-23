# Journal – CodeForge Project

## Week 3 – Fall 2025

**9/22/25**

This week I focused on getting the *CodeForge* project organized and aligned with the Enterprise Java class requirements. I created and refined several key project artifacts:

- Converted the **project rubric, overview, and course calendar** into clean Markdown files so they can live inside the repository (`/docs/`). This makes them easy to reference and ensures my project documentation matches the class expectations.
- Drafted a polished **README.md** for CodeForge that clearly explains the project purpose, roadmap, and tech stack.
- Designed a detailed **ProjectPlan.md** starting from Week 4 (current week) through Week 14. The plan separates *Class* tasks and *Indie* (CodeForge) tasks so I can track both at once.
- Compared the plan against my CodeForge vision (Practice Mode, Drill Mode, Progress Tracking, Admin interface, etc.) and then revised it to ensure nothing unique to the project was missing. This added items like **Drill Mode persistence and cycling**, **Admin challenge management**, a **Progress Tracking Dashboard**, and even a **CI/CD pipeline step** for testing and deployment hardening.
- Troubleshot IntelliJ IDEA’s *Non-Project Files Protection* warning when I moved files into `/docs/`. After some head-scratching, I discovered the IDE needed a full restart to re-index and properly recognize the moved files. Good reminder that IDEA sometimes caches aggressively!

Overall, I feel much more confident about the direction of the project. I now have a clear roadmap that aligns with the class rubric and also delivers on my original CodeForge vision: a simpler, friendlier coding practice platform with both Practice and Drill Modes.

---

### Reflection
This week was heavily documentation- and planning-focused, but that work was essential. The plan now shows a clear path from Week 4 to Week 14 that should both meet the Enterprise Java rubric and result in a working MVP of CodeForge. My biggest snag was IntelliJ’s project file indexing, which cost some time until I realized a restart solved it. Next week, I’ll move into actual Hibernate work and database schema setup, per the plan.
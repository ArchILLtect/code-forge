# Historical Note
This document is kept for coursework/history context. Current deployment/database implementation is Render + Docker + Neon PostgreSQL.

# Indie Project Peer Code Review

## Project:
### Personal Food Reviewer

### Developer:
Molly McCarthy

### Reviewer:
Nick Hanson

The table below contains my rubric-aligned review of the project, including strengths ("kudos") and two actionable suggestions for improvement for each criterion.

| Category | Criteria                       | Notes / Comments                                                                                                                                                                                                                    |
|---|--------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Project Scope & Functionality | Uses all required technologies | - **Kudos:** Strong end-to-end use of Hibernate, Cognito authentication, DAO-based CRUD, and an external API integration.                                                                                                           |
|  | –                            | - **Suggestion 1:** Replace hardcoded S3 placeholders with secure environment-based credential loading.                                                                                                                             |
|  | –                            | - **Suggestion 2:** Complete the photo upload pipeline (multipart → S3 → metadata persistence).                                                                                                                                     |
|  | Meets MVP requirements         | - **Kudos:** Core flows—login, review listing, details, creation, editing, deletion, and search—are implemented cleanly via JSPs.                                                                                                   |
|  | –                            | - **Suggestion 1:** Add clear user-facing feedback (success/error) to form submissions.                                                                                                                                             |
|  | –                            | - **Suggestion 2:** Either fully include photo uploads in MVP or intentionally scope it to post-MVP to avoid partial UX.                                                                                                            |
|  | Database relationships         | - **Kudos:** Well-structured `Review` → `Photo` and `Review` → `User` relationships with proper cascade and orphan removal.                                                                                                         |
|  | –                            | - **Suggestion 1:** Initialize `photos` list to avoid NPEs when rendering JSPs.                                                                                                                                                     |
|  | –                            | - **Suggestion 2:** Consider adding a mapped collection on `User` for reverse navigation if needed.                                                                                                                                 |
|  | Deployment readiness           | - **Kudos:** WAR packaging, redirect logic, and the presence of deployment configuration folders demonstrate solid deployment preparation.                                                                |
|  | –                            | - **Suggestion 1:** Add deployment steps or the live AWS URL to the README for easier verification.                                                                                                                                 |
|  | –                            | - **Suggestion 2:** Briefly document the purpose of deployment configuration files so future maintainers understand how runtime behavior is customized.                                                                  |
| Experimentation & Innovation | Original solutions             | - **Kudos:** The “personal food diary” concept paired with a dynamic Dad-joke feature adds charm and originality. |
|  | –                            | - **Suggestion 1:** Emphasize privacy features in UI (per-user visibility, restrictions).                                                                                                                                           |
|  | –                            | - **Suggestion 2:** Add simple tagging/categorization for richer review organization.                                                                                                                                               |
|  | Beyond-class technologies      | - **Kudos:** Strong integrations beyond class scope (Cognito OAuth + JWKS, AWS S3 SDK).                                                                                                                                             |
|  | –                            | - **Suggestion 1:** Align dependency ecosystem fully with either Jakarta EE 9+ or Javax to reduce friction.                                                                                                                         |
|  | –                            | - **Suggestion 2:** Introduce advanced features (Maps, Hibernate Search) incrementally with narrow scope.                                                                                                                           |
|  | Independent troubleshooting    | - **Kudos:** JWKS retrieval and RSA signature validation are correctly implemented.                                                                                                                                                 |
|  | –                            | - **Suggestion 1:** Centralize error handling and logging for auth flow.                                                                                                                                                            |
|  | –                            | - **Suggestion 2:** Document Cognito setup or add small auth tests for reproducibility.                                                                                                                                             |
| Code Quality & Testing | Readability & organization     | - **Kudos:** Clean package structure and cohesive controllers; JSPs use includes effectively.                                                                                                                                       |
|  | –                            | - **Suggestion 1:** Move S3 logic out of controllers into a dedicated service.                                                                                                                                                      |
|  | –                            | - **Suggestion 2:** Add a small validation helper to standardize parameter parsing.                                                                                                                                                 |
|  | Documentation                  | - **Kudos:** Helpful inline comments in entities and controllers.                                                                                                                                                                   |
|  | –                            | - **Suggestion 1:** Add concise Javadoc to controller methods.                                                                                                                                                                      |
|  | –                            | - **Suggestion 2:** Expand README with setup, DB config, Cognito, and deployment instructions.                                                                                                                                      |
|  | Efficiency & clarity           | - **Kudos:** DAO abstraction reduces boilerplate; logging is consistent.                                                                                                                                                            |
|  | –                            | - **Suggestion 1:** Guard against NPEs via collection initialization and null-safe getters.                                                                                                                                         |
|  | –                            | - **Suggestion 2:** Consolidate numeric/required parameter validation.                                                                                                                                                              |
|  | Testing depth                  | - **Kudos:** DAO tests cover key persistence operations.                                                                                                                                                                            |
|  | –                            | - **Suggestion 1:** Add integration tests with mock requests/responses.                                                                                                                                                             |
|  | –                            | - **Suggestion 2:** Add basic auth flow tests or document manual verification steps.                                                                                                                                                |
| Project Planning & Milestones | Planning quality               | - **Kudos:** Strong planning artifacts—ProjectPlan, TimeLog, design documents.                                                                                                                                                      |
|  | –                            | - **Suggestion 1:** Add milestone progress indicators.                                                                                                                                                                              |
|  | –                            | - **Suggestion 2:** Link user stories directly to implemented routes/pages.                                                                                                                                                         |
|  | Meeting milestones             | - **Kudos:** MVP features align well with planned scope.                                                                                                                                                                            |
|  | –                            | - **Suggestion 1:** Mark photo upload & deployment as explicit next-phase goals.                                                                                                                                                    |
|  | –                            | - **Suggestion 2:** Track blockers and decisions explicitly in the TimeLog.                                                                                                                                                         |
|  | Resource leveraging            | - **Kudos:** Effective use of AWS services, Hibernate, JSTL.                                                                                                                                                                        |
|  | –                            | - **Suggestion 1:** Align dependency versions.                                                                                                                                                                                      |
|  | –                            | - **Suggestion 2:** Use properties for environment-specific configuration.                                                                                                                                                          |
| Revision & Feedback | Feedback incorporation         | - **Kudos:** Well-documented pivot from Food Truck API to Dad jokes.                                                                                                                                                                |
|  | –                            | - **Suggestion 1:** Capture feedback and correlate to changes made.                                                                                                                                                                 |
|  | –                            | - **Suggestion 2:** Add a simple CHANGELOG for traceability.                                                                                                                                                                        |
|  | Measurable improvements        | - **Kudos:** Test reports and screenshots show progress.                                                                                                                                                                            |
|  | –                            | - **Suggestion 1:** Provide a deployment link or verification proof.                                                                                                                                                                |
|  | –                            | - **Suggestion 2:** Add test coverage summaries.                                                                                                                                                                                    |
| Complexity | Appropriate scope              | - **Kudos:** Good full-stack scope with auth, CRUD, external API, UI.                                                                                                                                                               |
|  | –                            | - **Suggestion 1:** Fully complete or defer half-integrated features (photo/S3).                                                                                                                                                    |
|  | –                            | - **Suggestion 2:** Document boundaries to manage complexity.                                                                                                                                                                       |
|  | Concept integration            | - **Kudos:** Strong integration of authentication, CRUD, JSPs, and persistence.                                                                                                                                                     |
|  | –                            | - **Suggestion 1:** Add a service layer to improve cohesion.                                                                                                                                                                        |
|  | –                            | - **Suggestion 2:** Add small UX improvements (breadcrumbs, success messages).                                                                                                                                                      |

## Further Comments:
I really enjoyed reviewing this project. It shows not only technical competence but also creativity and personality—something I’d bet money you don’t always see in enterprise Java coursework.
The personal food diary idea is clever on its own, and pairing it with a dynamic Dad-joke integration gives the app an unexpected charm that genuinely made me smile — a feature that stood out not only because it’s fun, but because it demonstrates initiative and independent thinking.
Your use of AWS Cognito, clean DAO abstraction, and organized package structure all point to a solid foundation in the concepts we’ve learned, and the project feels thoughtfully assembled from top to bottom.
I found parts of the implementation inspiring, and I’m already planning to incorporate similar touches into my own future work.

## Suggested Quick Wins

- **Complete the photo upload pipeline end-to-end.**  
  Implement multipart form handling, finalize S3 upload logic, and persist image metadata so uploaded photos display consistently throughout the application. This single enhancement would noticeably increase feature completeness and user experience.

- **Unify Jakarta/Javax dependencies to avoid subtle runtime issues.**  
  Choose one ecosystem and update imports and library versions accordingly. This prevents classloader conflicts, reduces friction during deployment, and makes future updates more predictable.

- **Improve the entity classes by initializing collections and adding validation helpers.**  
  Initializing the `photos` collection and centralizing input validation (e.g., numeric parsing, required fields) will eliminate common null-pointer and formatting issues while making controllers leaner and easier to maintain.

- **Expand testing into the web layer with controller and authentication tests.**  
  Adding tests that simulate HTTP requests and verify expected redirects, access control, and error handling will significantly strengthen reliability and confidence when deploying updates.

- **Enhance the README with environment setup and deployment documentation.**  
Include local setup steps, Cognito configuration notes, and a brief description of deployment/runtime configuration. This helps future maintainers and ensures smooth reproduction of the environment.



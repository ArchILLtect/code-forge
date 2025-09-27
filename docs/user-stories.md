<h2 align="center">Code Forge - User Stories</h2>

**Format:** As a [role], I want [capability], so that [benefit]. Each story has acceptance criteria (AC). MVP indicates Minimum Viable Product scope.

<div style="text-align: center;">
  <img src="https://img.shields.io/badge/-MVP%20🏆-gold?style=for-the-badge" alt="MVP Badge"/>
</div>

## Learner:

1. ### Register & Sign in with Cognito:
   #### As a learner, I want to sign up, sign in, and reset my password so that I can access my account securely.

   **Acceptance:**
   - [ ] “Sign in / Sign up” opens Cognito hosted UI; successful auth redirects back with session.
   - [ ] “Forgot password” flow works via Cognito.
   - [ ] Signed-in users see their name/menu; signed-out users see Sign in.

2. ### Browse challenges:
   #### As a learner, I want to see a list of challenges filtered by difficulty/topic so that I can pick what to practice.

   **Acceptance:** list view with title, difficulty, status badge (`Not started` / `In progress` / `Solved`); basic filter by difficulty.

3. ### View challenge detail:
   #### As a learner, I want a clean prompt with examples and constraints so that I know exactly what to build.

   **Acceptance:** title, prompt (Markdown rendered), examples I/O, constraints, “Start/Resume” CTA.

4. ### Submit solution & see result:
   #### As a learner, I want to submit my code and see pass/fail + basic diagnostics so that I can learn from feedback.

   **Acceptance:**
    - [ ] POST submission stores attempt, shows result (`Correct` / `Acceptable` / `Incorrect`), shows failing test name(s) or brief hint. (Execution may be stubbed early.)
    - [ ] **Persistence:** each attempt stored with outcome + timestamp (JPA).
    - [ ] **Testing:** repository tests cover save/find/list/delete.
    - [ ] **Logging:** submission received/result emitted at INFO; failures include terse diagnostic.

5. ### Drill Mode:
   #### As a learner, I want missed/skipped items to reappear until solved, and I can optionally flag challenges as “Ready to Drill,” so that I retain better.

   **Acceptance:**
    - [ ] Queue cycles `Incorrect`/`Skipped`; once `Correct`, it leaves the loop but may resurface later.
    - [ ] Button toggles drill flag; appears in Drill queue if not passed 2x consecutively.
    - [ ] Drill session pulls from learner’s weak set, records score, and updates next‑review time.
    - [ ] **Persistence:** queue state and outcomes are stored with JPA/Hibernate.
    - [ ] **Logging:** session start/finish + persisted outcomes logged at INFO; errors at WARN/ERROR.
    - [ ] **Testing:** DAO/repo unit tests cover queue persistence and deletion side effects.

6. ### Track outcomes:
   #### As a learner, I want each attempt recorded as `Correct`, `Acceptable`, `Incorrect`, or `Skipped` so that I can see progress.

   **Acceptance:** status stored per attempt; latest status surfaced in list/detail.

7. ### View submission history:
   #### As a learner, I want to see my past attempts for a challenge so that I can learn from changes over time.

   **Acceptance:** Table lists datetime, status, and (Optional/Stretch) link to diff of last change (basic plain‑text okay).

8. ### External API: inline definitions/hints:
   #### As a learner, I want short definitions (e.g., “hash map”, “two-pointer technique”) pulled from a reputable public API so that I get quick context without leaving the page.

   **Acceptance:** on challenge detail, app calls selected public API (e.g., Wikipedia summary or similar), shows 1–2 sentence definition; if API fails, graceful fallback. (Week 5 spike picks the API; Week 8 hardens with retries/timeouts.)

---

### Admin:

9. ### Create challenge:
    #### As an admin, I want to add a new challenge so that I can grow the catalog.

    **Acceptance:**
    - [ ] Form for title, difficulty, blurb, prompt (Markdown); validation; success flash; record appears in list.
    - [ ] **Persistence:** challenge saved via JPA; unique title enforced.
    - [ ] **Testing:** repository test for uniqueness and CRUD.
    - [ ] **Logging:** create/update/delete events logged at INFO.

10. ### Edit challenge:
    #### As an admin, I want to update an existing challenge so that I can fix or improve it.

    **Acceptance:**
    - [ ] Edit form prefilled; validation; success flash; changes visible in list/detail.
    - [ ] **Policy documented:** edits update challenge prompt only; existing attempts are preserved.
    - [ ] **Persistence:** updates saved via JPA.
    - [ ] **Testing:** repository test for update.
    - [ ] **Logging:**  edit events logged at INFO.


11. ### Delete challenge:
    #### As an admin, I want to delete a challenge so that I can remove duplicates or test content.

    **Acceptance:**
    - [ ] Delete requires confirm dialog; success flash; record disappears from list; linked attempts remain (or policy noted). (Soft-delete acceptable—document behavior.)
    - [ ] **Policy documented:** either soft-delete or hard-delete with attempt retention.
    - [ ] **Testing:** verify deletes don’t orphan required data.
    - [ ] **Logging:** delete with entity id/title at INFO.

---

<div style="text-align: center;">
  <img src="https://img.shields.io/badge/-STRETCH-blue?style=for-the-badge" alt="MVP Badge"/>
</div>

### Stretch (post-MVP):

---

12. ### Leaderboard & streaks:
    #### As a learner, I want streaks/leaderboard so that I stay motivated.

    **Acceptance:** daily streak display; simple leaderboard (opt-in).

13. ### Search & tags:
    #### As a learner, I want to search by keywords and filter by tags so that I can find relevant problems quickly.

    **Acceptance:** search box filters by title/description; tags filter by predefined categories (arrays or join table).

14. ### OAuth2 login options:
    #### As a learner, I want Google/GitHub sign-in via Cognito so that I can log in faster.

    **Acceptance:** Cognito configured with Google/GitHub; sign-in works end-to-end.

15. ### Richer diff & diagnostics:
    #### As a learner, I want better diffs and targeted hints so that I understand why my output differs.

    **Acceptance:** Diff highlights line changes; hints point to common pitfalls (off-by-one, nulls, etc.).

---

<h2 align="center">Summary</h2>

### MVP (Minimum Viable Product):
Stories 1-11.

### Stretch (post‑MVP):

Stories 12-15.
Plus additional polish features like rate limiting or CI/CD.

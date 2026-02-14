# Pull Request

## Title
<!-- Suggested: type(scope): short summary -->

## Linked Issue(s)
<!-- e.g., Closes #123, Relates to #456 -->

## Summary
- Problem: 
- Solution:

## Changes
- 

## Screenshots / Demos (if UI)
- 

## Checklist
- [ ] Issue linked and labeled (area:*, feature:*, priority:*, status:in-progress → status:needs-review)
- [ ] No server-side Pageable usage (Week 6 uses jQuery pagination via CDN)
- [ ] Logging: no System.out; Log4J used; sensitive data not logged
- [ ] Tests updated/added (DAO/Service/Web as needed)
- [ ] Local tests pass
  - mvn -B -DskipTests=false test
- [ ] Smoke-test endpoints OK locally
  - e.g., http://localhost:8080/home and/or http://localhost:8080/challenges load successfully
- [ ] Deployment packaging unaffected or updated
  - Docker/Render deployment path verified
  - CI artifacts/build outputs updated if needed
- [ ] Security: no secrets committed; Cognito config via env/secret store
- [ ] Docs updated if behavior/config changed
  - README
  - docs/project-plan.md
  - docs/week-6-plan.md
- [ ] Breaking changes documented in Release notes (if any)

## Deployment Notes (if any)
- 

## Additional Reviewers / Areas
- @<reviewer> (area:security)
- @<reviewer> (area:deployment)

---

### Dev Notes (optional)
- Commands:
  - Build: `mvn -B -ntp clean verify`
  - Package: `mvn -q -DskipTests=false clean package`


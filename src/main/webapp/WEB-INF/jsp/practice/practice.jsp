<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/14/2025
  Time: 11:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Drill Queue</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
    </head>
    <body>

        <jsp:include page="/WEB-INF/jsp/header.jsp" />

        <main class="cf-main">
            <section class="cf-page-header">
                <div>
                    <h1 class="cf-page-title">Practice</h1>
                    <p class="cf-page-subtitle">
                        Free-form coding practice without spaced-repetition scheduling.
                        Use it as a sandbox before committing problems into Drill Mode.
                    </p>
                </div>
            </section>

            <section class="cf-grid cf-grid-2 cf-grid-stack">
                <!-- Left: explanation / roadmap -->
                <article class="cf-card">
                    <h2 class="cf-section-title">What Practice is for</h2>
                    <p>
                        Practice mode is designed for experiments, warm-ups, and trying out
                        ideas before you lock them into a formal drill queue. Think of it as
                        your scratchpad:
                    </p>
                    <ul class="cf-list">
                        <li>Draft solutions for upcoming challenges</li>
                        <li>Work through new patterns or snippets</li>
                        <li>Copy/paste code from your editor to track attempts</li>
                    </ul>

                    <p>
                        In the future, this page can evolve into a fully featured
                        “scratch challenges” area:
                    </p>
                    <ul class="cf-list">
                        <li>Save and revisit practice sessions</li>
                        <li>Tag attempts as “keep”, “rewrite”, or “discard”</li>
                        <li>Promote a practice attempt into a tracked Drill challenge</li>
                    </ul>

                    <!-- Light-weight TODO for future enhancement -->
                    <!-- TODO: Evolve Practice into a full scratch-work area with saved attempts. -->
                </article>

                <!-- Right: practice form -->
                <article class="cf-card">
                    <h2 class="cf-section-title">Quick practice pad</h2>

                    <form class="cf-form">
                        <div class="cf-form-row">
                            <label for="practice-title">Title (optional)</label>
                            <input id="practice-title"
                                   type="text"
                                   name="title"
                                   placeholder="e.g., Two Sum attempt, Stream API warm-up" />
                        </div>

                        <div class="cf-form-row">
                            <label for="practice-notes">Notes (optional)</label>
                            <textarea id="practice-notes"
                                      name="notes"
                                      rows="3"
                                      placeholder="What are you trying to practice or learn here?"></textarea>
                        </div>

                        <div class="cf-form-row">
                            <label for="practice-code">Code scratchpad</label>
                            <textarea id="practice-code"
                                      name="code"
                                      rows="12"
                                      class="cf-mono-block"
                                      placeholder="// Type any code you want to experiment with...
                            // This area is not yet persisted — it’s a local practice pad for now."></textarea>
                        </div>

                        <div class="cf-form-actions">
                            <button type="button" class="cf-btn cf-btn-ghost" disabled>
                                Save practice (coming soon)
                            </button>
                        </div>

                        <p class="cf-muted cf-tiny">
                            For now, this pad is purely in-page and not stored.
                            Use it as a convenient place to think and prototype before
                            moving into Drill Mode.
                        </p>
                    </form>
                </article>
            </section>
        </main>

        <jsp:include page="/WEB-INF/jsp/footer.jsp" />

    </body>
</html>
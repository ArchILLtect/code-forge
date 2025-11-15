<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/03/2025
  Time: 6:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Solve – <c:out value="${challenge.title}"/></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/drill.css" />
</head>
<body>

<jsp:include page="/WEB-INF/jsp/header.jsp" />

<main class="cf-main">
  <section class="cf-page-header">
    <div>
      <h1 class="cf-page-title">
        <c:out value="${challenge.title}" />
      </h1>
      <p class="cf-page-subtitle drill-meta">
        Difficulty:
        <span class="cf-pill cf-pill-difficulty-${challenge.difficulty}">
          <c:out value="${challenge.difficulty}" />
        </span>

        <c:if test="${not empty drillItem}">
          <span class="drill-meta-divider">•</span>
          Seen:
          <strong><c:out value="${drillItem.timesSeen}" /></strong>
          <span class="drill-meta-divider">•</span>
          Streak:
          <strong><c:out value="${drillItem.streak}" /></strong>
        </c:if>
      </p>
    </div>

    <a class="cf-link" href="${pageContext.request.contextPath}/drill">
      ← Back to queue
    </a>
  </section>

  <!-- Flash messages -->
  <c:if test="${not empty success}">
    <div class="cf-alert cf-alert-success">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="cf-alert cf-alert-error">${error}</div>
  </c:if>
  <c:if test="${not empty info}">
    <div class="cf-alert cf-alert-info">${info}</div>
  </c:if>

  <section class="cf-grid cf-grid-2 drill-solve-grid">
    <!-- Prompt column -->
    <article class="cf-card drill-prompt-card">
      <h2 class="cf-section-title">Prompt</h2>
      <pre class="drill-prompt-text">
<c:out value="${challenge.promptMd}" />
      </pre>
    </article>

    <!-- Solution column -->
    <article class="cf-card drill-solution-card">
      <h2 class="cf-section-title">Submit your solution</h2>

      <form method="post"
            action="${pageContext.request.contextPath}/drill/${challenge.id}/submit"
            class="cf-form drill-form">

        <div class="cf-form-row cf-form-row-inline">
          <div>
            <label for="language">Language</label>
            <select id="language" name="language">
              <option value="java">Java (stub)</option>
            </select>
          </div>
        </div>

        <div class="cf-form-row">
          <label for="code">Code</label>
          <textarea id="code"
                    name="code"
                    class="drill-code-input"
                    placeholder="// Type your solution here...
// Add '// correct' to mark CORRECT,
// '// ok' for ACCEPTABLE,
 // anything else is INCORRECT."></textarea>
        </div>

        <div class="cf-form-actions">
          <button type="submit" class="cf-btn cf-btn-primary">
            Run &amp; Submit
          </button>
        </div>
      </form>

      <p class="drill-tip">
        Runner is a stub for demo only: it does not compile or execute your code.
        Heuristics: empty = <strong>SKIPPED</strong>; contains
        <code>// correct</code> or <code>// pass</code> = <strong>CORRECT</strong>;
        contains <code>// ok</code> = <strong>ACCEPTABLE</strong>; otherwise
        <strong>INCORRECT</strong>.
      </p>

      <p class="drill-tip-shortcut">
        Pro tip: press <kbd>Ctrl</kbd> + <kbd>Enter</kbd> to submit quickly.
      </p>
    </article>
  </section>
</main>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />

<script>
  // Ctrl+Enter to submit quickly
  document.addEventListener('keydown', function (e) {
    if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
      const form = document.querySelector('.drill-form');
      if (form) form.submit();
    }
  });
</script>

</body>
</html>

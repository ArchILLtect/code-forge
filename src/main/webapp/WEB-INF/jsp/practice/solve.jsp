<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Practice</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/drill.css" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />
<main class="cf-main">
  <section class="cf-page-header">
    <div>
      <h1 class="cf-page-title">Practice</h1>
      <p class="cf-page-subtitle">Try solving challenges without affecting your drill schedule.</p>
    </div>
    <a class="cf-btn" href="${pageContext.request.contextPath}/challenges">← Back to challenges</a>
  </section>

  <c:if test="${not empty outcome}">
    <div class="cf-alert ${outcome eq 'CORRECT' ? 'cf-alert-success' : (outcome eq 'ACCEPTABLE' ? 'cf-alert-info' : 'cf-alert-error')}">
      <strong><c:out value="${outcome}" /></strong> — <c:out value="${feedback}" />
    </div>
  </c:if>

  <section class="cf-card">
    <header>
      <h2><c:out value="${challenge.title}" /></h2>
      <p class="cf-muted"><c:out value="${challenge.blurb}" /></p>
    </header>

    <div class="cf-stack">
      <!-- Prompt content -->
      <div class="cf-field">
        <label class="cf-label">Prompt (Markdown)</label>
        <pre class="drill-prompt-text" style="border: 1px solid black; padding: 10px; overflow-x: auto;">
<c:out value="${challenge.promptMd}" />
        </pre>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/practice/${challenge.id}/submit">

        <div class="cf-field">
          <label>Language</label>
          <select name="language">
            <option value="java">Java</option>
          </select>
        </div>

        <div class="cf-field">
          <label>Code</label>
          <textarea name="code" rows="12" placeholder="Write your solution here..."></textarea>
          <c:if test="${not empty submittedCode}">
            <div class="cf-submission-preview">
              <div class="cf-submission-header">Last submission</div>
              <pre class="cf-code-block"><c:out value="${submittedCode}" /></pre>
            </div>
          </c:if>
        </div>

        <div class="cf-actions">
          <button class="cf-btn cf-btn-primary" type="submit">Run</button>
        </div>

      </form>
    </div>
  </section>
</main>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Solve – <c:out value="${challenge.title}"/></title>
  <style>
    body { margin: 0 20%; font-family: Arial, sans-serif; }
    .flash-success { background: #e6ffed; border: 1px solid #34c759; padding: .5rem; margin-bottom: 1rem; }
    .flash-error { background: #ffecec; border: 1px solid #ff3b30; padding: .5rem; margin-bottom: 1rem; }
    .flash-info { background: #eef5ff; border: 1px solid #3b82f6; padding: .5rem; margin-bottom: 1rem; }
    textarea { width: 100%; min-height: 240px; font-family: Consolas, Monaco, 'Courier New', monospace; }
    .meta { color: #555; }
    .tip { font-size: 0.9rem; color: #333; background: #fafafa; border: 1px dashed #ccc; padding: .5rem; }
    .row { display: flex; gap: 20px; align-items: start; }
    .col { flex: 1; }
  </style>
</head>
<body>
  <p>
    <a href="${pageContext.request.contextPath}/">Home</a> |
    <a href="${pageContext.request.contextPath}/drill">Queue</a>
  </p>

  <c:if test="${not empty success}"><div class="flash-success">${success}</div></c:if>
  <c:if test="${not empty error}"><div class="flash-error">${error}</div></c:if>
  <c:if test="${not empty info}"><div class="flash-info">${info}</div></c:if>

  <h1><c:out value="${challenge.title}"/></h1>
  <p class="meta">
    Difficulty: <strong><c:out value="${challenge.difficulty}"/></strong>
    <c:if test="${not empty drillItem}"> | Seen: <strong><c:out value="${drillItem.timesSeen}"/></strong> | Streak: <strong><c:out value="${drillItem.streak}"/></strong></c:if>
  </p>

  <div class="row">
    <div class="col">
      <h3>Prompt</h3>
      <pre style="white-space: pre-wrap;"><c:out value="${challenge.promptMd}"/></pre>
    </div>
    <div class="col">
      <h3>Submit your solution</h3>
      <form method="post" action="${pageContext.request.contextPath}/drill/${challenge.id}/submit">
        <label for="language">Language</label>
        <select id="language" name="language">
          <option value="java">Java (stub)</option>
        </select>
        <br/><br/>
        <label for="code">Code</label>
        <textarea id="code" name="code" placeholder="// Type your solution here...&#10;// Add '// correct' to mark CORRECT, '// ok' for ACCEPTABLE, anything else is INCORRECT."></textarea>
        <br/>
        <button type="submit">Run & Submit</button>
      </form>
      <p class="tip">
        Runner is a stub for demo only: it does not compile or execute your code. Heuristics: empty = SKIPPED; contains "// correct" or "// pass" = CORRECT; contains "// ok" = ACCEPTABLE; otherwise INCORRECT.
      </p>
    </div>
  </div>

  <script>
    // Ctrl+Enter to submit quickly
    document.addEventListener('keydown', function(e) {
      if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        const form = document.querySelector('form');
        if (form) form.submit();
      }
    });
  </script>
</body>
</html>


<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/03/2025
  Time: 6:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs/loader.min.js"></script>

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
          <span class="drill-meta-divider">•</span>
          Next due:
          <strong>
            <c:choose>
              <c:when test="${not empty drillItem.nextDueAt}">
                <fmt:formatDate value="${drillItem.nextDueAt}" pattern="yyyy-MM-dd HH:mm"/>
              </c:when>
              <c:otherwise>
                <span class="cf-muted">—</span>
              </c:otherwise>
            </c:choose>
          </strong>
        </c:if>
      </p>
    </div>

    <a class="cf-link" href="${pageContext.request.contextPath}/drill">
      ← Back to queue
    </a>
  </section>

  <!-- Flash messages -->
  <c:if test="${not empty success}">
    <div class="cf-alert cf-alert-success"><c:out value="${success}"/></div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="cf-alert cf-alert-error"><c:out value="${error}"/></div>
  </c:if>
  <c:if test="${not empty info}">
    <div class="cf-alert cf-alert-info"><c:out value="${info}"/></div>
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
              <option value="java">Java</option>
            </select>
          </div>
        </div>

        <div class="cf-form-row-drill">

          <label for="code-editor">Code</label>
          <div id="code-editor" style="height:400px;border:1px solid #ddd; padding: 5px;"></div>

          <!-- hidden field for the actual submission -->
          <input type="hidden" name="code" id="code"/>
          <div class="drill-form-actions">
            <div id="codeCharCount" class="cf-hint" style="color:#6b7280">
              Characters: <span id="codeCharCountVal">0</span>
            </div>
            <button type="button" id="clearCodeBtn" class="cf-btn cf-btn-secondary">Clear</button>
          </div>
        </div>

        <div class="drill-form-submit">
          <button type="submit" class="cf-btn cf-btn-primary">
            Run &amp; Submit
          </button>
        </div>
      </form>

      <p class="drill-tip-shortcut">
        Pro tip: press <kbd>Ctrl</kbd> + <kbd>Enter</kbd> to submit quickly.
      </p>
    </article>
  </section>
</main>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />

<script>
  require.config({ paths: { 'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs' } });

  require(['vs/editor/editor.main'], function () {
    const editor = monaco.editor.create(document.getElementById('code-editor'), {
      value: [
        '// Write your code here',
        'public class Main {',
        '    public static void main(String[] args) {',
        '        System.out.println("Hello CodeForge");',
        '    }',
        '}'
      ].join('\n'),
      language: 'java',
      theme: 'vs-dark',
      automaticLayout: true
    });

    // Hook editor content into a hidden form field so you can save it if you want
    const hiddenField = document.getElementById('code');
    if (hiddenField) {
      const syncEditorToHidden = () => hiddenField.value = editor.getValue();
      editor.onDidChangeModelContent(syncEditorToHidden);
      syncEditorToHidden();
    }
  });
  (function() {
    const textarea = document.getElementById('code');
    const chalId = '${challenge.id}'; // server-side value
    const storageKey = 'cf.challenge.' + chalId + '.code';
    const countEl = document.getElementById('codeCharCountVal');
    function updateCount() { if (countEl && textarea) { countEl.textContent = String(textarea.value.length); } }
    if (textarea) {
      // Restore saved value
      const saved = localStorage.getItem(storageKey);
      if (saved) {
        textarea.value = saved;
      }
      updateCount();
      let saveTimer;
      const persist = () => {
        try { localStorage.setItem(storageKey, textarea.value); } catch (e) { /* ignore quota errors */ }
      };
      textarea.addEventListener('input', function() {
        clearTimeout(saveTimer);
        saveTimer = setTimeout(persist, 300); // debounce
        updateCount();
      });
      // Ensure latest code stored on submit
      const form = document.querySelector('.drill-form');
      if (form) {
        form.addEventListener('submit', persist);
      }
      // Clear button
      const clearBtn = document.getElementById('clearCodeBtn');
      if (clearBtn) {
        clearBtn.addEventListener('click', function() {
          localStorage.removeItem(storageKey);
          textarea.value = '';
          updateCount();
        });
      }
    }

    // Ctrl+Enter to submit quickly (preserve existing behavior)
    document.addEventListener('keydown', function (e) {
      if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        const form = document.querySelector('.drill-form');
        if (form) form.submit();
      }
    });
  })();
</script>

</body>
</html>

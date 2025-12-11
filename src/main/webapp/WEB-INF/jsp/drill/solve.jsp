<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/03/2025
  Time: 6:30 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Solve ${challenge.title} | CodeForge" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <%@ include file="/WEB-INF/jsp/head-meta.jspf" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/drill.css" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs/loader.min.js"></script>
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

            <!-- Hint toggle -->
            <div class="cf-hint-toggle-row">
              <button type="button" id="toggleHintBtn" class="cf-link-button">
                Show hint
              </button>
              <div id="hintBox" class="cf-hint-box" style="display:none;">
                <strong>Hint</strong>
                <pre class="cf-code-block">
<c:out value="${challenge.expectedAnswer}" />
                </pre>
              </div>
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
  require.config({
    paths: {
      'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs'
    }
  });

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

    // Make editor accessible if you ever need it elsewhere
    window.cfEditor = editor;

    // --- Shared elements & config ---
    const hiddenField = document.getElementById('code');           // hidden field for form submit
    const form        = document.querySelector('.drill-form');
    const clearBtn    = document.getElementById('clearCodeBtn');
    const countEl     = document.getElementById('codeCharCountVal');
    const chalId      = '${challenge.id}';
    const storageKey  = 'cf.challenge.' + chalId + '.code';

    function getText() {
      return editor.getValue();
    }

    function setText(val) {
      editor.setValue(val);
      if (hiddenField) hiddenField.value = val;
      updateCount();
    }

    function updateCount() {
      if (!countEl) return;
      const len = getText().length;
      countEl.textContent = String(len);
    }

    // --- Restore from localStorage if available ---
    (function restoreInitial() {
      try {
        const saved = localStorage.getItem(storageKey);
        if (saved && saved.length > 0) {
          setText(saved);
        } else {
          // seed hidden field and counter from default editor value
          if (hiddenField) hiddenField.value = getText();
          updateCount();
        }
      } catch (e) {
        // localStorage might be blocked; just fall back to default
        if (hiddenField) hiddenField.value = getText();
        updateCount();
      }
    })();

    // --- Persist to localStorage + hidden field ---
    let saveTimer;
    function persist() {
      const val = getText();
      try {
        localStorage.setItem(storageKey, val);
      } catch (e) {
        // ignore quota/storage errors
      }
      if (hiddenField) hiddenField.value = val;
    }

    editor.onDidChangeModelContent(function () {
      clearTimeout(saveTimer);
      saveTimer = setTimeout(persist, 300); // debounce
      updateCount();
    });

    // --- Ensure latest code on submit ---
    if (form) {
      form.addEventListener('submit', function () {
        persist();
      });
    }

    // --- Clear button behavior ---
    if (clearBtn) {
      clearBtn.addEventListener('click', function () {
        try {
          localStorage.removeItem(storageKey);
        } catch (e) {
          // ignore
        }
        setText('');
      });
    }

    // --- Ctrl+Enter quick submit (same feature as before) ---
    document.addEventListener('keydown', function (e) {
      if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        if (form) {
          persist(); // make sure hidden field + localStorage are up to date
          form.submit();
        }
      }
    });
  });

  // Hint toggle behavior
  (function() {
    const btn  = document.getElementById('toggleHintBtn');
    const box  = document.getElementById('hintBox');

    if (!btn || !box) return;

    btn.addEventListener('click', function () {
      const isHidden = box.style.display === 'none' || box.style.display === '';
      box.style.display = isHidden ? 'block' : 'none';
      btn.textContent   = isHidden ? 'Hide hint' : 'Show hint';
    });
  })();
</script>

</body>
</html>

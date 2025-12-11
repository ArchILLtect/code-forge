<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/03/2025
  Time: 6:30 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Solve/Practice | CodeForge" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <%@ include file="/WEB-INF/jsp/head-meta.jspf" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/drill.css" />
  <!--// TODO: Remove drill.css once shared styles are refactored into practice.css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/practice.css" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs/loader.min.js"></script>
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
        <pre class="practice-prompt-text">
<c:out value="${challenge.promptMd}" />
        </pre>
      </div>

      <form method="post"
            action="${pageContext.request.contextPath}/practice/${challenge.id}/submit"
            class="cf-form drill-form">

        <div class="cf-field">
          <label>Language</label>
          <select name="language">
            <option value="java">Java</option>
          </select>
        </div>

        <div class="cf-field" style="width: 100%">
          <label for="code-editor">Code</label>
          <div id="code-editor" style="height:400px;border:1px solid #ddd; padding: 5px; width: 100%"></div>

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
<script>
  // Monaco loader config (shared)
  require.config({
    paths: {
      'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.0/min/vs'
    }
  });

  require(['vs/editor/editor.main'], function () {
    const chalId = '${challenge.id}'; // server-side
    const storageKey = 'cf.challenge.' + chalId + '.code';

    const editorContainer = document.getElementById('code-editor');
    const hiddenField = document.getElementById('code');
    const countEl = document.getElementById('codeCharCountVal');
    const clearBtn = document.getElementById('clearCodeBtn');
    const form = document.querySelector('.drill-form');

    if (!editorContainer || !hiddenField) {
      // Page isn't a solve/practice page or markup changed unexpectedly
      return;
    }

    const defaultSnippet = [
      '// Write your code here',
      'public class Main {',
      '    public static void main(String[] args) {',
      '        System.out.println("Hello CodeForge");',
      '    }',
      '}'
    ].join('\n');

    // Create Monaco editor
    const editor = monaco.editor.create(editorContainer, {
      value: '',
      language: 'java',
      theme: 'vs-dark',
      automaticLayout: true
    });

    // Restore saved code (if any) or fall back to template
    let initial = null;
    try {
      initial = localStorage.getItem(storageKey);
    } catch (e) {
      // ignore storage errors
    }
    if (!initial || initial.trim() === '') {
      initial = defaultSnippet;
    }

    editor.setValue(initial);
    hiddenField.value = initial;
    if (countEl) {
      countEl.textContent = String(initial.length);
    }

    // Keep everything in sync
    let saveTimer;

    const syncAll = () => {
      const value = editor.getValue();
      hiddenField.value = value;
      if (countEl) {
        countEl.textContent = String(value.length);
      }
    };

    const persist = () => {
      try {
        localStorage.setItem(storageKey, editor.getValue());
      } catch (e) {
        // ignore quota or private mode failures
      }
    };

    editor.onDidChangeModelContent(function () {
      syncAll();
      if (saveTimer) {
        window.clearTimeout(saveTimer);
      }
      saveTimer = window.setTimeout(persist, 300); // debounce saves
    });

    // Clear button
    if (clearBtn) {
      clearBtn.addEventListener('click', function () {
        editor.setValue('');
        try {
          localStorage.removeItem(storageKey);
        } catch (e) {}
        syncAll();
      });
    }

    // Ensure latest code is synced right before submit
    if (form) {
      form.addEventListener('submit', function () {
        syncAll();
        persist();
      });
    }

    // Ctrl+Enter shortcut
    document.addEventListener('keydown', function (e) {
      if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        if (form) {
          e.preventDefault();
          syncAll();
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

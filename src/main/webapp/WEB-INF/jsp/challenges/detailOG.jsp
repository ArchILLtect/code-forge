<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/24/2025
  Time: 2:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Challenge Detail</title>
  <style>
    .flash-success { background: #e6ffed; border: 1px solid #34c759; padding: .5rem; margin-bottom: 1rem; }
    .flash-error { background: #ffecec; border: 1px solid #ff3b30; padding: .5rem; margin-bottom: 1rem; }
    .actions { margin: 10px 0; }
    .hint { color: #555; }
  </style>
</head>
<body>
  <p>
    <a href="${pageContext.request.contextPath}/">Home</a> |
    <a href="${pageContext.request.contextPath}/challenges">All Challenges</a>
    <c:if test="${not empty sessionScope.user}"> |
      <a href="${pageContext.request.contextPath}/challenges/${challenge.id}/edit">Edit</a>
    </c:if>
  </p>

  <c:if test="${not empty success}">
    <div class="flash-success">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="flash-error">${error}</div>
  </c:if>

  <h1><c:out value="${challenge.title}"/></h1>
  <p>
    <strong>Difficulty:</strong> <c:out value="${challenge.difficulty}"/>
  </p>
  <div class="actions">
    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <c:choose>
          <c:when test="${drillEnrolled}">
            <a href="${pageContext.request.contextPath}/drill/${challenge.id}">Open in Drill</a>
          </c:when>
          <c:otherwise>
            <form method="post" action="${pageContext.request.contextPath}/drill/${challenge.id}/add" style="display:inline;">
              <button type="submit">Add to Drill</button>
            </form>
          </c:otherwise>
        </c:choose>
      </c:when>
      <c:otherwise>
        <span class="hint">Log in to access Drill Mode.</span>
        <a href="${pageContext.request.contextPath}/logIn" style="margin-left: 8px;">Log in</a>
      </c:otherwise>
    </c:choose>
  </div>
  <p>
    <strong>Summary:</strong> <c:out value="${challenge.blurb}"/>
  </p>
  <h3>Prompt</h3>
  <pre style="white-space: pre-wrap;"><c:out value="${challenge.promptMd}"/></pre>
</body>
</html>

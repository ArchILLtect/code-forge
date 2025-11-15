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
  <title>Drill Queue</title>
  <style>
    body { margin: 0 20%; font-family: Arial, sans-serif; }
    table { border-collapse: collapse; border: 1px solid #000; width: 100%; }
    th, td { padding: 6px 8px; border: 1px solid #aaa; }
    .flash-success { background: #e6ffed; border: 1px solid #34c759; padding: .5rem; margin-bottom: 1rem; }
    .flash-error { background: #ffecec; border: 1px solid #ff3b30; padding: .5rem; margin-bottom: 1rem; }
    .flash-info { background: #eef5ff; border: 1px solid #3b82f6; padding: .5rem; margin-bottom: 1rem; }
    .cta { margin: 10px 0; }
  </style>
</head>
<body>
  <p>
    <a href="${pageContext.request.contextPath}/">Home</a> |
    <a href="${pageContext.request.contextPath}/challenges">Challenges</a>
  </p>

  <c:if test="${not empty success}"><div class="flash-success">${success}</div></c:if>
  <c:if test="${not empty error}"><div class="flash-error">${error}</div></c:if>
  <c:if test="${not empty info}"><div class="flash-info">${info}</div></c:if>

  <h1>Drill Queue</h1>
  <p class="cta">
    <a href="${pageContext.request.contextPath}/drill/next">Start / Continue Drill →</a>
  </p>

  <c:choose>
    <c:when test="${empty rows}">
      <p>No items due yet. Click "Start / Continue Drill" to find the next upcoming item.</p>
    </c:when>
    <c:otherwise>
      <p>Total in view: <strong><c:out value="${count}"/></strong></p>
      <table>
        <thead><tr><th>Challenge</th><th>Difficulty</th><th>Next Due</th></tr></thead>
        <tbody>
          <c:forEach items="${rows}" var="r">
            <tr>
              <td>
                <a href="${pageContext.request.contextPath}/drill/${r.challengeId}">
                  <c:out value="${r.title}"/>
                </a>
              </td>
              <td><c:out value="${r.difficulty}"/></td>
              <td><c:out value="${r.nextDueAt}"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Challenges</title>
  <link rel="stylesheet" href="https://cdn.datatables.net/2.0.7/css/dataTables.dataTables.min.css"/>
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
  <script src="https://cdn.datatables.net/2.0.7/js/dataTables.min.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/challenges.css" />
  <style>
    body { margin: 0 20% ; font-family: Arial, sans-serif; }
    table { border-collapse: collapse; border: 1px solid #000; }
    form { margin-bottom: unset; }
    .flash-success { background: #e6ffed; border: 1px solid #34c759; padding: .5rem; margin-bottom: 1rem; }
    .flash-error { background: #ffecec; border: 1px solid #ff3b30; padding: .5rem; margin-bottom: 1rem; }
    .controls { display: flex; justify-content: center; align-items: center; gap: 10px; }
  </style>
</head>
<body>
  <h1>Challenges</h1>
  <p>
    <a href="${pageContext.request.contextPath}/">Home</a>
    <c:if test="${not empty sessionScope.user}"> | <a href="${pageContext.request.contextPath}/challenges/new">Create New</a></c:if>
  </p>

  <c:if test="${not empty success}">
    <div class="flash-success">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="flash-error">${error}</div>
  </c:if>

  <form method="get" action="${pageContext.request.contextPath}/challenges">
    <div class="controls">
      <div class="difficulty-filter">
        <label for="difficulty">Filter by difficulty:</label>
        <select name="difficulty" id="difficulty">
          <option value="" ${empty difficultyValue ? 'selected' : ''}>All</option>
          <option value="EASY" ${difficultyValue == 'EASY' ? 'selected' : ''}>EASY</option>
          <option value="MEDIUM" ${difficultyValue == 'MEDIUM' ? 'selected' : ''}>MEDIUM</option>
          <option value="HARD" ${difficultyValue == 'HARD' ? 'selected' : ''}>HARD</option>
        </select>
      </div>
      <button type="submit">Apply</button>
    </div>
  </form>

  <c:choose>
    <c:when test="${empty challenges}">
      <p>No challenges available.</p>
    </c:when>
    <c:otherwise>
      <table id="challenges" class="display">
        <thead><tr><th>ID</th><th>Title</th><th>Difficulty</th><th>Blurb</th></tr></thead>
        <tbody>
          <c:forEach items="${challenges}" var="ch">
            <tr>
              <td><c:out value="${ch.id}"/></td>
              <td><a href="${pageContext.request.contextPath}/challenges/${ch.id}"><c:out value="${ch.title}"/></a></td>
              <td><c:out value="${ch.difficulty}"/></td>
              <td><c:out value="${ch.blurb}"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <script>
        $(function () { $('#challenges').DataTable(); });
      </script>
    </c:otherwise>
  </c:choose>
</body>
</html>

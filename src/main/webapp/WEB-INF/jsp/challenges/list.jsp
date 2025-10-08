<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Challenges</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/challenges.css" />
  <style>
    table { border-collapse: collapse; border: 1px solid #000; }
    .flash-success { background: #e6ffed; border: 1px solid #34c759; padding: .5rem; margin-bottom: 1rem; }
    .flash-error { background: #ffecec; border: 1px solid #ff3b30; padding: .5rem; margin-bottom: 1rem; }
  </style>
</head>
<body>
  <h1>Challenges</h1>
  <p><a href="${pageContext.request.contextPath}/">Home</a> | <a href="${pageContext.request.contextPath}/challenges/new">Create New</a></p>

  <c:if test="${not empty success}">
    <div class="flash-success">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="flash-error">${error}</div>
  </c:if>

  <form method="get" action="${pageContext.request.contextPath}/challenges" style="margin-bottom: 1rem;">
    <label for="difficulty">Filter by difficulty:</label>
    <select name="difficulty" id="difficulty">
      <option value="" ${empty difficultyValue ? 'selected' : ''}>All</option>
      <option value="EASY" ${difficultyValue == 'EASY' ? 'selected' : ''}>EASY</option>
      <option value="MEDIUM" ${difficultyValue == 'MEDIUM' ? 'selected' : ''}>MEDIUM</option>
      <option value="HARD" ${difficultyValue == 'HARD' ? 'selected' : ''}>HARD</option>
    </select>
    <input type="hidden" name="sort" value="${sort}" />
    <input type="hidden" name="dir" value="${dir}" />
    <button type="submit">Apply</button>
    <c:if test="${not empty difficultyValue}">
      <a href="${pageContext.request.contextPath}/challenges">Clear</a>
    </c:if>
  </form>

  <c:choose>
    <c:when test="${empty challenges}">
      <p>No challenges available.</p>
    </c:when>
    <c:otherwise>
      <table>
        <thead>
          <tr>
            <th>
              <a href="${pageContext.request.contextPath}/challenges?sort=id&dir=${dir == 'asc' ? 'desc' : 'asc'}&difficulty=${difficultyValue}&page=${currentPage}&size=${pageSize}">ID</a>
            </th>
            <th>
              <a href="${pageContext.request.contextPath}/challenges?sort=title&dir=${dir == 'asc' ? 'desc' : 'asc'}&difficulty=${difficultyValue}&page=${currentPage}&size=${pageSize}">Title</a>
            </th>
            <th>
              <a href="${pageContext.request.contextPath}/challenges?sort=difficulty&dir=${dir == 'asc' ? 'desc' : 'asc'}&difficulty=${difficultyValue}&page=${currentPage}&size=${pageSize}">Difficulty</a>
            </th>
            <th>Blurb</th>
          </tr>
        </thead>
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

      <div style="margin-top: 1rem;">
        <c:if test="${totalPages > 1}">
          <span>Page ${currentPage + 1} of ${totalPages}</span>
          <div style="margin-top: 0.5rem;">
            <c:if test="${hasPrev}">
              <a href="${pageContext.request.contextPath}/challenges?page=${currentPage - 1}&size=${pageSize}&sort=${sort}&dir=${dir}&difficulty=${difficultyValue}">Prev</a>
            </c:if>
            <c:forEach var="i" begin="0" end="${totalPages - 1}">
              <c:choose>
                <c:when test="${i == currentPage}">
                  <strong>[${i + 1}]</strong>
                </c:when>
                <c:otherwise>
                  <a href="${pageContext.request.contextPath}/challenges?page=${i}&size=${pageSize}&sort=${sort}&dir=${dir}&difficulty=${difficultyValue}">${i + 1}</a>
                </c:otherwise>
              </c:choose>
              <c:if test="${i < totalPages - 1}"> </c:if>
            </c:forEach>
            <c:if test="${hasNext}">
              <a href="${pageContext.request.contextPath}/challenges?page=${currentPage + 1}&size=${pageSize}&sort=${sort}&dir=${dir}&difficulty=${difficultyValue}">Next</a>
            </c:if>
          </div>
        </c:if>
      </div>
    </c:otherwise>
  </c:choose>
</body>
</html>

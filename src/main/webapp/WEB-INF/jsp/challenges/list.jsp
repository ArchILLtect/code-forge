<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Challenges</title>
</head>
<body>
  <h1>Challenges</h1>
  <p><a href="${pageContext.request.contextPath}/">Home</a></p>

  <c:choose>
    <c:when test="${empty challenges}">
      <p>No challenges available.</p>
    </c:when>
    <c:otherwise>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Difficulty</th>
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
    </c:otherwise>
  </c:choose>
</body>
</html>

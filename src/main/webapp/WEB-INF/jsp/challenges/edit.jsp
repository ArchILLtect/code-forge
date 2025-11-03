<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Edit Challenge</title>
</head>
<body>
  <p><a href="${pageContext.request.contextPath}/">Home</a> | <a href="${pageContext.request.contextPath}/challenges">All Challenges</a></p>
  <h1>Edit Challenge</h1>

  <form method="post" action="${pageContext.request.contextPath}/challenges/${challengeId}">
    <div>
      <label for="title">Title</label><br/>
      <input type="text" id="title" name="title" value="${title}" />
    </div>

    <div>
      <label for="difficulty">Difficulty</label><br/>
      <select id="difficulty" name="difficulty">
        <c:forEach items="${difficulties}" var="d">
          <option value="${d}" ${d == difficulty ? 'selected' : ''}>${d}</option>
        </c:forEach>
      </select>
    </div>

    <div>
      <label for="blurb">Summary</label><br/>
      <textarea id="blurb" name="blurb" rows="3" cols="60">${blurb}</textarea>
    </div>

    <div>
      <label for="promptMd">Prompt (Markdown)</label><br/>
      <textarea id="promptMd" name="promptMd" rows="10" cols="80">${promptMd}</textarea>
    </div>

    <div style="margin-top: 1rem;">
      <button type="submit">Update</button>
    </div>
  </form>

  <c:if test="${not empty sessionScope.user}">
    <form method="post" action="${pageContext.request.contextPath}/challenges/${challengeId}/delete" style="margin-top: 1rem;">
      <button type="submit" onclick="return confirm('Delete this challenge?');">Delete</button>
    </form>
  </c:if>
</body>
</html>

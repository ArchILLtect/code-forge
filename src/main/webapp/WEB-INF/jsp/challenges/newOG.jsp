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
  <title>New Challenge</title>
</head>
<body>
  <p><a href="${pageContext.request.contextPath}/">Home</a> | <a href="${pageContext.request.contextPath}/challenges">All Challenges</a></p>
  <h1>Create New Challenge</h1>

  <form method="post" action="${pageContext.request.contextPath}/challenges">
    <div>
      <label for="title">Title</label><br/>
      <input type="text" name="title" id="title" />
      <div style="color: #b00;"><c:if test="${not empty param.title}"><c:out value="${param.title}"/></c:if></div>
    </div>

    <div>
      <label for="difficulty">Difficulty</label><br/>
      <select name="difficulty" id="difficulty">
        <c:forEach items="${difficulties}" var="difficulty">
          <option value="${difficulty}">${difficulty}</option>
        </c:forEach>
      </select>
      <div style="color: #b00;"><c:if test="${not empty param.difficulty}"><c:out value="${param.difficulty}"/></c:if></div>
    </div>

    <div>
      <label for="blurb">Summary</label><br/>
      <textarea name="blurb" id="blurb" rows="3" cols="60"></textarea>
      <div style="color: #b00;"><c:if test="${not empty param.blurb}"><c:out value="${param.blurb}"/></c:if></div>
    </div>

    <div>
      <label for="promptMd">Prompt (Markdown)</label><br/>
      <textarea name="promptMd" id="promptMd" rows="10" cols="80"></textarea>
      <div style="color: #b00;"><c:if test="${not empty param.promptMd}"><c:out value="${param.promptMd}"/></c:if></div>
    </div>

    <div style="margin-top: 1rem;">
      <button type="submit">Create</button>
    </div>
  </form>
</body>
</html>

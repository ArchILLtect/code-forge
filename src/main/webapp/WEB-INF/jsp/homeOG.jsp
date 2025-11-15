<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>CodeForge</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css"  />
</head>
<body>
  <h1>Welcome to CodeForge</h1>
  <img src="${pageContext.request.contextPath}/images/CodeForge-logo.png" alt="CodeForge Logo" />
  <c:choose>
      <c:when test="${empty user}">
        <span>Please </span>
        <a href="${pageContext.request.contextPath}/logIn">Log in</a>
        <span> to access Drill Mode.</span>
      </c:when>
      <c:otherwise>
        <h2>Welcome, ${user.userName}!</h2>
        <p>Your email: ${user.email}</p>
        <p>Your user ID: ${user.sub}</p>
        <a href="${pageContext.request.contextPath}/logout" aria-label="Log out of your account">Log out</a>
      </c:otherwise>
  </c:choose>

  <p>This home page is served via JSP.</p>
  <h2>Quick quote:</h2>
  <div class="mt-6 text-gray-700 italic text-sm">
    ${quote}
  </div>
  <ul>
    <li><a href="${pageContext.request.contextPath}/challenges">Browse Challenges</a></li>
    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <li><a href="${pageContext.request.contextPath}/drill">Drill Mode</a></li>
      </c:when>
      <c:otherwise>
        <li>
          <a href="${pageContext.request.contextPath}/logIn">Log in</a>
          <span> to access Drill Mode.</span>
        </li>
      </c:otherwise>
    </c:choose>
  </ul>
</body>
</html>

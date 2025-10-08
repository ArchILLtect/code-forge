<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>CodeForge</title>
</head>
<body>
  <h1>Welcome to CodeForge</h1>
  <p>This home page is served via JSP (Spring Boot + Tomcat Jasper).</p>
  <ul>
    <li><a href="${pageContext.request.contextPath}/challenges">Browse Challenges</a></li>
    <li><a href="${pageContext.request.contextPath}/healthz">Health Check</a></li>
    <li><a href="${pageContext.request.contextPath}/h2-console">H2 Console</a> (dev)</li>
  </ul>
</body>
</html>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Server Error</title>
</head>
<body>
  <h1>500 — Internal Server Error</h1>
  <p><strong>${status}</strong> ${error}</p>
  <p>${message}</p>
  <p><a href="${pageContext.request.contextPath}/">Back to Home</a></p>
</body>
</html>


<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Challenge Detail</title>
</head>
<body>
  <p><a href="${pageContext.request.contextPath}/">Home</a> | <a href="${pageContext.request.contextPath}/challenges">All Challenges</a></p>
  <h1><c:out value="${challenge.title}"/></h1>
  <p>
    <strong>Difficulty:</strong> <c:out value="${challenge.difficulty}"/>
  </p>
  <p>
    <strong>Summary:</strong> <c:out value="${challenge.blurb}"/>
  </p>
  <h3>Prompt</h3>
  <pre style="white-space: pre-wrap;"><c:out value="${challenge.promptMd}"/></pre>
</body>
</html>

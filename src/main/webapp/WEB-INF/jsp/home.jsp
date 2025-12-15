<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/14/2025
  Time: 9:40 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="CodeForge" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <%@ include file="/WEB-INF/jsp/head-meta.jspf" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<main class="cf-main">
  <!-- HERO SECTION -->
  <section class="cf-hero">
    <div class="cf-hero-text">
      <h1 class="cf-hero-title">Welcome to CodeForge</h1>
      <p class="cf-hero-subtitle">
        A coding dojo for sharpening your skills with structured challenges,
        realistic drills, and fast feedback.
      </p>

      <div class="cf-hero-actions">
        <a href="${pageContext.request.contextPath}/challenges" class="cf-btn cf-btn-primary">
          Browse Challenges
        </a>

        <c:choose>
          <c:when test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/drill" class="cf-btn cf-btn-outline">
              Jump into Drill Mode
            </a>
          </c:when>
          <c:otherwise>
            <a href="${pageContext.request.contextPath}/logIn" class="cf-btn cf-btn-outline">
              Log in to access Drill Mode
            </a>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <div class="cf-hero-image-wrapper">
      <img
              src="${pageContext.request.contextPath}/images/CodeForge-logo.png"
              alt="CodeForge Banner"
              class="cf-hero-image"
      />
    </div>
  </section>

  <!-- FEATURE GRID / DESCRIPTION -->
  <section class="cf-section cf-section-light">
    <div class="cf-section-inner">
      <h2 class="cf-section-title">What is CodeForge?</h2>
      <p class="cf-section-lead">
        CodeForge is a practice arena built for students and early-career
        developers. It focuses on <strong>repeatable drills</strong>,
        <strong>small wins</strong>, and <strong>confidence-building reps</strong>
        instead of giant, overwhelming projects.
      </p>

      <div class="cf-feature-grid">
        <article class="cf-feature-card">
          <h3>Guided Challenges</h3>
          <p>
            Work through curated problems that target specific skills:
            Java basics, collections, web concepts, and more.
          </p>
        </article>

        <article class="cf-feature-card">
          <h3>Drill Mode</h3>
          <p>
            Rapid-fire repetitions of similar problems, designed to build
            muscle memory and reduce “blank page” anxiety.
          </p>
        </article>

        <article class="cf-feature-card">
          <h3>Real-World Stack</h3>
          <p>
            Practice in an environment that looks like the real thing:
            Java, JSP, MySQL, AWS, and deployment pipelines.
          </p>
        </article>
      </div>
    </div>
  </section>

  <!-- QUICK QUOTE -->
  <section class="cf-section">
    <div class="cf-section-inner cf-quote-card">
      <h2 class="cf-section-title">Quick quote</h2>
      <p class="cf-quote-text">
        “${quote}”
      </p>
      <p class="cf-quote-caption">
        New quote each visit – a tiny nudge to keep you moving forward.
      </p>
    </div>
  </section>
</main>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>
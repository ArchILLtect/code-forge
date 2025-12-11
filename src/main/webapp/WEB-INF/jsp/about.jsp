<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/14/2025
  Time: 11:52 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="About | CodeForge" />

<!-- Add info about me, links to my showcase site, and other related links including GitHub repo -->

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <%@ include file="/WEB-INF/jsp/head-meta.jspf" %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
    </head>
    <body>

        <jsp:include page="/WEB-INF/jsp/header.jsp" />

        <main class="cf-main">
            <section class="cf-page-header">
                <div>
                    <h1 class="cf-page-title">About CodeForge</h1>
                    <p class="cf-page-subtitle">
                        CodeForge is a personal coding-challenge platform built as an
                        enterprise Java indie project, focusing on learning, repetition,
                        and real deployment experience.
                    </p>
                </div>
            </section>

            <section class="cf-grid cf-grid-2 cf-grid-stack">
                <!-- Left: story + goals -->
                <article class="cf-section cf-section-light cf-mb">
                    <div class="cf-section-inner">
                        <h2 class="cf-section-title">Project overview</h2>
                        <p class="cf-section-lead">
                            CodeForge lets you browse coding challenges, schedule them into
                            a spaced-repetition drill queue, and track your progress over time.
                            It was designed as a full-stack, end-to-end project:
                        </p>

                        <ul class="cf-section-lead">
                            <li>Java web app using servlets and JSP</li>
                            <li>DAO layer on top of a relational database</li>
                            <li>Drill engine for spaced-repetition practice</li>
                            <li>Deployment on AWS Elastic Beanstalk</li>
                            <li>Authentication via Amazon Cognito</li>
                        </ul>

                        <p class="cf-section-lead">
                            The main goal of the project is to practice “enterprise style”
                            architecture while still building something personally useful:
                            a place to level up problem-solving skills on real infrastructure.
                        </p>
                    </div>
                </article>

                <!-- Right: tech + features -->
                <article class="cf-section cf-section-light">
                    <div class="cf-section-inner">
                        <h2 class="cf-section-title">Tech stack</h2>

                        <ul class="cf-section-lead">
                            <li><strong>Backend:</strong> Java 11, Servlets, JSP, JSTL</li>
                            <li><strong>Persistence:</strong> DAO layer, MySQL (via JDBC)</li>
                            <li><strong>Auth:</strong> Amazon Cognito (Hosted UI + OAuth)</li>
                            <li><strong>Hosting:</strong> AWS Elastic Beanstalk (Tomcat)</li>
                            <li><strong>Routing:</strong> Nginx proxy → Tomcat app</li>
                            <li><strong>Static assets:</strong> AWS S3 (planned hardening)</li>
                        </ul>

                        <h2 class="cf-section-title">Current status</h2>
                        <ul class="cf-section-lead">
                            <li>Challenge browsing and CRUD pages implemented</li>
                            <li>Drill queue and evaluation stub in place</li>
                            <li>Cognito login + logout fully wired for dev and prod</li>
                            <li>Custom JSP layout + basic design system (CodeForge UI)</li>
                        </ul>

                        <!-- Nice spot to hint at future work -->
                        <h2 class="cf-section-title">Future enhancements</h2>
                        <ul class="cf-section-lead">
                            <li>Real sandboxed code execution for submissions</li>
                            <li>Stronger analytics and progress dashboards</li>
                            <li>More languages beyond Java for challenge solutions</li>
                        </ul>
                    </div>

                </article>
            </section>
        </main>

        <jsp:include page="/WEB-INF/jsp/footer.jsp" />

    </body>
</html>
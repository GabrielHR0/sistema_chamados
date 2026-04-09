<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${appName}"/> - <c:out value="${pageTitle}"/></title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-semibold" href="#"><c:out value="${appName}"/></a>
        <span class="navbar-text text-white-50">Ambiente <c:out value="${environment}"/></span>
    </div>
</nav>

<main class="container py-5">
    <div class="row align-items-center g-4 mb-4">
        <div class="col-lg-8">
            <span class="badge text-bg-success mb-3">JSP + Bootstrap + JSTL</span>
            <h1 class="display-5 fw-bold mb-3"><c:out value="${pageTitle}"/></h1>
            <p class="lead text-secondary mb-0">
                Base pronta para evoluir o front-end em paralelo com o backend, usando JSP como view server-side.
            </p>
        </div>
        <div class="col-lg-4">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h2 class="h5">Stack base</h2>
                    <ul class="list-unstyled mb-0">
                        <li class="mb-2">Spring Boot 3.5.13</li>
                        <li class="mb-2">Java 21</li>
                        <li class="mb-2">Docker Compose</li>
                        <li>Problem Details (RFC 7807)</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <c:forEach items="${featureCards}" var="feature">
            <div class="col-md-6 col-xl-3">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-body">
                        <span class="badge text-bg-primary mb-3"><c:out value="${feature.badge()}"/></span>
                        <h3 class="h5"><c:out value="${feature.title()}"/></h3>
                        <p class="text-secondary mb-0"><c:out value="${feature.description()}"/></p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</main>

<script src="<c:url value='/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js'/>"></script>
</body>
</html>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Erro <c:out value="${error.status}"/></title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8 col-xl-7">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4 p-md-5">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <div>
                            <span class="badge text-bg-danger mb-3">MVC Error</span>
                            <h1 class="h3 mb-2"><c:out value="${error.error}"/></h1>
                            <p class="text-secondary mb-0"><c:out value="${error.message}"/></p>
                        </div>
                        <div class="text-end">
                            <div class="display-6 fw-bold text-danger"><c:out value="${error.status}"/></div>
                            <small class="text-secondary">HTTP status</small>
                        </div>
                    </div>

                    <hr class="my-4">

                    <dl class="row mb-0">
                        <dt class="col-sm-3">Caminho</dt>
                        <dd class="col-sm-9"><c:out value="${error.path}"/></dd>
                        <dt class="col-sm-3">Horário</dt>
                        <dd class="col-sm-9"><c:out value="${error.timestamp}"/></dd>
                    </dl>

                    <div class="mt-4 d-flex gap-2">
                        <a class="btn btn-primary" href="<c:url value='/'/>">Voltar ao início</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<c:url value='/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js'/>"></script>
</body>
</html>


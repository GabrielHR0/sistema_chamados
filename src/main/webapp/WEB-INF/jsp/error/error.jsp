<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Erro - Chamados</title>
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/fontawesome-free/css/all.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/dist/css/adminlte.min.css'/>">
</head>
<body class="hold-transition">
<div class="wrapper">
  <section class="content">
    <div class="error-page mt-5">
      <h2 class="headline text-warning"><c:out value="${error.status}"/></h2>
      <div class="error-content">
        <h3><i class="fas fa-exclamation-triangle text-warning"></i> <c:out value="${error.error}"/></h3>
        <p><c:out value="${error.message}"/></p>
        <p><a href="<c:url value='/'/>">Voltar para o dashboard</a></p>
      </div>
    </div>
  </section>
</div>
<script src="<c:url value='/adminlte/plugins/jquery/jquery.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Editar Tipo de Chamado</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/fontawesome-free/css/all.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/overlayScrollbars/css/OverlayScrollbars.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/dist/css/adminlte.min.css'/>">
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
  <jsp:include page="/WEB-INF/jsp/includes/navbar.jsp" />
  <jsp:include page="/WEB-INF/jsp/includes/sidebar.jsp" />
  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Editar Tipo de Chamado</h1></div>
        </div>
      </div>
    </section>
    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title">Dados do tipo</h3></div>
          <form action="<c:url value='/chamados/tipos/${not empty tipoId ? tipoId : tipo.id}/editar'/>" method="post">
            <div class="card-body">
              <div class="form-group">
                <label for="titulo">Titulo</label>
                <input id="titulo" name="titulo" class="form-control" value="${tipo.titulo}" required />
              </div>
              <div class="form-group">
                <label for="slaHoras">SLA (horas)</label>
                <input id="slaHoras" name="slaHoras" type="number" min="1" class="form-control" value="${tipo.slaHoras}" required />
              </div>
              <div class="form-group">
                <label for="statusInicialId">Status inicial</label>
                <select id="statusInicialId" name="statusInicialId" class="form-control" required>
                  <option value="">Selecione</option>
                  <c:forEach var="status" items="${statusIniciais}">
                    <option value="${status.id}" ${tipo.statusInicialId == status.id ? 'selected' : ''}>
                      <c:out value="${status.nome}"/>
                    </option>
                  </c:forEach>
                </select>
              </div>
            </div>
            <div class="card-footer">
              <button type="submit" class="btn btn-primary btn-sm"><i class="fas fa-save"></i> Salvar</button>
              <a href="<c:url value='/chamados/tipos'/>" class="btn btn-secondary btn-sm">Cancelar</a>
            </div>
          </form>
        </div>
      </div>
    </section>
  </div>
  <jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
</div>
<script src="<c:url value='/adminlte/plugins/jquery/jquery.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js'/>"></script>
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
</body>
</html>

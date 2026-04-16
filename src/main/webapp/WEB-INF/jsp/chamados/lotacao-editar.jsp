<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Editar Lotacao</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/fontawesome-free/css/all.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/overlayScrollbars/css/OverlayScrollbars.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/select2/css/select2.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css'/>">
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
          <div class="col-sm-6"><h1>Editar Lotacao</h1></div>
        </div>
      </div>
    </section>
    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title">Dados da lotacao</h3></div>
          <form action="<c:url value='/chamados/admin/lotacoes/${lotacaoId}/editar'/>" method="post">
            <div class="card-body">
              <div class="form-group">
                <label for="nome">Nome</label>
                <input id="nome" name="nome" class="form-control" value="<c:out value='${lotacaoRequest.nome}'/>" required />
              </div>
              <div class="form-group">
                <label for="descricao">Descricao</label>
                <textarea id="descricao" name="descricao" class="form-control" rows="2"><c:out value="${lotacaoRequest.descricao}"/></textarea>
              </div>
              <div class="form-group">
                <label for="tiposChamadoIds">Tipos de chamado</label>
                <select id="tiposChamadoIds" name="tiposChamadoIds" class="form-control select2bs4" multiple="multiple">
                  <c:forEach var="tipo" items="${tiposDisponiveis}">
                    <c:set var="selectedTipo" value="false"/>
                    <c:forEach var="selectedId" items="${lotacaoRequest.tiposChamadoIds}">
                      <c:if test="${selectedId == tipo.id}">
                        <c:set var="selectedTipo" value="true"/>
                      </c:if>
                    </c:forEach>
                    <option value="${tipo.id}" ${selectedTipo ? 'selected="selected"' : ''}>
                      <c:out value="${tipo.titulo}"/>
                    </option>
                  </c:forEach>
                </select>
              </div>
            </div>
            <div class="card-footer">
              <button type="submit" class="btn btn-primary btn-sm"><i class="fas fa-save"></i> Salvar</button>
              <a href="<c:url value='/chamados/admin/lotacoes'/>" class="btn btn-secondary btn-sm">Cancelar</a>
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
<script src="<c:url value='/adminlte/plugins/select2/js/select2.full.min.js'/>"></script>
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
<script>
  $(function () {
    $('.select2bs4').select2({
      theme: 'bootstrap4',
      width: '100%'
    });
  });
</script>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Abrir Chamado</title>

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
          <div class="col-sm-6">
            <h1>Abrir Chamado</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/chamados/morador'/>">Meus Chamados</a></li>
              <li class="breadcrumb-item active">Novo</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <div class="row">
          <div class="col-md-8">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Preencha os dados</h3>
              </div>

              <form method="post" action="<c:url value='/chamados/morador/novo'/>" enctype="multipart/form-data">
                <div class="card-body">
                  <div class="form-group">
                    <label for="unidadeId">Unidade *</label>
                    <select class="form-control select2" id="unidadeId" name="unidadeId" required>
                      <option value="">Selecione</option>
                      <c:forEach var="u" items="${unidades}">
                        <option value="${u.id}" ${chamado.unidadeId == u.id ? 'selected' : ''}>
                          <c:out value="${u.identificacao}"/>
                        </option>
                      </c:forEach>
                    </select>
                  </div>

                  <div class="form-group">
                    <label for="tipoId">Tipo de Chamado *</label>
                    <select class="form-control select2" id="tipoId" name="tipoId" required>
                      <option value="">Selecione</option>
                      <c:forEach var="tipo" items="${tipos}">
                        <option value="${tipo.id}" ${chamado.tipoId == tipo.id ? 'selected' : ''}>
                          <c:out value="${tipo.titulo}"/> (SLA: <c:out value="${tipo.slaHoras}"/>h)
                        </option>
                      </c:forEach>
                    </select>
                  </div>

                  <div class="form-group">
                    <label for="descricao">Descricao *</label>
                    <textarea class="form-control" id="descricao" name="descricao" rows="5" required><c:out value="${chamado.descricao}"/></textarea>
                  </div>

                  <div class="form-group mb-0">
                    <label for="arquivo">Anexo (opcional)</label>
                    <input id="arquivo" name="arquivo" type="file" class="form-control-file" />
                  </div>
                </div>

                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">
                    <i class="fas fa-paper-plane"></i> Abrir Chamado
                  </button>
                  <a href="<c:url value='/chamados/morador'/>" class="btn btn-secondary">Cancelar</a>
                </div>
              </form>
            </div>
          </div>
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
  $(function() {
    $('.select2').select2({ theme: 'bootstrap4' });
  });
</script>
</body>
</html>

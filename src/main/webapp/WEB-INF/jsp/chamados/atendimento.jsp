<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Fila de Atendimento</title>

  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/fontawesome-free/css/all.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/overlayScrollbars/css/OverlayScrollbars.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/datatables-responsive/css/responsive.bootstrap4.min.css'/>">
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
            <h1>Fila de Atendimento</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item active">Fila de Atendimento</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <c:if test="${param.picked != null}">
          <div class="alert alert-success">Chamado assumido com sucesso.</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>

        <div class="card card-outline card-secondary">
          <div class="card-header">
            <h3 class="card-title">Filtros da fila da sua lotacao</h3>
          </div>
          <form method="get" action="<c:url value='/chamados/atendimento'/>">
            <div class="card-body">
              <div class="row">
                <div class="col-md-5">
                  <label for="tipoId">Tipo</label>
                  <select class="form-control" id="tipoId" name="tipoId">
                    <option value="">Todos</option>
                    <c:forEach var="tipo" items="${tiposFiltro}">
                      <option value="${tipo.id}" ${selectedTipoId == tipo.id ? 'selected' : ''}><c:out value="${tipo.titulo}"/></option>
                    </c:forEach>
                  </select>
                </div>
                <div class="col-md-5">
                  <label for="statusId">Status</label>
                  <select class="form-control" id="statusId" name="statusId">
                    <option value="">Todos</option>
                    <c:forEach var="status" items="${statusFiltro}">
                      <option value="${status.id}" ${selectedStatusId == status.id ? 'selected' : ''}><c:out value="${status.nome}"/></option>
                    </c:forEach>
                  </select>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                  <button class="btn btn-secondary btn-block" type="submit"><i class="fas fa-filter"></i> Filtrar</button>
                </div>
              </div>
            </div>
          </form>
        </div>

        <div class="card">
          <div class="card-header">
            <h3 class="card-title">Chamados disponiveis para assumir</h3>
          </div>
          <div class="card-body">
            <table id="atendimentoChamadosTable" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Unidade</th>
                <th>Tipo</th>
                <th>Status</th>
                <th>Data de Criacao</th>
                <th>Acoes</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="chamado" items="${chamados}">
                <tr>
                  <td><c:out value="${chamado.unidadeIdentificacao}"/></td>
                  <td><c:out value="${chamado.tipoTitulo}"/></td>
                  <td><span class="badge ${chamado.statusFinal ? 'badge-success' : 'badge-info'}"><c:out value="${chamado.statusNome}"/></span></td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty chamado.dataCriacao}">
                        <c:out value="${fn:replace(fn:substring(chamado.dataCriacao, 0, 16), 'T', ' ')}"/>
                      </c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <form action="<c:url value='/chamados/${chamado.id}/atender'/>" method="post" class="d-inline">
                      <button type="submit" class="btn btn-xs btn-success">
                        <i class="fas fa-hand-paper"></i> Atender
                      </button>
                    </form>
                    <a href="<c:url value='/chamados/${chamado.id}'/>" class="btn btn-xs btn-info">
                      <i class="fas fa-eye"></i>
                    </a>
                  </td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </div>
        </div>

        <div class="card card-outline card-success">
          <div class="card-header">
            <h3 class="card-title">Meus chamados em andamento</h3>
          </div>
          <div class="card-body">
            <table id="meusNaoFinaisTable" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Unidade</th>
                <th>Tipo</th>
                <th>Status</th>
                <th>Data de Criacao</th>
                <th>Acoes</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="chamado" items="${meusChamadosNaoFinais}">
                <tr>
                  <td><c:out value="${chamado.unidadeIdentificacao}"/></td>
                  <td><c:out value="${chamado.tipoTitulo}"/></td>
                  <td><span class="badge ${chamado.statusFinal ? 'badge-success' : 'badge-info'}"><c:out value="${chamado.statusNome}"/></span></td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty chamado.dataCriacao}">
                        <c:out value="${fn:replace(fn:substring(chamado.dataCriacao, 0, 16), 'T', ' ')}"/>
                      </c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <a href="<c:url value='/chamados/${chamado.id}'/>" class="btn btn-xs btn-info">
                      <i class="fas fa-eye"></i>
                    </a>
                  </td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
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
<script src="<c:url value='/adminlte/plugins/datatables/jquery.dataTables.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/datatables-responsive/js/dataTables.responsive.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/datatables-responsive/js/responsive.bootstrap4.min.js'/>"></script>
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
<script>
  $(function () {
    $('#atendimentoChamadosTable').DataTable({
      responsive: true,
      autoWidth: false,
      language: {
        url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json'
      }
    });
    $('#meusNaoFinaisTable').DataTable({
      responsive: true,
      autoWidth: false,
      language: {
        url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json'
      }
    });
  });
</script>
</body>
</html>

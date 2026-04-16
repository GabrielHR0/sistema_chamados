<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Detalhes do Andar - Chamados</title>

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
            <h1>Detalhes do Andar</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/blocos/${andar.blocoId}'/>">Bloco</a></li>
              <li class="breadcrumb-item active">Andar</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-lg-4">
            <div class="card card-primary card-outline">
              <div class="card-body">
                <h3 class="profile-username text-center">Andar <c:out value="${andar.numero}" /></h3>
                <p class="text-muted text-center">
                  <span class="badge ${andar.ativo ? 'badge-success' : 'badge-secondary'}">
                    <c:out value="${andar.ativo ? 'Ativo' : 'Inativo'}" />
                  </span>
                </p>
                <p><strong>Bloco:</strong> <c:out value="${andar.blocoIdentificacao}" /></p>
                <p><strong>Observações:</strong><br><c:out value="${andar.observacoes}" /></p>
                <c:if test="${canUnidadeCreate}">
                  <a href="<c:url value='/andares/${andar.id}/unidades/novo'/>" class="btn btn-primary btn-block">
                    <i class="fas fa-plus mr-1"></i>Nova Unidade
                  </a>
                </c:if>
              </div>
            </div>
          </div>

          <div class="col-lg-8">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Unidades</h3>
              </div>
              <div class="card-body">
                <table id="unidadesTable" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th>Identificação</th>
                    <th>Apartamento</th>
                    <th>Ativo</th>
                    <th>Status</th>
                    <th style="width: 180px;">Ações</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="unidade" items="${andar.unidades}">
                    <tr>
                      <td><c:out value="${unidade.identificacao}" /></td>
                      <td><c:out value="${unidade.apartamentoNumero}" /></td>
                      <td>
                        <span class="badge ${unidade.ativo ? 'badge-success' : 'badge-secondary'}">
                          <c:out value="${unidade.ativo ? 'Ativa' : 'Inativa'}" />
                        </span>
                      </td>
                      <td>
                        <span class="badge badge-info">
                          <c:out value="${unidade.status}" />
                        </span>
                      </td>
                      <td>
                        <c:if test="${canUnidadeRead}">
                          <a href="<c:url value='/unidades/${unidade.id}'/>" class="btn btn-xs btn-info">
                            <i class="fas fa-eye"></i>
                          </a>
                        </c:if>
                        <c:if test="${canUnidadeUpdate}">
                          <a href="<c:url value='/unidades/${unidade.id}/editar'/>" class="btn btn-xs btn-warning">
                            <i class="fas fa-edit"></i>
                          </a>
                        </c:if>
                        <c:if test="${canUnidadeDelete}">
                          <form action="<c:url value='/unidades/${unidade.id}/deletar'/>" method="post" class="d-inline">
                            <button type="submit" class="btn btn-xs btn-danger" onclick="return confirm('Deseja deletar esta unidade?')">
                              <i class="fas fa-trash"></i>
                            </button>
                          </form>
                        </c:if>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
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
<script src="<c:url value='/adminlte/plugins/datatables/jquery.dataTables.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/datatables-responsive/js/dataTables.responsive.min.js'/>"></script>
<script src="<c:url value='/adminlte/plugins/datatables-responsive/js/responsive.bootstrap4.min.js'/>"></script>
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
<script>
  $(function () {
    $('#unidadesTable').DataTable({
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

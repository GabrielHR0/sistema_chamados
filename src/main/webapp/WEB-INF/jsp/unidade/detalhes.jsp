<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Detalhes da Unidade - Chamados</title>

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
            <h1>Detalhes da Unidade</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/andares/${unidade.andarId}'/>">Andar</a></li>
              <li class="breadcrumb-item active">Unidade</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger alert-dismissible">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <c:out value="${errorMessage}" />
          </div>
        </c:if>
        <div class="row">
          <div class="col-lg-4">
            <div class="card card-primary card-outline">
              <div class="card-body">
                <h3 class="profile-username text-center"><c:out value="${unidade.identificacao}" /></h3>
                <p class="text-muted text-center">
                  <span class="badge badge-info"><c:out value="${unidade.status}" /></span>
                  <span class="badge ${unidade.ativo ? 'badge-success' : 'badge-secondary'}">
                    <c:out value="${unidade.ativo ? 'Ativa' : 'Inativa'}" />
                  </span>
                </p>
                <p><strong>Apartamento:</strong> <c:out value="${unidade.apartamentoNumero}" /></p>
                <p><strong>Observações:</strong><br><c:out value="${unidade.observacoes}" /></p>
              </div>
            </div>
          </div>

          <div class="col-lg-8">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Moradias</h3>
                <div class="card-tools">
                  <c:if test="${canMoradiaCreate || canUnidadeUpdate}">
                    <a href="<c:url value='/moradias?unidadeId=${unidade.id}'/>" class="btn btn-sm btn-primary">
                      <i class="fas fa-plus"></i> Nova Moradia
                    </a>
                  </c:if>
                </div>
              </div>
              <div class="card-body">
                <table id="moradiasTable" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th>Usuário</th>
                    <th>Usuário vinculado</th>
                    <th>Status</th>
                    <th>Início</th>
                    <th>Fim</th>
                    <th style="width: 140px;">Ações</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="moradia" items="${unidade.moradias}">
                    <tr>
                      <td><c:out value="${moradia.usuarioNome}" /></td>
                      <td><c:out value="${moradia.usuarioAtualNome}" /></td>
                      <td><c:out value="${moradia.status}" /></td>
                      <td><c:out value="${moradia.dataInicio}" /></td>
                      <td><c:out value="${moradia.dataFim}" /></td>
                      <td>
                        <a href="<c:url value='/usuarios/${moradia.usuarioId}'/>" class="btn btn-xs btn-warning" title="Visualizar usuário">
                          <i class="fas fa-eye"></i>
                        </a>
                        <c:if test="${moradia.status == 'ATIVA' && canMoradiaUpdate}">
                          <form action="<c:url value='/unidades/${unidade.id}/moradias/${moradia.id}/finalizar'/>" method="post" style="display:inline;">
                            <input type="hidden" name="_method" value="patch">
                            <button type="submit" class="btn btn-xs btn-danger" title="Finalizar moradia">
                              <i class="fas fa-stop"></i>
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
    $('#moradiasTable').DataTable({
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

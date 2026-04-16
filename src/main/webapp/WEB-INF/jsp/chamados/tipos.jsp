<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Tipos de Chamado</title>

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
            <h1>Tipos de Chamado</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item active">Tipos de Chamado</li>
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
        <c:if test="${param.created != null}">
          <div class="alert alert-success">Tipo cadastrado com sucesso.</div>
        </c:if>
        <c:if test="${param.updated != null}">
          <div class="alert alert-success">Tipo atualizado com sucesso.</div>
        </c:if>
        <c:if test="${param.deleted != null}">
          <div class="alert alert-success">Tipo removido com sucesso.</div>
        </c:if>

        <div class="row">
          <div class="col-md-4">
            <c:if test="${canChamadoTipoCreate}">
              <div class="card card-primary">
                <div class="card-header"><h3 class="card-title">Novo tipo</h3></div>
                <form action="<c:url value='/chamados/tipos'/>" method="post">
                  <div class="card-body">
                    <div class="form-group">
                      <label for="titulo">Titulo</label>
                      <input id="titulo" name="titulo" class="form-control" value="${tipoRequest.titulo}" required />
                    </div>
                    <div class="form-group">
                      <label for="slaHoras">SLA (horas)</label>
                      <input id="slaHoras" name="slaHoras" type="number" min="1" class="form-control" value="${tipoRequest.slaHoras}" required />
                    </div>
                    <div class="form-group">
                      <label for="statusInicialId">Status inicial</label>
                      <select id="statusInicialId" name="statusInicialId" class="form-control" required>
                        <option value="">Selecione</option>
                        <c:forEach var="status" items="${statusIniciais}">
                          <option value="${status.id}" ${tipoRequest.statusInicialId == status.id ? 'selected' : ''}><c:out value="${status.nome}"/></option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
                  <div class="card-footer">
                    <button type="submit" class="btn btn-primary btn-sm"><i class="fas fa-save"></i> Salvar</button>
                  </div>
                </form>
              </div>
            </c:if>
          </div>
          <div class="col-md-8">
            <div class="card">
              <div class="card-header"><h3 class="card-title">Tipos cadastrados</h3></div>
              <div class="card-body">
                <table id="tiposTable" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th>Titulo</th>
                    <th>SLA (horas)</th>
                    <th>Status inicial</th>
                    <th>Ações</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="tipo" items="${tipos}">
                    <tr>
                      <td><c:out value="${tipo.titulo}"/></td>
                      <td><c:out value="${tipo.slaHoras}"/></td>
                      <td><c:out value="${tipo.statusInicialNome}"/></td>
                      <td>
                        <c:if test="${canChamadoTipoUpdate}">
                          <a href="<c:url value='/chamados/tipos/${tipo.id}/editar'/>" class="btn btn-xs btn-warning">
                            <i class="fas fa-edit"></i>
                          </a>
                        </c:if>
                        <c:if test="${canChamadoTipoDelete}">
                          <form action="<c:url value='/chamados/tipos/${tipo.id}/deletar'/>" method="post" class="d-inline">
                            <button class="btn btn-xs btn-danger" type="submit" onclick="return confirm('Remover tipo de chamado?');">
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
    $('#tiposTable').DataTable({
      responsive: true,
      autoWidth: false,
      language: { url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json' }
    });
  });
</script>
</body>
</html>

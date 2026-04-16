<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Status de Chamado</title>

  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/fontawesome-free/css/all.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/overlayScrollbars/css/OverlayScrollbars.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/datatables-responsive/css/responsive.bootstrap4.min.css'/>">
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
            <h1>Status de Chamado</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item active">Status de Chamado</li>
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
          <div class="alert alert-success">Status cadastrado com sucesso.</div>
        </c:if>
        <c:if test="${param.updated != null}">
          <div class="alert alert-success">Status atualizado com sucesso.</div>
        </c:if>
        <c:if test="${param.deleted != null}">
          <div class="alert alert-success">Status removido com sucesso.</div>
        </c:if>

        <div class="row">
          <div class="col-md-5">
            <c:if test="${canChamadoStatusCreate}">
              <div class="card card-primary">
                <div class="card-header"><h3 class="card-title">Novo status</h3></div>
                <form action="<c:url value='/chamados/status'/>" method="post">
                  <div class="card-body">
                    <div class="form-group">
                      <label for="nome">Nome</label>
                      <input id="nome" name="nome" class="form-control" required />
                    </div>
                    <div class="form-group">
                      <label for="comportamentoTipo">Comportamento</label>
                      <select id="comportamentoTipo" name="comportamentoTipo" class="form-control" required>
                        <option value="">Selecione</option>
                        <c:forEach var="tipo" items="${comportamentos}">
                          <option value="${tipo}"><c:out value="${tipo}"/></option>
                        </c:forEach>
                      </select>
                    </div>
                    <div class="form-group">
                      <label for="proximosIds">Próximos status</label>
                      <select id="proximosIds" name="proximosIds" multiple class="form-control select2">
                        <c:forEach var="status" items="${statusDisponiveis}">
                          <option value="${status.id}"><c:out value="${status.nome}"/></option>
                        </c:forEach>
                      </select>
                      <small class="text-muted">Para status FINAL, deixe vazio.</small>
                    </div>
                  </div>
                  <div class="card-footer">
                    <button type="submit" class="btn btn-primary btn-sm"><i class="fas fa-save"></i> Salvar</button>
                  </div>
                </form>
              </div>
            </c:if>
          </div>
          <div class="col-md-7">
            <div class="card">
              <div class="card-header"><h3 class="card-title">Status cadastrados</h3></div>
              <div class="card-body">
                <table id="statusTable" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Próximos</th>
                    <th>Ações</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="status" items="${statusList}">
                    <tr>
                      <td><c:out value="${status.nome}"/></td>
                      <td><c:out value="${status.comportamentoTipo}"/></td>
                      <td>
                        <c:choose>
                          <c:when test="${empty status.proximosNomes}">-</c:when>
                          <c:otherwise><c:out value="${status.proximosNomes}"/></c:otherwise>
                        </c:choose>
                      </td>
                      <td>
                        <c:if test="${canChamadoStatusUpdate}">
                          <a href="<c:url value='/chamados/status/${status.id}/editar'/>" class="btn btn-xs btn-warning">
                            <i class="fas fa-edit"></i>
                          </a>
                        </c:if>
                        <c:if test="${canChamadoStatusDelete}">
                          <form action="<c:url value='/chamados/status/${status.id}/deletar'/>" method="post" class="d-inline">
                            <button class="btn btn-xs btn-danger" type="submit" onclick="return confirm('Remover status?');">
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
<script src="<c:url value='/adminlte/plugins/select2/js/select2.full.min.js'/>"></script>
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
<script>
  $(function () {
    $('#statusTable').DataTable({
      responsive: true,
      autoWidth: false,
      language: { url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json' }
    });
    $('.select2').select2({ theme: 'bootstrap4' });
  });
</script>
</body>
</html>

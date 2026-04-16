<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Detalhes do Usuario - Chamados</title>

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
            <h1>Detalhes do Usuario</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/usuarios'/>">Usuarios</a></li>
              <li class="breadcrumb-item active">Detalhes</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="card card-primary card-outline">
          <div class="card-header">
            <h3 class="card-title">Dados do Usuario</h3>
            <div class="card-tools">
              <c:if test="${canUserUpdate}">
                <a href="<c:url value='/usuarios/${usuario.id}/editar'/>" class="btn btn-sm btn-warning">
                  <i class="fas fa-edit mr-1"></i>Editar
                </a>
              </c:if>
            </div>
          </div>
          <div class="card-body">
            <dl class="row">
              <dt class="col-sm-3">Username</dt>
              <dd class="col-sm-9"><c:out value="${usuario.username}" /></dd>

              <dt class="col-sm-3">Email</dt>
              <dd class="col-sm-9"><c:out value="${usuario.email}" /></dd>

              <dt class="col-sm-3">Status</dt>
              <dd class="col-sm-9">
                <span class="badge ${usuario.enabled ? 'badge-success' : 'badge-danger'}">
                  <c:out value="${usuario.enabled ? 'Ativo' : 'Inativo'}" />
                </span>
              </dd>

              <dt class="col-sm-3">Cargo</dt>
              <dd class="col-sm-9">
                <c:forEach var="role" items="${usuario.roles}" varStatus="st">
                  <span class="badge badge-primary"><c:if test="${!st.first}">, </c:if><c:out value="${role}" /></span>
                </c:forEach>
              </dd>

              <dt class="col-sm-3">Lotacoes</dt>
              <dd class="col-sm-9">
                <c:choose>
                  <c:when test="${empty usuario.lotacoes}">
                    <span class="text-muted">Sem lotacao vinculada</span>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="lotacao" items="${usuario.lotacoes}" varStatus="st">
                      <span class="badge badge-info"><c:out value="${lotacao}" /></span><c:if test="${!st.last}"> </c:if>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </dd>

              <dt class="col-sm-3">Nome completo</dt>
              <dd class="col-sm-9"><c:out value="${usuario.perfil.nomeCompleto}" /></dd>

              <dt class="col-sm-3">CPF</dt>
              <dd class="col-sm-9"><c:out value="${usuario.perfil.cpf}" /></dd>

              <dt class="col-sm-3">Telefone</dt>
              <dd class="col-sm-9"><c:out value="${usuario.perfil.telefone}" /></dd>

              <dt class="col-sm-3">Data de nascimento</dt>
              <dd class="col-sm-9"><c:out value="${usuario.perfil.dataNascimento}" /></dd>
            </dl>
          </div>
          <div class="card-body border-top">
            <div id="moradiasAccordion">
              <div class="card card-outline card-secondary mb-0">
                <div class="card-header">
                  <h5 class="card-title mb-0">
                    <a class="d-block w-100" data-toggle="collapse" href="#collapseMoradias">
                      Moradias do usuário
                    </a>
                  </h5>
                </div>
                <div id="collapseMoradias" class="collapse" data-parent="#moradiasAccordion">
                  <div class="card-body">
                    <table id="moradiasUsuarioTable" class="table table-bordered table-striped">
                      <thead>
                      <tr>
                        <th>Unidade</th>
                        <th>Status</th>
                        <th>Início</th>
                        <th>Fim</th>
                      </tr>
                      </thead>
                      <tbody></tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-footer">
            <a href="<c:url value='/usuarios'/>" class="btn btn-default">Voltar</a>
            <c:if test="${canUserDelete}">
              <form action="<c:url value='/usuarios/${usuario.id}/deletar'/>" method="post" class="d-inline">
                <button type="submit" class="btn btn-danger" onclick="return confirm('Deseja deletar este usuario?')">
                  Deletar
                </button>
              </form>
            </c:if>
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
    const moradiasUrl = '<c:url value="/usuarios/${usuario.id}/moradias"/>';
    let loaded = false;
    let loading = false;

    function initMoradiasTable() {
      if (loaded || loading) return;
      loading = true;

      $('#moradiasUsuarioTable tbody').html(
        '<tr><td colspan="4" class="text-center text-muted">Carregando moradias...</td></tr>'
      );

      $('#moradiasUsuarioTable').DataTable({
        destroy: true,
        responsive: true,
        autoWidth: false,
        ajax: {
          url: moradiasUrl,
          dataSrc: '',
          error: function () {
            loading = false;
            $('#moradiasUsuarioTable tbody').html(
              '<tr><td colspan="4" class="text-danger text-center">Não foi possível carregar as moradias.</td></tr>'
            );
          }
        },
        columns: [
          { data: 'unidadeIdentificacao', defaultContent: '' },
          { data: 'status', defaultContent: '' },
          { data: 'dataInicio', defaultContent: '' },
          { data: 'dataFim', defaultContent: '' }
        ],
        language: {
          url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json'
        },
        initComplete: function () {
          loaded = true;
          loading = false;
        }
      });
    }

    $('#collapseMoradias').on('shown.bs.collapse', initMoradiasTable);
  });
</script>
</body>
</html>

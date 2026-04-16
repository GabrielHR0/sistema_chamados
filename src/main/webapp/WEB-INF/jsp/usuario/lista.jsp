<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Usuarios - Chamados</title>

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
            <h1>Usuarios</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item active">Usuarios</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="card">
          <div class="card-header">
            <h3 class="card-title">Gestao de Usuarios</h3>
            <div class="card-tools">
              <c:if test="${canUserCreate}">
                <a href="<c:url value='/usuarios/novo'/>" class="btn btn-sm btn-primary">
                  <i class="fas fa-user-plus mr-1"></i>Novo Usuario
                </a>
              </c:if>
            </div>
          </div>
          <div class="card-body">
            <table id="usuariosTable" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Usuario</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Status</th>
                <th>Cargo</th>
                <th style="width: 220px;">Acoes</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="u" items="${usuarios}">
                <tr>
                  <td><c:out value="${u.username}" /></td>
                  <td><c:out value="${u.perfil.nomeCompleto}" /></td>
                  <td><c:out value="${u.email}" /></td>
                  <td>
                    <span class="badge ${u.enabled ? 'badge-success' : 'badge-danger'}">
                      <c:out value="${u.enabled ? 'Ativo' : 'Inativo'}" />
                    </span>
                  </td>
                  <td>
                    <c:forEach var="role" items="${u.roles}" varStatus="st">
                      <c:if test="${!st.first}">, </c:if><c:out value="${role}" />
                    </c:forEach>
                  </td>
                  <td>
                    <a href="<c:url value='/usuarios/${u.id}'/>" class="btn btn-xs btn-info">
                      <i class="fas fa-eye"></i>
                    </a>
                    <c:if test="${canUserUpdate}">
                      <a href="<c:url value='/usuarios/${u.id}/editar'/>" class="btn btn-xs btn-warning">
                        <i class="fas fa-edit"></i>
                      </a>
                    </c:if>
                    <c:if test="${canUserDelete && !u.roles.contains('ADMIN')}">
                      <form action="<c:url value='/usuarios/${u.id}/deletar'/>" method="post" class="d-inline">
                        <button type="submit" class="btn btn-xs btn-danger" onclick="return confirm('Deseja deletar este usuario?')">
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
    $('#usuariosTable').DataTable({
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

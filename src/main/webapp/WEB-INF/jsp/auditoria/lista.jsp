<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Auditorias</title>

  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/fontawesome-free/css/all.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/overlayScrollbars/css/OverlayScrollbars.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/datatables-responsive/css/responsive.bootstrap4.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/dist/css/adminlte.min.css'/>">
  <style>
    .audit-json { max-width: 420px; white-space: pre-wrap; word-break: break-word; font-size: .8rem; }
  </style>
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
            <h1>Auditorias</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item active">Auditorias</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="card">
          <div class="card-header">
            <h3 class="card-title">Eventos de auditoria</h3>
          </div>
          <div class="card-body">
            <table id="auditoriaTable" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Data/Hora</th>
                <th>Tabela</th>
                <th>Ação</th>
                <th>Usuário DB</th>
                <th>Antes</th>
                <th>Depois</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="a" items="${auditorias}">
                <tr>
                  <td><c:out value="${a.actionTstamp}" /></td>
                  <td><c:out value="${a.schemaName}" />.<c:out value="${a.tableName}" /></td>
                  <td>
                    <span class="badge ${a.action == 'I' ? 'badge-success' : (a.action == 'U' ? 'badge-warning' : 'badge-danger')}">
                      <c:out value="${a.action}" />
                    </span>
                  </td>
                  <td><c:out value="${a.userName}" /></td>
                  <td><pre class="audit-json mb-0"><c:out value="${a.originalData}" /></pre></td>
                  <td><pre class="audit-json mb-0"><c:out value="${a.newData}" /></pre></td>
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
    $('#auditoriaTable').DataTable({
      responsive: true,
      autoWidth: false,
      order: [[0, 'desc']],
      pageLength: 25,
      language: {
        url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/Portuguese-Brasil.json'
      }
    });
  });
</script>
</body>
</html>

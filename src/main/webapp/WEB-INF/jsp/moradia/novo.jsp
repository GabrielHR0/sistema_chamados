<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Nova Moradia</title>

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
            <h1>Nova Moradia</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/unidades/${unidade.id}'/>">Unidade</a></li>
              <li class="breadcrumb-item active">Nova Moradia</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-8">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Selecione o usuário que vai ocupar a unidade</h3>
              </div>

              <form method="post" action="<c:url value='/unidades/${unidade.id}/moradias'/>">
                <div class="card-body">
                  <input type="hidden" name="unidadeId" value="${unidade.id}">
                  <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">
                      <c:out value="${errorMessage}" />
                    </div>
                  </c:if>
                  <div class="alert alert-info">
                    <strong><c:out value="${unidade.identificacao}" /></strong>
                    <div><c:out value="${unidade.status}" /> • <c:out value="${unidade.ativo ? 'Ativa' : 'Inativa'}" /></div>
                  </div>

                  <div class="form-group">
                    <label for="usuarioId">Usuário *</label>
                    <select class="form-control select2" id="usuarioId" name="usuarioId" required style="width: 100%;">
                      <option value="">Digite o nome do usuário</option>
                    </select>
                  </div>
                </div>

                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Criar Moradia
                  </button>
                  <a href="<c:url value='/unidades/${unidade.id}'/>" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Cancelar
                  </a>
                </div>
              </form>
            </div>
          </div>

          <div class="col-md-4">
            <div class="card card-info">
              <div class="card-header">
                <h3 class="card-title">Dica</h3>
              </div>
              <div class="card-body">
                <p>Digite ao menos duas letras para buscar o usuário pelo nome ou login.</p>
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
<script src="<c:url value='/adminlte/plugins/select2/js/select2.full.min.js'/>"></script>
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
<script>
  $(function() {
    $('#usuarioId').select2({
      theme: 'bootstrap4',
      placeholder: 'Digite o nome do usuário',
      minimumInputLength: 2,
      ajax: {
        url: '<c:url value="/moradias/usuarios/busca"/>',
        dataType: 'json',
        delay: 250,
        data: function (params) {
          return {
            q: params.term
          };
        },
        processResults: function (data) {
          return {
            results: data
          };
        },
        cache: true
      }
    });
  });
</script>
</body>
</html>

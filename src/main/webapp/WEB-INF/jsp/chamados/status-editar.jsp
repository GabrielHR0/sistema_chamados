<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Editar Status de Chamado</title>
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
          <div class="col-sm-6"><h1>Editar Status de Chamado</h1></div>
        </div>
      </div>
    </section>
    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title">Dados do status</h3></div>
          <form action="<c:url value='/chamados/status/${statusId}/editar'/>" method="post">
            <div class="card-body">
              <div class="form-group">
                <label for="nome">Nome</label>
                <input id="nome" name="nome" class="form-control" value="${statusRequest.nome}" required />
              </div>
              <div class="form-group">
                <label for="comportamentoTipo">Tipo de comportamento</label>
                <select id="comportamentoTipo" name="comportamentoTipo" class="form-control" required>
                  <c:forEach var="tipo" items="${comportamentos}">
                    <option value="${tipo}" <c:if test="${tipo == statusRequest.comportamentoTipo}">selected</c:if>>
                      ${tipo}
                    </option>
                  </c:forEach>
                </select>
              </div>
              <c:set var="selectedIds" value=","/>
              <c:forEach var="sid" items="${statusRequest.proximosIds}">
                <c:set var="selectedIds" value="${selectedIds}${sid},"/>
              </c:forEach>
              <div class="form-group">
                <label for="proximosIds">Proximos status permitidos</label>
                <select id="proximosIds" name="proximosIds" class="form-control select2" multiple>
                  <c:forEach var="st" items="${statusDisponiveis}">
                    <c:set var="sidToken" value=",${st.id}," />
                    <option
                        value="${st.id}"
                        <c:if test="${fn:contains(selectedIds, sidToken)}">selected</c:if>>
                      ${st.nome}
                    </option>
                  </c:forEach>
                </select>
                <small id="proximosHint" class="form-text text-muted">Para status FINAL, deixe vazio.</small>
                <div id="proximosUiError" class="text-danger small mt-1 d-none"></div>
              </div>
            </div>
            <div class="card-footer">
              <button type="submit" class="btn btn-primary btn-sm"><i class="fas fa-save"></i> Salvar</button>
              <a href="<c:url value='/chamados/status'/>" class="btn btn-secondary btn-sm">Cancelar</a>
            </div>
          </form>
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
  $(function () {
    var $tipo = $('#comportamentoTipo');
    var $proximos = $('#proximosIds');
    var $uiError = $('#proximosUiError');
    var statusAtualId = '${statusId}';

    $('.select2').select2({ theme: 'bootstrap4' });

    function setUiError(message) {
      if (message) {
        $uiError.text(message).removeClass('d-none');
      } else {
        $uiError.text('').addClass('d-none');
      }
    }

    function applyProximosRules() {
      var comportamento = $tipo.val();
      var selected = $proximos.val() || [];

      $proximos.find('option').each(function () {
        var $opt = $(this);
        var isSelf = $opt.val() === statusAtualId;
        $opt.prop('disabled', isSelf);
      });

      if (comportamento === 'FINAL') {
        $proximos.val([]).trigger('change.select2');
        $proximos.prop('disabled', true).trigger('change.select2');
        setUiError(null);
        return;
      }

      $proximos.prop('disabled', false).trigger('change.select2');

      var filtered = [];
      for (var i = 0; i < selected.length; i++) {
        var value = selected[i];
        var $option = $proximos.find("option[value='" + value + "']");
        if ($option.length > 0 && !$option.prop('disabled')) {
          filtered.push(value);
        }
      }
      if (filtered.length !== selected.length) {
        $proximos.val(filtered).trigger('change.select2');
      }

      setUiError(null);
    }

    $tipo.on('change', applyProximosRules);
    applyProximosRules();
  });
</script>
</body>
</html>

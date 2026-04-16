<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Novo Usuario - Chamados</title>

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
            <h1>Novo Usuario</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/usuarios'/>">Usuarios</a></li>
              <li class="breadcrumb-item active">Novo</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger">
            <c:out value="${errorMessage}" />
          </div>
        </c:if>
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Cadastrar Usuario</h3>
          </div>
          <form method="post" action="<c:url value='/usuarios'/>">
            <div class="card-body">
              <div class="form-group">
                <label for="username">Username</label>
                <input id="username" name="username" class="form-control" required minlength="3" maxlength="100"
                       value="<c:out value='${usuario.username}'/>">
              </div>
              <div class="form-group">
                <label for="email">Email</label>
                <input id="email" name="email" type="email" class="form-control" required
                       value="<c:out value='${usuario.email}'/>">
              </div>
              <div class="form-group">
                <label for="password">Senha</label>
                <input id="password" name="password" type="password" class="form-control" required minlength="6" maxlength="100">
              </div>
              <div class="form-group">
                <div class="custom-control custom-switch">
                  <input id="enabled" name="enabled" value="true" type="checkbox" class="custom-control-input" checked>
                  <input type="hidden" name="enabled" value="false">
                  <label class="custom-control-label" for="enabled">Usuario ativo</label>
                </div>
              </div>

              <hr>
              <h5>Perfil</h5>
              <div class="form-group">
                <label for="perfil.nomeCompleto">Nome completo</label>
                <input id="perfil.nomeCompleto" name="perfil.nomeCompleto" class="form-control" required maxlength="150"
                       value="<c:out value='${usuario.perfil.nomeCompleto}'/>">
              </div>
              <div class="form-group">
                <label for="perfil.cpf">CPF</label>
                <input id="perfil.cpf" name="perfil.cpf" class="form-control" maxlength="14"
                       value="<c:out value='${usuario.perfil.cpf}'/>">
              </div>
              <div class="form-group">
                <label for="perfil.telefone">Telefone</label>
                <input id="perfil.telefone" name="perfil.telefone" class="form-control" maxlength="20"
                       value="<c:out value='${usuario.perfil.telefone}'/>">
              </div>
              <div class="form-group">
                <label for="perfil.dataNascimento">Data de nascimento</label>
                <input id="perfil.dataNascimento" name="perfil.dataNascimento" type="date" class="form-control"
                       value="<c:out value='${usuario.perfil.dataNascimento}'/>">
              </div>

              <div class="form-group">
                <label>Cargo</label>
                <div>
                  <c:forEach var="role" items="${rolesDisponiveis}">
                    <div class="custom-control custom-checkbox">
                      <input class="custom-control-input role-checkbox" type="checkbox" id="role-${role.id}" name="roleIds"
                             value="${role.id}" data-role-name="${role.name}">
                      <label for="role-${role.id}" class="custom-control-label">
                        <c:out value="${role.name}" />
                      </label>
                    </div>
                  </c:forEach>
                </div>
              </div>

              <div class="form-group">
                <label for="lotacaoIds">Lotacoes</label>
                <select id="lotacaoIds" name="lotacaoIds" class="form-control select2bs4" multiple="multiple" data-placeholder="Selecione as lotacoes">
                  <c:forEach var="lotacao" items="${lotacoesDisponiveis}">
                    <option value="${lotacao.id}"><c:out value="${lotacao.nome}" /></option>
                  </c:forEach>
                </select>
                <small id="lotacaoHint" class="form-text text-muted">Usuarios com cargo MORADOR nao podem ter lotacao.</small>
              </div>
            </div>
            <div class="card-footer">
              <button type="submit" class="btn btn-primary">
                <i class="fas fa-save mr-1"></i>Salvar
              </button>
              <a href="<c:url value='/usuarios'/>" class="btn btn-secondary">Cancelar</a>
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
    const $lotacao = $('#lotacaoIds');
    $('.select2bs4').select2({
      theme: 'bootstrap4',
      width: '100%'
    });

    function syncLotacaoByRole() {
      const hasMorador = $('.role-checkbox[data-role-name="MORADOR"]:checked').length > 0;
      $lotacao.prop('disabled', hasMorador);
      if (hasMorador) {
        $lotacao.val(null).trigger('change');
      }
      $('#lotacaoHint').toggleClass('text-danger', hasMorador);
    }

    $('.role-checkbox').on('change', syncLotacaoByRole);
    syncLotacaoByRole();
  });
</script>
</body>
</html>

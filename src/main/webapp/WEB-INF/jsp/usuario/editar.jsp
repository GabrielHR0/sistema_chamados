<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Editar Usuario - Chamados</title>

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
            <h1>Editar Usuario</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/usuarios'/>">Usuarios</a></li>
              <li class="breadcrumb-item active">Editar</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div id="edit-alert"></div>
        <div class="row">
          <div class="col-lg-6">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Dados Cadastrais</h3>
              </div>
              <form method="post" action="<c:url value='/usuarios/${usuario.id}'/>">
                <div class="card-body">
                  <div class="form-group">
                    <label>Username</label>
                    <input class="form-control" value="${usuario.username}" disabled>
                    <input type="hidden" name="username" value="${usuario.username}">
                  </div>
                  <div class="form-group">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" class="form-control" value="${usuario.email}" required>
                  </div>
                  <hr>
                  <h5>Perfil</h5>
                  <div class="form-group">
                    <label for="perfil.nomeCompleto">Nome completo</label>
                    <input id="perfil.nomeCompleto" name="perfil.nomeCompleto" class="form-control" value="${usuario.perfil.nomeCompleto}" required maxlength="150">
                  </div>
                  <div class="form-group">
                    <label for="perfil.cpf">CPF</label>
                    <input id="perfil.cpf" name="perfil.cpf" class="form-control" value="${usuario.perfil.cpf}" maxlength="14">
                  </div>
                  <div class="form-group">
                    <label for="perfil.telefone">Telefone</label>
                    <input id="perfil.telefone" name="perfil.telefone" class="form-control" value="${usuario.perfil.telefone}" maxlength="20">
                  </div>
                  <div class="form-group">
                    <label for="perfil.dataNascimento">Data de nascimento</label>
                    <input id="perfil.dataNascimento" name="perfil.dataNascimento" type="date" class="form-control" value="${usuario.perfil.dataNascimento}">
                  </div>
                  <input type="hidden" name="password" value="ignored-for-update">
                  <div class="form-group">
                    <div class="custom-control custom-switch">
                      <input id="enabled" name="enabled" value="true" type="checkbox" class="custom-control-input" ${usuario.enabled ? 'checked' : ''}>
                      <input type="hidden" name="enabled" value="false">
                      <label class="custom-control-label" for="enabled">Usuario ativo</label>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="lotacaoIds">Lotacoes</label>
                    <select id="lotacaoIds" name="lotacaoIds" class="form-control select2bs4" multiple="multiple" data-placeholder="Selecione as lotacoes">
                      <c:forEach var="lotacao" items="${lotacoesDisponiveis}">
                        <option value="${lotacao.id}" ${usuario.lotacaoIds.contains(lotacao.id) ? 'selected' : ''}>
                          <c:out value="${lotacao.nome}" />
                        </option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">Salvar Dados</button>
                </div>
              </form>
            </div>
          </div>

          <div class="col-lg-6">
            <div class="card card-info">
              <div class="card-header">
                <h3 class="card-title">Cargo</h3>
              </div>
              <div class="card-body">
                <form id="roles-form">
                  <c:forEach var="role" items="${rolesDisponiveis}">
                    <div class="custom-control custom-checkbox">
                      <input class="custom-control-input role-checkbox"
                             type="checkbox"
                             id="role-${role.id}"
                             value="${role.id}"
                             ${usuario.roles.contains(role.name) ? 'checked' : ''}>
                      <label class="custom-control-label" for="role-${role.id}">
                        <c:out value="${role.name}" />
                      </label>
                    </div>
                  </c:forEach>
                </form>
              </div>
              <div class="card-footer">
                  <button id="save-roles-btn" type="button" class="btn btn-info">Salvar Cargos</button>
              </div>
            </div>

            <div class="card card-warning">
              <div class="card-header">
                <h3 class="card-title">Reset de Senha (Admin)</h3>
              </div>
              <form id="admin-password-form">
                <div class="card-body">
                  <div class="form-group">
                    <label for="newPassword">Nova Senha</label>
                    <input id="newPassword" type="password" class="form-control" minlength="6" required>
                  </div>
                  <div class="form-group">
                    <label for="confirmNewPassword">Confirmar Nova Senha</label>
                    <input id="confirmNewPassword" type="password" class="form-control" minlength="6" required>
                  </div>
                  <div class="form-group">
                    <label for="adminPassword">Sua Senha de Admin</label>
                    <input id="adminPassword" type="password" class="form-control" required>
                  </div>
                </div>
                <div class="card-footer">
                  <button type="submit" class="btn btn-warning">Resetar Senha</button>
                </div>
              </form>
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
  $(function () {
    $('.select2bs4').select2({
      theme: 'bootstrap4',
      width: '100%'
    });
  });

  const editAlert = document.getElementById('edit-alert');
  const userId = '${usuario.id}';

  function showAlert(type, message) {
    editAlert.innerHTML = '<div class="alert alert-' + type + '">' + message + '</div>';
  }

  document.getElementById('save-roles-btn').addEventListener('click', async function () {
    const roleIds = Array.from(document.querySelectorAll('.role-checkbox:checked')).map((el) => el.value);
    try {
      const response = await fetch('<c:url value="/usuarios/${usuario.id}/roles"/>', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ roleIds: roleIds })
      });

      if (!response.ok) {
        const error = await response.json().catch(() => ({ detail: 'Falha ao salvar roles.' }));
        throw new Error(error.detail || 'Falha ao salvar cargos.');
      }
      showAlert('success', 'Cargos atualizados com sucesso.');
    } catch (err) {
      showAlert('danger', err.message);
    }
  });

  document.getElementById('admin-password-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const payload = {
      newPassword: document.getElementById('newPassword').value,
      confirmNewPassword: document.getElementById('confirmNewPassword').value,
      adminPassword: document.getElementById('adminPassword').value
    };

    try {
      const response = await fetch('<c:url value="/usuarios/${usuario.id}/senha/admin"/>', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const error = await response.json().catch(() => ({ detail: 'Falha ao resetar senha.' }));
        throw new Error(error.detail || 'Falha ao resetar senha.');
      }

      showAlert('success', 'Senha redefinida com sucesso.');
      document.getElementById('admin-password-form').reset();
    } catch (err) {
      showAlert('danger', err.message);
    }
  });
</script>
</body>
</html>

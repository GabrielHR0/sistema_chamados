<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Meu Perfil - Chamados</title>

  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/fontawesome-free/css/all.min.css'/>">
  <link rel="stylesheet" href="<c:url value='/adminlte/plugins/overlayScrollbars/css/OverlayScrollbars.min.css'/>">
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
            <h1>Meu Perfil</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item active">Perfil</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div id="password-alert"></div>
        <div id="profile-alert"></div>
        <c:if test="${not empty successMessage}">
          <div class="alert alert-success">
            <c:out value="${successMessage}" />
          </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger">
            <c:out value="${errorMessage}" />
          </div>
        </c:if>
        <div class="row">
          <div class="col-md-8">
            <!-- Edição de Perfil -->
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Editar Perfil</h3>
              </div>
              <form id="profile-form">
                <div class="card-body">
                  <div class="form-group">
                    <label for="username">Username</label>
                    <input id="username" type="text" class="form-control" value="<c:out value='${usuario.username}'/>" disabled>
                    <small class="text-muted">Username não pode ser alterado</small>
                  </div>
                  <div class="form-group">
                    <label for="email">Email</label>
                    <input id="email" type="email" class="form-control" name="email" value="<c:out value='${usuario.email}'/>" required>
                  </div>
                  <hr>
                  <h5>Perfil</h5>
                  <div class="form-group">
                    <label for="nomeCompleto">Nome completo</label>
                    <input id="nomeCompleto" type="text" class="form-control" value="<c:out value='${usuario.perfil.nomeCompleto}'/>" required maxlength="150">
                  </div>
                  <div class="form-group">
                    <label for="cpf">CPF</label>
                    <input id="cpf" type="text" class="form-control" value="<c:out value='${usuario.perfil.cpf}'/>" maxlength="14">
                  </div>
                  <div class="form-group">
                    <label for="telefone">Telefone</label>
                    <input id="telefone" type="text" class="form-control" value="<c:out value='${usuario.perfil.telefone}'/>" maxlength="20">
                  </div>
                  <div class="form-group">
                    <label for="dataNascimento">Data de nascimento</label>
                    <input id="dataNascimento" type="date" class="form-control" value="<c:out value='${usuario.perfil.dataNascimento}'/>">
                  </div>
                </div>
                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save mr-1"></i> Salvar Alterações
                  </button>
                </div>
              </form>
            </div>

            <!-- Alteração de Senha -->
            <div class="card card-warning">
              <div class="card-header">
                <h3 class="card-title">Alterar Senha</h3>
              </div>
              <form id="own-password-form">
                <div class="card-body">
                  <div class="form-group">
                    <label for="currentPassword">Senha Atual</label>
                    <input id="currentPassword" name="currentPassword" type="password" class="form-control" required>
                  </div>
                  <div class="form-group">
                    <label for="newPassword">Nova Senha</label>
                    <input id="newPassword" name="newPassword" type="password" class="form-control" minlength="6" required>
                  </div>
                  <div class="form-group">
                    <label for="confirmNewPassword">Confirmar Nova Senha</label>
                    <input id="confirmNewPassword" name="confirmNewPassword" type="password" class="form-control" minlength="6" required>
                  </div>
                </div>
                <div class="card-footer">
                  <button type="submit" class="btn btn-warning">
                    <i class="fas fa-key mr-1"></i> Atualizar Senha
                  </button>
                </div>
              </form>
            </div>
          </div>

          <!-- Informações do Perfil -->
          <div class="col-md-4">
            <div class="card card-primary card-outline">
              <div class="card-body box-profile">
                <div class="text-center">
                  <span id="profile-photo-fallback" class="align-items-center justify-content-center bg-secondary text-white img-circle elevation-2"
                        style="width:128px;height:128px;display:none;">
                    <i class="fas fa-user fa-3x"></i>
                  </span>
                  <img id="profile-photo-preview" class="profile-user-img img-fluid img-circle"
                       src="<c:url value='/usuarios/perfil/foto'/>"
                       alt="Foto do usuario"
                       onerror="this.style.display='none';document.getElementById('profile-photo-fallback').style.display='inline-flex';">
                </div>
                <h3 class="profile-username text-center">
                  <c:out value="${usuario.perfil.nomeCompleto}" />
                </h3>
                <p class="text-muted text-center">
                  <c:out value="${usuario.email}" />
                </p>
                <ul class="list-group list-group-unbordered mb-3">
                  <li class="list-group-item">
                    <b>CPF</b> <span class="float-right"><c:out value="${usuario.perfil.cpf}" /></span>
                  </li>
                  <li class="list-group-item">
                    <b>Telefone</b> <span class="float-right"><c:out value="${usuario.perfil.telefone}" /></span>
                  </li>
                  <li class="list-group-item">
                    <b>Nascimento</b> <span class="float-right"><c:out value="${usuario.perfil.dataNascimento}" /></span>
                  </li>
                </ul>
                <form action="<c:url value='/usuarios/perfil/foto'/>" method="post" enctype="multipart/form-data">
                  <div class="form-group mb-2">
                    <label for="foto">Atualizar foto</label>
                    <div class="custom-file">
                      <input id="foto" name="foto" type="file" class="custom-file-input" accept="image/*" required>
                      <label class="custom-file-label" for="foto">Escolher imagem...</label>
                    </div>
                    <small class="text-muted">Envie JPG, PNG ou WEBP.</small>
                  </div>
                  <button type="submit" class="btn btn-outline-primary btn-block">
                    <i class="fas fa-camera mr-1"></i> Atualizar Foto
                  </button>
                </form>
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
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
<script>
  // Formulário de alteração de perfil
  const profileForm = document.getElementById('profile-form');
  const profileAlert = document.getElementById('profile-alert');
  const photoInput = document.getElementById('foto');
  const photoLabel = document.querySelector('label[for="foto"].custom-file-label');
  const photoPreview = document.getElementById('profile-photo-preview');
  const photoFallback = document.getElementById('profile-photo-fallback');

  photoInput.addEventListener('change', function () {
    const file = this.files && this.files.length > 0 ? this.files[0] : null;
    if (!file) {
      photoLabel.textContent = 'Escolher imagem...';
      return;
    }
    photoLabel.textContent = file.name;
    if (file.type && file.type.startsWith('image/')) {
      photoPreview.src = URL.createObjectURL(file);
      photoPreview.style.display = 'inline-block';
      photoFallback.style.display = 'none';
    }
  });
  
  profileForm.addEventListener('submit', async function (event) {
    event.preventDefault();
    
    const payload = {
      email: document.getElementById('email').value,
      perfil: {
        nomeCompleto: document.getElementById('nomeCompleto').value,
        cpf: document.getElementById('cpf').value,
        telefone: document.getElementById('telefone').value,
        dataNascimento: document.getElementById('dataNascimento').value || null
      }
    };
    
    try {
      const response = await fetch('<c:url value="/usuarios/perfil"/>', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const error = await response.json().catch(() => ({ detail: 'Falha ao atualizar perfil.' }));
        throw new Error(error.detail || 'Falha ao atualizar perfil.');
      }

      profileAlert.innerHTML = '<div class="alert alert-success">Perfil atualizado com sucesso.</div>';
    } catch (err) {
      profileAlert.innerHTML = '<div class="alert alert-danger">' + err.message + '</div>';
    }
  });

  // Formulário de alteração de senha
  const passwordForm = document.getElementById('own-password-form');
  const alertContainer = document.getElementById('password-alert');

  passwordForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    const payload = {
      currentPassword: document.getElementById('currentPassword').value,
      newPassword: document.getElementById('newPassword').value,
      confirmNewPassword: document.getElementById('confirmNewPassword').value
    };

    try {
      const response = await fetch('<c:url value="/usuarios/${usuario.id}/senha"/>', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const error = await response.json().catch(() => ({ detail: 'Falha ao atualizar senha.' }));
        throw new Error(error.detail || 'Falha ao atualizar senha.');
      }

      alertContainer.innerHTML = '<div class="alert alert-success">Senha atualizada com sucesso.</div>';
      passwordForm.reset();
    } catch (err) {
      alertContainer.innerHTML = '<div class="alert alert-danger">' + err.message + '</div>';
    }
  });
</script>
</body>
</html>

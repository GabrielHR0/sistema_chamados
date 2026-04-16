<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Editar Bloco - Chamados</title>

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
            <h1>Editar Bloco</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/blocos'/>">Blocos</a></li>
              <li class="breadcrumb-item active">Editar</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty param.updated}">
          <div class="alert alert-success">Bloco atualizado com sucesso.</div>
        </c:if>

        <div class="row">
          <div class="col-lg-8">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Dados do bloco</h3>
              </div>
              <form method="post" action="<c:url value='/blocos/${bloco.id}'/>">
                <div class="card-body">
                  <div class="form-group">
                    <label for="identificacao">Identificação</label>
                    <input id="identificacao" class="form-control" value="<c:out value='${bloco.identificacao}'/>" disabled>
                    <small class="text-muted">A identificação é fixa nesta etapa do cadastro.</small>
                  </div>
                  <div class="form-group">
                    <div class="custom-control custom-switch">
                      <input id="ativo" name="ativo" value="true" type="checkbox" class="custom-control-input" ${bloco.ativo ? 'checked' : ''}>
                      <input type="hidden" name="ativo" value="false">
                      <label class="custom-control-label" for="ativo">Bloco ativo</label>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="observacoes">Observações</label>
                    <textarea id="observacoes" name="observacoes" class="form-control" rows="6" maxlength="1000"><c:out value="${bloco.observacoes}" /></textarea>
                  </div>
                </div>
                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save mr-1"></i>Salvar alterações
                  </button>
                </div>
              </form>
            </div>
          </div>

          <div class="col-lg-4">
            <div class="card card-info">
              <div class="card-header">
                <h3 class="card-title">Resumo</h3>
              </div>
              <div class="card-body">
                <p class="mb-2"><strong>Status:</strong> <c:out value="${bloco.ativo ? 'Ativo' : 'Inativo'}" /></p>
                <p class="mb-0"><strong>Identificação:</strong> <c:out value="${bloco.identificacao}" /></p>
              </div>
            </div>

            <div class="card card-danger">
              <div class="card-header">
                <h3 class="card-title">Ações perigosas</h3>
              </div>
              <div class="card-body">
                <form action="<c:url value='/blocos/${bloco.id}/deletar'/>" method="post">
                  <button type="submit" class="btn btn-danger btn-block" onclick="return confirm('Deseja deletar este bloco?')">
                    Deletar bloco
                  </button>
                </form>
                <small class="text-muted d-block mt-2">A exclusão será bloqueada se existirem moradias vinculadas.</small>
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
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Nova Unidade - Chamados</title>

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
            <h1>Nova Unidade</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/andares/${unidade.andarId}'/>">Andar</a></li>
              <li class="breadcrumb-item active">Nova Unidade</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Cadastro da unidade</h3>
          </div>
          <form method="post" action="<c:url value='/andares/${unidade.andarId}/unidades'/>">
            <div class="card-body">
              <div class="form-group">
                <label for="apartamentoNumero">Número do apartamento</label>
                <input id="apartamentoNumero" name="apartamentoNumero" type="number" min="1" class="form-control" required>
              </div>
              <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status" class="form-control">
                  <option value="EM_FUNCIONAMENTO">Em funcionamento</option>
                  <option value="EM_MANUTENCAO">Em manutenção</option>
                  <option value="INTERDITADO">Interditado</option>
                </select>
              </div>
              <div class="form-group">
                <div class="custom-control custom-switch">
                  <input id="ativo" name="ativo" value="true" type="checkbox" class="custom-control-input" checked>
                  <input type="hidden" name="ativo" value="false">
                  <label class="custom-control-label" for="ativo">Unidade ativa</label>
                </div>
              </div>
              <div class="form-group">
                <label for="observacoes">Observações</label>
                <textarea id="observacoes" name="observacoes" class="form-control" rows="4" maxlength="1000"></textarea>
              </div>
            </div>
            <div class="card-footer">
              <button type="submit" class="btn btn-primary">Salvar e gerar unidade</button>
              <a href="<c:url value='/andares/${unidade.andarId}'/>" class="btn btn-secondary">Cancelar</a>
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
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
</body>
</html>

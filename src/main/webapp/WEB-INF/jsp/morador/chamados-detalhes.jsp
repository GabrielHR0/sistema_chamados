<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Detalhes do Meu Chamado</title>

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
            <h1>Chamado <small><c:out value="${chamadoCodigo}"/></small></h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item"><a href="<c:url value='/chamados/morador'/>">Meus Chamados</a></li>
              <li class="breadcrumb-item active">Detalhes</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <c:if test="${param.commented != null}">
          <div class="alert alert-success">Comentario adicionado.</div>
        </c:if>
        <c:if test="${param.created != null}">
          <div class="alert alert-success">Chamado aberto com sucesso.</div>
        </c:if>

        <div class="row">
          <div class="col-md-8">
            <div class="card card-outline card-primary">
              <div class="card-header">
                <h3 class="card-title">Resumo</h3>
              </div>
              <div class="card-body">
                <p><strong>Unidade:</strong> <c:out value="${chamado.unidadeIdentificacao}"/></p>
                <p><strong>Tipo:</strong> <c:out value="${chamado.tipoTitulo}"/> (SLA: <c:out value="${chamado.tipoSlaHoras}"/>h)</p>
                <p>
                  <strong>Status:</strong>
                  <span class="badge ${chamado.statusFinal ? 'badge-success' : 'badge-info'}"><c:out value="${chamado.statusNome}"/></span>
                  <c:if test="${chamado.statusFinal}">
                    <span class="badge badge-secondary ml-1"><i class="fas fa-check-circle"></i> Fechado</span>
                  </c:if>
                </p>
                <p><strong>Responsavel:</strong> <c:out value="${not empty chamado.colaboradorResponsavelNome ? chamado.colaboradorResponsavelNome : 'Aguardando atendimento'}"/></p>
                <p><strong>Criado em:</strong> <strong><c:out value="${chamadoDataCriacaoFmt}"/></strong></p>
                <c:if test="${not empty chamado.dataFinalizacao}">
                  <p><strong>Finalizado em:</strong> <strong><c:out value="${chamadoDataFinalizacaoFmt}"/></strong></p>
                </c:if>
                <hr/>
                <p><strong>Descricao:</strong></p>
                <p><c:out value="${chamado.descricao}"/></p>
              </div>
            </div>

            <div class="card card-outline card-warning">
              <div class="card-header">
                <h3 class="card-title">Anexos</h3>
              </div>
              <div class="card-body">
                <c:if test="${empty chamado.anexos}">
                  <p class="text-muted mb-0">Sem anexos enviados.</p>
                </c:if>
                <c:if test="${not empty chamado.anexos}">
                  <ul class="mb-0">
                    <c:forEach var="anexo" items="${chamado.anexos}">
                      <li>
                        <a href="<c:url value='/chamados/morador/anexos/${anexo.id}'/>">
                          <c:out value="${anexo.nomeOriginal}"/>
                        </a>
                        <small class="text-muted">(<c:out value="${anexo.tamanhoBytes}"/> bytes)</small>
                      </li>
                    </c:forEach>
                  </ul>
                </c:if>
              </div>
            </div>

            <div class="card card-outline card-info">
              <div class="card-header">
                <h3 class="card-title">Historico de Interacoes</h3>
              </div>
              <div class="card-body">
                <c:forEach var="comentario" items="${chamado.comentarios}">
                  <div class="border rounded p-3 mb-3">
                    <div class="d-flex justify-content-between">
                      <strong><c:out value="${comentario.autorNome}"/></strong>
                      <small class="text-muted"><c:out value="${comentario.dataCriacaoFormatada}"/></small>
                    </div>
                    <p class="mb-2 mt-2"><c:out value="${comentario.comentario}"/></p>
                    <c:if test="${not empty comentario.anexos}">
                      <small class="text-muted d-block mb-1">Anexos do comentario:</small>
                      <ul class="mb-0">
                        <c:forEach var="anexoComentario" items="${comentario.anexos}">
                          <li>
                            <a href="<c:url value='/chamados/morador/comentarios/anexos/${anexoComentario.id}'/>">
                              <c:out value="${anexoComentario.nomeOriginal}"/>
                            </a>
                          </li>
                        </c:forEach>
                      </ul>
                    </c:if>
                  </div>
                </c:forEach>
                <c:if test="${empty chamado.comentarios}">
                  <p class="text-muted mb-0">Sem comentarios ate o momento.</p>
                </c:if>
              </div>
            </div>

            <div class="card card-outline card-secondary">
              <div class="card-header">
                <h3 class="card-title">Adicionar comentario</h3>
              </div>
              <form action="<c:url value='/chamados/morador/${chamado.id}/comentarios'/>" method="post" enctype="multipart/form-data">
                <div class="card-body">
                  <div class="form-group">
                    <label for="comentario">Comentario</label>
                    <textarea id="comentario" name="comentario" class="form-control" rows="3" required></textarea>
                  </div>
                  <div class="form-group mb-0">
                    <label for="arquivoComentarioNovo">Anexo do comentario (opcional)</label>
                    <input id="arquivoComentarioNovo" name="arquivo" type="file" class="form-control-file" />
                  </div>
                </div>
                <div class="card-footer">
                  <button type="submit" class="btn btn-info btn-sm">
                    <i class="fas fa-paper-plane"></i> Publicar
                  </button>
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
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
</body>
</html>

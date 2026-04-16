<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Detalhes do Chamado</title>

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
              <li class="breadcrumb-item"><a href="<c:url value='/chamados/lista'/>">Chamados</a></li>
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
        <c:if test="${param.assigned != null}">
          <div class="alert alert-success">Chamado assumido com sucesso.</div>
        </c:if>
        <c:if test="${param.commented != null}">
          <div class="alert alert-success">Comentario adicionado.</div>
        </c:if>
        <c:if test="${param.created != null}">
          <div class="alert alert-success">Chamado criado com sucesso.</div>
        </c:if>
        <c:if test="${param.statusUpdated != null}">
          <div class="alert alert-success">Status atualizado com sucesso.</div>
        </c:if>
        <c:if test="${param.fileUploaded != null}">
          <div class="alert alert-success">Anexo enviado com sucesso.</div>
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
                    <span class="badge badge-secondary ml-1"><i class="fas fa-check-circle"></i> Chamado fechado</span>
                  </c:if>
                </p>
                <p><strong>Responsavel:</strong> <c:out value="${not empty chamado.colaboradorResponsavelNome ? chamado.colaboradorResponsavelNome : '-'}"/></p>
                <p><strong>Criado em:</strong> <strong><c:out value="${chamadoDataCriacaoFmt}"/></strong></p>
                <c:if test="${not empty chamado.dataFinalizacao}">
                  <p><strong>Finalizado em:</strong> <strong><c:out value="${chamadoDataFinalizacaoFmt}"/></strong></p>
                </c:if>
                <hr/>
                <p><strong>Descricao:</strong></p>
                <p><c:out value="${chamado.descricao}"/></p>
              </div>
              <div class="card-footer">
                <c:if test="${canChamadoUpdate && empty chamado.colaboradorResponsavelNome}">
                  <form action="<c:url value='/chamados/${chamado.id}/assumir'/>" method="post" class="d-inline">
                    <button class="btn btn-primary btn-sm" type="submit">
                      <i class="fas fa-user-check"></i> Assumir chamado
                    </button>
                  </form>
                </c:if>
              </div>
            </div>

            <c:if test="${canChamadoUpdate}">
              <div class="card card-outline card-success">
                <div class="card-header">
                  <h3 class="card-title">Fluxo de Status</h3>
                </div>
                <div class="card-body">
                  <p class="mb-2">
                    <strong>Status atual:</strong>
                    <span class="badge badge-info"><c:out value="${chamado.statusNome}"/></span>
                  </p>
                  <c:choose>
                    <c:when test="${not empty chamado.proximosStatus}">
                      <p class="text-muted mb-3">Selecione o próximo passo do fluxo:</p>
                      <div class="d-flex flex-wrap align-items-center">
                        <c:forEach var="proximo" items="${chamado.proximosStatus}" varStatus="idx">
                          <form action="<c:url value='/chamados/${chamado.id}/status'/>" method="post" class="d-inline mb-2 mr-2">
                            <input type="hidden" name="statusId" value="${proximo.id}" />
                            <button class="btn btn-success btn-sm" type="submit">
                              <i class="fas fa-arrow-right"></i>
                              <c:out value="${proximo.nome}"/>
                            </button>
                          </form>
                        </c:forEach>
                      </div>
                    </c:when>
                    <c:otherwise>
                      <span class="badge badge-secondary">Sem próximos status disponíveis</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </c:if>

            <div class="card card-outline card-info">
              <div class="card-header">
                <h3 class="card-title">Historico de Interacoes</h3>
              </div>
              <div class="card-body">
                <div class="timeline">
                  <c:forEach var="comentario" items="${chamado.comentarios}">
                    <div>
                      <i class="fas fa-comment bg-info"></i>
                      <div class="timeline-item">
                        <span class="time"><i class="far fa-clock"></i> <c:out value="${comentario.dataCriacaoFormatada}"/></span>
                        <h3 class="timeline-header"><c:out value="${comentario.autorNome}"/></h3>
                        <div class="timeline-body">
                          <p class="mb-2"><c:out value="${comentario.comentario}"/></p>
                          <div class="mb-2">
                            <strong>Anexos do comentario:</strong>
                            <c:choose>
                              <c:when test="${not empty comentario.anexos}">
                                <ul class="mb-0">
                                  <c:forEach var="anexoComentario" items="${comentario.anexos}">
                                    <li>
                                      <a href="<c:url value='/chamados/comentarios/anexos/${anexoComentario.id}'/>">
                                        <c:out value="${anexoComentario.nomeOriginal}"/>
                                      </a>
                                      <small class="text-muted">
                                        (<c:out value="${anexoComentario.tamanhoBytes}"/> bytes - <c:out value="${anexoComentario.dataCriacaoFormatada}"/>)
                                      </small>
                                    </li>
                                  </c:forEach>
                                </ul>
                              </c:when>
                              <c:otherwise>
                                <div class="text-muted">Sem anexos.</div>
                              </c:otherwise>
                            </c:choose>
                          </div>
                        </div>
                      </div>
                    </div>
                  </c:forEach>
                  <c:if test="${empty chamado.comentarios}">
                    <p class="text-muted mb-0">Sem comentarios ate o momento.</p>
                  </c:if>
                </div>
              </div>
            </div>

            <div class="card card-outline card-warning">
              <div class="card-header">
                <h3 class="card-title">Anexos do chamado</h3>
              </div>
              <div class="card-body">
                <c:if test="${empty chamado.anexos}">
                  <p class="text-muted mb-0">Sem anexos enviados.</p>
                </c:if>
                <c:if test="${not empty chamado.anexos}">
                  <table class="table table-sm table-bordered">
                    <thead>
                    <tr>
                      <th>Arquivo</th>
                      <th>Tipo</th>
                      <th>Tamanho (bytes)</th>
                      <th>Data</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="anexo" items="${chamado.anexos}">
                      <tr>
                        <td>
                          <a href="<c:url value='/chamados/anexos/${anexo.id}'/>">
                            <c:out value="${anexo.nomeOriginal}"/>
                          </a>
                        </td>
                        <td><c:out value="${not empty anexo.contentType ? anexo.contentType : '-'}"/></td>
                        <td><c:out value="${anexo.tamanhoBytes}"/></td>
                        <td><c:out value="${anexo.dataCriacaoFormatada}"/></td>
                      </tr>
                    </c:forEach>
                    </tbody>
                  </table>
                </c:if>
              </div>
            </div>

            <c:if test="${canChamadoUpdate}">
              <div class="card card-outline card-secondary">
                <div class="card-header">
                  <h3 class="card-title">Adicionar comentario</h3>
                </div>
                <form action="<c:url value='/chamados/${chamado.id}/comentarios'/>" method="post" enctype="multipart/form-data">
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

              <div class="card card-outline card-secondary">
                <div class="card-header">
                  <h3 class="card-title">Enviar anexo</h3>
                </div>
                <form action="<c:url value='/chamados/${chamado.id}/anexos'/>" method="post" enctype="multipart/form-data">
                  <div class="card-body">
                    <div class="form-group">
                      <label for="arquivo">Arquivo</label>
                      <input id="arquivo" name="arquivo" type="file" class="form-control-file" required />
                    </div>
                  </div>
                  <div class="card-footer">
                    <button type="submit" class="btn btn-warning btn-sm">
                      <i class="fas fa-upload"></i> Enviar
                    </button>
                  </div>
                </form>
              </div>
            </c:if>
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

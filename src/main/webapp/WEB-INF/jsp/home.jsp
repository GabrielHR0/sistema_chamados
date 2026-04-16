<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard - Chamados</title>

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
            <h1>Dashboard</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="<c:url value='/'/>">Home</a></li>
              <li class="breadcrumb-item active">Dashboard</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <c:choose>
          <c:when test="${dashboardRole == 'ADMIN'}">
            <div class="row">
              <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><c:out value="${kpiTotalChamados}" /></h3>
                    <p>Total de chamados</p>
                  </div>
                  <div class="icon"><i class="fas fa-ticket-alt"></i></div>
                  <a href="<c:url value='/chamados/lista'/>" class="small-box-footer">Ver lista <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                  <div class="inner">
                    <h3><c:out value="${kpiAbertos}" /></h3>
                    <p>Chamados nao finais</p>
                  </div>
                  <div class="icon"><i class="fas fa-folder-open"></i></div>
                  <a href="<c:url value='/chamados/lista'/>" class="small-box-footer">Acompanhar <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><c:out value="${kpiFinalizados}" /></h3>
                    <p>Finalizados</p>
                  </div>
                  <div class="icon"><i class="fas fa-check-circle"></i></div>
                  <a href="<c:url value='/chamados/lista'/>" class="small-box-footer">Ver historico <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                  <div class="inner">
                    <h3><c:out value="${kpiDisponiveisFila}" /></h3>
                    <p>Disponiveis na fila</p>
                  </div>
                  <div class="icon"><i class="fas fa-inbox"></i></div>
                  <a href="<c:url value='/chamados/atendimento'/>" class="small-box-footer">Abrir fila <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
          </c:when>
          <c:when test="${dashboardRole == 'COLABORADOR'}">
            <div class="row">
              <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><c:out value="${kpiEscopoTotal}" /></h3>
                    <p>Chamados no escopo</p>
                  </div>
                  <div class="icon"><i class="fas fa-layer-group"></i></div>
                  <a href="<c:url value='/chamados/lista'/>" class="small-box-footer">Ver lista <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                  <div class="inner">
                    <h3><c:out value="${kpiDisponiveisFila}" /></h3>
                    <p>Disponiveis para assumir</p>
                  </div>
                  <div class="icon"><i class="fas fa-inbox"></i></div>
                  <a href="<c:url value='/chamados/atendimento'/>" class="small-box-footer">Abrir fila <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                  <div class="inner">
                    <h3><c:out value="${kpiMeusNaoFinais}" /></h3>
                    <p>Meus chamados ativos</p>
                  </div>
                  <div class="icon"><i class="fas fa-user-clock"></i></div>
                  <a href="<c:url value='/chamados/atendimento'/>" class="small-box-footer">Atender <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><c:out value="${kpiTiposPermitidos}" /></h3>
                    <p>Tipos permitidos</p>
                  </div>
                  <div class="icon"><i class="fas fa-tags"></i></div>
                  <a href="<c:url value='/chamados/lista'/>" class="small-box-footer">Detalhar <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
          </c:when>
          <c:when test="${dashboardRole == 'MORADOR'}">
            <div class="row">
              <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><c:out value="${kpiMeusChamados}" /></h3>
                    <p>Meus chamados</p>
                  </div>
                  <div class="icon"><i class="fas fa-ticket-alt"></i></div>
                  <a href="<c:url value='/chamados/morador'/>" class="small-box-footer">Ver chamados <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                  <div class="inner">
                    <h3><c:out value="${kpiAbertos}" /></h3>
                    <p>Aguardando atendimento</p>
                  </div>
                  <div class="icon"><i class="fas fa-hourglass-half"></i></div>
                  <a href="<c:url value='/chamados/morador'/>" class="small-box-footer">Acompanhar <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><c:out value="${kpiFinalizados}" /></h3>
                    <p>Finalizados</p>
                  </div>
                  <div class="icon"><i class="fas fa-check-circle"></i></div>
                  <a href="<c:url value='/chamados/morador'/>" class="small-box-footer">Ver historico <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-primary">
                  <div class="inner">
                    <h3><c:out value="${kpiMinhasMoradias}" /></h3>
                    <p>Minhas moradias</p>
                  </div>
                  <div class="icon"><i class="fas fa-home"></i></div>
                  <a href="<c:url value='/chamados/morador/unidades'/>" class="small-box-footer">Ver moradias <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
          </c:when>
        </c:choose>

        <div class="card">
          <div class="card-header border-0">
            <h3 class="card-title mb-0">Ultimos chamados</h3>
          </div>
          <div class="card-body table-responsive p-0">
            <table class="table table-hover text-nowrap mb-0">
              <thead>
              <tr>
                <th>Unidade</th>
                <th>Tipo</th>
                <th>Status</th>
                <th>Criacao</th>
                <th class="text-right">Acao</th>
              </tr>
              </thead>
              <tbody>
              <c:choose>
                <c:when test="${empty recentChamados}">
                  <tr>
                    <td colspan="5" class="text-center text-muted">Sem chamados para exibir.</td>
                  </tr>
                </c:when>
                <c:otherwise>
                  <c:forEach var="ch" items="${recentChamados}">
                    <tr>
                      <td><c:out value="${ch.unidadeIdentificacao}" /></td>
                      <td><c:out value="${ch.tipoTitulo}" /></td>
                      <td>
                        <span class="badge ${ch.statusFinal ? 'badge-success' : 'badge-warning'}">
                          <c:out value="${ch.statusNome}" />
                        </span>
                      </td>
                      <td><c:out value="${ch.dataCriacaoFormatada}" /></td>
                      <td class="text-right">
                        <c:choose>
                          <c:when test="${dashboardRole == 'MORADOR'}">
                            <a class="btn btn-xs btn-outline-primary" href="<c:url value='/chamados/morador/${ch.id}'/>">Ver</a>
                          </c:when>
                          <c:otherwise>
                            <a class="btn btn-xs btn-outline-primary" href="<c:url value='/chamados/${ch.id}'/>">Ver</a>
                          </c:otherwise>
                        </c:choose>
                      </td>
                    </tr>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
              </tbody>
            </table>
          </div>
        </div>

        <div class="row">
          <div class="col-md-7">
            <div class="card card-outline card-primary">
              <div class="card-header">
                <h3 class="card-title mb-0">Distribuicao por status</h3>
              </div>
              <div class="card-body">
                <canvas id="statusChart" style="min-height: 280px; max-height: 320px;"></canvas>
              </div>
            </div>
          </div>
          <div class="col-md-5">
            <div class="card card-outline card-info">
              <div class="card-header">
                <h3 class="card-title mb-0">Atividade recente</h3>
              </div>
              <div class="card-body">
                <c:choose>
                  <c:when test="${empty recentChamados}">
                    <p class="text-muted mb-0">Sem atividade recente.</p>
                  </c:when>
                  <c:otherwise>
                    <div class="timeline timeline-inverse mb-0">
                      <c:forEach var="ch" items="${recentChamados}" varStatus="vs">
                        <div>
                          <i class="fas ${ch.statusFinal ? 'fa-check bg-success' : 'fa-clock bg-warning'}"></i>
                          <div class="timeline-item">
                            <span class="time"><i class="far fa-clock"></i> <c:out value="${ch.dataCriacaoFormatada}" /></span>
                            <h3 class="timeline-header">
                              <c:out value="${ch.tipoTitulo}" /> - <c:out value="${ch.unidadeIdentificacao}" />
                            </h3>
                            <div class="timeline-body">
                              Status atual: <strong><c:out value="${ch.statusNome}" /></strong>
                            </div>
                          </div>
                        </div>
                        <c:if test="${vs.last}">
                          <div>
                            <i class="far fa-calendar-alt bg-gray"></i>
                          </div>
                        </c:if>
                      </c:forEach>
                    </div>
                  </c:otherwise>
                </c:choose>
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
<script src="<c:url value='/adminlte/plugins/chart.js/Chart.min.js'/>"></script>
<script src="<c:url value='/adminlte/dist/js/adminlte.min.js'/>"></script>
<script>
  (function () {
    const labels = [
      <c:forEach var="label" items="${statusChartLabels}" varStatus="loop">
      '<c:out value="${label}" />'<c:if test="${!loop.last}">,</c:if>
      </c:forEach>
    ];
    const values = [
      <c:forEach var="value" items="${statusChartValues}" varStatus="loop">
      <c:out value="${value}" /><c:if test="${!loop.last}">,</c:if>
      </c:forEach>
    ];

    const canvas = document.getElementById('statusChart');
    if (!canvas || labels.length === 0) {
      return;
    }

    new Chart(canvas, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Chamados',
          data: values,
          backgroundColor: ['#17a2b8', '#ffc107', '#28a745', '#dc3545', '#6f42c1', '#fd7e14', '#20c997'],
          borderColor: '#ffffff',
          borderWidth: 1
        }]
      },
      options: {
        maintainAspectRatio: false,
        responsive: true,
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true,
              precision: 0
            }
          }]
        },
        legend: { display: false }
      }
    });
  })();
</script>
</body>
</html>

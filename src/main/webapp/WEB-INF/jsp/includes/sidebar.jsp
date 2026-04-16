<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Main Sidebar Container -->
<aside class="main-sidebar sidebar-dark-primary elevation-4">
  <!-- Brand Logo -->
  <a href="<c:url value='/'/>" class="brand-link">
    <img src="<c:url value='/adminlte/dist/img/AdminLTELogo.png'/>" alt="AdminLTE Logo" class="brand-image img-circle elevation-3" style="opacity: .8">
    <span class="brand-text font-weight-light">Chamados</span>
  </a>

  <!-- Sidebar -->
  <div class="sidebar">
    <!-- Sidebar user panel -->
      <div class="user-panel mt-3 pb-3 mb-3 d-flex">
        <div class="image">
          <a href="<c:url value='/usuarios/perfil'/>">
            <span class="d-inline-flex align-items-center justify-content-center bg-secondary elevation-2 img-circle"
                  style="width:34px;height:34px;">
              <img src="<c:url value='/usuarios/perfil/foto'/>"
                   class="img-circle"
                   alt="User Image"
                   style="width:34px;height:34px;object-fit:cover;"
                   onerror="this.style.display='none';this.parentElement.classList.remove('bg-secondary');this.parentElement.classList.add('bg-dark');this.nextElementSibling.style.display='inline-block';">
              <i class="fas fa-user text-white" style="display:none;"></i>
            </span>
          </a>
        </div>
        <div class="info">
          <a href="<c:url value='/usuarios/perfil'/>" class="d-block">
            <c:out value="${not empty loggedDisplayName ? loggedDisplayName : loggedUsername}"/>
          </a>
        </div>
      </div>

    <!-- Sidebar Menu -->
    <nav class="mt-2">
      <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
        <li class="nav-item">
          <a href="<c:url value='/'/>" class="nav-link ${activePage == 'home' ? 'active' : ''}">
            <i class="nav-icon fas fa-tachometer-alt"></i>
            <p>Dashboard</p>
          </a>
        </li>
        <c:if test="${isAdminRole}">
          <li class="nav-item">
            <a href="<c:url value='/auditorias'/>" class="nav-link ${activePage == 'auditorias-lista' ? 'active' : ''}">
              <i class="nav-icon fas fa-history"></i>
              <p>Auditorias</p>
            </a>
          </li>
        </c:if>
        <c:if test="${canManageChamados}">
          <li class="nav-item ${activePage == 'chamados-lista' || activePage == 'chamados-atendimento' || activePage == 'chamados-novo' || activePage == 'chamados-detalhes' || activePage == 'chamados-tipos' || activePage == 'chamados-status' || activePage == 'chamados-lotacoes' ? 'menu-open' : ''}">
            <a href="#" class="nav-link ${activePage == 'chamados-lista' || activePage == 'chamados-atendimento' || activePage == 'chamados-novo' || activePage == 'chamados-detalhes' || activePage == 'chamados-tipos' || activePage == 'chamados-status' || activePage == 'chamados-lotacoes' ? 'active' : ''}">
              <i class="nav-icon fas fa-ticket-alt"></i>
              <p>
                Chamados
                <i class="fas fa-angle-left right"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <c:if test="${canChamadoRead}">
                <li class="nav-item">
                  <a href="<c:url value='/chamados/lista'/>" class="nav-link ${activePage == 'chamados-lista' || activePage == 'chamados-detalhes' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Listar Chamados</p>
                  </a>
                </li>
              </c:if>
              <c:if test="${canChamadoUpdate}">
                <li class="nav-item">
                  <a href="<c:url value='/chamados/atendimento'/>" class="nav-link ${activePage == 'chamados-atendimento' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Fila de Atendimento</p>
                  </a>
                </li>
              </c:if>
              <c:if test="${canChamadoCreate}">
                <li class="nav-item">
                  <a href="<c:url value='/chamados/novo'/>" class="nav-link ${activePage == 'chamados-novo' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Novo Chamado</p>
                  </a>
                </li>
              </c:if>
              <c:if test="${canChamadoTipoRead || canChamadoTipoCreate}">
                <li class="nav-item">
                  <a href="<c:url value='/chamados/tipos'/>" class="nav-link ${activePage == 'chamados-tipos' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Tipos de Chamado</p>
                  </a>
                </li>
              </c:if>
              <c:if test="${canChamadoStatusRead || canChamadoStatusCreate}">
                <li class="nav-item">
                  <a href="<c:url value='/chamados/status'/>" class="nav-link ${activePage == 'chamados-status' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Status de Chamado</p>
                  </a>
                </li>
              </c:if>
              <c:if test="${canLotacaoRead || canLotacaoCreate}">
                <li class="nav-item">
                  <a href="<c:url value='/chamados/admin/lotacoes'/>" class="nav-link ${activePage == 'chamados-lotacoes' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Lotacoes</p>
                  </a>
                </li>
              </c:if>
            </ul>
          </li>
        </c:if>
        <c:if test="${isMoradorRole}">
          <li class="nav-item ${activePage == 'morador-chamados' || activePage == 'morador-chamados-novo' || activePage == 'morador-unidades' ? 'menu-open' : ''}">
            <a href="#" class="nav-link ${activePage == 'morador-chamados' || activePage == 'morador-chamados-novo' || activePage == 'morador-unidades' ? 'active' : ''}">
              <i class="nav-icon fas fa-home"></i>
              <p>
                Meu Espaco
                <i class="fas fa-angle-left right"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="<c:url value='/chamados/morador'/>" class="nav-link ${activePage == 'morador-chamados' ? 'active' : ''}">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Meus Chamados</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="<c:url value='/chamados/morador/novo'/>" class="nav-link ${activePage == 'morador-chamados-novo' ? 'active' : ''}">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Abrir Chamado</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="<c:url value='/chamados/morador/unidades'/>" class="nav-link ${activePage == 'morador-unidades' ? 'active' : ''}">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Minhas Moradias</p>
                </a>
              </li>
            </ul>
          </li>
        </c:if>
        <c:if test="${canManageUsers}">
          <li class="nav-item ${activePage == 'usuarios-lista' || activePage == 'usuarios-novo' ? 'menu-open' : ''}">
            <a href="#" class="nav-link ${activePage == 'usuarios-lista' || activePage == 'usuarios-novo' ? 'active' : ''}">
              <i class="nav-icon fas fa-users-cog"></i>
              <p>
                Usuarios
                <i class="fas fa-angle-left right"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <c:if test="${canUserUpdate}">
                <li class="nav-item">
                  <a href="<c:url value='/usuarios'/>" class="nav-link ${activePage == 'usuarios-lista' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Listar Usuarios</p>
                  </a>
                </li>
              </c:if>
              <c:if test="${canUserCreate}">
                <li class="nav-item">
                  <a href="<c:url value='/usuarios/novo'/>" class="nav-link ${activePage == 'usuarios-novo' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Novo Usuario</p>
                  </a>
                </li>
              </c:if>
            </ul>
          </li>
        </c:if>
        <c:if test="${canManageBlocks}">
          <li class="nav-item ${activePage == 'blocos-lista' || activePage == 'blocos-novo' || activePage == 'blocos-detalhes' || activePage == 'blocos-editar' || activePage == 'andares-novo' || activePage == 'andares-detalhes' || activePage == 'andares-editar' || activePage == 'unidades-novo' || activePage == 'unidades-detalhes' || activePage == 'unidades-editar' || activePage == 'moradias' ? 'menu-open' : ''}">
            <a href="#" class="nav-link ${activePage == 'blocos-lista' || activePage == 'blocos-novo' || activePage == 'blocos-detalhes' || activePage == 'blocos-editar' || activePage == 'andares-novo' || activePage == 'andares-detalhes' || activePage == 'andares-editar' || activePage == 'unidades-novo' || activePage == 'unidades-detalhes' || activePage == 'unidades-editar' || activePage == 'moradias' ? 'active' : ''}">
              <i class="nav-icon fas fa-building"></i>
              <p>
                Blocos
                <i class="fas fa-angle-left right"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="<c:url value='/blocos'/>" class="nav-link ${activePage == 'blocos-lista' || activePage == 'blocos-detalhes' || activePage == 'blocos-editar' ? 'active' : ''}">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Listar Blocos</p>
                </a>
              </li>
              <c:if test="${canManageBlocks}">
                <li class="nav-item">
                  <a href="<c:url value='/blocos/novo'/>" class="nav-link ${activePage == 'blocos-novo' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Novo Bloco</p>
                  </a>
                </li>
              </c:if>
              <c:if test="${canMoradiaRead || canMoradiaCreate || canMoradiaUpdate}">
                <li class="nav-item">
                  <a href="<c:url value='/moradias'/>" class="nav-link ${activePage == 'moradias' ? 'active' : ''}">
                    <i class="far fa-circle nav-icon"></i>
                    <p>Moradias</p>
                  </a>
                </li>
              </c:if>
            </ul>
          </li>
        </c:if>
        <li class="nav-item mt-2">
          <form action="<c:url value='/logout'/>" method="post" class="m-0">
            <button type="submit" class="nav-link btn btn-link text-left w-100">
              <i class="nav-icon fas fa-sign-out-alt"></i>
              <p>Sair</p>
            </button>
          </form>
        </li>
      </ul>
    </nav>
    <!-- /.sidebar-menu -->
  </div>
  <!-- /.sidebar -->
</aside>

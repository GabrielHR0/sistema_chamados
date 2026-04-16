package com.condominio.chamados.security.controller;

import com.condominio.chamados.security.permission.PermissionConstants;
import com.condominio.chamados.security.service.UserPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice
public class SecurityViewModelAdvice {

    @ModelAttribute
    public void addSecurityAttributes(Model model, Authentication authentication) {
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        model.addAttribute("isAuthenticated", isAuthenticated);

        if (!isAuthenticated) {
            model.addAttribute("canUserCreate", false);
            model.addAttribute("canUserRead", false);
            model.addAttribute("canUserUpdate", false);
            model.addAttribute("canUserDelete", false);
            model.addAttribute("canManageUsers", false);
            model.addAttribute("canManageBlocks", false);
            model.addAttribute("canAndarCreate", false);
            model.addAttribute("canAndarRead", false);
            model.addAttribute("canAndarUpdate", false);
            model.addAttribute("canAndarDelete", false);
            model.addAttribute("canUnidadeCreate", false);
            model.addAttribute("canUnidadeRead", false);
            model.addAttribute("canUnidadeUpdate", false);
            model.addAttribute("canUnidadeDelete", false);
            model.addAttribute("canMoradiaRead", false);
            model.addAttribute("canMoradiaCreate", false);
            model.addAttribute("canMoradiaUpdate", false);
            model.addAttribute("canChamadoRead", false);
            model.addAttribute("canChamadoCreate", false);
            model.addAttribute("canChamadoUpdate", false);
            model.addAttribute("canChamadoTipoRead", false);
            model.addAttribute("canChamadoTipoCreate", false);
            model.addAttribute("canChamadoTipoUpdate", false);
            model.addAttribute("canChamadoTipoDelete", false);
            model.addAttribute("canChamadoStatusRead", false);
            model.addAttribute("canChamadoStatusCreate", false);
            model.addAttribute("canChamadoStatusUpdate", false);
            model.addAttribute("canChamadoStatusDelete", false);
            model.addAttribute("canLotacaoRead", false);
            model.addAttribute("canLotacaoCreate", false);
            model.addAttribute("canLotacaoUpdate", false);
            model.addAttribute("canLotacaoDelete", false);
            model.addAttribute("canManageChamados", false);
            model.addAttribute("isMoradorRole", false);
            model.addAttribute("isAdminRole", false);
            return;
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        boolean isAdminRole = authorities.contains("ROLE_ADMIN");
        boolean isColaboradorRole = authorities.contains("ROLE_COLABORADOR");
        boolean isMoradorRole = authorities.contains("ROLE_MORADOR");

        boolean canUserCreate = authorities.contains(PermissionConstants.USER_CREATE);
        boolean canUserRead = authorities.contains(PermissionConstants.USER_READ);
        boolean canUserUpdate = authorities.contains(PermissionConstants.USER_UPDATE);
        boolean canUserDelete = authorities.contains(PermissionConstants.USER_DELETE);
        boolean canManageBlocks = authorities.contains(PermissionConstants.BLOCO_READ)
                || authorities.contains(PermissionConstants.BLOCO_UPDATE)
                || authorities.contains(PermissionConstants.BLOCO_DELETE)
                || authorities.contains(PermissionConstants.BLOCO_CREATE);
        boolean canAndarCreate = authorities.contains(PermissionConstants.ANDAR_CREATE);
        boolean canAndarRead = authorities.contains(PermissionConstants.ANDAR_READ);
        boolean canAndarUpdate = authorities.contains(PermissionConstants.ANDAR_UPDATE);
        boolean canAndarDelete = authorities.contains(PermissionConstants.ANDAR_DELETE);
        boolean canUnidadeCreate = authorities.contains(PermissionConstants.UNIDADE_CREATE);
        boolean canUnidadeRead = authorities.contains(PermissionConstants.UNIDADE_READ);
        boolean canUnidadeUpdate = authorities.contains(PermissionConstants.UNIDADE_UPDATE);
        boolean canUnidadeDelete = authorities.contains(PermissionConstants.UNIDADE_DELETE);
        boolean canMoradiaRead = authorities.contains(PermissionConstants.MORADIA_READ);
        boolean canMoradiaCreate = authorities.contains(PermissionConstants.MORADIA_CREATE);
        boolean canMoradiaUpdate = authorities.contains(PermissionConstants.MORADIA_UPDATE);
        boolean canChamadoRead = authorities.contains(PermissionConstants.CHAMADO_READ) || isAdminRole || isColaboradorRole;
        boolean canChamadoCreate = authorities.contains(PermissionConstants.CHAMADO_CREATE) || isAdminRole;
        boolean canChamadoUpdate = authorities.contains(PermissionConstants.CHAMADO_UPDATE) || isAdminRole || isColaboradorRole;
        boolean canChamadoTipoRead = authorities.contains(PermissionConstants.CHAMADO_TIPO_READ) || isAdminRole;
        boolean canChamadoTipoCreate = authorities.contains(PermissionConstants.CHAMADO_TIPO_CREATE) || isAdminRole;
        boolean canChamadoTipoUpdate = authorities.contains(PermissionConstants.CHAMADO_TIPO_UPDATE) || isAdminRole;
        boolean canChamadoTipoDelete = authorities.contains(PermissionConstants.CHAMADO_TIPO_DELETE) || isAdminRole;
        boolean canChamadoStatusRead = authorities.contains(PermissionConstants.CHAMADO_STATUS_READ) || isAdminRole;
        boolean canChamadoStatusCreate = authorities.contains(PermissionConstants.CHAMADO_STATUS_CREATE) || isAdminRole;
        boolean canChamadoStatusUpdate = authorities.contains(PermissionConstants.CHAMADO_STATUS_UPDATE) || isAdminRole;
        boolean canChamadoStatusDelete = authorities.contains(PermissionConstants.CHAMADO_STATUS_DELETE) || isAdminRole;
        boolean canLotacaoRead = authorities.contains(PermissionConstants.LOTACAO_READ) || isAdminRole;
        boolean canLotacaoCreate = authorities.contains(PermissionConstants.LOTACAO_CREATE) || isAdminRole;
        boolean canLotacaoUpdate = authorities.contains(PermissionConstants.LOTACAO_UPDATE) || isAdminRole;
        boolean canLotacaoDelete = authorities.contains(PermissionConstants.LOTACAO_DELETE) || isAdminRole;

        model.addAttribute("loggedUsername", authentication.getName());
        model.addAttribute("canUserCreate", canUserCreate);
        model.addAttribute("canUserRead", canUserRead);
        model.addAttribute("canUserUpdate", canUserUpdate);
        model.addAttribute("canUserDelete", canUserDelete);
        model.addAttribute("canManageUsers", canUserCreate || canUserRead || canUserUpdate || canUserDelete);
        model.addAttribute("canManageBlocks", canManageBlocks);
        model.addAttribute("canAndarCreate", canAndarCreate);
        model.addAttribute("canAndarRead", canAndarRead);
        model.addAttribute("canAndarUpdate", canAndarUpdate);
        model.addAttribute("canAndarDelete", canAndarDelete);
        model.addAttribute("canUnidadeCreate", canUnidadeCreate);
        model.addAttribute("canUnidadeRead", canUnidadeRead);
        model.addAttribute("canUnidadeUpdate", canUnidadeUpdate);
        model.addAttribute("canUnidadeDelete", canUnidadeDelete);
        model.addAttribute("canMoradiaRead", canMoradiaRead);
        model.addAttribute("canMoradiaCreate", canMoradiaCreate);
        model.addAttribute("canMoradiaUpdate", canMoradiaUpdate);
        model.addAttribute("canChamadoRead", canChamadoRead);
        model.addAttribute("canChamadoCreate", canChamadoCreate);
        model.addAttribute("canChamadoUpdate", canChamadoUpdate);
        model.addAttribute("canChamadoTipoRead", canChamadoTipoRead);
        model.addAttribute("canChamadoTipoCreate", canChamadoTipoCreate);
        model.addAttribute("canChamadoTipoUpdate", canChamadoTipoUpdate);
        model.addAttribute("canChamadoTipoDelete", canChamadoTipoDelete);
        model.addAttribute("canChamadoStatusRead", canChamadoStatusRead);
        model.addAttribute("canChamadoStatusCreate", canChamadoStatusCreate);
        model.addAttribute("canChamadoStatusUpdate", canChamadoStatusUpdate);
        model.addAttribute("canChamadoStatusDelete", canChamadoStatusDelete);
        model.addAttribute("canLotacaoRead", canLotacaoRead);
        model.addAttribute("canLotacaoCreate", canLotacaoCreate);
        model.addAttribute("canLotacaoUpdate", canLotacaoUpdate);
        model.addAttribute("canLotacaoDelete", canLotacaoDelete);
        model.addAttribute(
                "canManageChamados",
                canChamadoRead || canChamadoCreate || canChamadoUpdate
                        || canChamadoTipoRead || canChamadoTipoCreate || canChamadoTipoUpdate || canChamadoTipoDelete
                        || canChamadoStatusRead || canChamadoStatusCreate || canChamadoStatusUpdate || canChamadoStatusDelete
                        || canLotacaoRead || canLotacaoCreate || canLotacaoUpdate || canLotacaoDelete
        );
        model.addAttribute("isMoradorRole", isMoradorRole);
        model.addAttribute("isAdminRole", isAdminRole);

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            UUID userId = userPrincipal.getUser().getId();
            if (userId != null) {
                model.addAttribute("loggedUserId", userId.toString());
            }
            String displayName = userPrincipal.getUser().getPerfil() != null
                    && userPrincipal.getUser().getPerfil().getNomeCompleto() != null
                    && !userPrincipal.getUser().getPerfil().getNomeCompleto().isBlank()
                    ? userPrincipal.getUser().getPerfil().getNomeCompleto()
                    : authentication.getName();
            model.addAttribute("loggedDisplayName", displayName);
        }
    }
}

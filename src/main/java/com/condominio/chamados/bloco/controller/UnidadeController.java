package com.condominio.chamados.bloco.controller;

import com.condominio.chamados.bloco.dto.request.UnidadeRequest;
import com.condominio.chamados.bloco.dto.request.UnidadeUpdateRequest;
import com.condominio.chamados.bloco.service.UnidadeService;
import com.condominio.chamados.security.permission.PermissionConstants;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @GetMapping("/andares/{andarId}/unidades/novo")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.UNIDADE_CREATE + "')")
    public String showCreateForm(@PathVariable String andarId, Model model) {
        UnidadeRequest request = new UnidadeRequest();
        request.setAndarId(java.util.UUID.fromString(andarId));
        model.addAttribute("unidade", request);
        model.addAttribute("pageTitle", "Nova Unidade");
        model.addAttribute("activePage", "unidades-novo");
        return "unidade/novo";
    }

    @PostMapping("/andares/{andarId}/unidades")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.UNIDADE_CREATE + "')")
    public String create(
            @PathVariable String andarId,
            @Valid @ModelAttribute UnidadeRequest unidadeRequest,
            HttpServletRequest request
    ) {
        unidadeRequest.setAndarId(java.util.UUID.fromString(andarId));
        unidadeRequest.setAtivo(isAtivoSelecionado(request));
        unidadeService.criar(unidadeRequest);
        return "redirect:/andares/" + andarId + "?unidadeCreated";
    }

    @GetMapping("/unidades/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.UNIDADE_READ + "')")
    public String view(@PathVariable String id, Model model) {
        model.addAttribute("unidade", unidadeService.obterPorId(id));
        model.addAttribute("pageTitle", "Detalhes da Unidade");
        model.addAttribute("activePage", "unidades-detalhes");
        return "unidade/detalhes";
    }

    @GetMapping("/unidades/{id}/editar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.UNIDADE_UPDATE + "')")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("unidade", unidadeService.obterPorId(id));
        model.addAttribute("pageTitle", "Editar Unidade");
        model.addAttribute("activePage", "unidades-editar");
        return "unidade/editar";
    }

    @PostMapping("/unidades/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.UNIDADE_UPDATE + "')")
    public String update(
            @PathVariable String id,
            @Valid @ModelAttribute UnidadeUpdateRequest unidadeUpdateRequest,
            HttpServletRequest request
    ) {
        unidadeUpdateRequest.setAtivo(isAtivoSelecionado(request));
        unidadeService.atualizar(id, unidadeUpdateRequest);
        return "redirect:/unidades/" + id + "/editar?updated";
    }

    @PostMapping("/unidades/{id}/deletar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.UNIDADE_DELETE + "')")
    public String delete(@PathVariable String id) {
        unidadeService.deletar(id);
        return "redirect:/blocos?unidadeDeleted";
    }

    private boolean isAtivoSelecionado(HttpServletRequest request) {
        String[] valores = request.getParameterValues("ativo");
        if (valores == null) {
            return false;
        }

        for (String valor : valores) {
            if ("true".equalsIgnoreCase(valor)) {
                return true;
            }
        }
        return false;
    }
}

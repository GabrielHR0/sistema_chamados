package com.condominio.chamados.bloco.controller;

import com.condominio.chamados.bloco.dto.request.AndarRequest;
import com.condominio.chamados.bloco.dto.request.AndarUpdateRequest;
import com.condominio.chamados.bloco.service.AndarService;
import com.condominio.chamados.security.permission.PermissionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
public class AndarController {

    private final AndarService andarService;

    public AndarController(AndarService andarService) {
        this.andarService = andarService;
    }

    @GetMapping("/blocos/{blocoId}/andares/novo")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.ANDAR_CREATE + "')")
    public String showCreateForm(@PathVariable String blocoId, Model model) {
        AndarRequest request = new AndarRequest();
        request.setBlocoId(java.util.UUID.fromString(blocoId));
        model.addAttribute("andar", request);
        model.addAttribute("pageTitle", "Novo Andar");
        model.addAttribute("activePage", "andares-novo");
        return "andar/novo";
    }

    @PostMapping("/blocos/{blocoId}/andares")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.ANDAR_CREATE + "')")
    public String create(@PathVariable String blocoId, @Valid @ModelAttribute AndarRequest andarRequest, HttpServletRequest request) {
        andarRequest.setBlocoId(java.util.UUID.fromString(blocoId));
        andarRequest.setAtivo(isAtivoSelecionado(request));
        andarService.criar(andarRequest);
        return "redirect:/blocos/" + blocoId + "?andarCreated";
    }

    @GetMapping("/andares/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.ANDAR_READ + "')")
    public String view(@PathVariable String id, Model model) {
        model.addAttribute("andar", andarService.obterPorId(id));
        model.addAttribute("pageTitle", "Detalhes do Andar");
        model.addAttribute("activePage", "andares-detalhes");
        return "andar/detalhes";
    }

    @GetMapping("/andares/{id}/editar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.ANDAR_UPDATE + "')")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("andar", andarService.obterPorId(id));
        model.addAttribute("pageTitle", "Editar Andar");
        model.addAttribute("activePage", "andares-editar");
        return "andar/editar";
    }

    @PostMapping("/andares/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.ANDAR_UPDATE + "')")
    public String update(@PathVariable String id, @Valid @ModelAttribute AndarUpdateRequest andarUpdateRequest, HttpServletRequest request) {
        andarUpdateRequest.setAtivo(isAtivoSelecionado(request));
        andarService.atualizar(id, andarUpdateRequest);
        return "redirect:/andares/" + id + "/editar?updated";
    }

    @PostMapping("/andares/{id}/deletar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.ANDAR_DELETE + "')")
    public String delete(@PathVariable String id) {
        andarService.deletar(id);
        return "redirect:/blocos?andarDeleted";
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

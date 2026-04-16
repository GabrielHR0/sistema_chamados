package com.condominio.chamados.bloco.controller;

import com.condominio.chamados.bloco.dto.request.BlocoRequest;
import com.condominio.chamados.bloco.dto.request.BlocoUpdateRequest;
import com.condominio.chamados.bloco.service.AndarService;
import com.condominio.chamados.bloco.service.BlocoService;
import com.condominio.chamados.security.permission.PermissionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blocos")
public class BlocoController {

    private final BlocoService blocoService;
    private final AndarService andarService;

    public BlocoController(BlocoService blocoService, AndarService andarService) {
        this.blocoService = blocoService;
        this.andarService = andarService;
    }

    @GetMapping("/novo")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.BLOCO_CREATE + "')")
    public String showCreateForm(Model model) {
        model.addAttribute("bloco", new BlocoRequest());
        model.addAttribute("pageTitle", "Novo Bloco");
        model.addAttribute("activePage", "blocos-novo");
        return "bloco/novo";
    }

    @GetMapping
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.BLOCO_READ + "')")
    public String list(Model model) {
        model.addAttribute("blocos", blocoService.listar());
        model.addAttribute("pageTitle", "Blocos");
        model.addAttribute("activePage", "blocos-lista");
        return "bloco/lista";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.BLOCO_READ + "')")
    public String view(@org.springframework.web.bind.annotation.PathVariable String id, Model model) {
        model.addAttribute("bloco", blocoService.obterPorId(id));
        model.addAttribute("andares", andarService.listarPorBloco(id));
        model.addAttribute("pageTitle", "Detalhes do Bloco");
        model.addAttribute("activePage", "blocos-detalhes");
        return "bloco/detalhes";
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.BLOCO_UPDATE + "')")
    public String showEditForm(@org.springframework.web.bind.annotation.PathVariable String id, Model model) {
        model.addAttribute("bloco", blocoService.obterPorId(id));
        model.addAttribute("pageTitle", "Editar Bloco");
        model.addAttribute("activePage", "blocos-editar");
        return "bloco/editar";
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.BLOCO_CREATE + "')")
    public String create(@Valid @ModelAttribute BlocoRequest blocoRequest, HttpServletRequest request) {
        blocoRequest.setAtivo(isAtivoSelecionado(request));
        blocoService.criar(blocoRequest);
        return "redirect:/blocos/novo?created";
    }

    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.BLOCO_UPDATE + "')")
    public String update(@org.springframework.web.bind.annotation.PathVariable String id,
                         @Valid @ModelAttribute BlocoUpdateRequest blocoUpdateRequest,
                         HttpServletRequest request) {
        blocoUpdateRequest.setAtivo(isAtivoSelecionado(request));
        blocoService.atualizar(id, blocoUpdateRequest);
        return "redirect:/blocos/" + id + "/editar?updated";
    }

    @PostMapping("/{id}/deletar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.BLOCO_DELETE + "')")
    public String delete(@org.springframework.web.bind.annotation.PathVariable String id) {
        blocoService.deletar(id);
        return "redirect:/blocos?deleted";
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

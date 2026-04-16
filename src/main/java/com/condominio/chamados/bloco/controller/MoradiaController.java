package com.condominio.chamados.bloco.controller;

import com.condominio.chamados.bloco.dto.request.MoradiaCreateRequest;
import com.condominio.chamados.bloco.dto.response.UnidadeSearchResponse;
import com.condominio.chamados.bloco.service.MoradiaService;
import com.condominio.chamados.bloco.service.UnidadeService;
import com.condominio.chamados.security.dto.response.UserSearchResponse;
import com.condominio.chamados.security.permission.PermissionConstants;
import com.condominio.chamados.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping
public class MoradiaController {

    private final MoradiaService moradiaService;
    private final UnidadeService unidadeService;
    private final UserService userService;

    public MoradiaController(MoradiaService moradiaService, UnidadeService unidadeService, UserService userService) {
        this.moradiaService = moradiaService;
        this.unidadeService = unidadeService;
        this.userService = userService;
    }

    @GetMapping("/moradias")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.MORADIA_READ + "')")
    public String showManagementPage(
            @RequestParam(name = "unidadeId", required = false) String unidadeId,
            Model model
    ) {
        MoradiaCreateRequest request = new MoradiaCreateRequest();
        request.setUnidadeId(unidadeId);
        prepareManagementModel(model, request);
        return "moradia/gestao";
    }

    @GetMapping({"/unidades/{unidadeId}/moradias", "/unidades/{unidadeId}/moradias/novo"})
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.MORADIA_CREATE + "')")
    public String showCreateForm(@PathVariable String unidadeId, Model model) {
        prepareFormModel(model, unidadeId, new MoradiaCreateRequest());
        return "moradia/novo";
    }

    @PostMapping("/unidades/{unidadeId}/moradias")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.MORADIA_CREATE + "')")
    public String create(
            @PathVariable String unidadeId,
            @Valid @ModelAttribute("moradia") MoradiaCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (request.getUnidadeId() == null || request.getUnidadeId().isBlank()) {
            request.setUnidadeId(unidadeId);
        }
        if (bindingResult.hasErrors()) {
            prepareFormModel(model, unidadeId, request);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "moradia/novo";
        }

        try {
            moradiaService.criar(unidadeId, request);
            return "redirect:/unidades/" + unidadeId + "?moradiaCreated";
        } catch (IllegalArgumentException ex) {
            prepareFormModel(model, unidadeId, request);
            model.addAttribute("errorMessage", ex.getMessage());
            return "moradia/novo";
        }
    }

    @PostMapping("/moradias")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.MORADIA_CREATE + "')")
    public String createFromManagement(
            @Valid @ModelAttribute("moradia") MoradiaCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareManagementModel(model, request);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "moradia/gestao";
        }

        try {
            moradiaService.criar(request);
            return "redirect:/moradias?created";
        } catch (IllegalArgumentException ex) {
            prepareManagementModel(model, request);
            model.addAttribute("errorMessage", ex.getMessage());
            return "moradia/gestao";
        }
    }

    @RequestMapping(path = "/unidades/{unidadeId}/moradias/{moradiaId}/finalizar", method = {RequestMethod.PATCH, RequestMethod.POST})
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.MORADIA_UPDATE + "')")
    public String finalizar(
            @PathVariable String unidadeId,
            @PathVariable String moradiaId,
            Model model
    ) {
        try {
            moradiaService.finalizar(unidadeId, moradiaId);
            return "redirect:/unidades/" + unidadeId + "?moradiaFinalizada";
        } catch (IllegalArgumentException ex) {
            prepareFormModel(model, unidadeId, new MoradiaCreateRequest());
            model.addAttribute("errorMessage", ex.getMessage());
            return "unidade/detalhes";
        }
    }

    @GetMapping("/moradias/usuarios/busca")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.MORADIA_CREATE + "')")
    @ResponseBody
    public List<UserSearchResponse> searchUsuarios(@RequestParam(name = "q", required = false) String termo) {
        return userService.searchEnabledUsersByName(termo);
    }

    @GetMapping("/moradias/unidades/busca")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.MORADIA_CREATE + "')")
    @ResponseBody
    public List<UnidadeSearchResponse> searchUnidades(@RequestParam(name = "q", required = false) String termo) {
        return moradiaService.searchUnidadesByIdentificacao(termo);
    }

    private void prepareFormModel(Model model, String unidadeId, MoradiaCreateRequest moradia) {
        model.addAttribute("unidade", unidadeService.obterPorId(unidadeId));
        moradia.setUnidadeId(unidadeId);
        model.addAttribute("moradia", moradia);
        model.addAttribute("pageTitle", "Nova Moradia");
        model.addAttribute("activePage", "unidades-detalhes");
    }

    private void prepareManagementModel(Model model, MoradiaCreateRequest moradia) {
        model.addAttribute("moradia", moradia);
        model.addAttribute("moradias", moradiaService.listarTodas());
        if (moradia.getUnidadeId() != null && !moradia.getUnidadeId().isBlank()) {
            model.addAttribute("selectedUnidadeIdentificacao", unidadeService.obterPorId(moradia.getUnidadeId()).getIdentificacao());
        }
        model.addAttribute("pageTitle", "Moradias");
        model.addAttribute("activePage", "moradias");
    }
}

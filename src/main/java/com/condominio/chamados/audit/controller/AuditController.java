package com.condominio.chamados.audit.controller;

import com.condominio.chamados.audit.service.AuditService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auditorias")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public String listar(Model model) {
        model.addAttribute("auditorias", auditService.listarRecentes(500));
        model.addAttribute("pageTitle", "Auditorias");
        model.addAttribute("activePage", "auditorias-lista");
        return "auditoria/lista";
    }
}

package com.condominio.chamados.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {

        boolean hasError = error != null;
        boolean hasLogout = logout != null;

        model.addAttribute("hasError", hasError);
        model.addAttribute("hasLogout", hasLogout);

        if (hasError) {
            model.addAttribute("error", "Usuário ou senha inválidos");
        }

        if (hasLogout) {
            model.addAttribute("logout", "Você foi desconectado com sucesso");
        }

        return "login";
    }

}

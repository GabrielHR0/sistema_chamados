package com.condominio.chamados.web;

import com.condominio.chamados.web.model.FeatureCard;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

	@GetMapping({"/", "/home"})
	public String home(Model model) {
		model.addAttribute("appName", "Chamados");
		model.addAttribute("pageTitle", "Setup JSP e Docker");
		model.addAttribute("environment", "dev");
		model.addAttribute("featureCards", List.of(
				new FeatureCard("JSP", "Views server-side prontas para evoluir sem framework front separado.", "View"),
				new FeatureCard("JSTL", "Tags padrão para loops, condicionais e formatação no HTML.", "Taglib"),
				new FeatureCard("Bootstrap 5", "Componentes prontos via WebJars para agilizar telas e layout.", "UI"),
				new FeatureCard("Docker Compose", "Subida local com aplicação e PostgreSQL no mesmo fluxo.", "Infra")
		));
		return "home";
	}
}


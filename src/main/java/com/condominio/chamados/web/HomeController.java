package com.condominio.chamados.web;

import com.condominio.chamados.chamado.dto.response.ChamadoResumoResponse;
import com.condominio.chamados.chamado.service.ChamadoService;
import com.condominio.chamados.security.service.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

	private final ChamadoService chamadoService;

	public HomeController(ChamadoService chamadoService) {
		this.chamadoService = chamadoService;
	}

	@GetMapping({"/", "/home"})
	@PreAuthorize("isAuthenticated()")
	public String home(Model model, Authentication authentication) {
		model.addAttribute("appName", "Chamados");
		model.addAttribute("pageTitle", "Dashboard");
		model.addAttribute("activePage", "home");

		String userId = extractUserId(authentication);
		Set<String> authorities = extractAuthorities(authentication);
		boolean isAdmin = authorities.contains("ROLE_ADMIN");
		boolean isColaborador = authorities.contains("ROLE_COLABORADOR");
		boolean isMorador = authorities.contains("ROLE_MORADOR");

		if (isAdmin) {
			List<ChamadoResumoResponse> chamados = chamadoService.listarChamados(userId, true, null, null, null, null);
			List<ChamadoResumoResponse> disponiveis = chamadoService.listarChamadosDisponiveisParaLotacao(userId, true, null, null);
			model.addAttribute("dashboardRole", "ADMIN");
			model.addAttribute("kpiTotalChamados", chamados.size());
			model.addAttribute("kpiAbertos", chamados.stream().filter(ch -> !ch.isStatusFinal()).count());
			model.addAttribute("kpiFinalizados", chamados.stream().filter(ChamadoResumoResponse::isStatusFinal).count());
			model.addAttribute("kpiDisponiveisFila", disponiveis.size());
			model.addAttribute("kpiColaboradoresAtivos", chamadoService.listarColaboradoresAtivos().size());
			List<ChamadoResumoResponse> recentChamados = chamados.stream().limit(8).toList();
			model.addAttribute("recentChamados", recentChamados);
			addStatusChartData(model, chamados);
		} else if (isColaborador) {
			List<ChamadoResumoResponse> chamadosEscopo = chamadoService.listarChamados(userId, false, null, null, null, null);
			List<ChamadoResumoResponse> disponiveis = chamadoService.listarChamadosDisponiveisParaLotacao(userId, false, null, null);
			List<ChamadoResumoResponse> meusNaoFinais = chamadoService.listarChamadosNaoFinaisDoResponsavel(userId);
			model.addAttribute("dashboardRole", "COLABORADOR");
			model.addAttribute("kpiEscopoTotal", chamadosEscopo.size());
			model.addAttribute("kpiDisponiveisFila", disponiveis.size());
			model.addAttribute("kpiMeusNaoFinais", meusNaoFinais.size());
			model.addAttribute("kpiTiposPermitidos", chamadoService.listarTipos(userId, false).size());
			List<ChamadoResumoResponse> recentChamados = meusNaoFinais.stream().limit(8).toList();
			model.addAttribute("recentChamados", recentChamados);
			addStatusChartData(model, chamadosEscopo);
		} else if (isMorador) {
			List<ChamadoResumoResponse> meusChamados = chamadoService.listarChamadosMorador(userId);
			model.addAttribute("dashboardRole", "MORADOR");
			model.addAttribute("kpiMeusChamados", meusChamados.size());
			model.addAttribute("kpiAbertos", meusChamados.stream().filter(ch -> !ch.isStatusFinal()).count());
			model.addAttribute("kpiFinalizados", meusChamados.stream().filter(ChamadoResumoResponse::isStatusFinal).count());
			model.addAttribute("kpiMinhasMoradias", chamadoService.listarMoradiasMorador(userId).size());
			List<ChamadoResumoResponse> recentChamados = meusChamados.stream().limit(8).toList();
			model.addAttribute("recentChamados", recentChamados);
			addStatusChartData(model, meusChamados);
		} else {
			model.addAttribute("dashboardRole", "GERAL");
			model.addAttribute("recentChamados", List.of());
			model.addAttribute("statusChartLabels", List.of());
			model.addAttribute("statusChartValues", List.of());
		}
		return "home";
	}

	private void addStatusChartData(Model model, List<ChamadoResumoResponse> chamados) {
		Map<String, Long> grouped = chamados.stream()
				.collect(Collectors.groupingBy(
						ch -> ch.getStatusNome() == null || ch.getStatusNome().isBlank() ? "Sem status" : ch.getStatusNome(),
						LinkedHashMap::new,
						Collectors.counting()
				));
		model.addAttribute("statusChartLabels", grouped.keySet().stream().toList());
		model.addAttribute("statusChartValues", grouped.values().stream().toList());
	}

	private Set<String> extractAuthorities(Authentication authentication) {
		if (authentication == null || authentication.getAuthorities() == null) {
			return Set.of();
		}
		return authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}

	private String extractUserId(Authentication authentication) {
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserPrincipal userPrincipal && userPrincipal.getUser().getId() != null) {
			return userPrincipal.getUser().getId().toString();
		}
		return null;
	}
}


package com.condominio.chamados.web;

import com.condominio.chamados.chamado.service.ChamadoService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.mock;

class HomeControllerTest {

	private final ChamadoService chamadoService = mock(ChamadoService.class);
	private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(chamadoService)).build();

	@Test
	void deveExibirHomeComDadosBasicos() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("home"))
				.andExpect(model().attributeExists("appName"))
				.andExpect(model().attributeExists("dashboardRole"))
				.andExpect(model().attributeExists("recentChamados"));
	}
}


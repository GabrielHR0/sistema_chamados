package com.condominio.chamados.shared.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class GlobalExceptionHandlerTest {

	private final MockMvc mockMvc = MockMvcBuilders
			.standaloneSetup(new ErroForcadoController())
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();

	@Test
	void deveRetornarProblemDetailsParaIllegalArgumentException() throws Exception {
		mockMvc.perform(get("/erros/illegal-argument"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.title").value("Bad Request"))
				.andExpect(jsonPath("$.detail").value("Dados invalidos"))
				.andExpect(jsonPath("$.instance").value("/erros/illegal-argument"))
				.andExpect(jsonPath("$.type").value("about:blank"));
	}

	@Test
	void deveRenderizarViewJspParaRequisicaoHtml() throws Exception {
		var result = mockMvc.perform(get("/erros/illegal-argument").accept(MediaType.TEXT_HTML))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("error/error"))
				.andExpect(model().attributeExists("error"))
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assertThat(modelAndView).isNotNull();
		assertThat(modelAndView.getModel().get("error")).isInstanceOf(WebErrorModel.class);
		WebErrorModel error = (WebErrorModel) modelAndView.getModel().get("error");
		assertThat(error.getStatus()).isEqualTo(400);
		assertThat(error.getError()).isEqualTo("Bad Request");
		assertThat(error.getMessage()).isEqualTo("Dados invalidos");
		assertThat(error.getPath()).isEqualTo("/erros/illegal-argument");
	}

	@RestController
	@RequestMapping("/erros")
	static class ErroForcadoController {

		@GetMapping("/illegal-argument")
		String dispararErro() {
			throw new IllegalArgumentException("Dados invalidos");
		}
	}
}




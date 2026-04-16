package com.condominio.chamados.bloco.controller;

import com.condominio.chamados.bloco.domain.UnidadeStatus;
import com.condominio.chamados.bloco.dto.request.MoradiaCreateRequest;
import com.condominio.chamados.bloco.dto.response.MoradiaResponse;
import com.condominio.chamados.bloco.dto.response.UnidadeResponse;
import com.condominio.chamados.bloco.service.MoradiaService;
import com.condominio.chamados.bloco.service.UnidadeService;
import com.condominio.chamados.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class MoradiaControllerTest {

    private final MoradiaService moradiaService = mock(MoradiaService.class);
    private final UnidadeService unidadeService = mock(UnidadeService.class);
    private final UserService userService = mock(UserService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new MoradiaController(moradiaService, unidadeService, userService)).build();

    @Test
    void deveExibirFormularioDeMoradiaAPartirDaUnidade() throws Exception {
        String unidadeId = "f8e83020-f31b-421e-8f73-b7f26beb84a3";
        UnidadeResponse unidade = new UnidadeResponse();
        unidade.setId(unidadeId);
        unidade.setIdentificacao("BLOCO-01-01");
        unidade.setStatus(UnidadeStatus.EM_FUNCIONAMENTO);
        unidade.setAtivo(true);

        when(unidadeService.obterPorId(unidadeId)).thenReturn(unidade);

        mockMvc.perform(get("/unidades/{unidadeId}/moradias", unidadeId))
                .andExpect(status().isOk())
                .andExpect(view().name("moradia/novo"))
                .andExpect(model().attributeExists("moradia", "unidade"));
    }

    @Test
    void deveManterNaTelaQuandoCriacaoFalhar() throws Exception {
        String unidadeId = "f8e83020-f31b-421e-8f73-b7f26beb84a3";
        UnidadeResponse unidade = new UnidadeResponse();
        unidade.setId(unidadeId);
        unidade.setIdentificacao("BLOCO-01-01");
        unidade.setStatus(UnidadeStatus.EM_FUNCIONAMENTO);
        unidade.setAtivo(true);

        when(unidadeService.obterPorId(unidadeId)).thenReturn(unidade);
        when(moradiaService.criar(org.mockito.ArgumentMatchers.eq(unidadeId), org.mockito.ArgumentMatchers.any(MoradiaCreateRequest.class)))
                .thenThrow(new IllegalArgumentException("O usuário precisa ter o cargo MORADOR"));

        mockMvc.perform(post("/unidades/{unidadeId}/moradias", unidadeId)
                        .param("unidadeId", unidadeId)
                        .param("usuarioId", "11111111-1111-1111-1111-111111111111"))
                .andExpect(status().isOk())
                .andExpect(view().name("moradia/novo"))
                .andExpect(model().attribute("errorMessage", "O usuário precisa ter o cargo MORADOR"));
    }

    @Test
    void deveFinalizarMoradiaComPatch() throws Exception {
        String unidadeId = "f8e83020-f31b-421e-8f73-b7f26beb84a3";
        String moradiaId = "11111111-1111-1111-1111-111111111111";

        MoradiaResponse response = new MoradiaResponse();
        response.setId(moradiaId);

        when(moradiaService.finalizar(unidadeId, moradiaId)).thenReturn(response);

        mockMvc.perform(post("/unidades/{unidadeId}/moradias/{moradiaId}/finalizar", unidadeId, moradiaId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/unidades/" + unidadeId + "?moradiaFinalizada"));
    }
}

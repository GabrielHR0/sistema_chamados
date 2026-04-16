package com.condominio.chamados.bloco.controller;

import com.condominio.chamados.bloco.dto.request.UnidadeRequest;
import com.condominio.chamados.bloco.dto.request.UnidadeUpdateRequest;
import com.condominio.chamados.bloco.dto.response.UnidadeResponse;
import com.condominio.chamados.bloco.service.UnidadeService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UnidadeControllerTest {

    private final UnidadeService unidadeService = mock(UnidadeService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new UnidadeController(unidadeService)).build();

    @Test
    void deveEnviarAtivoComoTrueNoCadastroQuandoCheckboxVemMarcado() throws Exception {
        mockMvc.perform(post("/andares/{andarId}/unidades", UUID.randomUUID())
                        .param("apartamentoNumero", "101")
                        .param("ativo", "false")
                        .param("ativo", "true")
                        .param("status", "EM_FUNCIONAMENTO"))
                .andExpect(status().is3xxRedirection())
                ;

        var captor = org.mockito.ArgumentCaptor.forClass(UnidadeRequest.class);
        verify(unidadeService).criar(captor.capture());
        assertThat(captor.getValue().isAtivo()).isTrue();
    }

    @Test
    void deveEnviarAtivoComoFalseNoCadastroQuandoCheckboxNaoVemMarcado() throws Exception {
        mockMvc.perform(post("/andares/{andarId}/unidades", UUID.randomUUID())
                        .param("apartamentoNumero", "101")
                        .param("ativo", "false")
                        .param("status", "EM_FUNCIONAMENTO"))
                .andExpect(status().is3xxRedirection());

        var captor = org.mockito.ArgumentCaptor.forClass(UnidadeRequest.class);
        verify(unidadeService).criar(captor.capture());
        assertThat(captor.getValue().isAtivo()).isFalse();
    }

    @Test
    void deveEnviarAtivoComoTrueNaEdicaoQuandoCheckboxVemMarcado() throws Exception {
        mockMvc.perform(post("/unidades/{id}", UUID.randomUUID())
                        .param("apartamentoNumero", "101")
                        .param("ativo", "false")
                        .param("ativo", "true")
                        .param("status", "EM_FUNCIONAMENTO"))
                .andExpect(status().is3xxRedirection())
                ;

        var captor = org.mockito.ArgumentCaptor.forClass(UnidadeUpdateRequest.class);
        verify(unidadeService).atualizar(any(), captor.capture());
        assertThat(captor.getValue().isAtivo()).isTrue();
    }
}

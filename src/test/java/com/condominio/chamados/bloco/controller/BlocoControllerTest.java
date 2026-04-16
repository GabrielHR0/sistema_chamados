package com.condominio.chamados.bloco.controller;

import com.condominio.chamados.bloco.dto.request.BlocoRequest;
import com.condominio.chamados.bloco.dto.request.BlocoUpdateRequest;
import com.condominio.chamados.bloco.service.AndarService;
import com.condominio.chamados.bloco.service.BlocoService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BlocoControllerTest {

    private final BlocoService blocoService = mock(BlocoService.class);
    private final AndarService andarService = mock(AndarService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new BlocoController(blocoService, andarService)).build();

    @Test
    void deveEnviarAtivoComoTrueNoCadastroQuandoCheckboxVemMarcado() throws Exception {
        mockMvc.perform(post("/blocos")
                        .param("identificacao", "BLOCO-A")
                        .param("quantidadeAndares", "2")
                        .param("apartamentosPorAndar", "2")
                        .param("ativo", "false")
                        .param("ativo", "true"))
                .andExpect(status().is3xxRedirection());

        var captor = org.mockito.ArgumentCaptor.forClass(BlocoRequest.class);
        verify(blocoService).criar(captor.capture());
        assertThat(captor.getValue().isAtivo()).isTrue();
    }

    @Test
    void deveEnviarAtivoComoTrueNaEdicaoQuandoCheckboxVemMarcado() throws Exception {
        mockMvc.perform(post("/blocos/{id}", "123e4567-e89b-12d3-a456-426614174000")
                        .param("ativo", "false")
                        .param("ativo", "true"))
                .andExpect(status().is3xxRedirection());

        var captor = org.mockito.ArgumentCaptor.forClass(BlocoUpdateRequest.class);
        verify(blocoService).atualizar(org.mockito.ArgumentMatchers.anyString(), captor.capture());
        assertThat(captor.getValue().isAtivo()).isTrue();
    }
}

package com.condominio.chamados.audit.controller;

import com.condominio.chamados.audit.service.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AuditControllerAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void devePermitirAdminAcessarAuditorias() throws Exception {
        when(auditService.listarRecentes(500)).thenReturn(List.of());
        mockMvc.perform(get("/auditorias"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"COLABORADOR"})
    void deveBloquearNaoAdmin() throws Exception {
        mockMvc.perform(get("/auditorias"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRedirecionarAnonimoParaLogin() throws Exception {
        mockMvc.perform(get("/auditorias"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}

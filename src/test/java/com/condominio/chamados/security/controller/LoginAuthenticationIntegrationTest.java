package com.condominio.chamados.security.controller;

import com.condominio.chamados.AbstractTestcontainersTest;
import com.condominio.chamados.security.domain.Perfil;
import com.condominio.chamados.security.domain.Role;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.repository.RoleRepository;
import com.condominio.chamados.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class LoginAuthenticationIntegrationTest extends AbstractTestcontainersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Clear existing data
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Create a test user with MORADOR role
        Role morador = new Role();
        morador.setName("MORADOR");
        roleRepository.save(morador);

        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setEnabled(true);
        Perfil testProfile = new Perfil();
        testProfile.setNomeCompleto("Test User");
        testUser.setPerfil(testProfile);
        testUser.getRoles().add(morador);
        userRepository.save(testUser);

        User disabledUser = new User();
        disabledUser.setUsername("disableduser");
        disabledUser.setEmail("disableduser@example.com");
        disabledUser.setPassword(passwordEncoder.encode("password123"));
        disabledUser.setEnabled(false);
        Perfil disabledProfile = new Perfil();
        disabledProfile.setNomeCompleto("Disabled User");
        disabledUser.setPerfil(disabledProfile);
        disabledUser.getRoles().add(morador);
        userRepository.save(disabledUser);
    }

    @Test
    void testLoginPageLoadSuccessfully() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginWithValidCredentials() throws Exception {
        mockMvc.perform(post("/perform_login")
                .param("username", "testuser")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        mockMvc.perform(post("/perform_login")
                .param("username", "testuser")
                .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void testLoginWithNonexistentUser() throws Exception {
        mockMvc.perform(post("/perform_login")
                .param("username", "nonexistent")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void testLoginWithDisabledUser() throws Exception {
        mockMvc.perform(post("/perform_login")
                .param("username", "disableduser")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void testAccessProtectedResourceWithoutLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testAccessProtectedResourceAfterLogin() throws Exception {
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/perform_login")
                .param("username", "testuser")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession(false);

        assertNotNull(session);

        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void testLogoutAfterLogin() throws Exception {
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/perform_login")
                .param("username", "testuser")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession(false);

        assertNotNull(session);

        mockMvc.perform(post("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }
}

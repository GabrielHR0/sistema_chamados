package com.condominio.chamados.security.service;

import com.condominio.chamados.security.domain.Perfil;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.dto.response.UserSearchResponse;
import com.condominio.chamados.chamado.repository.LotacaoRepository;
import com.condominio.chamados.security.repository.RoleRepository;
import com.condominio.chamados.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceSearchTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LotacaoRepository lotacaoRepository;

    @Test
    void deveRetornarSugestoesFormatadasParaBuscaDeUsuarios() {
        UserService service = new UserService(userRepository, roleRepository, lotacaoRepository, passwordEncoder);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("jsilva");
        user.setPerfil(new Perfil());
        user.getPerfil().setNomeCompleto("Joao Silva");

        when(userRepository.searchEnabledByName("jo", Pageable.ofSize(10))).thenReturn(List.of(user));

        List<UserSearchResponse> result = service.searchEnabledUsersByName("jo");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getText()).isEqualTo("Joao Silva (jsilva)");
        verify(userRepository).searchEnabledByName("jo", Pageable.ofSize(10));
    }

    @Test
    void deveIgnorarBuscaVazia() {
        UserService service = new UserService(userRepository, roleRepository, lotacaoRepository, passwordEncoder);

        List<UserSearchResponse> result = service.searchEnabledUsersByName(" ");

        assertThat(result).isEmpty();
        verify(userRepository, never()).searchEnabledByName(any(), any(Pageable.class));
    }
}

package com.condominio.chamados.security.service;

import com.condominio.chamados.security.domain.Perfil;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.chamado.repository.LotacaoRepository;
import com.condominio.chamados.security.repository.RoleRepository;
import com.condominio.chamados.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceMoradiaTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LotacaoRepository lotacaoRepository;

    @Test
    void naoDevePreencherMoradiaNaListagemDeUsuarios() {
        UserService service = new UserService(userRepository, roleRepository, lotacaoRepository, passwordEncoder);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("maria");
        user.setPerfil(new Perfil());
        user.getPerfil().setNomeCompleto("Maria Souza");

        when(userRepository.findAll()).thenReturn(List.of(user));

        var result = service.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMoradiaUsuarioNome()).isNull();
    }
}

package com.condominio.chamados.security.service;

import com.condominio.chamados.chamado.repository.LotacaoRepository;
import com.condominio.chamados.security.domain.Role;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.dto.request.UserRequest;
import com.condominio.chamados.security.repository.RoleRepository;
import com.condominio.chamados.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceMoradorLotacaoRuleTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private LotacaoRepository lotacaoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, roleRepository, lotacaoRepository, passwordEncoder);
    }

    @Test
    void createUserDeveBloquearMoradorComLotacao() {
        UUID moradorRoleId = UUID.randomUUID();
        UserRequest request = new UserRequest();
        request.setUsername("morador1");
        request.setEmail("morador1@example.com");
        request.setPassword("123456");
        request.setRoleIds(Set.of(moradorRoleId.toString()));
        request.setLotacaoIds(Set.of(UUID.randomUUID().toString()));

        Role morador = new Role();
        morador.setId(moradorRoleId);
        morador.setName("MORADOR");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(moradorRoleId)).thenReturn(Optional.of(morador));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
    }

    @Test
    void updateUserDeveBloquearLotacaoQuandoUsuarioJaEhMorador() {
        UUID userId = UUID.randomUUID();
        User existing = new User();
        existing.setId(userId);
        Role morador = new Role();
        morador.setId(UUID.randomUUID());
        morador.setName("MORADOR");
        existing.setRoles(Set.of(morador));

        UserRequest request = new UserRequest();
        request.setEmail("morador-atualizado@example.com");
        request.setLotacaoIds(Set.of(UUID.randomUUID().toString()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId.toString(), request));
    }
}

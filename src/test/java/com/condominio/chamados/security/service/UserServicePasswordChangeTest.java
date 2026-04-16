package com.condominio.chamados.security.service;

import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.dto.request.AdminChangeUserPasswordRequest;
import com.condominio.chamados.security.dto.request.ChangeOwnPasswordRequest;
import com.condominio.chamados.chamado.repository.LotacaoRepository;
import com.condominio.chamados.security.repository.RoleRepository;
import com.condominio.chamados.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServicePasswordChangeTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LotacaoRepository lotacaoRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, roleRepository, lotacaoRepository, passwordEncoder);
    }

    @Test
    void changeOwnPasswordShouldUpdateWhenScopeAndCurrentPasswordAreValid() {
        UUID userId = UUID.randomUUID();
        User user = userWith(userId, "morador", "encoded-old-password");
        ChangeOwnPasswordRequest request = new ChangeOwnPasswordRequest();
        request.setCurrentPassword("old-password");
        request.setNewPassword("new-password");
        request.setConfirmNewPassword("new-password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old-password", "encoded-old-password")).thenReturn(true);
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");

        userService.changeOwnPassword(userId.toString(), userId, request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("encoded-new-password", captor.getValue().getPassword());
    }

    @Test
    void changeOwnPasswordShouldFailWhenScopeIsAnotherUser() {
        UUID authenticatedUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        User target = userWith(targetUserId, "alvo", "encoded-old-password");
        ChangeOwnPasswordRequest request = new ChangeOwnPasswordRequest();
        request.setCurrentPassword("old-password");
        request.setNewPassword("new-password");
        request.setConfirmNewPassword("new-password");

        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(target));

        assertThrows(
                AccessDeniedException.class,
                () -> userService.changeOwnPassword(targetUserId.toString(), authenticatedUserId, request)
        );
    }

    @Test
    void adminChangeUserPasswordShouldUpdateWhenAdminPasswordIsValid() {
        UUID adminId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        User admin = userWith(adminId, "admin", "encoded-admin-password");
        User target = userWith(targetUserId, "target", "encoded-target-password");

        AdminChangeUserPasswordRequest request = new AdminChangeUserPasswordRequest();
        request.setAdminPassword("admin-password");
        request.setNewPassword("new-password");
        request.setConfirmNewPassword("new-password");

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(target));
        when(passwordEncoder.matches("admin-password", "encoded-admin-password")).thenReturn(true);
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");

        userService.adminChangeUserPassword(targetUserId.toString(), adminId, request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(targetUserId, captor.getValue().getId());
        assertEquals("encoded-new-password", captor.getValue().getPassword());
    }

    @Test
    void adminChangeUserPasswordShouldFailWhenAdminPasswordIsInvalid() {
        UUID adminId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        User admin = userWith(adminId, "admin", "encoded-admin-password");

        AdminChangeUserPasswordRequest request = new AdminChangeUserPasswordRequest();
        request.setAdminPassword("wrong-admin-password");
        request.setNewPassword("new-password");
        request.setConfirmNewPassword("new-password");

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("wrong-admin-password", "encoded-admin-password")).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.adminChangeUserPassword(targetUserId.toString(), adminId, request)
        );
    }

    private User userWith(UUID id, String username, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(username + "@example.com");
        user.setEnabled(true);
        return user;
    }
}

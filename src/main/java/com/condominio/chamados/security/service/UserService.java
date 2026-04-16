package com.condominio.chamados.security.service;

import com.condominio.chamados.security.domain.Role;
import com.condominio.chamados.security.domain.Perfil;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.domain.UserFoto;
import com.condominio.chamados.chamado.domain.Lotacao;
import com.condominio.chamados.chamado.repository.LotacaoRepository;
import com.condominio.chamados.security.dto.request.AdminChangeUserPasswordRequest;
import com.condominio.chamados.security.dto.request.ChangeOwnPasswordRequest;
import com.condominio.chamados.security.dto.request.PerfilRequest;
import com.condominio.chamados.security.dto.request.UserProfileRequest;
import com.condominio.chamados.security.dto.request.UpdateUserRolesRequest;
import com.condominio.chamados.security.dto.request.UserRequest;
import com.condominio.chamados.security.dto.response.PerfilResponse;
import com.condominio.chamados.security.dto.response.UserSearchResponse;
import com.condominio.chamados.security.dto.response.UserResponse;
import com.condominio.chamados.chamado.dto.response.LotacaoResponse;
import com.condominio.chamados.security.repository.RoleRepository;
import com.condominio.chamados.security.repository.UserFotoRepository;
import com.condominio.chamados.security.repository.UserRepository;
import com.condominio.chamados.shared.upload.UploadStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.PageRequest;
import org.springframework.core.io.Resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LotacaoRepository lotacaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFotoRepository userFotoRepository;
    private final UploadStorageService uploadStorageService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            LotacaoRepository lotacaoRepository,
            PasswordEncoder passwordEncoder,
            UserFotoRepository userFotoRepository,
            UploadStorageService uploadStorageService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.lotacaoRepository = lotacaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.userFotoRepository = userFotoRepository;
        this.uploadStorageService = uploadStorageService;
    }

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            LotacaoRepository lotacaoRepository,
            PasswordEncoder passwordEncoder
    ) {
        this(userRepository, roleRepository, lotacaoRepository, passwordEncoder, null, null);
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        applyPerfil(user, request.getPerfil());

        Set<Role> roles = resolveRoles(request.getRoleIds());
        validateMoradorLotacaoRule(roles, request.getLotacaoIds());
        if (!roles.isEmpty()) {
            user.setRoles(roles);
        }
        user.setLotacoes(resolveLotacoes(request.getLotacaoIds()));

        User saved = userRepository.save(user);
        return mapUserToResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return mapUserToResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return mapUserToResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<LotacaoResponse> getAllLotacoes() {
        return lotacaoRepository.findAllByOrderByNomeAsc().stream()
                .map(lotacao -> {
                    LotacaoResponse response = new LotacaoResponse();
                    response.setId(lotacao.getId().toString());
                    response.setNome(lotacao.getNome());
                    response.setDescricao(lotacao.getDescricao());
                    return response;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserSearchResponse> searchEnabledUsersByName(String termo) {
        if (termo == null || termo.isBlank()) {
            return List.of();
        }

        String normalizedTerm = termo.trim();
        return userRepository.searchEnabledByName(normalizedTerm, PageRequest.of(0, 10)).stream()
                .map(this::mapUserToSearchResponse)
                .toList();
    }

    @Transactional
    public void updateUser(String id, UserRequest request) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        // Não permitir desativar admin
        boolean hasAdminRole = user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()));
        if (hasAdminRole && !request.isEnabled()) {
            throw new IllegalArgumentException("Não é permitido desativar usuários com cargo ADMIN");
        }

        user.setEmail(request.getEmail());
        user.setEnabled(request.isEnabled());
        applyPerfil(user, request.getPerfil());

        Set<Role> requestRoles = resolveRoles(request.getRoleIds());
        Set<Role> effectiveRoles = requestRoles.isEmpty() ? user.getRoles() : requestRoles;
        validateMoradorLotacaoRule(effectiveRoles, request.getLotacaoIds());
        if (!requestRoles.isEmpty()) {
            user.setRoles(requestRoles);
        }
        user.setLotacoes(resolveLotacoes(request.getLotacaoIds()));

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        
        // Não permitir deletar admin
        boolean hasAdminRole = user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()));
        if (hasAdminRole) {
            throw new IllegalArgumentException("Não é permitido deletar usuários com cargo ADMIN");
        }
        
        userRepository.delete(user);
    }

    @Transactional
    public void updateUserProfile(String userId, UserProfileRequest request) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Verificar se o email já está em uso por outro usuário
        userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(user.getId())) {
                throw new IllegalArgumentException("Email already in use: " + request.getEmail());
            }
        });

        user.setEmail(request.getEmail());
        applyPerfil(user, request.getPerfil());
        userRepository.save(user);
    }

    @Transactional
    public void updateOwnProfilePhoto(String userId, MultipartFile foto) {
        requireProfilePhotoSupport();
        if (foto == null || foto.isEmpty()) {
            throw new IllegalArgumentException("Selecione uma imagem para atualizar a foto do perfil.");
        }
        String contentType = foto.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo invalido. Envie apenas imagens.");
        }

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Perfil perfil = user.getPerfil();
        if (perfil == null || perfil.getId() == null) {
            throw new IllegalArgumentException("Perfil do usuario nao encontrado para atualizar foto.");
        }

        UploadStorageService.StoredUpload stored = uploadStorageService.store(foto);
        UserFoto userFoto = userFotoRepository.findByPerfil_User_Id(user.getId()).orElseGet(UserFoto::new);
        String oldStoredName = userFoto.getNomeArquivo();
        try {
            userFoto.setPerfil(perfil);
            userFoto.setNomeOriginal(stored.originalName());
            userFoto.setNomeArquivo(stored.storedName());
            userFoto.setContentType(stored.contentType());
            userFoto.setTamanhoBytes(stored.size());
            userFotoRepository.save(userFoto);
        } catch (RuntimeException ex) {
            uploadStorageService.delete(stored.storedName());
            throw ex;
        }

        if (oldStoredName != null && !oldStoredName.isBlank() && !oldStoredName.equals(stored.storedName())) {
            uploadStorageService.delete(oldStoredName);
        }
    }

    @Transactional(readOnly = true)
    public ProfilePhotoDownload getOwnProfilePhoto(String userId) {
        requireProfilePhotoSupport();
        UserFoto userFoto = userFotoRepository.findByPerfil_User_Id(UUID.fromString(userId)).orElse(null);
        if (userFoto == null) {
            return null;
        }

        try {
            Resource resource = uploadStorageService.loadAsResource(userFoto.getNomeArquivo());
            return new ProfilePhotoDownload(
                    resource,
                    userFoto.getNomeOriginal(),
                    userFoto.getContentType(),
                    userFoto.getTamanhoBytes()
            );
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Atualiza os roles de um usuário, substituindo completamente os relacionamentos atuais.
     *
     * @param id ID do usuário
     * @param request com Set de IDs de roles para atribuir
     * @return UserResponse atualizado
     */
    @Transactional
    public UserResponse updateUserRoles(String id, UpdateUserRolesRequest request) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        // Limpar roles atuais
        user.getRoles().clear();

        // Atribuir novos roles
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleId : request.getRoleIds()) {
                roleRepository.findById(UUID.fromString(roleId)).ifPresent(roles::add);
            }
            user.setRoles(roles);
        }

        User updated = userRepository.save(user);
        return mapUserToResponse(updated);
    }

    @Transactional
    public void changeOwnPassword(String targetUserId, UUID authenticatedUserId, ChangeOwnPasswordRequest request) {
        User targetUser = userRepository.findById(UUID.fromString(targetUserId))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + targetUserId));

        validateSelfScope(targetUser.getId(), authenticatedUserId);
        validatePasswordConfirmation(request.getNewPassword(), request.getConfirmNewPassword());

        if (!passwordEncoder.matches(request.getCurrentPassword(), targetUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        targetUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(targetUser);
    }

    @Transactional
    public void adminChangeUserPassword(
            String targetUserId,
            UUID authenticatedAdminUserId,
            AdminChangeUserPasswordRequest request
    ) {
        User authenticatedAdmin = userRepository.findById(authenticatedAdminUserId)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated admin not found"));

        if (!passwordEncoder.matches(request.getAdminPassword(), authenticatedAdmin.getPassword())) {
            throw new IllegalArgumentException("Admin password is incorrect");
        }

        validatePasswordConfirmation(request.getNewPassword(), request.getConfirmNewPassword());

        User targetUser = userRepository.findById(UUID.fromString(targetUserId))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + targetUserId));

        targetUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(targetUser);
    }

    private void validateSelfScope(UUID targetUserId, UUID authenticatedUserId) {
        if (!targetUserId.equals(authenticatedUserId)) {
            throw new AccessDeniedException("Authenticated user cannot change password for another user");
        }
    }

    private void validatePasswordConfirmation(String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }
    }

    private UserResponse mapUserToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId().toString());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setEnabled(user.isEnabled());
        response.setPerfil(mapPerfilToResponse(user.getPerfil()));
        response.setMoradiaUsuarioNome(resolveMoradiaUsuarioNome(user));

        Set<String> roleNames = new HashSet<>();
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> roleNames.add(role.getName()));
        }
        response.setRoles(roleNames);

        Set<String> lotacaoIds = new HashSet<>();
        Set<String> lotacoes = new HashSet<>();
        if (user.getLotacoes() != null) {
            user.getLotacoes().forEach(lotacao -> {
                lotacaoIds.add(lotacao.getId().toString());
                lotacoes.add(lotacao.getNome());
            });
        }
        response.setLotacaoIds(lotacaoIds);
        response.setLotacoes(lotacoes);

        return response;
    }

    private void applyPerfil(User user, PerfilRequest request) {
        Perfil perfil = user.getPerfil();
        if (perfil == null) {
            perfil = new Perfil();
        }

        perfil.setUser(user);
        perfil.setNomeCompleto(request.getNomeCompleto());
        perfil.setCpf(request.getCpf());
        perfil.setTelefone(request.getTelefone());
        perfil.setDataNascimento(request.getDataNascimento());
        user.setPerfil(perfil);
    }

    private PerfilResponse mapPerfilToResponse(Perfil perfil) {
        PerfilResponse response = new PerfilResponse();
        if (perfil != null) {
            response.setNomeCompleto(perfil.getNomeCompleto());
            response.setCpf(perfil.getCpf());
            response.setTelefone(perfil.getTelefone());
            response.setDataNascimento(perfil.getDataNascimento());
        }
        return response;
    }

    private String resolveMoradiaUsuarioNome(User user) {
        if (user.getMoradias() == null || user.getMoradias().isEmpty()) {
            return null;
        }

        return resolveUsuarioNome(user);
    }

    private String resolveUsuarioNome(User usuario) {
        return usuario.getPerfil() != null
                && usuario.getPerfil().getNomeCompleto() != null
                && !usuario.getPerfil().getNomeCompleto().isBlank()
                ? usuario.getPerfil().getNomeCompleto()
                : usuario.getUsername();
    }

    private UserSearchResponse mapUserToSearchResponse(User user) {
        String nomeCompleto = user.getPerfil() != null ? user.getPerfil().getNomeCompleto() : null;
        String texto = (nomeCompleto != null && !nomeCompleto.isBlank())
                ? nomeCompleto + " (" + user.getUsername() + ")"
                : user.getUsername();
        return new UserSearchResponse(user.getId().toString(), texto);
    }

    private Set<Lotacao> resolveLotacoes(Set<String> lotacaoIds) {
        Set<Lotacao> lotacoes = new HashSet<>();
        if (lotacaoIds == null || lotacaoIds.isEmpty()) {
            return lotacoes;
        }
        for (String lotacaoId : lotacaoIds) {
            if (lotacaoId == null || lotacaoId.isBlank()) {
                continue;
            }
            Lotacao lotacao = lotacaoRepository.findById(UUID.fromString(lotacaoId))
                    .orElseThrow(() -> new IllegalArgumentException("Lotacao nao encontrada: " + lotacaoId));
            lotacoes.add(lotacao);
        }
        return lotacoes;
    }

    private Set<Role> resolveRoles(Set<String> roleIds) {
        Set<Role> roles = new HashSet<>();
        if (roleIds == null || roleIds.isEmpty()) {
            return roles;
        }
        for (String roleId : roleIds) {
            if (roleId == null || roleId.isBlank()) {
                continue;
            }
            roleRepository.findById(UUID.fromString(roleId)).ifPresent(roles::add);
        }
        return roles;
    }

    private void validateMoradorLotacaoRule(Set<Role> roles, Set<String> lotacaoIds) {
        if (roles == null || roles.isEmpty() || lotacaoIds == null || lotacaoIds.isEmpty()) {
            return;
        }
        boolean hasMoradorRole = roles.stream().anyMatch(role -> "MORADOR".equalsIgnoreCase(role.getName()));
        if (hasMoradorRole) {
            throw new IllegalArgumentException("Usuario com cargo MORADOR nao pode ter lotacao.");
        }
    }

    private void requireProfilePhotoSupport() {
        if (userFotoRepository == null || uploadStorageService == null) {
            throw new IllegalStateException("Suporte a foto de perfil nao inicializado");
        }
    }

    public record ProfilePhotoDownload(
            Resource resource,
            String nomeOriginal,
            String contentType,
            long tamanhoBytes
    ) {}
}

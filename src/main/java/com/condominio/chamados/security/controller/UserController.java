package com.condominio.chamados.security.controller;

import com.condominio.chamados.bloco.service.MoradiaService;
import com.condominio.chamados.bloco.dto.response.MoradiaResponse;
import com.condominio.chamados.security.dto.request.AdminChangeUserPasswordRequest;
import com.condominio.chamados.security.dto.request.ChangeOwnPasswordRequest;
import com.condominio.chamados.security.dto.request.UserProfileRequest;
import com.condominio.chamados.security.dto.request.UpdateUserRolesRequest;
import com.condominio.chamados.security.dto.request.UserRequest;
import com.condominio.chamados.security.dto.response.UserResponse;
import com.condominio.chamados.security.permission.PermissionConstants;
import com.condominio.chamados.security.service.UserPrincipal;
import com.condominio.chamados.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;
import java.util.List;

/**
 * Controller para gerenciamento de usuários.
 *
 * Autorização baseada em permissions (RESOURCE:ACTION).
 * Exemplo: @PreAuthorize("hasAuthority('USER:CREATE')")
 */
@Controller
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;
    private final MoradiaService moradiaService;

    public UserController(UserService userService, MoradiaService moradiaService) {
        this.userService = userService;
        this.moradiaService = moradiaService;
    }

    /**
     * GET /usuarios - Listar usuários
     * Requer: USER:UPDATE (admin/colaborador que pode gerenciar usuários)
     */
    @GetMapping
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_UPDATE + "')")
    public String listUsers(Model model) {
        model.addAttribute("usuarios", userService.getAllUsers());
        model.addAttribute("pageTitle", "Usuarios");
        model.addAttribute("activePage", "usuarios-lista");
        return "usuario/lista";
    }

    /**
     * GET /usuarios/perfil - Ver perfil do usuário autenticado
     * Requer: Apenas autenticação (qualquer usuário pode ver seu próprio perfil)
     */
    @GetMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public String viewProfile(Model model, Authentication authentication) {
        UserResponse user = userService.getUserByUsername(authentication.getName());
        model.addAttribute("usuario", user);
        model.addAttribute("pageTitle", "Meu Perfil");
        model.addAttribute("activePage", "perfil");
        return "usuario/perfil";
    }

    @PostMapping(value = "/perfil/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public String updateOwnProfilePhoto(
            @RequestParam("foto") MultipartFile foto,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        UUID authenticatedUserId = extractAuthenticatedUserId(authentication);
        try {
            userService.updateOwnProfilePhoto(authenticatedUserId.toString(), foto);
            redirectAttributes.addFlashAttribute("successMessage", "Foto de perfil atualizada com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/usuarios/perfil";
    }

    @GetMapping("/perfil/foto")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<org.springframework.core.io.Resource> getOwnProfilePhoto(Authentication authentication) {
        UUID authenticatedUserId = extractAuthenticatedUserId(authentication);
        var foto = userService.getOwnProfilePhoto(authenticatedUserId.toString());
        if (foto == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (foto.contentType() != null && !foto.contentType().isBlank()) {
            mediaType = MediaType.parseMediaType(foto.contentType());
        }
        ContentDisposition disposition = ContentDisposition.inline()
                .filename(foto.nomeOriginal())
                .build();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(foto.tamanhoBytes())
                .body(foto.resource());
    }

    /**
     * PATCH /usuarios/perfil - Editar próprio perfil (email e dados pessoais)
     * Requer: autenticação
     */
    @PatchMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<Void> updateProfile(
            @Valid @RequestBody UserProfileRequest request,
            Authentication authentication
    ) {
        UUID authenticatedUserId = extractAuthenticatedUserId(authentication);
        userService.updateUserProfile(authenticatedUserId.toString(), request);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /usuarios/{id} - Ver detalhes de um usuário específico
     * Requer: USER:UPDATE (admin/colaborador)
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_UPDATE + "')")
    public String viewUser(@PathVariable String id, Model model) {
        UserResponse user = userService.getUserById(id);
        model.addAttribute("usuario", user);
        model.addAttribute("rolesDisponiveis", userService.getAllRoles());
        model.addAttribute("pageTitle", "Detalhes do Usuario");
        model.addAttribute("activePage", "usuarios-lista");
        return "usuario/detalhes";
    }

    @GetMapping("/{id}/moradias")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_UPDATE + "')")
    @ResponseBody
    public List<MoradiaResponse> listMoradiasByUsuario(@PathVariable String id) {
        return moradiaService.listarPorUsuario(id);
    }

    /**
     * GET /usuarios/novo - Formulário de criação de usuário
     * Requer: USER:CREATE
     */
    @GetMapping("/novo")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_CREATE + "')")
    public String showCreateForm(Model model) {
        model.addAttribute("usuario", new UserRequest());
        model.addAttribute("rolesDisponiveis", userService.getAllRoles());
        model.addAttribute("lotacoesDisponiveis", userService.getAllLotacoes());
        model.addAttribute("pageTitle", "Novo Usuario");
        model.addAttribute("activePage", "usuarios-novo");
        return "usuario/novo";
    }

    /**
     * POST /usuarios - Criar novo usuário
     * Requer: USER:CREATE
     */
    @PostMapping
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_CREATE + "')")
    public String createUser(@ModelAttribute UserRequest userRequest, Model model) {
        try {
            userService.createUser(userRequest);
            return "redirect:/usuarios";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("usuario", userRequest);
            model.addAttribute("rolesDisponiveis", userService.getAllRoles());
            model.addAttribute("lotacoesDisponiveis", userService.getAllLotacoes());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("pageTitle", "Novo Usuario");
            model.addAttribute("activePage", "usuarios-novo");
            return "usuario/novo";
        }
    }

    /**
     * GET /usuarios/{id}/editar - Formulário de edição de usuário
     * Requer: USER:UPDATE
     */
    @GetMapping("/{id}/editar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_UPDATE + "')")
    public String showEditForm(@PathVariable String id, Model model) {
        UserResponse user = userService.getUserById(id);
        model.addAttribute("usuario", user);
        model.addAttribute("rolesDisponiveis", userService.getAllRoles());
        model.addAttribute("lotacoesDisponiveis", userService.getAllLotacoes());
        model.addAttribute("pageTitle", "Editar Usuario");
        model.addAttribute("activePage", "usuarios-lista");
        return "usuario/editar";
    }

    /**
     * POST /usuarios/{id} - Atualizar usuário
     * Requer: USER:UPDATE
     */
    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_UPDATE + "')")
    public String updateUser(@PathVariable String id, @ModelAttribute UserRequest userRequest) {
        userService.updateUser(id, userRequest);
        return "redirect:/usuarios/" + id;
    }

    /**
     * POST /usuarios/{id}/deletar - Deletar usuário
     * Requer: USER:DELETE
     */
    @PostMapping("/{id}/deletar")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_DELETE + "')")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/usuarios";
    }

    /**
     * POST /usuarios/{id}/roles - Atualizar roles de um usuário
     * Requer: USER:UPDATE
     * Body: UpdateUserRolesRequest com Set de IDs de roles para atribuir.
     * Sobrescreve completamente os relacionamentos atuais.
     */
    @PostMapping("/{id}/roles")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_UPDATE + "')")
    public String updateUserRoles(@PathVariable String id, @RequestBody UpdateUserRolesRequest request) {
        userService.updateUserRoles(id, request);
        return "redirect:/usuarios/" + id;
    }

    /**
     * PATCH /usuarios/{id}/senha - Alterar a própria senha
     * Requer: autenticação e escopo do usuário autenticado sobre o próprio id.
     */
    @PatchMapping("/{id}/senha")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<Void> changeOwnPassword(
            @PathVariable String id,
            @Valid @RequestBody ChangeOwnPasswordRequest request,
            Authentication authentication
    ) {
        UUID authenticatedUserId = extractAuthenticatedUserId(authentication);
        userService.changeOwnPassword(id, authenticatedUserId, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /usuarios/{id}/senha/admin - Alterar senha de usuário com confirmação da senha do admin.
     * Requer: autenticação e USER:UPDATE.
     */
    @PatchMapping("/{id}/senha/admin")
    @PreAuthorize("isAuthenticated() and hasAuthority('" + PermissionConstants.USER_UPDATE + "')")
    @ResponseBody
    public ResponseEntity<Void> adminChangeUserPassword(
            @PathVariable String id,
            @Valid @RequestBody AdminChangeUserPasswordRequest request,
            Authentication authentication
    ) {
        UUID authenticatedAdminId = extractAuthenticatedUserId(authentication);
        userService.adminChangeUserPassword(id, authenticatedAdminId, request);
        return ResponseEntity.noContent().build();
    }

    private UUID extractAuthenticatedUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser().getId();
        }
        throw new IllegalArgumentException("Authenticated principal does not expose user id");
    }
}

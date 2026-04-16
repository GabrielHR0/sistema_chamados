package com.condominio.chamados.security.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO para atualizar os roles de um usuário.
 * Sobrescreve completamente os relacionamentos atuais.
 */
public class UpdateUserRolesRequest {

    @NotNull(message = "roleIds não pode ser nulo")
    private Set<String> roleIds = new HashSet<>();

    public UpdateUserRolesRequest() {}

    public UpdateUserRolesRequest(Set<String> roleIds) {
        this.roleIds = roleIds != null ? roleIds : new HashSet<>();
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds != null ? roleIds : new HashSet<>();
    }
}

package com.condominio.chamados.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.Set;

public class UserRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    private boolean enabled = true;

    @Valid
    private PerfilRequest perfil = new PerfilRequest();

    private Set<String> roleIds = new HashSet<>();
    private Set<String> lotacaoIds = new HashSet<>();

    public UserRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PerfilRequest getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilRequest perfil) {
        this.perfil = perfil;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<String> getLotacaoIds() {
        return lotacaoIds;
    }

    public void setLotacaoIds(Set<String> lotacaoIds) {
        this.lotacaoIds = lotacaoIds;
    }
}

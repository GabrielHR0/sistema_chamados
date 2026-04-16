package com.condominio.chamados.security.dto.response;

import java.util.HashSet;
import java.util.Set;

public class UserResponse {

    private String id;
    private String username;
    private String email;
    private boolean enabled;
    private PerfilResponse perfil = new PerfilResponse();
    private Set<String> roles = new HashSet<>();
    private Set<String> lotacaoIds = new HashSet<>();
    private Set<String> lotacoes = new HashSet<>();
    private String moradiaUsuarioNome;

    public UserResponse() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public PerfilResponse getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilResponse perfil) {
        this.perfil = perfil;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getLotacaoIds() {
        return lotacaoIds;
    }

    public void setLotacaoIds(Set<String> lotacaoIds) {
        this.lotacaoIds = lotacaoIds;
    }

    public Set<String> getLotacoes() {
        return lotacoes;
    }

    public void setLotacoes(Set<String> lotacoes) {
        this.lotacoes = lotacoes;
    }

    public String getMoradiaUsuarioNome() {
        return moradiaUsuarioNome;
    }

    public void setMoradiaUsuarioNome(String moradiaUsuarioNome) {
        this.moradiaUsuarioNome = moradiaUsuarioNome;
    }
}

package com.condominio.chamados.security.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserProfileRequest {

    @NotBlank
    @Email
    private String email;

    @Valid
    private PerfilRequest perfil = new PerfilRequest();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PerfilRequest getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilRequest perfil) {
        this.perfil = perfil;
    }
}

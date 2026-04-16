package com.condominio.chamados.bloco.dto.request;

import jakarta.validation.constraints.NotBlank;

public class MoradiaCreateRequest {

    @NotBlank(message = "Unidade é obrigatória")
    private String unidadeId;

    @NotBlank(message = "Usuario é obrigatório")
    private String usuarioId;

    public String getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(String unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}

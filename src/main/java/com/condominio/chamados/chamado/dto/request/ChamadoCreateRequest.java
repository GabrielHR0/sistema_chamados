package com.condominio.chamados.chamado.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ChamadoCreateRequest {

    @NotBlank(message = "Unidade e obrigatoria")
    private String unidadeId;

    @NotBlank(message = "Tipo de chamado e obrigatorio")
    private String tipoId;

    private String responsavelId;

    @NotBlank(message = "Descricao e obrigatoria")
    private String descricao;

    public String getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(String unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public String getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(String responsavelId) {
        this.responsavelId = responsavelId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

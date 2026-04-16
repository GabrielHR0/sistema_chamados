package com.condominio.chamados.bloco.dto.request;

import jakarta.validation.constraints.Size;

public class BlocoUpdateRequest {

    private boolean ativo = true;

    @Size(max = 1000)
    private String observacoes;

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}

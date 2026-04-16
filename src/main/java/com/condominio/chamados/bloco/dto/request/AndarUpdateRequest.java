package com.condominio.chamados.bloco.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class AndarUpdateRequest {

    @Min(1)
    private Integer numero;

    private boolean ativo = true;

    @Size(max = 1000)
    private String observacoes;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

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

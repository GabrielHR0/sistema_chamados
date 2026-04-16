package com.condominio.chamados.bloco.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AndarRequest {

    @NotNull
    private UUID blocoId;

    @NotNull
    @Min(1)
    private Integer numero;

    private boolean ativo = true;

    @Size(max = 1000)
    private String observacoes;

    public UUID getBlocoId() {
        return blocoId;
    }

    public void setBlocoId(UUID blocoId) {
        this.blocoId = blocoId;
    }

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

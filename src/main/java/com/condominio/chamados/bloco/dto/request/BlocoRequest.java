package com.condominio.chamados.bloco.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BlocoRequest {

    @NotBlank
    @Size(max = 50)
    private String identificacao;

    @NotNull
    @Min(1)
    private Integer quantidadeAndares;

    @NotNull
    @Min(1)
    private Integer apartamentosPorAndar;

    private boolean ativo = true;

    @Size(max = 1000)
    private String observacoes;

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public Integer getQuantidadeAndares() {
        return quantidadeAndares;
    }

    public void setQuantidadeAndares(Integer quantidadeAndares) {
        this.quantidadeAndares = quantidadeAndares;
    }

    public Integer getApartamentosPorAndar() {
        return apartamentosPorAndar;
    }

    public void setApartamentosPorAndar(Integer apartamentosPorAndar) {
        this.apartamentosPorAndar = apartamentosPorAndar;
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

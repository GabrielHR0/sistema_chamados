package com.condominio.chamados.bloco.dto.request;

import com.condominio.chamados.bloco.domain.UnidadeStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UnidadeUpdateRequest {

    @Min(1)
    private Integer apartamentoNumero;

    private UnidadeStatus status = UnidadeStatus.EM_FUNCIONAMENTO;

    private boolean ativo = true;

    @Size(max = 1000)
    private String observacoes;

    public Integer getApartamentoNumero() {
        return apartamentoNumero;
    }

    public void setApartamentoNumero(Integer apartamentoNumero) {
        this.apartamentoNumero = apartamentoNumero;
    }

    public UnidadeStatus getStatus() {
        return status;
    }

    public void setStatus(UnidadeStatus status) {
        this.status = status;
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

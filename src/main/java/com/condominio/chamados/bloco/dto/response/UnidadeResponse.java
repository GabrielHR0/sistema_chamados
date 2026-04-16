package com.condominio.chamados.bloco.dto.response;

import com.condominio.chamados.bloco.domain.UnidadeStatus;

import java.util.ArrayList;
import java.util.List;

public class UnidadeResponse {

    private String id;
    private String andarId;
    private Integer apartamentoNumero;
    private String identificacao;
    private boolean ativo;
    private UnidadeStatus status;
    private String observacoes;
    private List<MoradiaResponse> moradias = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAndarId() {
        return andarId;
    }

    public void setAndarId(String andarId) {
        this.andarId = andarId;
    }

    public Integer getApartamentoNumero() {
        return apartamentoNumero;
    }

    public void setApartamentoNumero(Integer apartamentoNumero) {
        this.apartamentoNumero = apartamentoNumero;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public UnidadeStatus getStatus() {
        return status;
    }

    public void setStatus(UnidadeStatus status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<MoradiaResponse> getMoradias() {
        return moradias;
    }

    public void setMoradias(List<MoradiaResponse> moradias) {
        this.moradias = moradias;
    }
}

package com.condominio.chamados.bloco.dto.response;

import java.util.ArrayList;
import java.util.List;

public class AndarResponse {

    private String id;
    private String blocoId;
    private String blocoIdentificacao;
    private Integer numero;
    private boolean ativo;
    private String observacoes;
    private List<UnidadeResponse> unidades = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlocoId() {
        return blocoId;
    }

    public void setBlocoId(String blocoId) {
        this.blocoId = blocoId;
    }

    public String getBlocoIdentificacao() {
        return blocoIdentificacao;
    }

    public void setBlocoIdentificacao(String blocoIdentificacao) {
        this.blocoIdentificacao = blocoIdentificacao;
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

    public List<UnidadeResponse> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadeResponse> unidades) {
        this.unidades = unidades;
    }
}

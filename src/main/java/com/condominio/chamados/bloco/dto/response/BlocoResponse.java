package com.condominio.chamados.bloco.dto.response;

import java.util.ArrayList;
import java.util.List;

public class BlocoResponse {

    private String id;
    private String identificacao;
    private boolean ativo;
    private String observacoes;
    private List<AndarResponse> andares = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<AndarResponse> getAndares() {
        return andares;
    }

    public void setAndares(List<AndarResponse> andares) {
        this.andares = andares;
    }
}

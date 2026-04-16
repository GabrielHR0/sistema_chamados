package com.condominio.chamados.chamado.dto.response;

import java.util.ArrayList;
import java.util.List;

public class LotacaoResponse {

    private String id;
    private String nome;
    private String descricao;
    private List<TipoChamadoResponse> tiposChamado = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<TipoChamadoResponse> getTiposChamado() {
        return tiposChamado;
    }

    public void setTiposChamado(List<TipoChamadoResponse> tiposChamado) {
        this.tiposChamado = tiposChamado;
    }
}

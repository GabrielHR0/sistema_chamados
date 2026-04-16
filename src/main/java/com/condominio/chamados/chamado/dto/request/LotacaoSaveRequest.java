package com.condominio.chamados.chamado.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class LotacaoSaveRequest {

    @NotBlank(message = "Nome da lotacao e obrigatorio")
    private String nome;

    private String descricao;

    private List<String> tiposChamadoIds = new ArrayList<>();

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

    public List<String> getTiposChamadoIds() {
        return tiposChamadoIds;
    }

    public void setTiposChamadoIds(List<String> tiposChamadoIds) {
        this.tiposChamadoIds = tiposChamadoIds;
    }
}

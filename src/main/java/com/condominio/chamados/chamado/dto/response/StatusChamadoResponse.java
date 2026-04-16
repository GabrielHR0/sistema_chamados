package com.condominio.chamados.chamado.dto.response;

import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;

import java.util.ArrayList;
import java.util.List;

public class StatusChamadoResponse {

    private String id;
    private String nome;
    private StatusComportamentoTipo comportamentoTipo;
    private List<String> proximosNomes = new ArrayList<>();

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

    public StatusComportamentoTipo getComportamentoTipo() {
        return comportamentoTipo;
    }

    public void setComportamentoTipo(StatusComportamentoTipo comportamentoTipo) {
        this.comportamentoTipo = comportamentoTipo;
    }

    public List<String> getProximosNomes() {
        return proximosNomes;
    }

    public void setProximosNomes(List<String> proximosNomes) {
        this.proximosNomes = proximosNomes;
    }
}

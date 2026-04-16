package com.condominio.chamados.chamado.dto.request;

import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StatusChamadoCreateRequest {

    @NotBlank(message = "Nome do status e obrigatorio")
    private String nome;

    @NotNull(message = "Tipo de comportamento e obrigatorio")
    private StatusComportamentoTipo comportamentoTipo;

    private List<String> proximosIds = new ArrayList<>();

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

    public List<String> getProximosIds() {
        return proximosIds;
    }

    public void setProximosIds(List<String> proximosIds) {
        this.proximosIds = proximosIds;
    }
}

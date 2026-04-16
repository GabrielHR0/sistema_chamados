package com.condominio.chamados.bloco.dto.response;

import com.condominio.chamados.bloco.domain.MoradiaStatus;

import java.time.LocalDate;

public class MoradiaResponse {

    private String id;
    private String usuarioId;
    private String usuarioNome;
    private String usuarioAtualNome;
    private String unidadeId;
    private String unidadeIdentificacao;
    private MoradiaStatus status;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioAtualNome() {
        return usuarioAtualNome;
    }

    public void setUsuarioAtualNome(String usuarioAtualNome) {
        this.usuarioAtualNome = usuarioAtualNome;
    }

    public String getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(String unidadeId) {
        this.unidadeId = unidadeId;
    }

    public MoradiaStatus getStatus() {
        return status;
    }

    public void setStatus(MoradiaStatus status) {
        this.status = status;
    }

    public String getUnidadeIdentificacao() {
        return unidadeIdentificacao;
    }

    public void setUnidadeIdentificacao(String unidadeIdentificacao) {
        this.unidadeIdentificacao = unidadeIdentificacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}

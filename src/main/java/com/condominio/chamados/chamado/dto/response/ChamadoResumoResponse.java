package com.condominio.chamados.chamado.dto.response;

import java.time.OffsetDateTime;

public class ChamadoResumoResponse {

    private String id;
    private String unidadeIdentificacao;
    private String tipoTitulo;
    private String statusNome;
    private boolean statusFinal;
    private String colaboradorResponsavelNome;
    private OffsetDateTime dataCriacao;
    private String dataCriacaoFormatada;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnidadeIdentificacao() {
        return unidadeIdentificacao;
    }

    public void setUnidadeIdentificacao(String unidadeIdentificacao) {
        this.unidadeIdentificacao = unidadeIdentificacao;
    }

    public String getTipoTitulo() {
        return tipoTitulo;
    }

    public void setTipoTitulo(String tipoTitulo) {
        this.tipoTitulo = tipoTitulo;
    }

    public String getStatusNome() {
        return statusNome;
    }

    public void setStatusNome(String statusNome) {
        this.statusNome = statusNome;
    }

    public boolean isStatusFinal() {
        return statusFinal;
    }

    public void setStatusFinal(boolean statusFinal) {
        this.statusFinal = statusFinal;
    }

    public String getColaboradorResponsavelNome() {
        return colaboradorResponsavelNome;
    }

    public void setColaboradorResponsavelNome(String colaboradorResponsavelNome) {
        this.colaboradorResponsavelNome = colaboradorResponsavelNome;
    }

    public OffsetDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(OffsetDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataCriacaoFormatada() {
        return dataCriacaoFormatada;
    }

    public void setDataCriacaoFormatada(String dataCriacaoFormatada) {
        this.dataCriacaoFormatada = dataCriacaoFormatada;
    }
}

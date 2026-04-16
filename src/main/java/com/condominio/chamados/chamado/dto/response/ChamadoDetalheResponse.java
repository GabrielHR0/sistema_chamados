package com.condominio.chamados.chamado.dto.response;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChamadoDetalheResponse {

    private String id;
    private String codigo;
    private String unidadeIdentificacao;
    private String tipoTitulo;
    private Integer tipoSlaHoras;
    private String statusId;
    private String statusNome;
    private boolean statusFinal;
    private String colaboradorResponsavelNome;
    private String descricao;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataFinalizacao;
    private String dataCriacaoFormatada;
    private String dataFinalizacaoFormatada;
    private List<ComentarioChamadoResponse> comentarios = new ArrayList<>();
    private List<ChamadoAnexoResponse> anexos = new ArrayList<>();
    private List<StatusOpcaoResponse> proximosStatus = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Integer getTipoSlaHoras() {
        return tipoSlaHoras;
    }

    public void setTipoSlaHoras(Integer tipoSlaHoras) {
        this.tipoSlaHoras = tipoSlaHoras;
    }

    public String getStatusNome() {
        return statusNome;
    }

    public void setStatusNome(String statusNome) {
        this.statusNome = statusNome;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OffsetDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(OffsetDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public OffsetDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public String getDataCriacaoFormatada() {
        return dataCriacaoFormatada;
    }

    public void setDataCriacaoFormatada(String dataCriacaoFormatada) {
        this.dataCriacaoFormatada = dataCriacaoFormatada;
    }

    public String getDataFinalizacaoFormatada() {
        return dataFinalizacaoFormatada;
    }

    public void setDataFinalizacaoFormatada(String dataFinalizacaoFormatada) {
        this.dataFinalizacaoFormatada = dataFinalizacaoFormatada;
    }

    public List<ComentarioChamadoResponse> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioChamadoResponse> comentarios) {
        this.comentarios = comentarios;
    }

    public List<StatusOpcaoResponse> getProximosStatus() {
        return proximosStatus;
    }

    public void setProximosStatus(List<StatusOpcaoResponse> proximosStatus) {
        this.proximosStatus = proximosStatus;
    }

    public List<ChamadoAnexoResponse> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<ChamadoAnexoResponse> anexos) {
        this.anexos = anexos;
    }
}

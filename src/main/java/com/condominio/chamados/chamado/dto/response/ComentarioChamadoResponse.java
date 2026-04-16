package com.condominio.chamados.chamado.dto.response;

import java.time.OffsetDateTime;

public class ComentarioChamadoResponse {

    private String id;
    private String autorNome;
    private String comentario;
    private OffsetDateTime dataCriacao;
    private String dataCriacaoFormatada;
    private java.util.List<ComentarioAnexoResponse> anexos = new java.util.ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutorNome() {
        return autorNome;
    }

    public void setAutorNome(String autorNome) {
        this.autorNome = autorNome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public java.util.List<ComentarioAnexoResponse> getAnexos() {
        return anexos;
    }

    public void setAnexos(java.util.List<ComentarioAnexoResponse> anexos) {
        this.anexos = anexos;
    }
}

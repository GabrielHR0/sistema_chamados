package com.condominio.chamados.chamado.dto.response;

public class TipoChamadoResponse {

    private String id;
    private String titulo;
    private Integer slaHoras;
    private String statusInicialId;
    private String statusInicialNome;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getSlaHoras() {
        return slaHoras;
    }

    public void setSlaHoras(Integer slaHoras) {
        this.slaHoras = slaHoras;
    }

    public String getStatusInicialId() {
        return statusInicialId;
    }

    public void setStatusInicialId(String statusInicialId) {
        this.statusInicialId = statusInicialId;
    }

    public String getStatusInicialNome() {
        return statusInicialNome;
    }

    public void setStatusInicialNome(String statusInicialNome) {
        this.statusInicialNome = statusInicialNome;
    }
}

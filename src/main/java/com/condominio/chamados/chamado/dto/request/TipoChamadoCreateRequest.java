package com.condominio.chamados.chamado.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TipoChamadoCreateRequest {

    @NotBlank(message = "Titulo e obrigatorio")
    private String titulo;

    @Min(value = 1, message = "SLA deve ser maior que zero")
    private Integer slaHoras;

    @NotBlank(message = "Status inicial e obrigatorio")
    private String statusInicialId;

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
}

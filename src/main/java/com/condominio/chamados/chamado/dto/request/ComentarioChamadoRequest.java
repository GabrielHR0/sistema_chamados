package com.condominio.chamados.chamado.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ComentarioChamadoRequest {

    @NotBlank(message = "Comentario e obrigatorio")
    private String comentario;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

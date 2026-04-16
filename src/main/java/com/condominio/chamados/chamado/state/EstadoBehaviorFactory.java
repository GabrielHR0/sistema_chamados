package com.condominio.chamados.chamado.state;

import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;

public final class EstadoBehaviorFactory {

    private EstadoBehaviorFactory() {
    }

    public static EstadoBehavior fromTipo(StatusComportamentoTipo tipo) {
        return switch (tipo) {
            case INICIAL -> new EstadoInicialBehavior();
            case INTERMEDIARIO -> new EstadoIntermediarioBehavior();
            case FINAL -> new EstadoFinalBehavior();
        };
    }
}

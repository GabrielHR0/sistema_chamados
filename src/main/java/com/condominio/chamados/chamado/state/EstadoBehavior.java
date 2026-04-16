package com.condominio.chamados.chamado.state;

import com.condominio.chamados.chamado.domain.StatusChamado;
import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;

import java.util.Set;

public interface EstadoBehavior {
    StatusComportamentoTipo getTipo();

    void validarProximo(StatusChamado statusAtual, StatusChamado proximo);

    default void validarProximosConfigurados(StatusChamado statusAtual, Set<StatusChamado> proximos) {
        if (proximos == null) {
            return;
        }
        for (StatusChamado proximo : proximos) {
            if (proximo == null) {
                throw new IllegalArgumentException("Proximo status deve ser informado.");
            }
        }
    }
}

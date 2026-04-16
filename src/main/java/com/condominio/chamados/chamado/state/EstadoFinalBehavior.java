package com.condominio.chamados.chamado.state;

import com.condominio.chamados.chamado.domain.StatusChamado;
import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;

import java.util.Set;

public class EstadoFinalBehavior implements EstadoBehavior {
    @Override
    public StatusComportamentoTipo getTipo() {
        return StatusComportamentoTipo.FINAL;
    }

    @Override
    public void validarProximo(StatusChamado statusAtual, StatusChamado proximo) {
        throw new IllegalStateException("Status FINAL nao pode ter proximos status.");
    }

    @Override
    public void validarProximosConfigurados(StatusChamado statusAtual, Set<StatusChamado> proximos) {
        if (proximos != null && !proximos.isEmpty()) {
            throw new IllegalStateException("Status FINAL nao pode ter proximos status.");
        }
    }
}

package com.condominio.chamados.chamado.state;

import com.condominio.chamados.chamado.domain.StatusChamado;
import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;

public class EstadoInicialBehavior implements EstadoBehavior {
    @Override
    public StatusComportamentoTipo getTipo() {
        return StatusComportamentoTipo.INICIAL;
    }

    @Override
    public void validarProximo(StatusChamado statusAtual, StatusChamado proximo) {
        if (proximo == null) {
            throw new IllegalArgumentException("Proximo status deve ser informado.");
        }
        if (proximo.getId() == null) {
            throw new IllegalArgumentException("Proximo status precisa existir para transicao.");
        }
        if (statusAtual == null || statusAtual.getProximos() == null || !statusAtual.getProximos().contains(proximo)) {
            throw new IllegalStateException("Transicao invalida: proximo status nao pertence aos proximos permitidos do status atual.");
        }
    }
}

package com.condominio.chamados.chamado.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatusChamadoBehaviorTest {

    @Test
    void devePermitirProximosParaStatusInicial() {
        StatusChamado inicial = novoStatus("ABERTO", StatusComportamentoTipo.INICIAL);
        StatusChamado intermediario = novoStatusExistente("EM_ANALISE", StatusComportamentoTipo.INTERMEDIARIO);

        assertDoesNotThrow(() -> inicial.addProximo(intermediario));
        assertDoesNotThrow(() -> inicial.validarProximo(intermediario));
    }

    @Test
    void devePermitirProximosParaStatusIntermediario() {
        StatusChamado intermediario = novoStatus("EM_ANALISE", StatusComportamentoTipo.INTERMEDIARIO);
        StatusChamado finalStatus = novoStatusExistente("ENCERRADO", StatusComportamentoTipo.FINAL);

        assertDoesNotThrow(() -> intermediario.addProximo(finalStatus));
        assertDoesNotThrow(() -> intermediario.validarProximo(finalStatus));
    }

    @Test
    void naoDevePermitirProximosParaStatusFinal() {
        StatusChamado finalStatus = novoStatus("ENCERRADO", StatusComportamentoTipo.FINAL);
        StatusChamado intermediario = novoStatusExistente("EM_ANALISE", StatusComportamentoTipo.INTERMEDIARIO);

        assertThrows(IllegalStateException.class, () -> finalStatus.addProximo(intermediario));
        assertThrows(IllegalStateException.class, () -> finalStatus.validarProximo(intermediario));
    }

    @Test
    void naoDevePermitirTrocarParaFinalQuandoJaExistemProximos() {
        StatusChamado status = novoStatus("EM_ANALISE", StatusComportamentoTipo.INTERMEDIARIO);
        StatusChamado proximo = novoStatusExistente("ENCERRADO", StatusComportamentoTipo.FINAL);
        status.addProximo(proximo);

        assertThrows(IllegalStateException.class, () -> status.setComportamentoTipo(StatusComportamentoTipo.FINAL));
    }

    @Test
    void naoDevePermitirTransicaoQuandoProximoNaoPertenceAoAtual() {
        StatusChamado inicial = novoStatus("ABERTO", StatusComportamentoTipo.INICIAL);
        StatusChamado emAndamento = novoStatusExistente("EM_ANDAMENTO", StatusComportamentoTipo.INTERMEDIARIO);

        assertThrows(IllegalStateException.class, () -> inicial.validarProximo(emAndamento));
    }

    @Test
    void naoDevePermitirTransicaoParaProximoNaoPersistido() {
        StatusChamado inicial = novoStatus("ABERTO", StatusComportamentoTipo.INICIAL);
        StatusChamado novoProximo = novoStatus("NOVO", StatusComportamentoTipo.INTERMEDIARIO);
        inicial.addProximo(novoProximo);

        assertThrows(IllegalArgumentException.class, () -> inicial.validarProximo(novoProximo));
    }

    private StatusChamado novoStatus(String nome, StatusComportamentoTipo tipo) {
        StatusChamado status = new StatusChamado();
        status.setNome(nome);
        status.setComportamentoTipo(tipo);
        return status;
    }

    private StatusChamado novoStatusExistente(String nome, StatusComportamentoTipo tipo) {
        StatusChamado status = novoStatus(nome, tipo);
        status.setId(UUID.randomUUID());
        return status;
    }
}

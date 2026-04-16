package com.condominio.chamados.chamado.service;

import com.condominio.chamados.chamado.domain.Lotacao;
import com.condominio.chamados.chamado.domain.TipoChamado;
import com.condominio.chamados.chamado.dto.request.LotacaoSaveRequest;
import com.condominio.chamados.chamado.dto.response.LotacaoResponse;
import com.condominio.chamados.chamado.repository.LotacaoRepository;
import com.condominio.chamados.chamado.repository.TipoChamadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LotacaoServiceTest {

    @Mock
    private LotacaoRepository lotacaoRepository;
    @Mock
    private TipoChamadoRepository tipoChamadoRepository;

    @Test
    void deveCriarLotacaoComTipos() {
        LotacaoService service = new LotacaoService(lotacaoRepository, tipoChamadoRepository);
        UUID tipoId = UUID.randomUUID();

        TipoChamado tipo = new TipoChamado();
        tipo.setId(tipoId);
        tipo.setTitulo("Eletrica");

        LotacaoSaveRequest request = new LotacaoSaveRequest();
        request.setNome("Manutencao");
        request.setDescricao("Equipe de manutencao");
        request.setTiposChamadoIds(List.of(tipoId.toString()));

        when(lotacaoRepository.findByNome("Manutencao")).thenReturn(Optional.empty());
        when(tipoChamadoRepository.findById(tipoId)).thenReturn(Optional.of(tipo));

        service.criar(request);

        ArgumentCaptor<Lotacao> captor = ArgumentCaptor.forClass(Lotacao.class);
        verify(lotacaoRepository).save(captor.capture());
        Lotacao saved = captor.getValue();
        assertThat(saved.getNome()).isEqualTo("Manutencao");
        assertThat(saved.getTiposChamado()).containsExactly(tipo);
    }

    @Test
    void deveListarLotacoesComTipos() {
        LotacaoService service = new LotacaoService(lotacaoRepository, tipoChamadoRepository);
        UUID lotacaoId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();

        TipoChamado tipo = new TipoChamado();
        tipo.setId(tipoId);
        tipo.setTitulo("Hidraulica");
        tipo.setSlaHoras(24);

        Lotacao lotacao = new Lotacao();
        lotacao.setId(lotacaoId);
        lotacao.setNome("Operacoes");
        lotacao.setTiposChamado(Set.of(tipo));

        when(lotacaoRepository.findAllWithTipos()).thenReturn(List.of(lotacao));

        List<LotacaoResponse> result = service.listar();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNome()).isEqualTo("Operacoes");
        assertThat(result.getFirst().getTiposChamado()).hasSize(1);
        assertThat(result.getFirst().getTiposChamado().getFirst().getTitulo()).isEqualTo("Hidraulica");
    }

    @Test
    void deveAtualizarLotacao() {
        LotacaoService service = new LotacaoService(lotacaoRepository, tipoChamadoRepository);
        UUID lotacaoId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();

        Lotacao lotacao = new Lotacao();
        lotacao.setId(lotacaoId);
        lotacao.setNome("Antiga");

        TipoChamado tipo = new TipoChamado();
        tipo.setId(tipoId);
        tipo.setTitulo("Pintura");

        LotacaoSaveRequest request = new LotacaoSaveRequest();
        request.setNome("Nova");
        request.setDescricao("Descricao nova");
        request.setTiposChamadoIds(List.of(tipoId.toString()));

        when(lotacaoRepository.findByIdWithTipos(lotacaoId)).thenReturn(Optional.of(lotacao));
        when(lotacaoRepository.findByNome("Nova")).thenReturn(Optional.empty());
        when(tipoChamadoRepository.findById(tipoId)).thenReturn(Optional.of(tipo));

        service.atualizar(lotacaoId.toString(), request);

        verify(lotacaoRepository).save(any(Lotacao.class));
        assertThat(lotacao.getNome()).isEqualTo("Nova");
        assertThat(lotacao.getDescricao()).isEqualTo("Descricao nova");
        assertThat(lotacao.getTiposChamado()).containsExactly(tipo);
    }

    @Test
    void deveObterLotacaoPorId() {
        LotacaoService service = new LotacaoService(lotacaoRepository, tipoChamadoRepository);
        UUID lotacaoId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();

        TipoChamado tipo = new TipoChamado();
        tipo.setId(tipoId);
        tipo.setTitulo("Eletrica");
        tipo.setSlaHoras(12);

        Lotacao lotacao = new Lotacao();
        lotacao.setId(lotacaoId);
        lotacao.setNome("Plantao");
        lotacao.setDescricao("Equipe de plantao");
        lotacao.setTiposChamado(Set.of(tipo));

        when(lotacaoRepository.findByIdWithTipos(lotacaoId)).thenReturn(Optional.of(lotacao));

        LotacaoResponse result = service.obterPorId(lotacaoId.toString());

        assertThat(result.getId()).isEqualTo(lotacaoId.toString());
        assertThat(result.getNome()).isEqualTo("Plantao");
        assertThat(result.getDescricao()).isEqualTo("Equipe de plantao");
        assertThat(result.getTiposChamado()).hasSize(1);
        assertThat(result.getTiposChamado().getFirst().getId()).isEqualTo(tipoId.toString());
    }

    @Test
    void deveBloquearCriacaoQuandoNomeJaExistir() {
        LotacaoService service = new LotacaoService(lotacaoRepository, tipoChamadoRepository);
        LotacaoSaveRequest request = new LotacaoSaveRequest();
        request.setNome("Manutencao");

        when(lotacaoRepository.findByNome("Manutencao")).thenReturn(Optional.of(new Lotacao()));

        assertThatThrownBy(() -> service.criar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ja existe lotacao");
    }
}

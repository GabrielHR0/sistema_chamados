package com.condominio.chamados.chamado.service;

import com.condominio.chamados.chamado.domain.Lotacao;
import com.condominio.chamados.chamado.domain.TipoChamado;
import com.condominio.chamados.chamado.dto.request.LotacaoSaveRequest;
import com.condominio.chamados.chamado.dto.response.LotacaoResponse;
import com.condominio.chamados.chamado.dto.response.TipoChamadoResponse;
import com.condominio.chamados.chamado.repository.LotacaoRepository;
import com.condominio.chamados.chamado.repository.TipoChamadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class LotacaoService {

    private final LotacaoRepository lotacaoRepository;
    private final TipoChamadoRepository tipoChamadoRepository;

    public LotacaoService(LotacaoRepository lotacaoRepository, TipoChamadoRepository tipoChamadoRepository) {
        this.lotacaoRepository = lotacaoRepository;
        this.tipoChamadoRepository = tipoChamadoRepository;
    }

    @Transactional
    public void criar(LotacaoSaveRequest request) {
        String nome = request.getNome().trim();
        lotacaoRepository.findByNome(nome).ifPresent(existing -> {
            throw new IllegalArgumentException("Ja existe lotacao com este nome");
        });

        Lotacao lotacao = new Lotacao();
        lotacao.setNome(nome);
        lotacao.setDescricao(normalizeDescricao(request.getDescricao()));
        lotacao.setTiposChamado(resolverTipos(request.getTiposChamadoIds()));
        lotacaoRepository.save(lotacao);
    }

    @Transactional(readOnly = true)
    public List<LotacaoResponse> listar() {
        return lotacaoRepository.findAllWithTipos().stream()
                .sorted((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()))
                .map(this::mapLotacao)
                .toList();
    }

    @Transactional(readOnly = true)
    public LotacaoResponse obterPorId(String lotacaoId) {
        UUID id = parseUuid(lotacaoId, "ID de lotacao invalido");
        Lotacao lotacao = lotacaoRepository.findByIdWithTipos(id)
                .orElseThrow(() -> new IllegalArgumentException("Lotacao nao encontrada"));
        return mapLotacao(lotacao);
    }

    @Transactional
    public void atualizar(String lotacaoId, LotacaoSaveRequest request) {
        UUID id = parseUuid(lotacaoId, "ID de lotacao invalido");
        Lotacao lotacao = lotacaoRepository.findByIdWithTipos(id)
                .orElseThrow(() -> new IllegalArgumentException("Lotacao nao encontrada"));

        String novoNome = request.getNome().trim();
        lotacaoRepository.findByNome(novoNome).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Ja existe lotacao com este nome");
            }
        });

        lotacao.setNome(novoNome);
        lotacao.setDescricao(normalizeDescricao(request.getDescricao()));
        lotacao.setTiposChamado(resolverTipos(request.getTiposChamadoIds()));
        lotacaoRepository.save(lotacao);
    }

    private Set<TipoChamado> resolverTipos(List<String> tipoIds) {
        List<String> ids = tipoIds != null ? tipoIds : List.of();
        Set<TipoChamado> tipos = new LinkedHashSet<>();
        for (String id : ids) {
            if (id == null || id.isBlank()) {
                continue;
            }
            TipoChamado tipo = tipoChamadoRepository.findById(parseUuid(id, "ID de tipo de chamado invalido"))
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de chamado nao encontrado para lotacao"));
            tipos.add(tipo);
        }
        return tipos;
    }

    private LotacaoResponse mapLotacao(Lotacao lotacao) {
        LotacaoResponse response = new LotacaoResponse();
        response.setId(lotacao.getId().toString());
        response.setNome(lotacao.getNome());
        response.setDescricao(lotacao.getDescricao());
        response.setTiposChamado(
                lotacao.getTiposChamado().stream()
                        .sorted((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()))
                        .map(this::mapTipo)
                        .toList()
        );
        return response;
    }

    private TipoChamadoResponse mapTipo(TipoChamado tipo) {
        TipoChamadoResponse response = new TipoChamadoResponse();
        response.setId(tipo.getId().toString());
        response.setTitulo(tipo.getTitulo());
        response.setSlaHoras(tipo.getSlaHoras());
        return response;
    }

    private String normalizeDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return null;
        }
        return descricao.trim();
    }

    private UUID parseUuid(String value, String message) {
        try {
            return UUID.fromString(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(message);
        }
    }
}

package com.condominio.chamados.bloco.service;

import com.condominio.chamados.bloco.domain.Andar;
import com.condominio.chamados.bloco.domain.Bloco;
import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.dto.request.AndarRequest;
import com.condominio.chamados.bloco.dto.request.AndarUpdateRequest;
import com.condominio.chamados.bloco.dto.response.AndarResponse;
import com.condominio.chamados.bloco.dto.response.UnidadeResponse;
import com.condominio.chamados.bloco.repository.AndarRepository;
import com.condominio.chamados.bloco.repository.BlocoRepository;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AndarService {

    private final AndarRepository andarRepository;
    private final BlocoRepository blocoRepository;
    private final UnidadeRepository unidadeRepository;
    private final MoradiaRepository moradiaRepository;

    public AndarService(
            AndarRepository andarRepository,
            BlocoRepository blocoRepository,
            UnidadeRepository unidadeRepository,
            MoradiaRepository moradiaRepository
    ) {
        this.andarRepository = andarRepository;
        this.blocoRepository = blocoRepository;
        this.unidadeRepository = unidadeRepository;
        this.moradiaRepository = moradiaRepository;
    }

    @Transactional
    public List<AndarResponse> listarPorBloco(String blocoId) {
        UUID uuid = UUID.fromString(blocoId);
        return andarRepository.findByBloco_IdOrderByNumeroAsc(uuid).stream()
                .map(this::mapToResponseSemMoradias)
                .toList();
    }

    @Transactional
    public AndarResponse obterPorId(String id) {
        Andar andar = andarRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Andar não encontrado: " + id));
        return mapToResponseComUnidades(andar);
    }

    @Transactional
    public AndarResponse criar(AndarRequest request) {
        Bloco bloco = blocoRepository.findById(request.getBlocoId())
                .orElseThrow(() -> new IllegalArgumentException("Bloco não encontrado: " + request.getBlocoId()));

        andarRepository.findByBloco_IdAndNumero(bloco.getId(), request.getNumero())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Já existe um andar com o número " + request.getNumero() + " neste bloco");
                });

        Andar andar = new Andar();
        andar.setBloco(bloco);
        andar.setNumero(request.getNumero());
        andar.setAtivo(request.isAtivo());
        andar.setObservacoes(request.getObservacoes());

        Andar saved = andarRepository.save(andar);
        return mapToResponseComUnidades(saved);
    }

    @Transactional
    public AndarResponse atualizar(String id, AndarUpdateRequest request) {
        Andar andar = andarRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Andar não encontrado: " + id));

        if (request.getNumero() != null && !request.getNumero().equals(andar.getNumero())) {
            andarRepository.findByBloco_IdAndNumero(andar.getBloco().getId(), request.getNumero())
                    .ifPresent(existing -> {
                        throw new IllegalArgumentException("Já existe um andar com o número " + request.getNumero() + " neste bloco");
                    });
            andar.setNumero(request.getNumero());
        }

        andar.setAtivo(request.isAtivo());
        andar.setObservacoes(request.getObservacoes());

        return mapToResponseComUnidades(andarRepository.save(andar));
    }

    @Transactional
    public void deletar(String id) {
        UUID andarId = UUID.fromString(id);
        if (moradiaRepository.existsAnyByAndarId(andarId)) {
            throw new IllegalArgumentException("Não é permitido deletar um andar com moradias vinculadas");
        }

        Andar andar = andarRepository.findById(andarId)
                .orElseThrow(() -> new IllegalArgumentException("Andar não encontrado: " + id));
        andarRepository.delete(andar);
    }

    private AndarResponse mapToResponseSemMoradias(Andar andar) {
        AndarResponse response = new AndarResponse();
        response.setId(andar.getId().toString());
        response.setBlocoId(andar.getBloco().getId().toString());
        response.setBlocoIdentificacao(andar.getBloco().getIdentificacao());
        response.setNumero(andar.getNumero());
        response.setAtivo(andar.isAtivo());
        response.setObservacoes(andar.getObservacoes());
        return response;
    }

    private AndarResponse mapToResponseComUnidades(Andar andar) {
        AndarResponse response = mapToResponseSemMoradias(andar);
        response.setUnidades(unidadeRepository.findByAndar_IdOrderByApartamentoNumeroAsc(andar.getId()).stream()
                .map(this::mapUnidadeToResponse)
                .toList());
        return response;
    }

    private UnidadeResponse mapUnidadeToResponse(Unidade unidade) {
        UnidadeResponse response = new UnidadeResponse();
        response.setId(unidade.getId().toString());
        response.setAndarId(unidade.getAndar().getId().toString());
        response.setApartamentoNumero(unidade.getApartamentoNumero());
        response.setIdentificacao(unidade.getIdentificacao());
        response.setAtivo(unidade.isAtivo());
        response.setStatus(unidade.getStatus());
        response.setObservacoes(unidade.getObservacoes());
        return response;
    }
}

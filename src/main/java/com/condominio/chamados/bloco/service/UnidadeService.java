package com.condominio.chamados.bloco.service;

import com.condominio.chamados.bloco.domain.Andar;
import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.domain.UnidadeStatus;
import com.condominio.chamados.bloco.dto.request.UnidadeRequest;
import com.condominio.chamados.bloco.dto.request.UnidadeUpdateRequest;
import com.condominio.chamados.bloco.dto.response.MoradiaResponse;
import com.condominio.chamados.bloco.dto.response.UnidadeResponse;
import com.condominio.chamados.bloco.repository.AndarRepository;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final AndarRepository andarRepository;
    private final MoradiaRepository moradiaRepository;

    public UnidadeService(UnidadeRepository unidadeRepository, AndarRepository andarRepository, MoradiaRepository moradiaRepository) {
        this.unidadeRepository = unidadeRepository;
        this.andarRepository = andarRepository;
        this.moradiaRepository = moradiaRepository;
    }

    @Transactional
    public List<UnidadeResponse> listarPorAndar(String andarId) {
        UUID uuid = UUID.fromString(andarId);
        return unidadeRepository.findByAndar_IdOrderByApartamentoNumeroAsc(uuid).stream()
                .map(this::mapToResponseSemMoradias)
                .toList();
    }

    @Transactional
    public UnidadeResponse obterPorId(String id) {
        Unidade unidade = unidadeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada: " + id));
        return mapToResponseComMoradias(unidade);
    }

    @Transactional
    public UnidadeResponse criar(UnidadeRequest request) {
        Andar andar = andarRepository.findById(request.getAndarId())
                .orElseThrow(() -> new IllegalArgumentException("Andar não encontrado: " + request.getAndarId()));

        validarUnicidade(andar.getId(), request.getApartamentoNumero(), null);

        Unidade unidade = new Unidade();
        unidade.setAndar(andar);
        unidade.setApartamentoNumero(request.getApartamentoNumero());
        unidade.setStatus(request.getStatus());
        unidade.setAtivo(request.isAtivo());
        unidade.setObservacoes(request.getObservacoes());
        unidade.setIdentificacao(formatarIdentificacao(andar, request.getApartamentoNumero()));

        return mapToResponseSemMoradias(unidadeRepository.save(unidade));
    }

    @Transactional
    public UnidadeResponse atualizar(String id, UnidadeUpdateRequest request) {
        Unidade unidade = unidadeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada: " + id));

        if (request.getApartamentoNumero() != null && !request.getApartamentoNumero().equals(unidade.getApartamentoNumero())) {
            validarUnicidade(unidade.getAndar().getId(), request.getApartamentoNumero(), unidade.getId());
            unidade.setApartamentoNumero(request.getApartamentoNumero());
            unidade.setIdentificacao(formatarIdentificacao(unidade.getAndar(), request.getApartamentoNumero()));
        }

        if (request.getStatus() != null) {
            unidade.setStatus(request.getStatus());
        }
        unidade.setAtivo(request.isAtivo());
        unidade.setObservacoes(request.getObservacoes());

        return mapToResponseComMoradias(unidadeRepository.save(unidade));
    }

    @Transactional
    public void deletar(String id) {
        UUID unidadeId = UUID.fromString(id);
        if (moradiaRepository.existsAnyByUnidadeId(unidadeId)) {
            throw new IllegalArgumentException("Não é permitido deletar uma unidade com moradias vinculadas");
        }

        Unidade unidade = unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada: " + id));
        unidadeRepository.delete(unidade);
    }

    private void validarUnicidade(UUID andarId, Integer apartamentoNumero, UUID unidadeIdIgnorar) {
        unidadeRepository.findByAndar_IdAndApartamentoNumero(andarId, apartamentoNumero)
                .ifPresent(existing -> {
                    if (unidadeIdIgnorar == null || !existing.getId().equals(unidadeIdIgnorar)) {
                        throw new IllegalArgumentException("Já existe uma unidade com o apartamento " + apartamentoNumero + " neste andar");
                    }
                });
    }

    private String formatarIdentificacao(Andar andar, Integer apartamentoNumero) {
        return String.format("%s-%02d%02d", andar.getBloco().getIdentificacao(), andar.getNumero(), apartamentoNumero);
    }

    private UnidadeResponse mapToResponseSemMoradias(Unidade unidade) {
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

    private UnidadeResponse mapToResponseComMoradias(Unidade unidade) {
        UnidadeResponse response = mapToResponseSemMoradias(unidade);
        response.setMoradias(moradiaRepository.findByUnidade_IdOrderByDataInicioDesc(unidade.getId()).stream()
                .map(this::mapMoradiaToResponse)
                .toList());
        return response;
    }

    private MoradiaResponse mapMoradiaToResponse(com.condominio.chamados.bloco.domain.Moradia moradia) {
        MoradiaResponse response = new MoradiaResponse();
        response.setId(moradia.getId().toString());
        response.setUsuarioId(moradia.getUsuario().getId().toString());
        String usuarioNome = resolveUsuarioNome(moradia);
        response.setUsuarioNome(usuarioNome);
        response.setUsuarioAtualNome(usuarioNome);
        response.setUnidadeId(moradia.getUnidade().getId().toString());
        response.setStatus(moradia.getStatus());
        response.setDataInicio(moradia.getDataInicio());
        response.setDataFim(moradia.getDataFim());
        return response;
    }

    private String resolveUsuarioNome(com.condominio.chamados.bloco.domain.Moradia moradia) {
        return resolveUsuarioNome(moradia.getUsuario());
    }

    private String resolveUsuarioNome(com.condominio.chamados.security.domain.User usuario) {
        return usuario.getPerfil() != null
                && usuario.getPerfil().getNomeCompleto() != null
                && !usuario.getPerfil().getNomeCompleto().isBlank()
                ? usuario.getPerfil().getNomeCompleto()
                : usuario.getUsername();
    }
}

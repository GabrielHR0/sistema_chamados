package com.condominio.chamados.bloco.service;

import com.condominio.chamados.bloco.domain.Moradia;
import com.condominio.chamados.bloco.domain.MoradiaStatus;
import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.domain.UnidadeStatus;
import com.condominio.chamados.bloco.dto.request.MoradiaCreateRequest;
import com.condominio.chamados.bloco.dto.response.MoradiaResponse;
import com.condominio.chamados.bloco.dto.response.UnidadeSearchResponse;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.UnidadeRepository;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MoradiaService {

    private final MoradiaRepository moradiaRepository;
    private final UnidadeRepository unidadeRepository;
    private final UserRepository userRepository;

    public MoradiaService(
            MoradiaRepository moradiaRepository,
            UnidadeRepository unidadeRepository,
            UserRepository userRepository
    ) {
        this.moradiaRepository = moradiaRepository;
        this.unidadeRepository = unidadeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MoradiaResponse criar(String unidadeId, MoradiaCreateRequest request) {
        UUID unidadeUuid = UUID.fromString(unidadeId);
        Unidade unidade = unidadeRepository.findById(unidadeUuid)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada: " + unidadeId));

        User usuario = userRepository.findById(UUID.fromString(request.getUsuarioId()))
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado: " + request.getUsuarioId()));

        validarUnidade(unidade);
        validarHierarquia(unidade);
        validarUsuario(usuario);

        moradiaRepository.findFirstByUnidade_IdAndStatusOrderByDataInicioDesc(unidadeUuid, MoradiaStatus.ATIVA)
                .ifPresent(moradiaAtiva -> {
                    throw new IllegalArgumentException("A unidade já possui uma moradia ativa");
                });

        Moradia moradia = new Moradia();
        moradia.setUnidade(unidade);
        moradia.setUsuario(usuario);
        moradia.setStatus(MoradiaStatus.ATIVA);
        moradia.setDataInicio(LocalDate.now());

        return mapToResponse(moradiaRepository.save(moradia));
    }

    @Transactional
    public MoradiaResponse criar(MoradiaCreateRequest request) {
        return criar(request.getUnidadeId(), request);
    }

    @Transactional(readOnly = true)
    public List<MoradiaResponse> listarTodas() {
        return moradiaRepository.findAllWithUsuarioAndUnidadeOrderByDataInicioDesc().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MoradiaResponse> listarPorUsuario(String usuarioId) {
        UUID usuarioUuid = UUID.fromString(usuarioId);
        return moradiaRepository.findByUsuarioIdOrderByDataInicioDesc(usuarioUuid).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UnidadeSearchResponse> searchUnidadesByIdentificacao(String termo) {
        if (termo == null || termo.isBlank()) {
            return List.of();
        }

        String normalizedTerm = termo.trim();
        return unidadeRepository.searchByIdentificacao(normalizedTerm, PageRequest.of(0, 10)).stream()
                .map(unidade -> new UnidadeSearchResponse(unidade.getId().toString(), unidade.getIdentificacao()))
                .toList();
    }

    @Transactional
    public MoradiaResponse finalizar(String unidadeId, String moradiaId) {
        UUID moradiaUuid = UUID.fromString(moradiaId);
        UUID unidadeUuid = UUID.fromString(unidadeId);

        Moradia moradia = moradiaRepository.findById(moradiaUuid)
                .orElseThrow(() -> new IllegalArgumentException("Moradia não encontrada: " + moradiaId));

        if (moradia.getUnidade() == null || moradia.getUnidade().getId() == null || !moradia.getUnidade().getId().equals(unidadeUuid)) {
            throw new IllegalArgumentException("A moradia não pertence à unidade informada");
        }
        if (moradia.getStatus() != MoradiaStatus.ATIVA) {
            throw new IllegalArgumentException("A moradia já está finalizada");
        }

        moradia.setStatus(MoradiaStatus.ENCERRADA);
        moradia.setDataFim(LocalDate.now());
        return mapToResponse(moradiaRepository.save(moradia));
    }

    private void validarUnidade(Unidade unidade) {
        if (!unidade.isAtivo()) {
            throw new IllegalArgumentException("A unidade está inativa");
        }
        if (unidade.getStatus() != UnidadeStatus.EM_FUNCIONAMENTO) {
            throw new IllegalArgumentException("A unidade precisa estar em funcionamento");
        }
    }

    private void validarHierarquia(Unidade unidade) {
        if (!unidade.getAndar().isAtivo()) {
            throw new IllegalArgumentException("O andar está inativo");
        }
        if (!unidade.getAndar().getBloco().isAtivo()) {
            throw new IllegalArgumentException("O bloco está inativo");
        }
    }

    private void validarUsuario(User usuario) {
        if (!usuario.isEnabled()) {
            throw new IllegalArgumentException("O usuário está desativado");
        }
        boolean isMorador = usuario.getRoles().stream()
                .anyMatch(role -> "MORADOR".equals(role.getName()));
        if (!isMorador) {
            throw new IllegalArgumentException("O usuário precisa ter o cargo MORADOR");
        }
    }

    private MoradiaResponse mapToResponse(Moradia moradia) {
        MoradiaResponse response = new MoradiaResponse();
        response.setId(moradia.getId().toString());
        response.setUsuarioId(moradia.getUsuario().getId().toString());
        response.setUsuarioNome(resolveUsuarioNome(moradia.getUsuario()));
        response.setUnidadeId(moradia.getUnidade().getId().toString());
        response.setUnidadeIdentificacao(moradia.getUnidade().getIdentificacao());
        response.setStatus(moradia.getStatus());
        response.setDataInicio(moradia.getDataInicio());
        response.setDataFim(moradia.getDataFim());
        return response;
    }

    private String resolveUsuarioNome(User usuario) {
        return usuario.getPerfil() != null
                && usuario.getPerfil().getNomeCompleto() != null
                && !usuario.getPerfil().getNomeCompleto().isBlank()
                ? usuario.getPerfil().getNomeCompleto()
                : usuario.getUsername();
    }
}

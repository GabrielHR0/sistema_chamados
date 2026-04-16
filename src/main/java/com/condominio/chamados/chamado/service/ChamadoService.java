package com.condominio.chamados.chamado.service;

import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.domain.MoradiaStatus;
import com.condominio.chamados.bloco.dto.response.MoradiaResponse;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.UnidadeRepository;
import com.condominio.chamados.chamado.domain.Chamado;
import com.condominio.chamados.chamado.domain.ChamadoAnexo;
import com.condominio.chamados.chamado.domain.ComentarioAnexo;
import com.condominio.chamados.chamado.domain.ComentarioChamado;
import com.condominio.chamados.chamado.domain.StatusChamado;
import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;
import com.condominio.chamados.chamado.domain.TipoChamado;
import com.condominio.chamados.chamado.dto.request.ChamadoCreateRequest;
import com.condominio.chamados.chamado.dto.response.ChamadoAnexoResponse;
import com.condominio.chamados.chamado.dto.request.StatusChamadoCreateRequest;
import com.condominio.chamados.chamado.dto.request.TipoChamadoCreateRequest;
import com.condominio.chamados.chamado.dto.response.ChamadoDetalheResponse;
import com.condominio.chamados.chamado.dto.response.ChamadoResumoResponse;
import com.condominio.chamados.chamado.dto.response.ComentarioAnexoResponse;
import com.condominio.chamados.chamado.dto.response.ComentarioChamadoResponse;
import com.condominio.chamados.chamado.dto.response.StatusOpcaoResponse;
import com.condominio.chamados.chamado.dto.response.StatusChamadoResponse;
import com.condominio.chamados.chamado.dto.response.TipoChamadoResponse;
import com.condominio.chamados.chamado.repository.ChamadoAnexoRepository;
import com.condominio.chamados.chamado.repository.ChamadoRepository;
import com.condominio.chamados.chamado.repository.ComentarioAnexoRepository;
import com.condominio.chamados.chamado.repository.ComentarioChamadoRepository;
import com.condominio.chamados.chamado.repository.StatusChamadoRepository;
import com.condominio.chamados.chamado.repository.TipoChamadoRepository;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.repository.UserRepository;
import com.condominio.chamados.shared.upload.UploadStorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChamadoService {
    private static final DateTimeFormatter DATA_HORA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ChamadoRepository chamadoRepository;
    private final ChamadoAnexoRepository chamadoAnexoRepository;
    private final ComentarioAnexoRepository comentarioAnexoRepository;
    private final TipoChamadoRepository tipoChamadoRepository;
    private final StatusChamadoRepository statusChamadoRepository;
    private final ComentarioChamadoRepository comentarioChamadoRepository;
    private final UnidadeRepository unidadeRepository;
    private final MoradiaRepository moradiaRepository;
    private final UserRepository userRepository;
    private final UploadStorageService uploadStorageService;

    public ChamadoService(
            ChamadoRepository chamadoRepository,
            ChamadoAnexoRepository chamadoAnexoRepository,
            ComentarioAnexoRepository comentarioAnexoRepository,
            TipoChamadoRepository tipoChamadoRepository,
            StatusChamadoRepository statusChamadoRepository,
            ComentarioChamadoRepository comentarioChamadoRepository,
            UnidadeRepository unidadeRepository,
            MoradiaRepository moradiaRepository,
            UserRepository userRepository,
            UploadStorageService uploadStorageService
    ) {
        this.chamadoRepository = chamadoRepository;
        this.chamadoAnexoRepository = chamadoAnexoRepository;
        this.comentarioAnexoRepository = comentarioAnexoRepository;
        this.tipoChamadoRepository = tipoChamadoRepository;
        this.statusChamadoRepository = statusChamadoRepository;
        this.comentarioChamadoRepository = comentarioChamadoRepository;
        this.unidadeRepository = unidadeRepository;
        this.moradiaRepository = moradiaRepository;
        this.userRepository = userRepository;
        this.uploadStorageService = uploadStorageService;
    }

    @Transactional(readOnly = true)
    public List<ChamadoResumoResponse> listarChamados(
            String userId,
            boolean isAdmin,
            String tipoId,
            String statusId,
            String unidadeId,
            String responsavelId
    ) {
        UUID tipoUuid = tryParseUuid(tipoId);
        UUID statusUuid = tryParseUuid(statusId);
        UUID unidadeUuid = tryParseUuid(unidadeId, "ID de unidade invalido");
        UUID responsavelUuid = tryParseUuid(responsavelId, "ID de responsavel invalido");
        List<Chamado> chamados = isAdmin
                ? chamadoRepository.findAllWithRelationsOrderByDataCriacaoDesc(tipoUuid, statusUuid, unidadeUuid, responsavelUuid)
                : chamadoRepository.findScopedByUserWithRelationsOrderByDataCriacaoDesc(
                        parseUuid(userId, "ID de usuario invalido"),
                        tipoUuid,
                        statusUuid,
                        unidadeUuid,
                        responsavelUuid
                );

        return chamados.stream()
                .map(this::mapChamadoResumo)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChamadoResumoResponse> listarChamadosMorador(String userId) {
        UUID userUuid = parseUuid(userId, "ID de usuario invalido");
        return chamadoRepository.findBySolicitanteWithRelationsOrderByDataCriacaoDesc(userUuid)
                .stream()
                .map(this::mapChamadoResumo)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChamadoResumoResponse> listarChamadosDisponiveisParaLotacao(String userId, boolean isAdmin, String tipoId, String statusId) {
        UUID tipoUuid = tryParseUuid(tipoId);
        UUID statusUuid = tryParseUuid(statusId);
        UUID userUuid = parseUuid(userId, "ID de usuario invalido");
        List<Chamado> chamados = isAdmin
                ? chamadoRepository.findDisponiveisWithRelationsOrderByDataCriacaoDesc(tipoUuid, statusUuid)
                : chamadoRepository.findDisponiveisByUserLotacaoWithRelationsOrderByDataCriacaoDesc(
                        userUuid,
                        tipoUuid,
                        statusUuid
                );
        return chamados.stream()
                .map(this::mapChamadoResumo)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChamadoResumoResponse> listarChamadosNaoFinaisDoResponsavel(String userId) {
        UUID userUuid = parseUuid(userId, "ID de usuario invalido");
        return chamadoRepository.findNaoFinaisByResponsavelWithRelationsOrderByDataCriacaoDesc(
                        userUuid,
                        StatusComportamentoTipo.FINAL
                ).stream()
                .filter(chamado -> chamado.getStatus() != null
                        && chamado.getStatus().getComportamentoTipo() != StatusComportamentoTipo.FINAL)
                .map(this::mapChamadoResumo)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChamadoDetalheResponse obterDetalhes(String chamadoId, String userId, boolean isAdmin) {
        UUID id = parseUuid(chamadoId, "ID de chamado invalido");
        Chamado chamado = obterChamadoComEscopo(id, userId, isAdmin);
        List<ComentarioChamadoResponse> comentarios = comentarioChamadoRepository
                .findByChamadoIdWithAutorOrderByDataCriacaoAsc(id)
                .stream()
                .map(this::mapComentario)
                .toList();
        List<ChamadoAnexoResponse> anexos = chamadoAnexoRepository
                .findByChamado_IdOrderByDataCriacaoDesc(id)
                .stream()
                .map(this::mapAnexo)
                .toList();

        ChamadoDetalheResponse response = new ChamadoDetalheResponse();
        response.setId(chamado.getId().toString());
        response.setCodigo(chamado.getCodigo());
        response.setUnidadeIdentificacao(chamado.getUnidade().getIdentificacao());
        response.setTipoTitulo(chamado.getTipo().getTitulo());
        response.setTipoSlaHoras(chamado.getTipo().getSlaHoras());
        response.setStatusId(chamado.getStatus().getId().toString());
        response.setStatusNome(chamado.getStatus().getNome());
        response.setStatusFinal(chamado.getStatus().getComportamentoTipo() == StatusComportamentoTipo.FINAL);
        response.setColaboradorResponsavelNome(resolveUserDisplayName(chamado.getColaboradorResponsavel()));
        response.setDescricao(chamado.getDescricao());
        response.setDataCriacao(chamado.getDataCriacao());
        response.setDataFinalizacao(chamado.getDataFinalizacao());
        response.setDataCriacaoFormatada(chamado.getDataCriacao() != null ? chamado.getDataCriacao().format(DATA_HORA_BR) : "-");
        response.setDataFinalizacaoFormatada(chamado.getDataFinalizacao() != null ? chamado.getDataFinalizacao().format(DATA_HORA_BR) : "-");
        response.setComentarios(comentarios);
        response.setAnexos(anexos);
        response.setProximosStatus(
                chamado.getStatus().getProximos().stream()
                        .sorted((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()))
                        .map(status -> {
                            StatusOpcaoResponse opcao = new StatusOpcaoResponse();
                            opcao.setId(status.getId().toString());
                            opcao.setNome(status.getNome());
                            return opcao;
                        })
                        .toList()
        );
        return response;
    }

    @Transactional
    public String criarChamado(ChamadoCreateRequest request, String userId, boolean isAdmin) {
        return criarChamado(request, userId, isAdmin, null);
    }

    @Transactional
    public String criarChamado(ChamadoCreateRequest request, String userId, boolean isAdmin, MultipartFile arquivo) {
        User actor = carregarUsuarioEscopo(userId);
        Unidade unidade = unidadeRepository.findById(parseUuid(request.getUnidadeId(), "Unidade invalida"))
                .orElseThrow(() -> new IllegalArgumentException("Unidade nao encontrada"));
        TipoChamado tipo = tipoChamadoRepository.findById(parseUuid(request.getTipoId(), "Tipo de chamado invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Tipo de chamado nao encontrado"));
        StatusChamado statusInicial = tipo.getStatusInicial();

        validarTipoNoEscopo(actor, tipo.getId(), isAdmin);
        validarUnidadeNoEscopo(actor, unidade.getId(), isAdmin);
        if (statusInicial == null || statusInicial.getComportamentoTipo() != StatusComportamentoTipo.INICIAL) {
            throw new IllegalArgumentException("Tipo de chamado sem status inicial valido");
        }

        Chamado chamado = new Chamado();
        chamado.setUnidade(unidade);
        chamado.setTipo(tipo);
        chamado.setStatus(statusInicial);
        chamado.setSolicitante(actor);
        chamado.setColaboradorResponsavel(resolverResponsavelOpcional(request.getResponsavelId()));
        chamado.setDescricao(request.getDescricao().trim());

        Chamado salvo = chamadoRepository.save(chamado);
        if (arquivo != null && !arquivo.isEmpty()) {
            UploadStorageService.StoredUpload stored = uploadStorageService.store(arquivo);
            try {
                ChamadoAnexo anexo = new ChamadoAnexo();
                anexo.setChamado(salvo);
                anexo.setNomeOriginal(stored.originalName());
                anexo.setNomeArquivo(stored.storedName());
                anexo.setContentType(stored.contentType());
                anexo.setTamanhoBytes(stored.size());
                chamadoAnexoRepository.save(anexo);
            } catch (RuntimeException ex) {
                uploadStorageService.delete(stored.storedName());
                throw ex;
            }
        }

        return salvo.getId().toString();
    }

    @Transactional
    public void assumirResponsabilidade(String chamadoId, String userId, boolean isAdmin) {
        User colaborador = carregarUsuarioEscopo(userId);
        Chamado chamado = obterChamadoComEscopo(parseUuid(chamadoId, "ID de chamado invalido"), userId, isAdmin);

        boolean isColaborador = colaborador.getRoles().stream().anyMatch(role -> "COLABORADOR".equals(role.getName()));
        if (!isColaborador) {
            throw new IllegalArgumentException("Apenas usuario com cargo COLABORADOR pode assumir chamado");
        }

        chamado.setColaboradorResponsavel(colaborador);
        chamadoRepository.save(chamado);
    }

    @Transactional
    public void assumirChamadoDaLotacao(String chamadoId, String userId, boolean isAdmin) {
        UUID chamadoUuid = parseUuid(chamadoId, "ID de chamado invalido");
        UUID userUuid = parseUuid(userId, "ID de usuario invalido");
        User atendente = carregarUsuarioEscopo(userId);

        boolean isColaborador = atendente.getRoles().stream().anyMatch(role -> "COLABORADOR".equals(role.getName()));
        boolean isAdminRole = atendente.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()));
        if (!isColaborador && !isAdminRole) {
            throw new IllegalArgumentException("Apenas usuario com cargo COLABORADOR ou ADMIN pode assumir chamado");
        }

        Chamado chamado = isAdmin
                ? chamadoRepository.findByIdWithRelations(chamadoUuid)
                .orElseThrow(() -> new IllegalArgumentException("Chamado nao encontrado"))
                : chamadoRepository.findByIdDisponivelScopedByUserLotacao(chamadoUuid, userUuid)
                .orElseThrow(() -> new IllegalArgumentException("Chamado indisponivel para lotacao do colaborador"));

        if (chamado.getColaboradorResponsavel() != null) {
            throw new IllegalArgumentException("Chamado ja possui responsavel");
        }

        chamado.setColaboradorResponsavel(atendente);
        chamadoRepository.save(chamado);
    }

    @Transactional
    public void adicionarComentario(String chamadoId, String autorId, String comentarioTexto, MultipartFile arquivo, boolean isAdmin) {
        Chamado chamado = obterChamadoComEscopo(parseUuid(chamadoId, "ID de chamado invalido"), autorId, isAdmin);
        User autor = carregarUsuarioEscopo(autorId);

        ComentarioChamado comentario = new ComentarioChamado();
        comentario.setChamado(chamado);
        comentario.setAutor(autor);
        comentario.setComentario(comentarioTexto.trim());
        comentario.setDataCriacao(OffsetDateTime.now());
        comentarioChamadoRepository.save(comentario);

        if (arquivo == null || arquivo.isEmpty()) {
            return;
        }

        UploadStorageService.StoredUpload stored = uploadStorageService.store(arquivo);
        try {
            ComentarioAnexo anexo = new ComentarioAnexo();
            anexo.setComentario(comentario);
            anexo.setNomeOriginal(stored.originalName());
            anexo.setNomeArquivo(stored.storedName());
            anexo.setContentType(stored.contentType());
            anexo.setTamanhoBytes(stored.size());
            comentarioAnexoRepository.save(anexo);
        } catch (RuntimeException ex) {
            uploadStorageService.delete(stored.storedName());
            throw ex;
        }
    }

    @Transactional
    public void adicionarAnexoComentario(String chamadoId, String comentarioId, String userId, MultipartFile arquivo, boolean isAdmin) {
        UUID chamadoUuid = parseUuid(chamadoId, "ID de chamado invalido");
        obterChamadoComEscopo(chamadoUuid, userId, isAdmin);

        ComentarioChamado comentario = comentarioChamadoRepository.findByIdWithChamado(parseUuid(comentarioId, "ID de comentario invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Comentario nao encontrado"));
        if (!comentario.getChamado().getId().equals(chamadoUuid)) {
            throw new IllegalArgumentException("Comentario nao pertence ao chamado informado");
        }

        UploadStorageService.StoredUpload stored = uploadStorageService.store(arquivo);
        try {
            ComentarioAnexo anexo = new ComentarioAnexo();
            anexo.setComentario(comentario);
            anexo.setNomeOriginal(stored.originalName());
            anexo.setNomeArquivo(stored.storedName());
            anexo.setContentType(stored.contentType());
            anexo.setTamanhoBytes(stored.size());
            comentarioAnexoRepository.save(anexo);
        } catch (RuntimeException ex) {
            uploadStorageService.delete(stored.storedName());
            throw ex;
        }
    }

    @Transactional
    public void adicionarAnexo(String chamadoId, String userId, MultipartFile arquivo, boolean isAdmin) {
        UUID chamadoUuid = parseUuid(chamadoId, "ID de chamado invalido");
        Chamado chamado = obterChamadoComEscopo(chamadoUuid, userId, isAdmin);
        UploadStorageService.StoredUpload stored = uploadStorageService.store(arquivo);

        try {
            ChamadoAnexo anexo = new ChamadoAnexo();
            anexo.setChamado(chamado);
            anexo.setNomeOriginal(stored.originalName());
            anexo.setNomeArquivo(stored.storedName());
            anexo.setContentType(stored.contentType());
            anexo.setTamanhoBytes(stored.size());
            chamadoAnexoRepository.save(anexo);
        } catch (RuntimeException ex) {
            uploadStorageService.delete(stored.storedName());
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public AnexoDownload obterAnexoParaDownload(String anexoId, String userId, boolean isAdmin) {
        ChamadoAnexo anexo = chamadoAnexoRepository
                .findByIdWithChamado(parseUuid(anexoId, "ID de anexo invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Anexo nao encontrado"));

        UUID chamadoId = anexo.getChamado().getId();
        obterChamadoComEscopo(chamadoId, userId, isAdmin);

        Resource resource = uploadStorageService.loadAsResource(anexo.getNomeArquivo());
        return new AnexoDownload(resource, anexo.getNomeOriginal(), anexo.getContentType(), anexo.getTamanhoBytes());
    }

    @Transactional(readOnly = true)
    public AnexoDownload obterAnexoComentarioParaDownload(String anexoId, String userId, boolean isAdmin) {
        ComentarioAnexo anexo = comentarioAnexoRepository
                .findByIdWithComentarioAndChamado(parseUuid(anexoId, "ID de anexo invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Anexo de comentario nao encontrado"));

        UUID chamadoId = anexo.getComentario().getChamado().getId();
        obterChamadoComEscopo(chamadoId, userId, isAdmin);

        Resource resource = uploadStorageService.loadAsResource(anexo.getNomeArquivo());
        return new AnexoDownload(resource, anexo.getNomeOriginal(), anexo.getContentType(), anexo.getTamanhoBytes());
    }

    @Transactional
    public void atualizarStatus(String chamadoId, String novoStatusId, String userId, boolean isAdmin) {
        UUID chamadoUuid = parseUuid(chamadoId, "ID de chamado invalido");
        Chamado chamado = obterChamadoComEscopo(chamadoUuid, userId, isAdmin);
        StatusChamado novoStatus = statusChamadoRepository.findById(parseUuid(novoStatusId, "Status invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Status nao encontrado"));

        chamado.getStatus().validarProximo(novoStatus);
        chamado.setStatus(novoStatus);

        if (novoStatus.getComportamentoTipo() == StatusComportamentoTipo.FINAL) {
            chamado.setDataFinalizacao(OffsetDateTime.now());
        }

        chamadoRepository.save(chamado);
    }

    @Transactional(readOnly = true)
    public List<TipoChamadoResponse> listarTipos(String userId, boolean isAdmin) {
        List<TipoChamado> tipos;
        if (isAdmin) {
            tipos = tipoChamadoRepository.findAllByOrderByTituloAsc();
        } else {
            User colaborador = carregarUsuarioEscopo(userId);
            tipos = colaborador.getTiposChamadoPermitidos().stream()
                    .sorted((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()))
                    .toList();
        }
        return tipos.stream()
                .map(tipo -> {
                    TipoChamadoResponse response = new TipoChamadoResponse();
                    response.setId(tipo.getId().toString());
                    response.setTitulo(tipo.getTitulo());
                    response.setSlaHoras(tipo.getSlaHoras());
                    response.setStatusInicialId(tipo.getStatusInicial() != null ? tipo.getStatusInicial().getId().toString() : null);
                    response.setStatusInicialNome(tipo.getStatusInicial() != null ? tipo.getStatusInicial().getNome() : null);
                    return response;
                })
                .toList();
    }

    @Transactional
    public void criarTipo(TipoChamadoCreateRequest request) {
        tipoChamadoRepository.findByTitulo(request.getTitulo().trim()).ifPresent(existing -> {
            throw new IllegalArgumentException("Ja existe tipo de chamado com este titulo");
        });

        TipoChamado tipoChamado = new TipoChamado();
        tipoChamado.setTitulo(request.getTitulo().trim());
        tipoChamado.setSlaHoras(request.getSlaHoras());
        tipoChamado.setStatusInicial(
                statusChamadoRepository.findById(parseUuid(request.getStatusInicialId(), "Status inicial invalido"))
                        .orElseThrow(() -> new IllegalArgumentException("Status inicial nao encontrado"))
        );
        if (tipoChamado.getStatusInicial().getComportamentoTipo() != StatusComportamentoTipo.INICIAL) {
            throw new IllegalArgumentException("Status inicial do tipo deve possuir comportamento INICIAL");
        }
        tipoChamadoRepository.save(tipoChamado);
    }

    @Transactional(readOnly = true)
    public TipoChamadoResponse obterTipoPorId(String tipoId) {
        TipoChamado tipo = tipoChamadoRepository.findById(parseUuid(tipoId, "ID de tipo invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Tipo de chamado nao encontrado"));
        TipoChamadoResponse response = new TipoChamadoResponse();
        response.setId(tipo.getId().toString());
        response.setTitulo(tipo.getTitulo());
        response.setSlaHoras(tipo.getSlaHoras());
        response.setStatusInicialId(tipo.getStatusInicial() != null ? tipo.getStatusInicial().getId().toString() : null);
        response.setStatusInicialNome(tipo.getStatusInicial() != null ? tipo.getStatusInicial().getNome() : null);
        return response;
    }

    @Transactional
    public void editarTipo(String tipoId, TipoChamadoCreateRequest request) {
        UUID id = parseUuid(tipoId, "ID de tipo invalido");
        TipoChamado tipo = tipoChamadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de chamado nao encontrado"));
        String novoTitulo = request.getTitulo().trim();

        tipoChamadoRepository.findByTitulo(novoTitulo).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Ja existe tipo de chamado com este titulo");
            }
        });

        tipo.setTitulo(novoTitulo);
        tipo.setSlaHoras(request.getSlaHoras());
        StatusChamado statusInicial = statusChamadoRepository
                .findById(parseUuid(request.getStatusInicialId(), "Status inicial invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Status inicial nao encontrado"));
        if (statusInicial.getComportamentoTipo() != StatusComportamentoTipo.INICIAL) {
            throw new IllegalArgumentException("Status inicial do tipo deve possuir comportamento INICIAL");
        }
        tipo.setStatusInicial(statusInicial);
        tipoChamadoRepository.save(tipo);
    }

    @Transactional
    public void deletarTipo(String tipoId) {
        UUID id = parseUuid(tipoId, "ID de tipo invalido");
        if (chamadoRepository.existsByTipo_Id(id)) {
            throw new IllegalArgumentException("Nao e possivel remover tipo com chamados vinculados");
        }
        TipoChamado tipo = tipoChamadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de chamado nao encontrado"));
        tipoChamadoRepository.delete(tipo);
    }

    @Transactional(readOnly = true)
    public List<StatusChamadoResponse> listarStatus() {
        return statusChamadoRepository.findAllByOrderByNomeAsc().stream()
                .map(status -> {
                    StatusChamadoResponse response = new StatusChamadoResponse();
                    response.setId(status.getId().toString());
                    response.setNome(status.getNome());
                    response.setComportamentoTipo(status.getComportamentoTipo());
                    response.setProximosNomes(
                            status.getProximos().stream()
                                    .map(StatusChamado::getNome)
                                    .sorted()
                                    .toList()
                    );
                    return response;
                })
                .toList();
    }

    @Transactional
    public void criarStatus(StatusChamadoCreateRequest request) {
        statusChamadoRepository.findByNome(request.getNome().trim()).ifPresent(existing -> {
            throw new IllegalArgumentException("Ja existe status com este nome");
        });

        try {
            StatusChamado status = new StatusChamado();
            status.setNome(request.getNome().trim());
            status.setComportamentoTipo(request.getComportamentoTipo());
            resolverProximos(request.getProximosIds(), status);
            statusChamadoRepository.save(status);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public StatusChamadoResponse obterStatusPorId(String statusId) {
        StatusChamado status = statusChamadoRepository.findById(parseUuid(statusId, "ID de status invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Status nao encontrado"));
        StatusChamadoResponse response = new StatusChamadoResponse();
        response.setId(status.getId().toString());
        response.setNome(status.getNome());
        response.setComportamentoTipo(status.getComportamentoTipo());
        response.setProximosNomes(
                status.getProximos().stream()
                        .map(s -> s.getId().toString())
                        .toList()
        );
        return response;
    }

    @Transactional
    public void editarStatus(String statusId, StatusChamadoCreateRequest request) {
        UUID id = parseUuid(statusId, "ID de status invalido");
        StatusChamado status = statusChamadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status nao encontrado"));
        String novoNome = request.getNome().trim();

        statusChamadoRepository.findByNome(novoNome).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("Ja existe status com este nome");
            }
        });

        try {
            status.setNome(novoNome);
            status.setComportamentoTipo(request.getComportamentoTipo());
            resolverProximos(request.getProximosIds(), status);
            statusChamadoRepository.save(status);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Transactional
    public void deletarStatus(String statusId) {
        UUID id = parseUuid(statusId, "ID de status invalido");
        if (chamadoRepository.existsByStatus_Id(id)) {
            throw new IllegalArgumentException("Nao e possivel remover status com chamados vinculados");
        }
        StatusChamado status = statusChamadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status nao encontrado"));
        statusChamadoRepository.delete(status);
    }

    @Transactional(readOnly = true)
    public List<Unidade> listarUnidadesParaChamado() {
        return unidadeRepository.findAllByOrderByIdentificacaoAsc();
    }

    @Transactional(readOnly = true)
    public List<Unidade> listarUnidadesAtivasMorador(String userId) {
        UUID userUuid = parseUuid(userId, "ID de usuario invalido");
        return moradiaRepository.findUnidadesByUsuarioIdAndStatus(userUuid, MoradiaStatus.ATIVA);
    }

    @Transactional(readOnly = true)
    public List<MoradiaResponse> listarMoradiasMorador(String userId) {
        UUID userUuid = parseUuid(userId, "ID de usuario invalido");
        return moradiaRepository.findByUsuarioIdOrderByDataInicioDesc(userUuid).stream()
                .map(moradia -> {
                    MoradiaResponse response = new MoradiaResponse();
                    response.setId(moradia.getId().toString());
                    response.setUsuarioId(moradia.getUsuario().getId().toString());
                    response.setUnidadeId(moradia.getUnidade().getId().toString());
                    response.setUnidadeIdentificacao(moradia.getUnidade().getIdentificacao());
                    response.setStatus(moradia.getStatus());
                    response.setDataInicio(moradia.getDataInicio());
                    response.setDataFim(moradia.getDataFim());
                    return response;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<User> listarColaboradoresAtivos() {
        return userRepository.findEnabledCollaborators();
    }

    @Transactional(readOnly = true)
    public List<StatusChamado> listarStatusEntidade() {
        return statusChamadoRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<StatusChamadoResponse> listarStatusParaFiltro() {
        return statusChamadoRepository.findAllByOrderByNomeAsc().stream()
                .map(status -> {
                    StatusChamadoResponse response = new StatusChamadoResponse();
                    response.setId(status.getId().toString());
                    response.setNome(status.getNome());
                    response.setComportamentoTipo(status.getComportamentoTipo());
                    return response;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StatusChamadoResponse> listarStatusIniciais() {
        return statusChamadoRepository.findAllByOrderByNomeAsc().stream()
                .filter(status -> status.getComportamentoTipo() == StatusComportamentoTipo.INICIAL)
                .map(status -> {
                    StatusChamadoResponse response = new StatusChamadoResponse();
                    response.setId(status.getId().toString());
                    response.setNome(status.getNome());
                    response.setComportamentoTipo(status.getComportamentoTipo());
                    return response;
                })
                .toList();
    }

    private ChamadoResumoResponse mapChamadoResumo(Chamado chamado) {
        ChamadoResumoResponse response = new ChamadoResumoResponse();
        response.setId(chamado.getId().toString());
        response.setUnidadeIdentificacao(chamado.getUnidade().getIdentificacao());
        response.setTipoTitulo(chamado.getTipo().getTitulo());
        response.setStatusNome(chamado.getStatus().getNome());
        response.setStatusFinal(chamado.getStatus().getComportamentoTipo() == StatusComportamentoTipo.FINAL);
        response.setColaboradorResponsavelNome(resolveUserDisplayName(chamado.getColaboradorResponsavel()));
        response.setDataCriacao(chamado.getDataCriacao());
        response.setDataCriacaoFormatada(chamado.getDataCriacao() != null ? chamado.getDataCriacao().format(DATA_HORA_BR) : "-");
        return response;
    }

    private ComentarioChamadoResponse mapComentario(ComentarioChamado comentario) {
        ComentarioChamadoResponse response = new ComentarioChamadoResponse();
        response.setId(comentario.getId().toString());
        response.setAutorNome(resolveUserDisplayName(comentario.getAutor()));
        response.setComentario(comentario.getComentario());
        response.setDataCriacao(comentario.getDataCriacao());
        response.setDataCriacaoFormatada(comentario.getDataCriacao() != null ? comentario.getDataCriacao().format(DATA_HORA_BR) : "-");
        response.setAnexos(
                comentario.getAnexos().stream()
                        .sorted((a, b) -> b.getDataCriacao().compareTo(a.getDataCriacao()))
                        .map(this::mapComentarioAnexo)
                        .toList()
        );
        return response;
    }

    private ChamadoAnexoResponse mapAnexo(ChamadoAnexo anexo) {
        ChamadoAnexoResponse response = new ChamadoAnexoResponse();
        response.setId(anexo.getId().toString());
        response.setNomeOriginal(anexo.getNomeOriginal());
        response.setContentType(anexo.getContentType());
        response.setTamanhoBytes(anexo.getTamanhoBytes());
        response.setDataCriacao(anexo.getDataCriacao());
        response.setDataCriacaoFormatada(anexo.getDataCriacao() != null ? anexo.getDataCriacao().format(DATA_HORA_BR) : "-");
        return response;
    }

    private ComentarioAnexoResponse mapComentarioAnexo(ComentarioAnexo anexo) {
        ComentarioAnexoResponse response = new ComentarioAnexoResponse();
        response.setId(anexo.getId().toString());
        response.setNomeOriginal(anexo.getNomeOriginal());
        response.setContentType(anexo.getContentType());
        response.setTamanhoBytes(anexo.getTamanhoBytes());
        response.setDataCriacao(anexo.getDataCriacao());
        response.setDataCriacaoFormatada(anexo.getDataCriacao() != null ? anexo.getDataCriacao().format(DATA_HORA_BR) : "-");
        return response;
    }

    private String resolveUserDisplayName(User user) {
        if (user == null) {
            return null;
        }
        if (user.getPerfil() != null && user.getPerfil().getNomeCompleto() != null && !user.getPerfil().getNomeCompleto().isBlank()) {
            return user.getPerfil().getNomeCompleto();
        }
        return user.getUsername();
    }

    private UUID parseUuid(String value, String message) {
        try {
            return UUID.fromString(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private UUID tryParseUuid(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return parseUuid(value, "UUID invalido");
    }

    private UUID tryParseUuid(String value, String message) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return parseUuid(value, message);
    }

    private User carregarUsuarioEscopo(String userId) {
        return userRepository.findByIdWithRolesAndTipos(parseUuid(userId, "ID de usuario invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
    }

    private void validarTipoNoEscopo(User actor, UUID tipoId, boolean isAdmin) {
        if (isAdmin) {
            return;
        }
        if (hasRole(actor, "MORADOR")) {
            return;
        }
        Set<UUID> tiposPermitidos = actor.getTiposChamadoPermitidos().stream()
                .map(TipoChamado::getId)
                .collect(Collectors.toCollection(HashSet::new));
        if (!tiposPermitidos.contains(tipoId)) {
            throw new IllegalArgumentException("Tipo de chamado fora do escopo do colaborador");
        }
    }

    private void validarUnidadeNoEscopo(User actor, UUID unidadeId, boolean isAdmin) {
        if (isAdmin || !hasRole(actor, "MORADOR")) {
            return;
        }
        boolean pertenceAoMorador = moradiaRepository
                .findUnidadesByUsuarioIdAndStatus(actor.getId(), MoradiaStatus.ATIVA)
                .stream()
                .anyMatch(unidade -> unidade.getId().equals(unidadeId));
        if (!pertenceAoMorador) {
            throw new IllegalArgumentException("Unidade fora do escopo do morador");
        }
    }

    private Chamado obterChamadoComEscopo(UUID chamadoId, String userId, boolean isAdmin) {
        if (isAdmin) {
            return chamadoRepository.findByIdWithRelations(chamadoId)
                    .orElseThrow(() -> new IllegalArgumentException("Chamado nao encontrado"));
        }
        User actor = carregarUsuarioEscopo(userId);
        if (hasRole(actor, "MORADOR")) {
            return chamadoRepository.findByIdWithRelationsScopedByMorador(chamadoId, actor.getId(), MoradiaStatus.ATIVA)
                    .orElseThrow(() -> new IllegalArgumentException("Chamado nao encontrado no escopo do morador"));
        }
        return chamadoRepository.findByIdWithRelationsScopedByUser(chamadoId, actor.getId())
                .orElseThrow(() -> new IllegalArgumentException("Chamado nao encontrado no escopo do colaborador"));
    }

    private boolean hasRole(User actor, String roleName) {
        return actor.getRoles().stream().anyMatch(role -> roleName.equals(role.getName()));
    }

    private Set<StatusChamado> resolverProximos(List<String> proximosIds, StatusChamado statusAtual) {
        List<String> ids = proximosIds != null ? proximosIds : List.of();
        Set<StatusChamado> proximos = ids.stream()
                .filter(id -> id != null && !id.isBlank())
                .map(id -> statusChamadoRepository.findById(parseUuid(id, "ID de status invalido"))
                        .orElseThrow(() -> new IllegalArgumentException("Status de proximo nao encontrado")))
                .map(proximo -> {
                    UUID statusAtualId = statusAtual != null ? statusAtual.getId() : null;
                    if (statusAtualId != null && statusAtualId.equals(proximo.getId())) {
                        throw new IllegalArgumentException("Status nao pode apontar para ele mesmo como proximo");
                    }
                    if (statusAtualId != null && proximo.getProximos().stream().anyMatch(p -> p.getId().equals(statusAtualId))) {
                        throw new IllegalArgumentException("Relacao de proximos nao pode ser bidirecional");
                    }
                    return proximo;
                })
                .collect(Collectors.toSet());

        if (statusAtual != null) {
            try {
                statusAtual.setProximos(proximos);
                proximos.forEach(statusAtual::validarProximo);
            } catch (IllegalStateException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
        return proximos;
    }

    private User resolverResponsavelOpcional(String responsavelId) {
        if (responsavelId == null || responsavelId.isBlank()) {
            return null;
        }
        User responsavel = userRepository.findByIdWithRolesAndTipos(parseUuid(responsavelId, "Responsavel invalido"))
                .orElseThrow(() -> new IllegalArgumentException("Responsavel nao encontrado"));
        boolean isColaborador = responsavel.getRoles().stream().anyMatch(role -> "COLABORADOR".equals(role.getName()));
        if (!isColaborador) {
            throw new IllegalArgumentException("Responsavel deve possuir cargo COLABORADOR");
        }
        return responsavel;
    }

    public record AnexoDownload(
            Resource resource,
            String nomeOriginal,
            String contentType,
            long tamanhoBytes
    ) {}
}

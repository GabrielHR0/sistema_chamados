package com.condominio.chamados.web;

import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;
import com.condominio.chamados.chamado.dto.request.ChamadoCreateRequest;
import com.condominio.chamados.chamado.dto.request.LotacaoSaveRequest;
import com.condominio.chamados.chamado.dto.request.ComentarioChamadoRequest;
import com.condominio.chamados.chamado.dto.request.StatusChamadoCreateRequest;
import com.condominio.chamados.chamado.dto.request.TipoChamadoCreateRequest;
import com.condominio.chamados.chamado.service.ChamadoService;
import com.condominio.chamados.chamado.service.LotacaoService;
import com.condominio.chamados.security.permission.PermissionConstants;
import com.condominio.chamados.security.service.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/chamados")
public class ChamadoController {
    private static final DateTimeFormatter DATA_HORA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final String ROLE_OR_CHAMADO_READ = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_READ + "') or hasAnyRole('ADMIN','COLABORADOR'))";
    private static final String ROLE_OR_CHAMADO_CREATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_CREATE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_UPDATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_UPDATE + "') or hasAnyRole('ADMIN','COLABORADOR'))";
    private static final String ROLE_MORADOR = "isAuthenticated() and hasRole('MORADOR')";
    private static final String ROLE_ATENDIMENTO_CHAMADO = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_UPDATE + "') or hasAnyRole('ADMIN','COLABORADOR'))";
    private static final String ROLE_OR_CHAMADO_TIPO_READ = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_TIPO_READ + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_TIPO_CREATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_TIPO_CREATE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_TIPO_UPDATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_TIPO_UPDATE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_TIPO_DELETE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_TIPO_DELETE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_STATUS_READ = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_STATUS_READ + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_STATUS_CREATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_STATUS_CREATE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_STATUS_UPDATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_STATUS_UPDATE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_CHAMADO_STATUS_DELETE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.CHAMADO_STATUS_DELETE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_LOTACAO_READ = "isAuthenticated() and (hasAuthority('" + PermissionConstants.LOTACAO_READ + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_LOTACAO_CREATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.LOTACAO_CREATE + "') or hasRole('ADMIN'))";
    private static final String ROLE_OR_LOTACAO_UPDATE = "isAuthenticated() and (hasAuthority('" + PermissionConstants.LOTACAO_UPDATE + "') or hasRole('ADMIN'))";

    private final ChamadoService chamadoService;
    private final LotacaoService lotacaoService;

    public ChamadoController(ChamadoService chamadoService, LotacaoService lotacaoService) {
        this.chamadoService = chamadoService;
        this.lotacaoService = lotacaoService;
    }

    @GetMapping
    @PreAuthorize(ROLE_OR_CHAMADO_READ)
    public String index() {
        return "redirect:/chamados/lista";
    }

    @GetMapping("/lista")
    @PreAuthorize(ROLE_OR_CHAMADO_READ)
    public String lista(
            @RequestParam(name = "tipoId", required = false) String tipoId,
            @RequestParam(name = "statusId", required = false) String statusId,
            @RequestParam(name = "unidadeId", required = false) String unidadeId,
            @RequestParam(name = "responsavelId", required = false) String responsavelId,
            Authentication authentication,
            Model model
    ) {
        String userId = extractAuthenticatedUserId(authentication);
        boolean isAdmin = isAdminUser(authentication);
        model.addAttribute("chamados", chamadoService.listarChamados(userId, isAdmin, tipoId, statusId, unidadeId, responsavelId));
        model.addAttribute("tiposFiltro", chamadoService.listarTipos(userId, isAdmin));
        model.addAttribute("statusFiltro", chamadoService.listarStatusParaFiltro());
        model.addAttribute("unidadesFiltro", chamadoService.listarUnidadesParaChamado());
        model.addAttribute("responsaveisFiltro", chamadoService.listarColaboradoresAtivos());
        model.addAttribute("selectedTipoId", tipoId);
        model.addAttribute("selectedStatusId", statusId);
        model.addAttribute("selectedUnidadeId", unidadeId);
        model.addAttribute("selectedResponsavelId", responsavelId);
        model.addAttribute("pageTitle", "Chamados");
        model.addAttribute("activePage", "chamados-lista");
        return "chamados/lista";
    }

    @GetMapping("/atendimento")
    @PreAuthorize(ROLE_ATENDIMENTO_CHAMADO)
    public String filaAtendimento(
            @RequestParam(name = "tipoId", required = false) String tipoId,
            @RequestParam(name = "statusId", required = false) String statusId,
            Authentication authentication,
            Model model
    ) {
        String userId = extractAuthenticatedUserId(authentication);
        model.addAttribute("chamados", chamadoService.listarChamadosDisponiveisParaLotacao(userId, isAdminUser(authentication), tipoId, statusId));
        model.addAttribute("meusChamadosNaoFinais", chamadoService.listarChamadosNaoFinaisDoResponsavel(userId));
        model.addAttribute("tiposFiltro", chamadoService.listarTipos(userId, false));
        model.addAttribute("statusFiltro", chamadoService.listarStatusParaFiltro());
        model.addAttribute("selectedTipoId", tipoId);
        model.addAttribute("selectedStatusId", statusId);
        model.addAttribute("pageTitle", "Fila de Atendimento");
        model.addAttribute("activePage", "chamados-atendimento");
        return "chamados/atendimento";
    }

    @GetMapping("/novo")
    @PreAuthorize(ROLE_OR_CHAMADO_CREATE)
    public String novo(Authentication authentication, Model model) {
        prepareNovoModel(model, new ChamadoCreateRequest(), authentication);
        model.addAttribute("colaboradores", chamadoService.listarColaboradoresAtivos());
        return "chamados/novo";
    }

    @PostMapping("/novo")
    @PreAuthorize(ROLE_OR_CHAMADO_CREATE)
    public String criar(
            @Valid @ModelAttribute("chamado") ChamadoCreateRequest request,
            BindingResult bindingResult,
            @RequestParam(name = "arquivo", required = false) MultipartFile arquivo,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareNovoModel(model, request, authentication);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/novo";
        }

        try {
            String chamadoId = chamadoService.criarChamado(
                    request,
                    extractAuthenticatedUserId(authentication),
                    isAdminUser(authentication),
                    arquivo
            );
            return "redirect:/chamados/" + chamadoId + "?created";
        } catch (IllegalArgumentException ex) {
            prepareNovoModel(model, request, authentication);
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/novo";
        }
    }

    @GetMapping("/morador")
    @PreAuthorize(ROLE_MORADOR)
    public String listaMorador(Authentication authentication, Model model) {
        String userId = extractAuthenticatedUserId(authentication);
        model.addAttribute("chamados", chamadoService.listarChamadosMorador(userId));
        model.addAttribute("pageTitle", "Meus Chamados");
        model.addAttribute("activePage", "morador-chamados");
        return "morador/chamados-lista";
    }

    @GetMapping("/morador/unidades")
    @PreAuthorize(ROLE_MORADOR)
    public String unidadesMorador(Authentication authentication, Model model) {
        String userId = extractAuthenticatedUserId(authentication);
        model.addAttribute("moradias", chamadoService.listarMoradiasMorador(userId));
        model.addAttribute("pageTitle", "Minhas Moradias");
        model.addAttribute("activePage", "morador-unidades");
        return "morador/unidades";
    }

    @GetMapping("/morador/novo")
    @PreAuthorize(ROLE_MORADOR)
    public String novoMorador(Authentication authentication, Model model) {
        prepareNovoMoradorModel(model, new ChamadoCreateRequest(), authentication);
        return "morador/chamados-novo";
    }

    @PostMapping("/morador/novo")
    @PreAuthorize(ROLE_MORADOR)
    public String criarMorador(
            @Valid @ModelAttribute("chamado") ChamadoCreateRequest request,
            BindingResult bindingResult,
            @RequestParam(name = "arquivo", required = false) MultipartFile arquivo,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareNovoMoradorModel(model, request, authentication);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "morador/chamados-novo";
        }

        try {
            request.setResponsavelId(null);
            String chamadoId = chamadoService.criarChamado(request, extractAuthenticatedUserId(authentication), false, arquivo);
            return "redirect:/chamados/morador/" + chamadoId + "?created";
        } catch (IllegalArgumentException ex) {
            prepareNovoMoradorModel(model, request, authentication);
            model.addAttribute("errorMessage", ex.getMessage());
            return "morador/chamados-novo";
        }
    }

    @GetMapping("/morador/{id:[a-fA-F0-9-]{36}}")
    @PreAuthorize(ROLE_MORADOR)
    public String detalhesMorador(@PathVariable String id, Authentication authentication, Model model) {
        fillDetalhesMoradorModel(id, model, authentication);
        return "morador/chamados-detalhes";
    }

    @GetMapping("/{id}")
    @PreAuthorize(ROLE_OR_CHAMADO_READ)
    public String detalhes(@PathVariable String id, Authentication authentication, Model model) {
        var chamado = chamadoService.obterDetalhes(id, extractAuthenticatedUserId(authentication), isAdminUser(authentication));
        model.addAttribute("chamado", chamado);
        model.addAttribute("chamadoCodigo", gerarCodigoVisual(chamado.getId()));
        model.addAttribute("chamadoDataCriacaoFmt", formatarDataHora(chamado.getDataCriacao()));
        model.addAttribute("chamadoDataFinalizacaoFmt", formatarDataHora(chamado.getDataFinalizacao()));
        model.addAttribute("colaboradores", chamadoService.listarColaboradoresAtivos());
        model.addAttribute("comentarioRequest", new ComentarioChamadoRequest());
        model.addAttribute("pageTitle", "Detalhes do Chamado");
        model.addAttribute("activePage", "chamados-detalhes");
        return "chamados/detalhes";
    }

    @PostMapping("/{id}/assumir")
    @PreAuthorize(ROLE_OR_CHAMADO_UPDATE)
    public String assumir(@PathVariable String id, Authentication authentication, Model model) {
        try {
            chamadoService.assumirResponsabilidade(id, extractAuthenticatedUserId(authentication), isAdminUser(authentication));
            return "redirect:/chamados/" + id + "?assigned";
        } catch (IllegalArgumentException ex) {
            fillDetalhesModel(id, model, authentication);
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/detalhes";
        }
    }

    @PostMapping("/{id}/atender")
    @PreAuthorize(ROLE_ATENDIMENTO_CHAMADO)
    public String atender(@PathVariable String id, Authentication authentication, Model model) {
        try {
            chamadoService.assumirChamadoDaLotacao(id, extractAuthenticatedUserId(authentication), isAdminUser(authentication));
            return "redirect:/chamados/" + id + "?assigned";
        } catch (IllegalArgumentException ex) {
            String userId = extractAuthenticatedUserId(authentication);
            model.addAttribute("chamados", chamadoService.listarChamadosDisponiveisParaLotacao(userId, isAdminUser(authentication), null, null));
            model.addAttribute("meusChamadosNaoFinais", chamadoService.listarChamadosNaoFinaisDoResponsavel(userId));
            model.addAttribute("tiposFiltro", chamadoService.listarTipos(userId, isAdminUser(authentication)));
            model.addAttribute("statusFiltro", chamadoService.listarStatusParaFiltro());
            model.addAttribute("selectedTipoId", null);
            model.addAttribute("selectedStatusId", null);
            model.addAttribute("pageTitle", "Fila de Atendimento");
            model.addAttribute("activePage", "chamados-atendimento");
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/atendimento";
        }
    }

    @PostMapping("/{id}/comentarios")
    @PreAuthorize(ROLE_OR_CHAMADO_UPDATE)
    public String comentar(
            @PathVariable String id,
            @Valid @ModelAttribute("comentarioRequest") ComentarioChamadoRequest request,
            BindingResult bindingResult,
            @RequestParam(name = "arquivo", required = false) MultipartFile arquivo,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            fillDetalhesModel(id, model, authentication);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/detalhes";
        }
        try {
            chamadoService.adicionarComentario(
                    id,
                    extractAuthenticatedUserId(authentication),
                    request.getComentario(),
                    arquivo,
                    isAdminUser(authentication)
            );
            return "redirect:/chamados/" + id + "?commented";
        } catch (IllegalArgumentException ex) {
            fillDetalhesModel(id, model, authentication);
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/detalhes";
        }
    }

    @PostMapping("/morador/{id:[a-fA-F0-9-]{36}}/comentarios")
    @PreAuthorize(ROLE_MORADOR)
    public String comentarMorador(
            @PathVariable String id,
            @Valid @ModelAttribute("comentarioRequest") ComentarioChamadoRequest request,
            BindingResult bindingResult,
            @RequestParam(name = "arquivo", required = false) MultipartFile arquivo,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            fillDetalhesMoradorModel(id, model, authentication);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "morador/chamados-detalhes";
        }
        try {
            chamadoService.adicionarComentario(
                    id,
                    extractAuthenticatedUserId(authentication),
                    request.getComentario(),
                    arquivo,
                    false
            );
            return "redirect:/chamados/morador/" + id + "?commented";
        } catch (IllegalArgumentException ex) {
            fillDetalhesMoradorModel(id, model, authentication);
            model.addAttribute("errorMessage", ex.getMessage());
            return "morador/chamados-detalhes";
        }
    }

    @PostMapping("/{id}/status")
    @PreAuthorize(ROLE_OR_CHAMADO_UPDATE)
    public String atualizarStatus(
            @PathVariable String id,
            @RequestParam("statusId") String statusId,
            Authentication authentication,
            Model model
    ) {
        try {
            chamadoService.atualizarStatus(id, statusId, extractAuthenticatedUserId(authentication), isAdminUser(authentication));
            return "redirect:/chamados/" + id + "?statusUpdated";
        } catch (IllegalArgumentException ex) {
            fillDetalhesModel(id, model, authentication);
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/detalhes";
        }
    }

    @PostMapping("/{id}/anexos")
    @PreAuthorize(ROLE_OR_CHAMADO_UPDATE)
    public String uploadAnexo(
            @PathVariable String id,
            @RequestParam("arquivo") MultipartFile arquivo,
            Authentication authentication,
            Model model
    ) {
        try {
            chamadoService.adicionarAnexo(id, extractAuthenticatedUserId(authentication), arquivo, isAdminUser(authentication));
            return "redirect:/chamados/" + id + "?fileUploaded";
        } catch (IllegalArgumentException ex) {
            fillDetalhesModel(id, model, authentication);
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/detalhes";
        }
    }

    @GetMapping("/anexos/{anexoId}")
    @PreAuthorize(ROLE_OR_CHAMADO_READ)
    public ResponseEntity<org.springframework.core.io.Resource> downloadAnexo(
            @PathVariable String anexoId,
            Authentication authentication
    ) {
        var anexo = chamadoService.obterAnexoParaDownload(
                anexoId,
                extractAuthenticatedUserId(authentication),
                isAdminUser(authentication)
        );
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (anexo.contentType() != null && !anexo.contentType().isBlank()) {
            mediaType = MediaType.parseMediaType(anexo.contentType());
        }
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(anexo.nomeOriginal())
                .build();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(anexo.tamanhoBytes())
                .body(anexo.resource());
    }

    @GetMapping("/comentarios/anexos/{anexoId}")
    @PreAuthorize(ROLE_OR_CHAMADO_READ)
    public ResponseEntity<org.springframework.core.io.Resource> downloadAnexoComentario(
            @PathVariable String anexoId,
            Authentication authentication
    ) {
        var anexo = chamadoService.obterAnexoComentarioParaDownload(
                anexoId,
                extractAuthenticatedUserId(authentication),
                isAdminUser(authentication)
        );
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (anexo.contentType() != null && !anexo.contentType().isBlank()) {
            mediaType = MediaType.parseMediaType(anexo.contentType());
        }
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(anexo.nomeOriginal())
                .build();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(anexo.tamanhoBytes())
                .body(anexo.resource());
    }

    @GetMapping("/morador/anexos/{anexoId}")
    @PreAuthorize(ROLE_MORADOR)
    public ResponseEntity<org.springframework.core.io.Resource> downloadAnexoMorador(
            @PathVariable String anexoId,
            Authentication authentication
    ) {
        var anexo = chamadoService.obterAnexoParaDownload(
                anexoId,
                extractAuthenticatedUserId(authentication),
                false
        );
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (anexo.contentType() != null && !anexo.contentType().isBlank()) {
            mediaType = MediaType.parseMediaType(anexo.contentType());
        }
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(anexo.nomeOriginal())
                .build();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(anexo.tamanhoBytes())
                .body(anexo.resource());
    }

    @GetMapping("/morador/comentarios/anexos/{anexoId}")
    @PreAuthorize(ROLE_MORADOR)
    public ResponseEntity<org.springframework.core.io.Resource> downloadAnexoComentarioMorador(
            @PathVariable String anexoId,
            Authentication authentication
    ) {
        var anexo = chamadoService.obterAnexoComentarioParaDownload(
                anexoId,
                extractAuthenticatedUserId(authentication),
                false
        );
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (anexo.contentType() != null && !anexo.contentType().isBlank()) {
            mediaType = MediaType.parseMediaType(anexo.contentType());
        }
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(anexo.nomeOriginal())
                .build();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(anexo.tamanhoBytes())
                .body(anexo.resource());
    }

    @GetMapping("/tipos")
    @PreAuthorize(ROLE_OR_CHAMADO_TIPO_READ)
    public String tipos(Model model) {
        model.addAttribute("tipos", chamadoService.listarTipos(null, true));
        model.addAttribute("tipoRequest", new TipoChamadoCreateRequest());
        model.addAttribute("statusIniciais", chamadoService.listarStatusIniciais());
        model.addAttribute("pageTitle", "Tipos de Chamado");
        model.addAttribute("activePage", "chamados-tipos");
        return "chamados/tipos";
    }

    @PostMapping("/tipos")
    @PreAuthorize(ROLE_OR_CHAMADO_TIPO_CREATE)
    public String criarTipo(
            @Valid @ModelAttribute("tipoRequest") TipoChamadoCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tipos", chamadoService.listarTipos(null, true));
            model.addAttribute("statusIniciais", chamadoService.listarStatusIniciais());
            model.addAttribute("pageTitle", "Tipos de Chamado");
            model.addAttribute("activePage", "chamados-tipos");
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/tipos";
        }

        try {
            chamadoService.criarTipo(request);
            return "redirect:/chamados/tipos?created";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("tipos", chamadoService.listarTipos(null, true));
            model.addAttribute("statusIniciais", chamadoService.listarStatusIniciais());
            model.addAttribute("pageTitle", "Tipos de Chamado");
            model.addAttribute("activePage", "chamados-tipos");
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/tipos";
        }
    }

    @GetMapping("/tipos/{id}/editar")
    @PreAuthorize(ROLE_OR_CHAMADO_TIPO_UPDATE)
    public String editarTipoForm(@PathVariable String id, Model model) {
        model.addAttribute("tipoId", id);
        model.addAttribute("tipo", chamadoService.obterTipoPorId(id));
        model.addAttribute("statusIniciais", chamadoService.listarStatusIniciais());
        model.addAttribute("pageTitle", "Editar Tipo de Chamado");
        model.addAttribute("activePage", "chamados-tipos");
        return "chamados/tipo-editar";
    }

    @PostMapping("/tipos/{id}/editar")
    @PreAuthorize(ROLE_OR_CHAMADO_TIPO_UPDATE)
    public String editarTipo(
            @PathVariable String id,
            @Valid @ModelAttribute("tipo") TipoChamadoCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tipoId", id);
            model.addAttribute("statusIniciais", chamadoService.listarStatusIniciais());
            model.addAttribute("pageTitle", "Editar Tipo de Chamado");
            model.addAttribute("activePage", "chamados-tipos");
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/tipo-editar";
        }
        try {
            chamadoService.editarTipo(id, request);
            return "redirect:/chamados/tipos?updated";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("tipoId", id);
            model.addAttribute("statusIniciais", chamadoService.listarStatusIniciais());
            model.addAttribute("pageTitle", "Editar Tipo de Chamado");
            model.addAttribute("activePage", "chamados-tipos");
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/tipo-editar";
        }
    }

    @PostMapping("/tipos/{id}/deletar")
    @PreAuthorize(ROLE_OR_CHAMADO_TIPO_DELETE)
    public String deletarTipo(@PathVariable String id, Model model) {
        try {
            chamadoService.deletarTipo(id);
            return "redirect:/chamados/tipos?deleted";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("tipos", chamadoService.listarTipos(null, true));
            model.addAttribute("tipoRequest", new TipoChamadoCreateRequest());
            model.addAttribute("statusIniciais", chamadoService.listarStatusIniciais());
            model.addAttribute("pageTitle", "Tipos de Chamado");
            model.addAttribute("activePage", "chamados-tipos");
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/tipos";
        }
    }

    @GetMapping("/status")
    @PreAuthorize(ROLE_OR_CHAMADO_STATUS_READ)
    public String status(Model model) {
        model.addAttribute("statusList", chamadoService.listarStatus());
        model.addAttribute("statusDisponiveis", chamadoService.listarStatusEntidade());
        model.addAttribute("statusRequest", new StatusChamadoCreateRequest());
        model.addAttribute("comportamentos", StatusComportamentoTipo.values());
        model.addAttribute("pageTitle", "Status de Chamado");
        model.addAttribute("activePage", "chamados-status");
        return "chamados/status";
    }

    @PostMapping("/status")
    @PreAuthorize(ROLE_OR_CHAMADO_STATUS_CREATE)
    public String criarStatus(
            @Valid @ModelAttribute("statusRequest") StatusChamadoCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareStatusModel(model, request);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/status";
        }

        try {
            chamadoService.criarStatus(request);
            return "redirect:/chamados/status?created";
        } catch (IllegalArgumentException ex) {
            prepareStatusModel(model, request);
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/status";
        }
    }

    @GetMapping("/status/{id}/editar")
    @PreAuthorize(ROLE_OR_CHAMADO_STATUS_UPDATE)
    public String editarStatusForm(@PathVariable String id, Model model) {
        StatusChamadoCreateRequest request = mapStatusToRequest(id);
        model.addAttribute("statusId", id);
        model.addAttribute("statusRequest", request);
        model.addAttribute("statusDisponiveis", chamadoService.listarStatusEntidade());
        model.addAttribute("comportamentos", StatusComportamentoTipo.values());
        model.addAttribute("pageTitle", "Editar Status de Chamado");
        model.addAttribute("activePage", "chamados-status");
        return "chamados/status-editar";
    }

    @PostMapping("/status/{id}/editar")
    @PreAuthorize(ROLE_OR_CHAMADO_STATUS_UPDATE)
    public String editarStatus(
            @PathVariable String id,
            @Valid @ModelAttribute("statusRequest") StatusChamadoCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statusId", id);
            model.addAttribute("statusDisponiveis", chamadoService.listarStatusEntidade());
            model.addAttribute("comportamentos", StatusComportamentoTipo.values());
            model.addAttribute("pageTitle", "Editar Status de Chamado");
            model.addAttribute("activePage", "chamados-status");
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/status-editar";
        }
        try {
            chamadoService.editarStatus(id, request);
            return "redirect:/chamados/status?updated";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("statusId", id);
            model.addAttribute("statusDisponiveis", chamadoService.listarStatusEntidade());
            model.addAttribute("comportamentos", StatusComportamentoTipo.values());
            model.addAttribute("pageTitle", "Editar Status de Chamado");
            model.addAttribute("activePage", "chamados-status");
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/status-editar";
        }
    }

    @PostMapping("/status/{id}/deletar")
    @PreAuthorize(ROLE_OR_CHAMADO_STATUS_DELETE)
    public String deletarStatus(@PathVariable String id, Model model) {
        try {
            chamadoService.deletarStatus(id);
            return "redirect:/chamados/status?deleted";
        } catch (IllegalArgumentException ex) {
            prepareStatusModel(model, new StatusChamadoCreateRequest());
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/status";
        }
    }

    @GetMapping({"/admin/lotacoes", "/lotacoes"})
    @PreAuthorize(ROLE_OR_LOTACAO_READ)
    public String lotacoes(Model model) {
        model.addAttribute("lotacoes", lotacaoService.listar());
        model.addAttribute("tiposDisponiveis", chamadoService.listarTipos(null, true));
        model.addAttribute("lotacaoRequest", new LotacaoSaveRequest());
        model.addAttribute("pageTitle", "Lotacoes");
        model.addAttribute("activePage", "chamados-lotacoes");
        return "chamados/lotacoes";
    }

    @PostMapping({"/admin/lotacoes", "/lotacoes"})
    @PreAuthorize(ROLE_OR_LOTACAO_CREATE)
    public String criarLotacao(
            @Valid @ModelAttribute("lotacaoRequest") LotacaoSaveRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            fillLotacoesModel(model, request);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/lotacoes";
        }
        try {
            lotacaoService.criar(request);
            fillLotacoesModel(model, new LotacaoSaveRequest());
            model.addAttribute("successMessage", "Lotacao cadastrada com sucesso.");
            return "chamados/lotacoes";
        } catch (IllegalArgumentException ex) {
            fillLotacoesModel(model, request);
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/lotacoes";
        }
    }

    @GetMapping({"/admin/lotacoes/{id}/editar", "/lotacoes/{id}/editar"})
    @PreAuthorize(ROLE_OR_LOTACAO_UPDATE)
    public String editarLotacaoForm(@PathVariable String id, Model model) {
        model.addAttribute("lotacaoId", id);
        model.addAttribute("lotacaoRequest", mapLotacaoToRequest(id));
        model.addAttribute("tiposDisponiveis", chamadoService.listarTipos(null, true));
        model.addAttribute("pageTitle", "Editar Lotacao");
        model.addAttribute("activePage", "chamados-lotacoes");
        return "chamados/lotacao-editar";
    }

    @PostMapping({"/admin/lotacoes/{id}/editar", "/lotacoes/{id}/editar"})
    @PreAuthorize(ROLE_OR_LOTACAO_UPDATE)
    public String editarLotacao(
            @PathVariable String id,
            @Valid @ModelAttribute("lotacaoRequest") LotacaoSaveRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("lotacaoId", id);
            model.addAttribute("tiposDisponiveis", chamadoService.listarTipos(null, true));
            model.addAttribute("pageTitle", "Editar Lotacao");
            model.addAttribute("activePage", "chamados-lotacoes");
            model.addAttribute("errorMessage", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "chamados/lotacao-editar";
        }
        try {
            lotacaoService.atualizar(id, request);
            fillLotacoesModel(model, new LotacaoSaveRequest());
            model.addAttribute("successMessage", "Lotacao atualizada com sucesso.");
            return "chamados/lotacoes";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("lotacaoId", id);
            model.addAttribute("tiposDisponiveis", chamadoService.listarTipos(null, true));
            model.addAttribute("pageTitle", "Editar Lotacao");
            model.addAttribute("activePage", "chamados-lotacoes");
            model.addAttribute("errorMessage", ex.getMessage());
            return "chamados/lotacao-editar";
        }
    }

    private void prepareNovoModel(Model model, ChamadoCreateRequest request, Authentication authentication) {
        model.addAttribute("chamado", request);
        model.addAttribute("unidades", chamadoService.listarUnidadesParaChamado());
        model.addAttribute("tipos", chamadoService.listarTipos(extractAuthenticatedUserId(authentication), isAdminUser(authentication)));
        model.addAttribute("colaboradores", chamadoService.listarColaboradoresAtivos());
        model.addAttribute("pageTitle", "Novo Chamado");
        model.addAttribute("activePage", "chamados-novo");
    }

    private void prepareNovoMoradorModel(Model model, ChamadoCreateRequest request, Authentication authentication) {
        String userId = extractAuthenticatedUserId(authentication);
        model.addAttribute("chamado", request);
        model.addAttribute("unidades", chamadoService.listarUnidadesAtivasMorador(userId));
        model.addAttribute("tipos", chamadoService.listarTipos(null, true));
        model.addAttribute("pageTitle", "Abrir Chamado");
        model.addAttribute("activePage", "morador-chamados-novo");
    }

    private void fillDetalhesModel(String chamadoId, Model model, Authentication authentication) {
        var chamado = chamadoService.obterDetalhes(chamadoId, extractAuthenticatedUserId(authentication), isAdminUser(authentication));
        model.addAttribute("chamado", chamado);
        model.addAttribute("chamadoCodigo", gerarCodigoVisual(chamado.getId()));
        model.addAttribute("chamadoDataCriacaoFmt", formatarDataHora(chamado.getDataCriacao()));
        model.addAttribute("chamadoDataFinalizacaoFmt", formatarDataHora(chamado.getDataFinalizacao()));
        model.addAttribute("colaboradores", chamadoService.listarColaboradoresAtivos());
        if (!model.containsAttribute("comentarioRequest")) {
            model.addAttribute("comentarioRequest", new ComentarioChamadoRequest());
        }
        model.addAttribute("pageTitle", "Detalhes do Chamado");
        model.addAttribute("activePage", "chamados-detalhes");
    }

    private void fillDetalhesMoradorModel(String chamadoId, Model model, Authentication authentication) {
        var chamado = chamadoService.obterDetalhes(chamadoId, extractAuthenticatedUserId(authentication), false);
        model.addAttribute("chamado", chamado);
        model.addAttribute("chamadoCodigo", gerarCodigoVisual(chamado.getId()));
        model.addAttribute("chamadoDataCriacaoFmt", formatarDataHora(chamado.getDataCriacao()));
        model.addAttribute("chamadoDataFinalizacaoFmt", formatarDataHora(chamado.getDataFinalizacao()));
        if (!model.containsAttribute("comentarioRequest")) {
            model.addAttribute("comentarioRequest", new ComentarioChamadoRequest());
        }
        model.addAttribute("pageTitle", "Detalhes do Meu Chamado");
        model.addAttribute("activePage", "morador-chamados");
    }

    private String gerarCodigoVisual(String chamadoId) {
        if (chamadoId == null || chamadoId.isBlank()) {
            return "-";
        }
        String compact = chamadoId.replace("-", "").toUpperCase();
        return compact.length() >= 8 ? compact.substring(0, 8) : compact;
    }

    private String formatarDataHora(OffsetDateTime dataHora) {
        if (dataHora == null) {
            return "-";
        }
        return dataHora.format(DATA_HORA_BR);
    }

    private void prepareStatusModel(Model model, StatusChamadoCreateRequest request) {
        model.addAttribute("statusList", chamadoService.listarStatus());
        model.addAttribute("statusDisponiveis", chamadoService.listarStatusEntidade());
        model.addAttribute("statusRequest", request);
        model.addAttribute("comportamentos", StatusComportamentoTipo.values());
        model.addAttribute("pageTitle", "Status de Chamado");
        model.addAttribute("activePage", "chamados-status");
    }

    private void fillLotacoesModel(Model model, LotacaoSaveRequest request) {
        model.addAttribute("lotacoes", lotacaoService.listar());
        model.addAttribute("tiposDisponiveis", chamadoService.listarTipos(null, true));
        model.addAttribute("lotacaoRequest", request);
        model.addAttribute("pageTitle", "Lotacoes");
        model.addAttribute("activePage", "chamados-lotacoes");
    }

    private LotacaoSaveRequest mapLotacaoToRequest(String lotacaoId) {
        var lotacao = lotacaoService.obterPorId(lotacaoId);
        LotacaoSaveRequest request = new LotacaoSaveRequest();
        request.setNome(lotacao.getNome());
        request.setDescricao(lotacao.getDescricao());
        request.setTiposChamadoIds(
                lotacao.getTiposChamado().stream()
                        .map(tipo -> tipo.getId())
                        .toList()
        );
        return request;
    }

    private StatusChamadoCreateRequest mapStatusToRequest(String statusId) {
        var status = chamadoService.obterStatusPorId(statusId);
        StatusChamadoCreateRequest request = new StatusChamadoCreateRequest();
        request.setNome(status.getNome());
        request.setComportamentoTipo(status.getComportamentoTipo());
        request.setProximosIds(status.getProximosNomes());
        return request;
    }

    private String extractAuthenticatedUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser().getId().toString();
        }
        throw new IllegalArgumentException("Usuario autenticado invalido");
    }

    private boolean isAdminUser(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

}

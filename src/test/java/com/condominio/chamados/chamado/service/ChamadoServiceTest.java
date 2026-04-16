package com.condominio.chamados.chamado.service;

import com.condominio.chamados.bloco.domain.MoradiaStatus;
import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.UnidadeRepository;
import com.condominio.chamados.chamado.domain.Chamado;
import com.condominio.chamados.chamado.domain.ComentarioChamado;
import com.condominio.chamados.chamado.domain.StatusChamado;
import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;
import com.condominio.chamados.chamado.domain.TipoChamado;
import com.condominio.chamados.chamado.dto.request.ChamadoCreateRequest;
import com.condominio.chamados.chamado.dto.response.StatusChamadoResponse;
import com.condominio.chamados.chamado.repository.ChamadoAnexoRepository;
import com.condominio.chamados.chamado.repository.ChamadoRepository;
import com.condominio.chamados.chamado.repository.ComentarioAnexoRepository;
import com.condominio.chamados.chamado.repository.ComentarioChamadoRepository;
import com.condominio.chamados.chamado.repository.StatusChamadoRepository;
import com.condominio.chamados.chamado.repository.TipoChamadoRepository;
import com.condominio.chamados.security.domain.Role;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.repository.UserRepository;
import com.condominio.chamados.shared.upload.UploadStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

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
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;
    @Mock
    private ChamadoAnexoRepository chamadoAnexoRepository;
    @Mock
    private ComentarioAnexoRepository comentarioAnexoRepository;
    @Mock
    private TipoChamadoRepository tipoChamadoRepository;
    @Mock
    private StatusChamadoRepository statusChamadoRepository;
    @Mock
    private ComentarioChamadoRepository comentarioChamadoRepository;
    @Mock
    private UnidadeRepository unidadeRepository;
    @Mock
    private MoradiaRepository moradiaRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UploadStorageService uploadStorageService;

    @Test
    void deveCriarChamadoComStatusInicialSelecionado() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );

        UUID unidadeId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();
        UUID chamadoId = UUID.randomUUID();

        Unidade unidade = new Unidade();
        unidade.setId(unidadeId);
        TipoChamado tipo = new TipoChamado();
        tipo.setId(tipoId);
        tipo.setTitulo("Manutencao");
        StatusChamado inicial = new StatusChamado();
        inicial.setId(UUID.randomUUID());
        inicial.setNome("ABERTO");
        inicial.setComportamentoTipo(StatusComportamentoTipo.INICIAL);
        tipo.setStatusInicial(inicial);

        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.of(unidade));
        when(tipoChamadoRepository.findById(tipoId)).thenReturn(Optional.of(tipo));
        User admin = new User();
        admin.setId(UUID.randomUUID());
        when(userRepository.findByIdWithRolesAndTipos(admin.getId())).thenReturn(Optional.of(admin));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> {
            Chamado chamado = invocation.getArgument(0);
            chamado.setId(chamadoId);
            return chamado;
        });

        ChamadoCreateRequest request = new ChamadoCreateRequest();
        request.setUnidadeId(unidadeId.toString());
        request.setTipoId(tipoId.toString());
        request.setDescricao("Portao da garagem com falha");

        String result = service.criarChamado(request, admin.getId().toString(), true);

        assertThat(result).isEqualTo(chamadoId.toString());
    }

    @Test
    void deveListarSomenteStatusIniciais() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );
        StatusChamado inicial = new StatusChamado();
        inicial.setId(UUID.randomUUID());
        inicial.setNome("Inicial");
        inicial.setComportamentoTipo(StatusComportamentoTipo.INICIAL);

        StatusChamado finalStatus = new StatusChamado();
        finalStatus.setId(UUID.randomUUID());
        finalStatus.setNome("Final");
        finalStatus.setComportamentoTipo(StatusComportamentoTipo.FINAL);

        when(statusChamadoRepository.findAllByOrderByNomeAsc()).thenReturn(List.of(finalStatus, inicial));

        List<StatusChamadoResponse> iniciais = service.listarStatusIniciais();

        assertThat(iniciais).hasSize(1);
        assertThat(iniciais.getFirst().getComportamentoTipo()).isEqualTo(StatusComportamentoTipo.INICIAL);
        assertThat(iniciais.getFirst().getNome()).isEqualTo("Inicial");
    }

    @Test
    void deveBloquearAssumirQuandoUsuarioNaoForColaborador() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );

        UUID chamadoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Chamado chamado = new Chamado();
        chamado.setId(chamadoId);
        User user = new User();
        user.setId(userId);
        Role role = new Role();
        role.setName("MORADOR");
        user.setRoles(Set.of(role));

        when(chamadoRepository.findByIdWithRelations(chamadoId)).thenReturn(Optional.of(chamado));
        when(userRepository.findByIdWithRolesAndTipos(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.assumirResponsabilidade(chamadoId.toString(), userId.toString(), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("COLABORADOR");
    }

    @Test
    void deveCriarComentarioComAnexoRelacionado() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );

        UUID chamadoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Chamado chamado = new Chamado();
        chamado.setId(chamadoId);
        StatusChamado status = new StatusChamado();
        status.setId(UUID.randomUUID());
        chamado.setStatus(status);
        TipoChamado tipo = new TipoChamado();
        tipo.setId(UUID.randomUUID());
        chamado.setTipo(tipo);
        Unidade unidade = new Unidade();
        unidade.setId(UUID.randomUUID());
        chamado.setUnidade(unidade);

        User autor = new User();
        autor.setId(userId);

        when(chamadoRepository.findByIdWithRelations(chamadoId)).thenReturn(Optional.of(chamado));
        when(userRepository.findByIdWithRolesAndTipos(userId)).thenReturn(Optional.of(autor));
        when(uploadStorageService.store(any())).thenReturn(
                new UploadStorageService.StoredUpload("abc123.png", "foto.png", "image/png", 10)
        );

        MockMultipartFile arquivo = new MockMultipartFile("arquivo", "foto.png", "image/png", new byte[]{1, 2});

        service.adicionarComentario(chamadoId.toString(), userId.toString(), "comentario", arquivo, true);

        verify(comentarioChamadoRepository).save(any(ComentarioChamado.class));
        verify(comentarioAnexoRepository).save(any());
    }

    @Test
    void deveAssumirChamadoDisponivelDaLotacao() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );

        UUID chamadoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Chamado chamado = new Chamado();
        chamado.setId(chamadoId);

        User colaborador = new User();
        colaborador.setId(userId);
        Role role = new Role();
        role.setName("COLABORADOR");
        colaborador.setRoles(Set.of(role));

        when(userRepository.findByIdWithRolesAndTipos(userId)).thenReturn(Optional.of(colaborador));
        when(chamadoRepository.findByIdDisponivelScopedByUserLotacao(chamadoId, userId)).thenReturn(Optional.of(chamado));

        service.assumirChamadoDaLotacao(chamadoId.toString(), userId.toString(), false);

        assertThat(chamado.getColaboradorResponsavel()).isEqualTo(colaborador);
        verify(chamadoRepository).save(chamado);
    }

    @Test
    void deveBloquearCriacaoDeChamadoParaMoradorComUnidadeForaDoVinculo() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );

        UUID unidadeId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        UUID moradorId = UUID.randomUUID();

        Unidade unidade = new Unidade();
        unidade.setId(unidadeId);
        TipoChamado tipo = new TipoChamado();
        tipo.setId(tipoId);
        StatusChamado statusInicial = new StatusChamado();
        statusInicial.setId(statusId);
        statusInicial.setComportamentoTipo(StatusComportamentoTipo.INICIAL);
        tipo.setStatusInicial(statusInicial);

        User morador = new User();
        morador.setId(moradorId);
        Role role = new Role();
        role.setName("MORADOR");
        morador.setRoles(Set.of(role));

        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.of(unidade));
        when(tipoChamadoRepository.findById(tipoId)).thenReturn(Optional.of(tipo));
        when(userRepository.findByIdWithRolesAndTipos(moradorId)).thenReturn(Optional.of(morador));
        when(moradiaRepository.findUnidadesByUsuarioIdAndStatus(moradorId, MoradiaStatus.ATIVA)).thenReturn(List.of());

        ChamadoCreateRequest request = new ChamadoCreateRequest();
        request.setUnidadeId(unidadeId.toString());
        request.setTipoId(tipoId.toString());
        request.setDescricao("Teste");

        assertThatThrownBy(() -> service.criarChamado(request, moradorId.toString(), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("escopo do morador");
    }

    @Test
    void deveNegarComentarioDeMoradorForaDoEscopo() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );

        UUID chamadoId = UUID.randomUUID();
        UUID moradorId = UUID.randomUUID();
        User morador = new User();
        morador.setId(moradorId);
        Role role = new Role();
        role.setName("MORADOR");
        morador.setRoles(Set.of(role));

        when(userRepository.findByIdWithRolesAndTipos(moradorId)).thenReturn(Optional.of(morador));
        when(chamadoRepository.findByIdWithRelationsScopedByMorador(chamadoId, moradorId, MoradiaStatus.ATIVA)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionarComentario(chamadoId.toString(), moradorId.toString(), "texto", null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("escopo do morador");
    }

    @Test
    void devePermitirDetalhesParaColaboradorNoEscopoPorLotacaoTipo() {
        ChamadoService service = new ChamadoService(
                chamadoRepository, chamadoAnexoRepository, comentarioAnexoRepository, tipoChamadoRepository, statusChamadoRepository,
                comentarioChamadoRepository, unidadeRepository, moradiaRepository, userRepository, uploadStorageService
        );

        UUID chamadoId = UUID.randomUUID();
        UUID colaboradorId = UUID.randomUUID();

        User colaborador = new User();
        colaborador.setId(colaboradorId);
        Role role = new Role();
        role.setName("COLABORADOR");
        colaborador.setRoles(Set.of(role));

        Chamado chamado = new Chamado();
        chamado.setId(chamadoId);
        Unidade unidade = new Unidade();
        unidade.setId(UUID.randomUUID());
        unidade.setIdentificacao("A-01-101");
        chamado.setUnidade(unidade);
        TipoChamado tipo = new TipoChamado();
        tipo.setId(UUID.randomUUID());
        tipo.setTitulo("Eletrica");
        tipo.setSlaHoras(24);
        chamado.setTipo(tipo);
        StatusChamado status = new StatusChamado();
        status.setId(UUID.randomUUID());
        status.setNome("Aberto");
        status.setComportamentoTipo(StatusComportamentoTipo.INICIAL);
        chamado.setStatus(status);

        when(userRepository.findByIdWithRolesAndTipos(colaboradorId)).thenReturn(Optional.of(colaborador));
        when(chamadoRepository.findByIdWithRelationsScopedByUser(chamadoId, colaboradorId)).thenReturn(Optional.of(chamado));
        when(comentarioChamadoRepository.findByChamadoIdWithAutorOrderByDataCriacaoAsc(chamadoId)).thenReturn(List.of());
        when(chamadoAnexoRepository.findByChamado_IdOrderByDataCriacaoDesc(chamadoId)).thenReturn(List.of());

        var detalhe = service.obterDetalhes(chamadoId.toString(), colaboradorId.toString(), false);

        assertThat(detalhe.getId()).isEqualTo(chamadoId.toString());
        assertThat(detalhe.getTipoTitulo()).isEqualTo("Eletrica");
    }
}

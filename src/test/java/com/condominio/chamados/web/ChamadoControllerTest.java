package com.condominio.chamados.web;

import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.dto.response.MoradiaResponse;
import com.condominio.chamados.chamado.dto.response.ChamadoResumoResponse;
import com.condominio.chamados.chamado.dto.response.LotacaoResponse;
import com.condominio.chamados.chamado.dto.response.StatusChamadoResponse;
import com.condominio.chamados.chamado.dto.response.TipoChamadoResponse;
import com.condominio.chamados.chamado.service.ChamadoService;
import com.condominio.chamados.chamado.service.LotacaoService;
import com.condominio.chamados.security.domain.Role;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.service.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChamadoControllerTest {

    private final ChamadoService chamadoService = mock(ChamadoService.class);
    private final LotacaoService lotacaoService = mock(LotacaoService.class);
    private final ChamadoController controller = new ChamadoController(chamadoService, lotacaoService);

    @Test
    void devePopularColaboradoresNoFormularioNovo() {
        Unidade unidade = new Unidade();
        unidade.setId(UUID.randomUUID());
        unidade.setIdentificacao("A-0102");

        TipoChamadoResponse tipo = new TipoChamadoResponse();
        tipo.setId(UUID.randomUUID().toString());
        tipo.setTitulo("Hidraulica");
        tipo.setSlaHoras(24);

        User colaborador = new User();
        colaborador.setId(UUID.randomUUID());
        colaborador.setUsername("colab");

        User actor = new User();
        actor.setId(UUID.randomUUID());
        actor.setUsername("admin");
        Role role = new Role();
        role.setName("ADMIN");
        actor.setRoles(Set.of(role));

        when(chamadoService.listarUnidadesParaChamado()).thenReturn(List.of(unidade));
        when(chamadoService.listarTipos(actor.getId().toString(), true)).thenReturn(List.of(tipo));
        when(chamadoService.listarColaboradoresAtivos()).thenReturn(List.of(colaborador));

        Authentication authentication = authenticationFromUser(actor);
        Model model = new ExtendedModelMap();

        String view = controller.novo(authentication, model);

        assertThat(view).isEqualTo("chamados/novo");
        assertThat(model.getAttribute("colaboradores")).isEqualTo(List.of(colaborador));
    }

    @Test
    void devePopularFormularioEdicaoLotacao() {
        String lotacaoId = UUID.randomUUID().toString();
        TipoChamadoResponse tipo = new TipoChamadoResponse();
        tipo.setId(UUID.randomUUID().toString());
        tipo.setTitulo("Eletrica");

        LotacaoResponse lotacao = new LotacaoResponse();
        lotacao.setId(lotacaoId);
        lotacao.setNome("Manutencao");
        lotacao.setDescricao("Equipe");
        lotacao.setTiposChamado(List.of(tipo));

        when(lotacaoService.obterPorId(lotacaoId)).thenReturn(lotacao);
        when(chamadoService.listarTipos(null, true)).thenReturn(List.of(tipo));

        Model model = new ExtendedModelMap();
        String view = controller.editarLotacaoForm(lotacaoId, model);

        assertThat(view).isEqualTo("chamados/lotacao-editar");
        assertThat(model.getAttribute("lotacaoId")).isEqualTo(lotacaoId);
        assertThat(model.getAttribute("tiposDisponiveis")).isEqualTo(List.of(tipo));
    }

    @Test
    void devePopularTelaFilaAtendimentoParaColaborador() {
        User colaborador = new User();
        colaborador.setId(UUID.randomUUID());
        colaborador.setUsername("colab");
        Role role = new Role();
        role.setName("COLABORADOR");
        colaborador.setRoles(Set.of(role));

        ChamadoResumoResponse chamado = new ChamadoResumoResponse();
        chamado.setId(UUID.randomUUID().toString());

        TipoChamadoResponse tipo = new TipoChamadoResponse();
        tipo.setId(UUID.randomUUID().toString());
        tipo.setTitulo("Eletrica");

        StatusChamadoResponse status = new StatusChamadoResponse();
        status.setId(UUID.randomUUID().toString());
        status.setNome("ABERTO");

        when(chamadoService.listarChamadosDisponiveisParaLotacao(colaborador.getId().toString(), false, null, null))
                .thenReturn(List.of(chamado));
        when(chamadoService.listarChamadosNaoFinaisDoResponsavel(colaborador.getId().toString()))
                .thenReturn(List.of(chamado));
        when(chamadoService.listarTipos(colaborador.getId().toString(), false)).thenReturn(List.of(tipo));
        when(chamadoService.listarStatusParaFiltro()).thenReturn(List.of(status));

        Authentication authentication = authenticationFromUser(colaborador);
        Model model = new ExtendedModelMap();

        String view = controller.filaAtendimento(null, null, authentication, model);

        assertThat(view).isEqualTo("chamados/atendimento");
        assertThat(model.getAttribute("chamados")).isEqualTo(List.of(chamado));
        assertThat(model.getAttribute("meusChamadosNaoFinais")).isEqualTo(List.of(chamado));
        assertThat(model.getAttribute("activePage")).isEqualTo("chamados-atendimento");
    }

    @Test
    void deveRenderizarListaDeChamadosDoMorador() {
        User morador = new User();
        morador.setId(UUID.randomUUID());
        morador.setUsername("morador");
        Role role = new Role();
        role.setName("MORADOR");
        morador.setRoles(Set.of(role));

        ChamadoResumoResponse chamado = new ChamadoResumoResponse();
        chamado.setId(UUID.randomUUID().toString());

        when(chamadoService.listarChamadosMorador(morador.getId().toString())).thenReturn(List.of(chamado));

        Authentication authentication = authenticationFromUser(morador);
        Model model = new ExtendedModelMap();

        String view = controller.listaMorador(authentication, model);

        assertThat(view).isEqualTo("morador/chamados-lista");
        assertThat(model.getAttribute("chamados")).isEqualTo(List.of(chamado));
        assertThat(model.getAttribute("activePage")).isEqualTo("morador-chamados");
    }

    @Test
    void devePopularFormularioNovoDoMoradorComUnidadesVinculadas() {
        User morador = new User();
        morador.setId(UUID.randomUUID());
        morador.setUsername("morador");
        Role role = new Role();
        role.setName("MORADOR");
        morador.setRoles(Set.of(role));

        Unidade unidade = new Unidade();
        unidade.setId(UUID.randomUUID());
        unidade.setIdentificacao("B-0203");

        TipoChamadoResponse tipo = new TipoChamadoResponse();
        tipo.setId(UUID.randomUUID().toString());
        tipo.setTitulo("Eletrica");
        tipo.setSlaHoras(12);

        when(chamadoService.listarUnidadesAtivasMorador(morador.getId().toString())).thenReturn(List.of(unidade));
        when(chamadoService.listarTipos(null, true)).thenReturn(List.of(tipo));

        Authentication authentication = authenticationFromUser(morador);
        Model model = new ExtendedModelMap();

        String view = controller.novoMorador(authentication, model);

        assertThat(view).isEqualTo("morador/chamados-novo");
        assertThat(model.getAttribute("unidades")).isEqualTo(List.of(unidade));
        assertThat(model.getAttribute("activePage")).isEqualTo("morador-chamados-novo");
    }

    @Test
    void deveRenderizarTabelaDeUnidadesDoMorador() {
        User morador = new User();
        morador.setId(UUID.randomUUID());
        morador.setUsername("morador");
        Role role = new Role();
        role.setName("MORADOR");
        morador.setRoles(Set.of(role));

        MoradiaResponse moradia = new MoradiaResponse();
        moradia.setId(UUID.randomUUID().toString());
        moradia.setUnidadeIdentificacao("A-0304");

        when(chamadoService.listarMoradiasMorador(morador.getId().toString())).thenReturn(List.of(moradia));

        Authentication authentication = authenticationFromUser(morador);
        Model model = new ExtendedModelMap();

        String view = controller.unidadesMorador(authentication, model);

        assertThat(view).isEqualTo("morador/unidades");
        assertThat(model.getAttribute("moradias")).isEqualTo(List.of(moradia));
        assertThat(model.getAttribute("activePage")).isEqualTo("morador-unidades");
    }

    private Authentication authenticationFromUser(User user) {
        UserPrincipal principal = new UserPrincipal(user, true);
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }
}

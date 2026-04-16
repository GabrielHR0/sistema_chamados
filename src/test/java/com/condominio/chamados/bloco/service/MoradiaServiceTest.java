package com.condominio.chamados.bloco.service;

import com.condominio.chamados.bloco.domain.Moradia;
import com.condominio.chamados.bloco.domain.MoradiaStatus;
import com.condominio.chamados.bloco.domain.Andar;
import com.condominio.chamados.bloco.domain.Bloco;
import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.dto.request.MoradiaCreateRequest;
import com.condominio.chamados.bloco.dto.response.MoradiaResponse;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.UnidadeRepository;
import com.condominio.chamados.security.domain.Perfil;
import com.condominio.chamados.security.domain.Role;
import com.condominio.chamados.security.domain.User;
import com.condominio.chamados.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoradiaServiceTest {

    @Mock
    private MoradiaRepository moradiaRepository;

    @Mock
    private UnidadeRepository unidadeRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void deveCriarMoradiaAtivaParaUnidade() {
        MoradiaService service = new MoradiaService(moradiaRepository, unidadeRepository, userRepository);

        UUID unidadeId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        UUID moradiaId = UUID.randomUUID();

        Unidade unidade = new Unidade();
        unidade.setId(unidadeId);
        Andar andar = new Andar();
        andar.setAtivo(true);
        Bloco bloco = new Bloco();
        bloco.setAtivo(true);
        andar.setBloco(bloco);
        unidade.setAndar(andar);

        User usuario = new User();
        usuario.setId(usuarioId);
        usuario.setUsername("maria");
        usuario.setPerfil(new Perfil());
        usuario.getPerfil().setNomeCompleto("Maria Souza");
        Role morador = new Role();
        morador.setName("MORADOR");
        usuario.setRoles(Set.of(morador));

        unidade.setAtivo(true);
        unidade.setStatus(com.condominio.chamados.bloco.domain.UnidadeStatus.EM_FUNCIONAMENTO);

        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.of(unidade));
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(moradiaRepository.findFirstByUnidade_IdAndStatusOrderByDataInicioDesc(unidadeId, MoradiaStatus.ATIVA))
                .thenReturn(Optional.empty());
        when(moradiaRepository.save(any(Moradia.class))).thenAnswer(invocation -> {
            Moradia moradia = invocation.getArgument(0);
            moradia.setId(moradiaId);
            return moradia;
        });

        MoradiaCreateRequest request = new MoradiaCreateRequest();
        request.setUsuarioId(usuarioId.toString());

        MoradiaResponse response = service.criar(unidadeId.toString(), request);

        assertThat(response.getId()).isEqualTo(moradiaId.toString());
        assertThat(response.getStatus()).isEqualTo(MoradiaStatus.ATIVA);
        assertThat(response.getUsuarioNome()).isEqualTo("Maria Souza");
        assertThat(response.getDataInicio()).isNotNull();
    }

    @Test
    void deveBloquearCriacaoQuandoUnidadeJaPossuiMoradiaAtiva() {
        MoradiaService service = new MoradiaService(moradiaRepository, unidadeRepository, userRepository);

        UUID unidadeId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();

        Unidade unidade = new Unidade();
        unidade.setId(unidadeId);
        Andar andar = new Andar();
        andar.setAtivo(true);
        Bloco bloco = new Bloco();
        bloco.setAtivo(true);
        andar.setBloco(bloco);
        unidade.setAndar(andar);

        User usuario = new User();
        usuario.setId(usuarioId);
        usuario.setUsername("maria");
        Role morador = new Role();
        morador.setName("MORADOR");
        usuario.setRoles(Set.of(morador));

        unidade.setAtivo(true);
        unidade.setStatus(com.condominio.chamados.bloco.domain.UnidadeStatus.EM_FUNCIONAMENTO);

        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.of(unidade));
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(moradiaRepository.findFirstByUnidade_IdAndStatusOrderByDataInicioDesc(unidadeId, MoradiaStatus.ATIVA))
                .thenReturn(Optional.of(new Moradia()));

        MoradiaCreateRequest request = new MoradiaCreateRequest();
        request.setUsuarioId(usuarioId.toString());

        assertThatThrownBy(() -> service.criar(unidadeId.toString(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("já possui uma moradia ativa");
    }

    @Test
    void deveBloquearCriacaoQuandoAndarEstiverInativo() {
        MoradiaService service = new MoradiaService(moradiaRepository, unidadeRepository, userRepository);

        UUID unidadeId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();

        Unidade unidade = new Unidade();
        unidade.setId(unidadeId);
        unidade.setAtivo(true);
        unidade.setStatus(com.condominio.chamados.bloco.domain.UnidadeStatus.EM_FUNCIONAMENTO);
        Andar andar = new Andar();
        andar.setAtivo(false);
        Bloco bloco = new Bloco();
        bloco.setAtivo(true);
        andar.setBloco(bloco);
        unidade.setAndar(andar);

        User usuario = new User();
        usuario.setId(usuarioId);
        usuario.setUsername("maria");
        Role morador = new Role();
        morador.setName("MORADOR");
        usuario.setRoles(Set.of(morador));

        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.of(unidade));
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        MoradiaCreateRequest request = new MoradiaCreateRequest();
        request.setUsuarioId(usuarioId.toString());
        request.setUnidadeId(unidadeId.toString());

        assertThatThrownBy(() -> service.criar(unidadeId.toString(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("andar está inativo");
    }

    @Test
    void deveBloquearCriacaoQuandoBlocoEstiverInativo() {
        MoradiaService service = new MoradiaService(moradiaRepository, unidadeRepository, userRepository);

        UUID unidadeId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();

        Unidade unidade = new Unidade();
        unidade.setId(unidadeId);
        unidade.setAtivo(true);
        unidade.setStatus(com.condominio.chamados.bloco.domain.UnidadeStatus.EM_FUNCIONAMENTO);
        Andar andar = new Andar();
        andar.setAtivo(true);
        Bloco bloco = new Bloco();
        bloco.setAtivo(false);
        andar.setBloco(bloco);
        unidade.setAndar(andar);

        User usuario = new User();
        usuario.setId(usuarioId);
        usuario.setUsername("maria");
        Role morador = new Role();
        morador.setName("MORADOR");
        usuario.setRoles(Set.of(morador));

        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.of(unidade));
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        MoradiaCreateRequest request = new MoradiaCreateRequest();
        request.setUsuarioId(usuarioId.toString());
        request.setUnidadeId(unidadeId.toString());

        assertThatThrownBy(() -> service.criar(unidadeId.toString(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("bloco está inativo");
    }

    @Test
    void deveFinalizarMoradiaNaDataAtual() {
        MoradiaService service = new MoradiaService(moradiaRepository, unidadeRepository, userRepository);

        UUID unidadeId = UUID.randomUUID();
        UUID moradiaId = UUID.randomUUID();

        Unidade unidade = new Unidade();
        unidade.setId(unidadeId);

        Moradia moradia = new Moradia();
        moradia.setId(moradiaId);
        moradia.setUnidade(unidade);
        moradia.setStatus(MoradiaStatus.ATIVA);
        User usuario = new User();
        usuario.setId(UUID.randomUUID());
        usuario.setUsername("maria");
        moradia.setUsuario(usuario);

        when(moradiaRepository.findById(moradiaId)).thenReturn(Optional.of(moradia));
        when(moradiaRepository.save(any(Moradia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.finalizar(unidadeId.toString(), moradiaId.toString());

        assertThat(response.getStatus()).isEqualTo(MoradiaStatus.ENCERRADA);
        assertThat(response.getDataFim()).isNotNull();
    }

    @Test
    void deveBloquearFinalizacaoQuandoMoradiaNaoPertencerAUnidade() {
        MoradiaService service = new MoradiaService(moradiaRepository, unidadeRepository, userRepository);

        UUID unidadeId = UUID.randomUUID();
        UUID moradiaId = UUID.randomUUID();

        Unidade outraUnidade = new Unidade();
        outraUnidade.setId(UUID.randomUUID());

        Moradia moradia = new Moradia();
        moradia.setId(moradiaId);
        moradia.setUnidade(outraUnidade);
        moradia.setStatus(MoradiaStatus.ATIVA);

        when(moradiaRepository.findById(moradiaId)).thenReturn(Optional.of(moradia));

        assertThatThrownBy(() -> service.finalizar(unidadeId.toString(), moradiaId.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pertence à unidade");
    }
}

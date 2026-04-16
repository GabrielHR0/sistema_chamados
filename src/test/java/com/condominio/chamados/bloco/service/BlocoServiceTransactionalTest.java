package com.condominio.chamados.bloco.service;

import com.condominio.chamados.AbstractTestcontainersTest;
import com.condominio.chamados.bloco.domain.Andar;
import com.condominio.chamados.bloco.domain.Bloco;
import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.dto.request.BlocoRequest;
import com.condominio.chamados.bloco.repository.AndarRepository;
import com.condominio.chamados.bloco.repository.BlocoRepository;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.UnidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlocoServiceTransactionalTest extends AbstractTestcontainersTest {

	@Autowired
	private BlocoService blocoService;

	@Autowired
	private BlocoRepository blocoRepository;

	@Autowired
	private AndarRepository andarRepository;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private MoradiaRepository moradiaRepository;

	@BeforeEach
	void limparDados() {
		moradiaRepository.deleteAllInBatch();
		unidadeRepository.deleteAllInBatch();
		andarRepository.deleteAllInBatch();
		blocoRepository.deleteAllInBatch();
	}

	@Test
	void deveCriarBlocoComTodosOsAndaresEUnidades() {
		BlocoRequest request = novoRequest("BLOCO-A", 3, 3);

		Bloco bloco = blocoService.criar(request);

		assertThat(bloco.getId()).isNotNull();
		assertThat(blocoRepository.count()).isEqualTo(1);
		assertThat(andarRepository.count()).isEqualTo(3);
		assertThat(unidadeRepository.count()).isEqualTo(9);
		assertThat(unidadeRepository.findByIdentificacao("BLOCO-A-0101")).isPresent();
		assertThat(unidadeRepository.findByIdentificacao("BLOCO-A-0303")).isPresent();
	}

	@Test
	void deveFazerRollbackQuandoUmaUnidadeViolarUnique() {
		prepararUnidadeConflitante("BLOCO-X-0101");

		BlocoRequest request = novoRequest("BLOCO-X", 1, 1);

		assertThatThrownBy(() -> blocoService.criar(request))
				.isInstanceOf(DataIntegrityViolationException.class);

		assertThat(blocoRepository.findByIdentificacao("BLOCO-X")).isEmpty();
		assertThat(blocoRepository.count()).isEqualTo(1);
		assertThat(andarRepository.count()).isEqualTo(1);
		assertThat(unidadeRepository.count()).isEqualTo(1);
	}

	private void prepararUnidadeConflitante(String identificacaoUnidade) {
		Bloco bloco = new Bloco();
		bloco.setIdentificacao("BASE");
		bloco.setAtivo(true);

		Andar andar = new Andar();
		andar.setBloco(bloco);
		andar.setNumero(1);
		andar.setAtivo(true);

		Unidade unidade = new Unidade();
		unidade.setAndar(andar);
		unidade.setApartamentoNumero(1);
		unidade.setIdentificacao(identificacaoUnidade);
		unidade.setAtivo(true);

		andar.getUnidades().add(unidade);
		bloco.getAndares().add(andar);
		blocoRepository.saveAndFlush(bloco);
	}

	private BlocoRequest novoRequest(String identificacao, int quantidadeAndares, int apartamentosPorAndar) {
		BlocoRequest request = new BlocoRequest();
		request.setIdentificacao(identificacao);
		request.setQuantidadeAndares(quantidadeAndares);
		request.setApartamentosPorAndar(apartamentosPorAndar);
		request.setAtivo(true);
		request.setObservacoes("Teste transacional");
		return request;
	}
}

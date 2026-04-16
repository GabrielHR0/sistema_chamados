package com.condominio.chamados.bloco.service;

import com.condominio.chamados.bloco.domain.Andar;
import com.condominio.chamados.bloco.domain.Bloco;
import com.condominio.chamados.bloco.domain.Unidade;
import com.condominio.chamados.bloco.dto.request.BlocoRequest;
import com.condominio.chamados.bloco.dto.request.BlocoUpdateRequest;
import com.condominio.chamados.bloco.repository.MoradiaRepository;
import com.condominio.chamados.bloco.repository.BlocoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BlocoService {

    private final BlocoRepository blocoRepository;
    private final MoradiaRepository moradiaRepository;

    public BlocoService(BlocoRepository blocoRepository, MoradiaRepository moradiaRepository) {
        this.blocoRepository = blocoRepository;
        this.moradiaRepository = moradiaRepository;
    }

    @Transactional
    public List<Bloco> listar() {
        return blocoRepository.findAll();
    }

    @Transactional
    public Bloco obterPorId(String id) {
        return blocoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Bloco não encontrado: " + id));
    }

    @Transactional(rollbackFor = Exception.class)
    public Bloco criar(BlocoRequest request) {
        if (blocoRepository.findByIdentificacao(request.getIdentificacao()).isPresent()) {
            throw new IllegalArgumentException("Bloco já cadastrado: " + request.getIdentificacao());
        }

        Bloco bloco = new Bloco();
        bloco.setIdentificacao(request.getIdentificacao());
        bloco.setAtivo(request.isAtivo());
        bloco.setObservacoes(request.getObservacoes());

        for (int numeroAndar = 1; numeroAndar <= request.getQuantidadeAndares(); numeroAndar++) {
            Andar andar = new Andar();
            andar.setBloco(bloco);
            andar.setNumero(numeroAndar);
            andar.setAtivo(request.isAtivo());

            for (int numeroApartamento = 1; numeroApartamento <= request.getApartamentosPorAndar(); numeroApartamento++) {
                Unidade unidade = new Unidade();
                unidade.setAndar(andar);
                unidade.setApartamentoNumero(numeroApartamento);
                unidade.setAtivo(request.isAtivo());
                unidade.setIdentificacao(formatarIdentificacao(bloco.getIdentificacao(), numeroAndar, numeroApartamento));
                andar.getUnidades().add(unidade);
            }

            bloco.getAndares().add(andar);
        }

        return blocoRepository.save(bloco);
    }

    @Transactional
    public Bloco atualizar(String id, BlocoUpdateRequest request) {
        Bloco bloco = blocoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Bloco não encontrado: " + id));

        bloco.setAtivo(request.isAtivo());
        bloco.setObservacoes(request.getObservacoes());

        return blocoRepository.save(bloco);
    }

    @Transactional
    public void deletar(String id) {
        UUID blocoId = UUID.fromString(id);
        if (moradiaRepository.existsAnyByBlocoId(blocoId)) {
            throw new IllegalArgumentException("Não é permitido deletar um bloco com moradias vinculadas");
        }

        Bloco bloco = blocoRepository.findById(blocoId)
                .orElseThrow(() -> new IllegalArgumentException("Bloco não encontrado: " + id));
        blocoRepository.delete(bloco);
    }

    private String formatarIdentificacao(String blocoIdentificacao, int numeroAndar, int numeroApartamento) {
        return String.format("%s-%02d%02d", blocoIdentificacao, numeroAndar, numeroApartamento);
    }
}

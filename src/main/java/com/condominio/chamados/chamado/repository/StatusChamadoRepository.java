package com.condominio.chamados.chamado.repository;

import com.condominio.chamados.chamado.domain.StatusChamado;
import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusChamadoRepository extends JpaRepository<StatusChamado, UUID> {
    List<StatusChamado> findAllByOrderByNomeAsc();

    Optional<StatusChamado> findFirstByComportamentoTipoOrderByNomeAsc(StatusComportamentoTipo comportamentoTipo);
    List<StatusChamado> findByComportamentoTipoOrderByNomeAsc(StatusComportamentoTipo comportamentoTipo);

    Optional<StatusChamado> findByNome(String nome);
}

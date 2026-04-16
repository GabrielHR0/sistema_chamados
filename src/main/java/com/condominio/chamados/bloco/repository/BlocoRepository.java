package com.condominio.chamados.bloco.repository;

import com.condominio.chamados.bloco.domain.Bloco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlocoRepository extends JpaRepository<Bloco, UUID> {
    Optional<Bloco> findByIdentificacao(String identificacao);
}

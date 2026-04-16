package com.condominio.chamados.bloco.repository;

import com.condominio.chamados.bloco.domain.Andar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AndarRepository extends JpaRepository<Andar, UUID> {
    List<Andar> findByBloco_IdOrderByNumeroAsc(UUID blocoId);

    Optional<Andar> findByBloco_IdAndNumero(UUID blocoId, Integer numero);
}

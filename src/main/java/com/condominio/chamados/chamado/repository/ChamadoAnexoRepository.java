package com.condominio.chamados.chamado.repository;

import com.condominio.chamados.chamado.domain.ChamadoAnexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChamadoAnexoRepository extends JpaRepository<ChamadoAnexo, UUID> {

    List<ChamadoAnexo> findByChamado_IdOrderByDataCriacaoDesc(UUID chamadoId);

    @Query("""
            select a
            from ChamadoAnexo a
            join fetch a.chamado c
            where a.id = :anexoId
            """)
    Optional<ChamadoAnexo> findByIdWithChamado(@Param("anexoId") UUID anexoId);
}

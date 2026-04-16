package com.condominio.chamados.chamado.repository;

import com.condominio.chamados.chamado.domain.ComentarioAnexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComentarioAnexoRepository extends JpaRepository<ComentarioAnexo, UUID> {

    List<ComentarioAnexo> findByComentario_IdOrderByDataCriacaoDesc(UUID comentarioId);

    @Query("""
            select a
            from ComentarioAnexo a
            join fetch a.comentario c
            join fetch c.chamado ch
            where a.id = :anexoId
            """)
    Optional<ComentarioAnexo> findByIdWithComentarioAndChamado(@Param("anexoId") UUID anexoId);
}

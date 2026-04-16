package com.condominio.chamados.chamado.repository;

import com.condominio.chamados.chamado.domain.ComentarioChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComentarioChamadoRepository extends JpaRepository<ComentarioChamado, UUID> {
    @Query("""
            select distinct cc
            from ComentarioChamado cc
            join fetch cc.autor a
            left join fetch a.perfil p
            left join fetch cc.anexos an
            where cc.chamado.id = :chamadoId
            order by cc.dataCriacao asc
            """)
    List<ComentarioChamado> findByChamadoIdWithAutorOrderByDataCriacaoAsc(@Param("chamadoId") UUID chamadoId);

    @Query("""
            select cc
            from ComentarioChamado cc
            join fetch cc.chamado ch
            where cc.id = :comentarioId
            """)
    Optional<ComentarioChamado> findByIdWithChamado(@Param("comentarioId") UUID comentarioId);
}

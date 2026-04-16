package com.condominio.chamados.bloco.repository;

import com.condominio.chamados.bloco.domain.Moradia;
import com.condominio.chamados.bloco.domain.MoradiaStatus;
import com.condominio.chamados.bloco.domain.Unidade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoradiaRepository extends JpaRepository<Moradia, UUID> {
    @Query("select case when count(m) > 0 then true else false end from Moradia m where m.unidade.andar.bloco.id = :blocoId")
    boolean existsAnyByBlocoId(@Param("blocoId") UUID blocoId);

    @Query("select case when count(m) > 0 then true else false end from Moradia m where m.unidade.andar.id = :andarId")
    boolean existsAnyByAndarId(@Param("andarId") UUID andarId);

    List<Moradia> findByUnidade_IdOrderByDataInicioDesc(UUID unidadeId);

    @Query("""
            select m
            from Moradia m
            join fetch m.unidade u
            join fetch m.usuario usr
            left join fetch usr.perfil p
            order by m.dataInicio desc
            """)
    List<Moradia> findAllWithUsuarioAndUnidadeOrderByDataInicioDesc();

    @Query("""
            select m
            from Moradia m
            join fetch m.unidade u
            join fetch m.usuario usr
            left join fetch usr.perfil p
            where usr.id = :usuarioId
            order by m.dataInicio desc
            """)
    List<Moradia> findByUsuarioIdOrderByDataInicioDesc(@Param("usuarioId") UUID usuarioId);

    Optional<Moradia> findFirstByUnidade_IdAndStatusOrderByDataInicioDesc(UUID unidadeId, MoradiaStatus status);

    @Query("select case when count(m) > 0 then true else false end from Moradia m where m.unidade.id = :unidadeId")
    boolean existsAnyByUnidadeId(@Param("unidadeId") UUID unidadeId);

    @Query("""
            select distinct u
            from Moradia m
            join m.unidade u
            where m.usuario.id = :usuarioId
              and m.status = :status
            order by u.identificacao asc
            """)
    List<Unidade> findUnidadesByUsuarioIdAndStatus(
            @Param("usuarioId") UUID usuarioId,
            @Param("status") MoradiaStatus status
    );
}

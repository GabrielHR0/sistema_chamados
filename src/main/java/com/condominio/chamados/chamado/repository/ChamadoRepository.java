package com.condominio.chamados.chamado.repository;

import com.condominio.chamados.chamado.domain.Chamado;
import com.condominio.chamados.chamado.domain.StatusComportamentoTipo;
import com.condominio.chamados.bloco.domain.MoradiaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, UUID> {
    boolean existsByTipo_Id(UUID tipoId);

    boolean existsByStatus_Id(UUID statusId);

    @Query("""
            select distinct c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
             where (:tipoId is null or c.tipo.id = :tipoId)
               and (:statusId is null or c.status.id = :statusId)
               and (:unidadeId is null or c.unidade.id = :unidadeId)
              and (:responsavelId is null or c.colaboradorResponsavel.id = :responsavelId)
            order by c.dataCriacao desc
            """)
    List<Chamado> findAllWithRelationsOrderByDataCriacaoDesc(
            @Param("tipoId") UUID tipoId,
            @Param("statusId") UUID statusId,
            @Param("unidadeId") UUID unidadeId,
            @Param("responsavelId") UUID responsavelId
    );

    @Query("""
            select c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
             where (
                    c.colaboradorResponsavel.id = :userId
                    or t.id in (
                        select tp.id
                        from User u2
                        join u2.lotacoes l
                        join l.tiposChamado tp
                        where u2.id = :userId
                    )
              )
              and (:tipoId is null or c.tipo.id = :tipoId)
              and (:statusId is null or c.status.id = :statusId)
              and (:unidadeId is null or c.unidade.id = :unidadeId)
              and (:responsavelId is null or c.colaboradorResponsavel.id = :responsavelId)
            order by c.dataCriacao desc
            """)
    List<Chamado> findScopedByUserWithRelationsOrderByDataCriacaoDesc(
            @Param("userId") UUID userId,
            @Param("tipoId") UUID tipoId,
            @Param("statusId") UUID statusId,
            @Param("unidadeId") UUID unidadeId,
            @Param("responsavelId") UUID responsavelId
    );

    @Query("""
            select distinct c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
             where c.colaboradorResponsavel is null
              and (:tipoId is null or c.tipo.id = :tipoId)
              and (:statusId is null or c.status.id = :statusId)
            order by c.dataCriacao desc
            """)
    List<Chamado> findDisponiveisWithRelationsOrderByDataCriacaoDesc(
            @Param("tipoId") UUID tipoId,
            @Param("statusId") UUID statusId
    );

    @Query("""
            select distinct c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
             where c.colaboradorResponsavel is null
              and t.id in (
                select tp.id
                from User u2
                join u2.lotacoes l
                join l.tiposChamado tp
                where u2.id = :userId
              )
              and (:tipoId is null or c.tipo.id = :tipoId)
              and (:statusId is null or c.status.id = :statusId)
            order by c.dataCriacao desc
            """)
    List<Chamado> findDisponiveisByUserLotacaoWithRelationsOrderByDataCriacaoDesc(
            @Param("userId") UUID userId,
            @Param("tipoId") UUID tipoId,
            @Param("statusId") UUID statusId
    );

    @Query("""
            select distinct c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
             where c.colaboradorResponsavel.id = :responsavelId
              and s.comportamentoTipo <> :comportamentoTipo
            order by c.dataCriacao desc
            """)
    List<Chamado> findNaoFinaisByResponsavelWithRelationsOrderByDataCriacaoDesc(
            @Param("responsavelId") UUID responsavelId,
            @Param("comportamentoTipo") StatusComportamentoTipo comportamentoTipo
    );

    @Query("""
            select distinct c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
             where c.id = :chamadoId
              and c.colaboradorResponsavel is null
              and t.id in (
                select tp.id
                from User u2
                join u2.lotacoes l
                join l.tiposChamado tp
                where u2.id = :userId
              )
            """)
    Optional<Chamado> findByIdDisponivelScopedByUserLotacao(
            @Param("chamadoId") UUID chamadoId,
            @Param("userId") UUID userId
    );

    @Query("""
            select c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
             where c.id = :chamadoId
            """)
    Optional<Chamado> findByIdWithRelations(@Param("chamadoId") UUID chamadoId);

    @Query("""
            select c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
             join fetch c.status s
             left join fetch c.colaboradorResponsavel cr
             left join fetch cr.perfil p
             left join fetch c.solicitante sol
             left join fetch sol.perfil sp
              where c.id = :chamadoId
                and (
                    c.colaboradorResponsavel.id = :userId
                    or t.id in (
                        select tp.id
                        from User u2
                        join u2.lotacoes l
                        join l.tiposChamado tp
                        where u2.id = :userId
                    )
                )
            """)
    Optional<Chamado> findByIdWithRelationsScopedByUser(
            @Param("chamadoId") UUID chamadoId,
            @Param("userId") UUID userId
    );

    @Query("""
            select c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
            join fetch c.status s
            left join fetch c.colaboradorResponsavel cr
            left join fetch cr.perfil p
            left join fetch c.solicitante sol
            left join fetch sol.perfil sp
            where c.solicitante.id = :userId
            order by c.dataCriacao desc
            """)
    List<Chamado> findBySolicitanteWithRelationsOrderByDataCriacaoDesc(@Param("userId") UUID userId);

    @Query("""
            select c
            from Chamado c
            join fetch c.unidade u
            join fetch c.tipo t
            join fetch c.status s
            left join fetch c.colaboradorResponsavel cr
            left join fetch cr.perfil p
            left join fetch c.solicitante sol
            left join fetch sol.perfil sp
            where c.id = :chamadoId
              and (
                   c.solicitante.id = :userId
                   or c.unidade.id in (
                        select m.unidade.id
                        from Moradia m
                        where m.usuario.id = :userId
                          and m.status = :moradiaStatus
                   )
              )
            """)
    Optional<Chamado> findByIdWithRelationsScopedByMorador(
            @Param("chamadoId") UUID chamadoId,
            @Param("userId") UUID userId,
            @Param("moradiaStatus") MoradiaStatus moradiaStatus
    );
}

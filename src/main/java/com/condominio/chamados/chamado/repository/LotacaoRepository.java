package com.condominio.chamados.chamado.repository;

import com.condominio.chamados.chamado.domain.Lotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LotacaoRepository extends JpaRepository<Lotacao, UUID> {
    List<Lotacao> findAllByOrderByNomeAsc();

    Optional<Lotacao> findByNome(String nome);

    @Query("""
            select distinct l
            from Lotacao l
            left join fetch l.tiposChamado
            """)
    List<Lotacao> findAllWithTipos();

    @Query("""
            select distinct l
            from Lotacao l
            left join fetch l.tiposChamado
            where l.id = :id
            """)
    Optional<Lotacao> findByIdWithTipos(@Param("id") UUID id);
}

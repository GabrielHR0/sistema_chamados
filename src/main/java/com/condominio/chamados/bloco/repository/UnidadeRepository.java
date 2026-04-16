package com.condominio.chamados.bloco.repository;

import com.condominio.chamados.bloco.domain.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, UUID> {
    List<Unidade> findAllByOrderByIdentificacaoAsc();

    List<Unidade> findByAndar_IdOrderByApartamentoNumeroAsc(UUID andarId);

    Optional<Unidade> findByIdentificacao(String identificacao);

    Optional<Unidade> findByAndar_IdAndApartamentoNumero(UUID andarId, Integer apartamentoNumero);

    @Query("""
            select u
            from Unidade u
            where lower(u.identificacao) like lower(concat('%', :termo, '%'))
            order by u.identificacao
            """)
    List<Unidade> searchByIdentificacao(@Param("termo") String termo, Pageable pageable);
}

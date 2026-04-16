package com.condominio.chamados.chamado.repository;

import com.condominio.chamados.chamado.domain.TipoChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoChamadoRepository extends JpaRepository<TipoChamado, UUID> {
    List<TipoChamado> findAllByOrderByTituloAsc();

    Optional<TipoChamado> findByTitulo(String titulo);
}

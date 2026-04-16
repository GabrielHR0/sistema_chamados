package com.condominio.chamados.security.repository;

import com.condominio.chamados.security.domain.UserFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFotoRepository extends JpaRepository<UserFoto, UUID> {
    Optional<UserFoto> findByPerfil_User_Id(UUID userId);
}

package com.condominio.chamados.security.repository;

import com.condominio.chamados.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("""
            select distinct u
            from User u
            left join fetch u.perfil p
            left join fetch u.moradias m
            """)
    List<User> findAllWithMoradias();

    @Query("""
            select u
            from User u
            left join u.perfil p
            where u.enabled = true
              and (
                   lower(coalesce(p.nomeCompleto, '')) like lower(concat('%', :termo, '%'))
                or lower(u.username) like lower(concat('%', :termo, '%'))
              )
            order by coalesce(p.nomeCompleto, u.username)
            """)
    List<User> searchEnabledByName(@Param("termo") String termo, Pageable pageable);

    @Query("""
            select u
            from User u
            left join fetch u.perfil p
            where u.enabled = true
              and exists (
                    select 1
                    from u.roles r
                    where r.name = 'COLABORADOR'
              )
            order by p.nomeCompleto, u.username
            """)
    List<User> findEnabledCollaborators();

    @Query("""
            select distinct u
            from User u
            left join fetch u.roles r
            left join fetch u.tiposChamadoPermitidos t
            where u.id = :userId
            """)
    Optional<User> findByIdWithRolesAndTipos(@Param("userId") UUID userId);
}

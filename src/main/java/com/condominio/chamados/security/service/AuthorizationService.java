package com.condominio.chamados.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Serviço para verificação de permissões em tempo de execução.
 * Complementa as anotações @PreAuthorize para lógica mais complexa.
 */
@Service
public class AuthorizationService {

    /**
     * Verifica se o usuário atual tem uma permissão específica.
     *
     * @param permission a permissão no formato "RESOURCE:ACTION"
     * @return true se o usuário tem a permissão, false caso contrário
     */
    public boolean hasPermission(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(permission));
    }

    /**
     * Verifica se o usuário tem qualquer uma das permissões fornecidas.
     *
     * @param permissions lista de permissões no formato "RESOURCE:ACTION"
     * @return true se o usuário tem pelo menos uma permissão
     */
    public boolean hasAnyPermission(String... permissions) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> {
                    for (String permission : permissions) {
                        if (authority.equals(permission)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    /**
     * Verifica se o usuário tem todas as permissões fornecidas.
     *
     * @param permissions lista de permissões no formato "RESOURCE:ACTION"
     * @return true se o usuário tem todas as permissões
     */
    public boolean hasAllPermissions(String... permissions) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        var authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        for (String permission : permissions) {
            if (!authorities.contains(permission)) {
                return false;
            }
        }
        return true;
    }
}

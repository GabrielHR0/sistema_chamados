package com.condominio.chamados.security.service;

import com.condominio.chamados.security.domain.Permission;
import com.condominio.chamados.security.domain.Role;
import com.condominio.chamados.security.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserPrincipal implements UserDetails {

    private final User user;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonLocked;

    public UserPrincipal(User user, boolean accountNonLocked) {
        this.user = user;
        this.authorities = buildAuthorities(user);
        this.accountNonLocked = accountNonLocked;
    }

    private Set<GrantedAuthority> buildAuthorities(User user) {
        Set<GrantedAuthority> auths = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getName() != null) {
                    auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                }
                if (role.getPermissions() != null) {
                    for (Permission p : role.getPermissions()) {
                        String perm = p.getResource() + ":" + p.getAction();
                        auths.add(new SimpleGrantedAuthority(perm));
                    }
                }
            }
        }
        return auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal)) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId());
    }
}

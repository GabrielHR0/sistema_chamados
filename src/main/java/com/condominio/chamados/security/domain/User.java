package com.condominio.chamados.security.domain;

import com.condominio.chamados.chamado.domain.TipoChamado;
import com.condominio.chamados.chamado.domain.Lotacao;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.condominio.chamados.bloco.domain.Moradia;
import com.condominio.chamados.security.domain.Role;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "user_name", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name= "password", nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, optional = false)
    private Perfil perfil = new Perfil();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_tipo_chamado",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_id")
    )
    private Set<TipoChamado> tiposChamadoPermitidos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_lotacao",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lotacao_id")
    )
    private Set<Lotacao> lotacoes = new HashSet<>();

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Set<Moradia> moradias = new HashSet<>();

    public User() {
        setPerfil(new Perfil());
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        if (perfil != null && perfil.getUser() != this) {
            perfil.setUser(this);
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Moradia> getMoradias() {
        return moradias;
    }

    public void setMoradias(Set<Moradia> moradias) {
        this.moradias = moradias;
    }

    public Set<TipoChamado> getTiposChamadoPermitidos() {
        return tiposChamadoPermitidos;
    }

    public void setTiposChamadoPermitidos(Set<TipoChamado> tiposChamadoPermitidos) {
        this.tiposChamadoPermitidos = tiposChamadoPermitidos;
    }

    public Set<Lotacao> getLotacoes() {
        return lotacoes;
    }

    public void setLotacoes(Set<Lotacao> lotacoes) {
        this.lotacoes = lotacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

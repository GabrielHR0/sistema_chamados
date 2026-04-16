package com.condominio.chamados.chamado.domain;

import com.condominio.chamados.chamado.state.EstadoBehavior;
import com.condominio.chamados.chamado.state.EstadoBehaviorFactory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "chamado_status")
public class StatusChamado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "comportamento_tipo", nullable = false, length = 20)
    private StatusComportamentoTipo comportamentoTipo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chamado_status_proximos",
            joinColumns = @JoinColumn(name = "status_id"),
            inverseJoinColumns = @JoinColumn(name = "proximo_status_id")
    )
    private Set<StatusChamado> proximos = new LinkedHashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusComportamentoTipo getComportamentoTipo() {
        return comportamentoTipo;
    }

    public void setComportamentoTipo(StatusComportamentoTipo comportamentoTipo) {
        this.comportamentoTipo = comportamentoTipo;
        validarRegrasDeProximos();
    }

    public Set<StatusChamado> getProximos() {
        return proximos;
    }

    public void setProximos(Set<StatusChamado> proximos) {
        this.proximos = proximos != null ? new LinkedHashSet<>(proximos) : new LinkedHashSet<>();
        validarRegrasDeProximos();
    }

    public void addProximo(StatusChamado proximo) {
        this.proximos.add(proximo);
        validarRegrasDeProximos();
    }

    public void validarProximo(StatusChamado proximo) {
        if (comportamentoTipo == null) {
            throw new IllegalStateException("Comportamento do status atual nao foi definido.");
        }
        getBehavior().validarProximo(this, proximo);
    }

    @Transient
    public EstadoBehavior getBehavior() {
        return EstadoBehaviorFactory.fromTipo(comportamentoTipo);
    }

    @PrePersist
    @PreUpdate
    void validarConsistencia() {
        validarRegrasDeProximos();
    }

    private void validarRegrasDeProximos() {
        if (comportamentoTipo == null) {
            return;
        }
        getBehavior().validarProximosConfigurados(this, proximos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusChamado)) return false;
        StatusChamado that = (StatusChamado) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}

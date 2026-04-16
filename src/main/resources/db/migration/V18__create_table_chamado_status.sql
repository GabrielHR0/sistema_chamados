CREATE TABLE IF NOT EXISTS chamado_status (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL UNIQUE,
    comportamento_tipo VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_chamado_status_tipo CHECK (comportamento_tipo IN ('INICIAL', 'INTERMEDIARIO', 'FINAL'))
);

CREATE TABLE IF NOT EXISTS chamado_status_proximos (
    status_id UUID NOT NULL,
    proximo_status_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (status_id, proximo_status_id),
    CONSTRAINT fk_chamado_status_proximos_status
        FOREIGN KEY (status_id) REFERENCES chamado_status(id) ON DELETE CASCADE,
    CONSTRAINT fk_chamado_status_proximos_proximo
        FOREIGN KEY (proximo_status_id) REFERENCES chamado_status(id) ON DELETE CASCADE,
    CONSTRAINT ck_chamado_status_proximos_diff CHECK (status_id <> proximo_status_id)
);

CREATE INDEX IF NOT EXISTS idx_chamado_status_nome ON chamado_status (nome);
CREATE INDEX IF NOT EXISTS idx_chamado_status_proximos_status ON chamado_status_proximos (status_id);

-- V10: create unidades table
-- Unidades sao geradas a partir da combinacao andar + apartamento.
CREATE TABLE IF NOT EXISTS unidades (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    identificacao VARCHAR(20) NOT NULL UNIQUE,
    andar_id UUID NOT NULL,
    apartamento_numero INTEGER NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    observacoes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_unidades_apartamento_numero CHECK (apartamento_numero > 0)
);

CREATE INDEX IF NOT EXISTS idx_unidades_andar_id ON unidades (andar_id);
CREATE INDEX IF NOT EXISTS idx_unidades_identificacao ON unidades (identificacao);

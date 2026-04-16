-- V11: create andares table
-- Andares pertencem a um bloco e serao vinculados por bloco_id.
CREATE TABLE IF NOT EXISTS andares (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bloco_id UUID NOT NULL,
    numero INTEGER NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    observacoes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_andares_numero CHECK (numero > 0),
    CONSTRAINT uq_andares_bloco_numero UNIQUE (bloco_id, numero)
);

CREATE INDEX IF NOT EXISTS idx_andares_bloco_id ON andares (bloco_id);

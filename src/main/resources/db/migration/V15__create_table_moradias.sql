-- V15: create moradias table
CREATE TABLE IF NOT EXISTS moradias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    unidade_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_moradias_usuario FOREIGN KEY (usuario_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_moradias_unidade FOREIGN KEY (unidade_id) REFERENCES unidades(id) ON DELETE RESTRICT,
    CONSTRAINT ck_moradias_status CHECK (status IN ('ATIVA', 'ENCERRADA', 'TRANSFERIDA')),
    CONSTRAINT ck_moradias_data_fim CHECK (data_fim IS NULL OR data_fim >= data_inicio)
);

CREATE INDEX IF NOT EXISTS idx_moradias_usuario_id ON moradias (usuario_id);
CREATE INDEX IF NOT EXISTS idx_moradias_unidade_id ON moradias (unidade_id);
CREATE INDEX IF NOT EXISTS idx_moradias_status ON moradias (status);

CREATE UNIQUE INDEX IF NOT EXISTS uq_moradias_unidade_ativa
    ON moradias (unidade_id)
    WHERE status = 'ATIVA';

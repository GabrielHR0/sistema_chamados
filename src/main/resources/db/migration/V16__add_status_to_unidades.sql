-- V16: add status column to unidades
ALTER TABLE unidades
    ADD COLUMN IF NOT EXISTS status VARCHAR(30) NOT NULL DEFAULT 'DISPONIVEL';

CREATE INDEX IF NOT EXISTS idx_unidades_status ON unidades (status);

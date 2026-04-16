ALTER TABLE chamados
    ADD COLUMN IF NOT EXISTS solicitante_id UUID;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_chamados_solicitante'
    ) THEN
        ALTER TABLE chamados
            ADD CONSTRAINT fk_chamados_solicitante
                FOREIGN KEY (solicitante_id) REFERENCES users(id) ON DELETE RESTRICT;
    END IF;
END$$;

CREATE INDEX IF NOT EXISTS idx_chamados_solicitante_id ON chamados (solicitante_id);

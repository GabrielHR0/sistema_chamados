ALTER TABLE tipos_chamado
    ADD COLUMN IF NOT EXISTS status_inicial_id UUID;

DO $$
DECLARE
    v_status_inicial_id UUID;
BEGIN
    SELECT id
    INTO v_status_inicial_id
    FROM chamado_status
    WHERE comportamento_tipo = 'INICIAL'
    ORDER BY nome
    LIMIT 1;

    IF v_status_inicial_id IS NOT NULL THEN
        UPDATE tipos_chamado
        SET status_inicial_id = v_status_inicial_id
        WHERE status_inicial_id IS NULL;
    END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_tipos_chamado_status_inicial'
    ) THEN
        ALTER TABLE tipos_chamado
            ADD CONSTRAINT fk_tipos_chamado_status_inicial
                FOREIGN KEY (status_inicial_id) REFERENCES chamado_status(id) ON DELETE RESTRICT;
    END IF;
END$$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM chamado_status WHERE comportamento_tipo = 'INICIAL')
       AND NOT EXISTS (SELECT 1 FROM tipos_chamado WHERE status_inicial_id IS NULL) THEN
        ALTER TABLE tipos_chamado
            ALTER COLUMN status_inicial_id SET NOT NULL;
    END IF;
END$$;

CREATE INDEX IF NOT EXISTS idx_tipos_chamado_status_inicial_id ON tipos_chamado (status_inicial_id);

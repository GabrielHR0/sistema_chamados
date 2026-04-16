-- Codigo curto e unico para identificacao amigavel de chamados.
CREATE OR REPLACE FUNCTION gerar_codigo_chamado_curto()
RETURNS VARCHAR(8) AS $$
DECLARE
    novo_codigo VARCHAR(8);
BEGIN
    LOOP
        novo_codigo := upper(substr(md5(gen_random_uuid()::text), 1, 8));
        EXIT WHEN NOT EXISTS (SELECT 1 FROM chamados c WHERE c.codigo = novo_codigo);
    END LOOP;
    RETURN novo_codigo;
END;
$$ LANGUAGE plpgsql;

ALTER TABLE chamados
    ADD COLUMN IF NOT EXISTS codigo VARCHAR(8);

UPDATE chamados
SET codigo = gerar_codigo_chamado_curto()
WHERE codigo IS NULL;

ALTER TABLE chamados
    ALTER COLUMN codigo SET NOT NULL,
    ALTER COLUMN codigo SET DEFAULT gerar_codigo_chamado_curto();

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uq_chamados_codigo'
    ) THEN
        ALTER TABLE chamados
            ADD CONSTRAINT uq_chamados_codigo UNIQUE (codigo);
    END IF;
END
$$;

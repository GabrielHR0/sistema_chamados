CREATE TABLE IF NOT EXISTS lotacoes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(120) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_lotacoes_nome CHECK (length(btrim(nome)) > 0)
);

CREATE TABLE IF NOT EXISTS lotacao_tipo_chamado (
    lotacao_id UUID NOT NULL,
    tipo_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (lotacao_id, tipo_id),
    CONSTRAINT fk_lotacao_tipo_chamado_lotacao FOREIGN KEY (lotacao_id) REFERENCES lotacoes(id) ON DELETE CASCADE,
    CONSTRAINT fk_lotacao_tipo_chamado_tipo FOREIGN KEY (tipo_id) REFERENCES tipos_chamado(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_lotacoes_nome ON lotacoes (nome);
CREATE INDEX IF NOT EXISTS idx_lotacao_tipo_chamado_lotacao_id ON lotacao_tipo_chamado (lotacao_id);
CREATE INDEX IF NOT EXISTS idx_lotacao_tipo_chamado_tipo_id ON lotacao_tipo_chamado (tipo_id);

DROP TABLE IF EXISTS user_tipo_chamado;

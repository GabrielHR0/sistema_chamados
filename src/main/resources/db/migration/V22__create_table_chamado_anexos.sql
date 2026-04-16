CREATE TABLE IF NOT EXISTS chamado_anexos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chamado_id UUID NOT NULL,
    nome_original VARCHAR(255) NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL UNIQUE,
    content_type VARCHAR(150),
    tamanho_bytes BIGINT NOT NULL,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_chamado_anexos_chamado FOREIGN KEY (chamado_id) REFERENCES chamados(id) ON DELETE CASCADE,
    CONSTRAINT ck_chamado_anexos_tamanho_bytes CHECK (tamanho_bytes > 0)
);

CREATE INDEX IF NOT EXISTS idx_chamado_anexos_chamado_id ON chamado_anexos (chamado_id);
CREATE INDEX IF NOT EXISTS idx_chamado_anexos_data_criacao ON chamado_anexos (data_criacao);

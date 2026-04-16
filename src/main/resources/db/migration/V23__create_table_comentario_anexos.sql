CREATE TABLE IF NOT EXISTS comentario_anexos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    comentario_id UUID NOT NULL,
    nome_original VARCHAR(255) NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL UNIQUE,
    content_type VARCHAR(150),
    tamanho_bytes BIGINT NOT NULL,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_comentario_anexos_comentario FOREIGN KEY (comentario_id) REFERENCES chamado_comentarios(id) ON DELETE CASCADE,
    CONSTRAINT ck_comentario_anexos_tamanho_bytes CHECK (tamanho_bytes > 0)
);

CREATE INDEX IF NOT EXISTS idx_comentario_anexos_comentario_id ON comentario_anexos (comentario_id);
CREATE INDEX IF NOT EXISTS idx_comentario_anexos_data_criacao ON comentario_anexos (data_criacao);

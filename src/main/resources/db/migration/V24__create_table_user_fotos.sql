CREATE TABLE IF NOT EXISTS user_fotos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    nome_original VARCHAR(255) NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL UNIQUE,
    content_type VARCHAR(150),
    tamanho_bytes BIGINT NOT NULL,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_user_fotos_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT ck_user_fotos_tamanho_bytes CHECK (tamanho_bytes > 0)
);

CREATE INDEX IF NOT EXISTS idx_user_fotos_data_criacao ON user_fotos (data_criacao);

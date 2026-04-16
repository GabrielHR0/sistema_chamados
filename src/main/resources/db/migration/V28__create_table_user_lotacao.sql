CREATE TABLE IF NOT EXISTS user_lotacao (
    user_id UUID NOT NULL,
    lotacao_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, lotacao_id),
    CONSTRAINT fk_user_lotacao_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_lotacao_lotacao FOREIGN KEY (lotacao_id) REFERENCES lotacoes(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_lotacao_user_id ON user_lotacao (user_id);
CREATE INDEX IF NOT EXISTS idx_user_lotacao_lotacao_id ON user_lotacao (lotacao_id);

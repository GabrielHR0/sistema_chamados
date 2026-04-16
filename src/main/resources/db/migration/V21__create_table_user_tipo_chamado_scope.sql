CREATE TABLE IF NOT EXISTS user_tipo_chamado (
    user_id UUID NOT NULL,
    tipo_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, tipo_id),
    CONSTRAINT fk_user_tipo_chamado_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_tipo_chamado_tipo FOREIGN KEY (tipo_id) REFERENCES tipos_chamado(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_tipo_chamado_user_id ON user_tipo_chamado (user_id);
CREATE INDEX IF NOT EXISTS idx_user_tipo_chamado_tipo_id ON user_tipo_chamado (tipo_id);

-- Default de compatibilidade: colaboradores existentes recebem escopo para os tipos já cadastrados.
INSERT INTO user_tipo_chamado (user_id, tipo_id)
SELECT u.id, t.id
FROM users u
JOIN user_role ur ON ur.user_id = u.id
JOIN roles r ON r.id = ur.role_id AND r.name = 'COLABORADOR'
JOIN tipos_chamado t ON TRUE
ON CONFLICT DO NOTHING;

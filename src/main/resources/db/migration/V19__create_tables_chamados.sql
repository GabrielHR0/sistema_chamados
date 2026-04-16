CREATE TABLE IF NOT EXISTS tipos_chamado (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(120) NOT NULL UNIQUE,
    sla_horas INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_tipos_chamado_sla_horas CHECK (sla_horas > 0)
);

CREATE TABLE IF NOT EXISTS chamados (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    unidade_id UUID NOT NULL,
    tipo_id UUID NOT NULL,
    status_id UUID NOT NULL,
    colaborador_responsavel_id UUID,
    descricao TEXT NOT NULL,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT now(),
    data_finalizacao TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_chamados_unidade FOREIGN KEY (unidade_id) REFERENCES unidades(id) ON DELETE RESTRICT,
    CONSTRAINT fk_chamados_tipo FOREIGN KEY (tipo_id) REFERENCES tipos_chamado(id) ON DELETE RESTRICT,
    CONSTRAINT fk_chamados_status FOREIGN KEY (status_id) REFERENCES chamado_status(id) ON DELETE RESTRICT,
    CONSTRAINT fk_chamados_colaborador_responsavel FOREIGN KEY (colaborador_responsavel_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT ck_chamados_descricao CHECK (length(btrim(descricao)) > 0),
    CONSTRAINT ck_chamados_data_finalizacao CHECK (data_finalizacao IS NULL OR data_finalizacao >= data_criacao)
);

CREATE TABLE IF NOT EXISTS chamado_comentarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chamado_id UUID NOT NULL,
    autor_id UUID NOT NULL,
    comentario TEXT NOT NULL,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_chamado_comentarios_chamado FOREIGN KEY (chamado_id) REFERENCES chamados(id) ON DELETE CASCADE,
    CONSTRAINT fk_chamado_comentarios_autor FOREIGN KEY (autor_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT ck_chamado_comentarios_comentario CHECK (length(btrim(comentario)) > 0)
);

CREATE INDEX IF NOT EXISTS idx_tipos_chamado_titulo ON tipos_chamado (titulo);
CREATE INDEX IF NOT EXISTS idx_chamados_unidade_id ON chamados (unidade_id);
CREATE INDEX IF NOT EXISTS idx_chamados_tipo_id ON chamados (tipo_id);
CREATE INDEX IF NOT EXISTS idx_chamados_status_id ON chamados (status_id);
CREATE INDEX IF NOT EXISTS idx_chamados_colaborador_responsavel_id ON chamados (colaborador_responsavel_id);
CREATE INDEX IF NOT EXISTS idx_chamados_data_criacao ON chamados (data_criacao);
CREATE INDEX IF NOT EXISTS idx_chamado_comentarios_chamado_id ON chamado_comentarios (chamado_id);
CREATE INDEX IF NOT EXISTS idx_chamado_comentarios_autor_id ON chamado_comentarios (autor_id);
CREATE INDEX IF NOT EXISTS idx_chamado_comentarios_data_criacao ON chamado_comentarios (data_criacao);

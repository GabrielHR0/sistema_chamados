-- Bootstrap SQL de dados de desenvolvimento.
-- Executa inserts/upserts idempotentes para preparar ambiente local.
BEGIN;

-- =========================
-- STATUS DE CHAMADO (grafo base)
-- =========================
INSERT INTO chamado_status (id, nome, comportamento_tipo, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'ABERTO', 'INICIAL', now(), now()),
    (gen_random_uuid(), 'EM_ATENDIMENTO', 'INTERMEDIARIO', now(), now()),
    (gen_random_uuid(), 'AGUARDANDO_MORADOR', 'INTERMEDIARIO', now(), now()),
    (gen_random_uuid(), 'RESOLVIDO', 'FINAL', now(), now()),
    (gen_random_uuid(), 'CANCELADO', 'FINAL', now(), now())
ON CONFLICT (nome) DO UPDATE
SET comportamento_tipo = EXCLUDED.comportamento_tipo,
    updated_at = now();

INSERT INTO chamado_status_proximos (status_id, proximo_status_id, created_at)
SELECT atual.id, prox.id, now()
FROM chamado_status atual
JOIN chamado_status prox
  ON (atual.nome = 'ABERTO' AND prox.nome IN ('EM_ATENDIMENTO', 'CANCELADO'))
  OR (atual.nome = 'EM_ATENDIMENTO' AND prox.nome IN ('AGUARDANDO_MORADOR', 'RESOLVIDO', 'CANCELADO'))
  OR (atual.nome = 'AGUARDANDO_MORADOR' AND prox.nome IN ('EM_ATENDIMENTO', 'RESOLVIDO', 'CANCELADO'))
ON CONFLICT (status_id, proximo_status_id) DO NOTHING;

-- =========================
-- TIPOS DE CHAMADO
-- =========================
INSERT INTO tipos_chamado (id, titulo, sla_horas, status_inicial_id, created_at, updated_at)
SELECT gen_random_uuid(), tipo.titulo, tipo.sla_horas, st.id, now(), now()
FROM (
    VALUES
        ('ELETRICA', 24),
        ('HIDRAULICA', 24),
        ('LIMPEZA', 48),
        ('SEGURANCA', 12),
        ('MANUTENCAO_GERAL', 72)
) AS tipo(titulo, sla_horas)
JOIN chamado_status st ON st.nome = 'ABERTO'
ON CONFLICT (titulo) DO UPDATE
SET sla_horas = EXCLUDED.sla_horas,
    status_inicial_id = EXCLUDED.status_inicial_id,
    updated_at = now();

-- =========================
-- LOTACOES E RELACAO COM TIPOS
-- =========================
INSERT INTO lotacoes (id, nome, descricao, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'EQUIPE_MANUTENCAO', 'Equipe de manutencao predial', now(), now()),
    (gen_random_uuid(), 'EQUIPE_PORTARIA', 'Equipe de portaria e seguranca', now(), now()),
    (gen_random_uuid(), 'EQUIPE_SERVICOS_GERAIS', 'Equipe de limpeza e servicos gerais', now(), now())
ON CONFLICT (nome) DO UPDATE
SET descricao = EXCLUDED.descricao,
    updated_at = now();

INSERT INTO lotacao_tipo_chamado (lotacao_id, tipo_id, created_at)
SELECT l.id, t.id, now()
FROM lotacoes l
JOIN tipos_chamado t
  ON (l.nome = 'EQUIPE_MANUTENCAO' AND t.titulo IN ('ELETRICA', 'HIDRAULICA', 'MANUTENCAO_GERAL'))
  OR (l.nome = 'EQUIPE_PORTARIA' AND t.titulo IN ('SEGURANCA'))
  OR (l.nome = 'EQUIPE_SERVICOS_GERAIS' AND t.titulo IN ('LIMPEZA'))
ON CONFLICT (lotacao_id, tipo_id) DO NOTHING;

-- =========================
-- USUARIOS DEV (COLABORADORES E MORADORES)
-- =========================
INSERT INTO users (id, user_name, email, password, enabled, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'colab.manutencao', 'colab.manutencao@dev.local', crypt('dev123', gen_salt('bf', 12)), TRUE, now(), now()),
    (gen_random_uuid(), 'colab.portaria', 'colab.portaria@dev.local', crypt('dev123', gen_salt('bf', 12)), TRUE, now(), now()),
    (gen_random_uuid(), 'morador.101', 'morador.101@dev.local', crypt('dev123', gen_salt('bf', 12)), TRUE, now(), now()),
    (gen_random_uuid(), 'morador.102', 'morador.102@dev.local', crypt('dev123', gen_salt('bf', 12)), TRUE, now(), now())
ON CONFLICT (email) DO UPDATE
SET user_name = EXCLUDED.user_name,
    password = EXCLUDED.password,
    enabled = TRUE,
    updated_at = now();

INSERT INTO profiles (id, user_id, nome_completo, created_at, updated_at)
SELECT gen_random_uuid(), u.id, p.nome_completo, now(), now()
FROM users u
JOIN (
    VALUES
        ('colab.manutencao@dev.local', 'Colaborador Manutencao'),
        ('colab.portaria@dev.local', 'Colaborador Portaria'),
        ('morador.101@dev.local', 'Morador Unidade 101'),
        ('morador.102@dev.local', 'Morador Unidade 102')
) AS p(email, nome_completo) ON p.email = u.email
ON CONFLICT (user_id) DO UPDATE
SET nome_completo = EXCLUDED.nome_completo,
    updated_at = now();

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'COLABORADOR'
WHERE u.email IN ('colab.manutencao@dev.local', 'colab.portaria@dev.local')
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'MORADOR'
WHERE u.email IN ('morador.101@dev.local', 'morador.102@dev.local')
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Vinculo de colaboradores com lotacoes
INSERT INTO user_lotacao (user_id, lotacao_id, created_at)
SELECT u.id, l.id, now()
FROM users u
JOIN lotacoes l
  ON (u.email = 'colab.manutencao@dev.local' AND l.nome = 'EQUIPE_MANUTENCAO')
  OR (u.email = 'colab.portaria@dev.local' AND l.nome = 'EQUIPE_PORTARIA')
ON CONFLICT (user_id, lotacao_id) DO NOTHING;

-- Compatibilidade com escopo legado por usuario/tipo
INSERT INTO user_tipo_chamado (user_id, tipo_id, created_at)
SELECT DISTINCT ul.user_id, ltc.tipo_id, now()
FROM user_lotacao ul
JOIN lotacao_tipo_chamado ltc ON ltc.lotacao_id = ul.lotacao_id
ON CONFLICT (user_id, tipo_id) DO NOTHING;

COMMIT;

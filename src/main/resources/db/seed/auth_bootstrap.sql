-- Bootstrap SQL de dados base de autenticacao/autorizacao.
-- Nao e migration Flyway versionada: execute quando precisar preparar
-- ambiente local com dados iniciais de acesso.
BEGIN;

-- =========================
-- PERMISSIONS
-- =========================
INSERT INTO permissions (id, resource, action, description, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'USER', 'CREATE', 'Permission to create users', now(), now()),
    (gen_random_uuid(), 'USER', 'UPDATE', 'Permission to update users', now(), now()),
    (gen_random_uuid(), 'USER', 'DELETE', 'Permission to delete users', now(), now()),
    (gen_random_uuid(), 'USER', 'READ', 'Permission to read users', now(), now()),
    (gen_random_uuid(), 'BLOCO', 'CREATE', 'Permission to create blocks', now(), now()),
    (gen_random_uuid(), 'BLOCO', 'READ', 'Permission to read blocks', now(), now()),
    (gen_random_uuid(), 'BLOCO', 'UPDATE', 'Permission to update blocks', now(), now()),
    (gen_random_uuid(), 'BLOCO', 'DELETE', 'Permission to delete blocks', now(), now()),
    (gen_random_uuid(), 'ANDAR', 'CREATE', 'Permission to create floors', now(), now()),
    (gen_random_uuid(), 'ANDAR', 'READ', 'Permission to read floors', now(), now()),
    (gen_random_uuid(), 'ANDAR', 'UPDATE', 'Permission to update floors', now(), now()),
    (gen_random_uuid(), 'ANDAR', 'DELETE', 'Permission to delete floors', now(), now()),
    (gen_random_uuid(), 'UNIDADE', 'CREATE', 'Permission to create units', now(), now()),
    (gen_random_uuid(), 'UNIDADE', 'READ', 'Permission to read units', now(), now()),
    (gen_random_uuid(), 'UNIDADE', 'UPDATE', 'Permission to update units', now(), now()),
    (gen_random_uuid(), 'UNIDADE', 'DELETE', 'Permission to delete units', now(), now()),
    (gen_random_uuid(), 'MORADIA', 'CREATE', 'Permission to create housing assignments', now(), now()),
    (gen_random_uuid(), 'MORADIA', 'READ', 'Permission to read housing assignments', now(), now()),
    (gen_random_uuid(), 'MORADIA', 'UPDATE', 'Permission to update housing assignments', now(), now()),
    (gen_random_uuid(), 'MORADIA', 'DELETE', 'Permission to delete housing assignments', now(), now()),
    (gen_random_uuid(), 'CHAMADO', 'CREATE', 'Permission to create tickets', now(), now()),
    (gen_random_uuid(), 'CHAMADO', 'READ', 'Permission to read tickets', now(), now()),
    (gen_random_uuid(), 'CHAMADO', 'UPDATE', 'Permission to update tickets', now(), now()),
    (gen_random_uuid(), 'CHAMADO', 'DELETE', 'Permission to delete tickets', now(), now()),
    (gen_random_uuid(), 'CHAMADO_TIPO', 'CREATE', 'Permission to create ticket types', now(), now()),
    (gen_random_uuid(), 'CHAMADO_TIPO', 'READ', 'Permission to read ticket types', now(), now()),
    (gen_random_uuid(), 'CHAMADO_TIPO', 'UPDATE', 'Permission to update ticket types', now(), now()),
    (gen_random_uuid(), 'CHAMADO_TIPO', 'DELETE', 'Permission to delete ticket types', now(), now()),
    (gen_random_uuid(), 'CHAMADO_STATUS', 'CREATE', 'Permission to create ticket statuses', now(), now()),
    (gen_random_uuid(), 'CHAMADO_STATUS', 'READ', 'Permission to read ticket statuses', now(), now()),
    (gen_random_uuid(), 'CHAMADO_STATUS', 'UPDATE', 'Permission to update ticket statuses', now(), now()),
    (gen_random_uuid(), 'CHAMADO_STATUS', 'DELETE', 'Permission to delete ticket statuses', now(), now()),
    (gen_random_uuid(), 'LOTACAO', 'CREATE', 'Permission to create lotacoes', now(), now()),
    (gen_random_uuid(), 'LOTACAO', 'READ', 'Permission to read lotacoes', now(), now()),
    (gen_random_uuid(), 'LOTACAO', 'UPDATE', 'Permission to update lotacoes', now(), now()),
    (gen_random_uuid(), 'LOTACAO', 'DELETE', 'Permission to delete lotacoes', now(), now())
    ON CONFLICT (resource, action) DO NOTHING;

-- =========================
-- ROLES
-- =========================
INSERT INTO roles (id, name, description, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'ADMIN', 'Administrator role', now(), now()),
    (gen_random_uuid(), 'MORADOR', 'Resident role', now(), now()),
    (gen_random_uuid(), 'COLABORADOR', 'Worker role', now(), now())
    ON CONFLICT (name) DO NOTHING;

-- =========================
-- ADMIN USER
-- =========================
INSERT INTO users (id, user_name, email, password, enabled, created_at, updated_at)
VALUES
    (
        gen_random_uuid(),
        'admin',
        'admin@example.com',
        crypt('admin123', gen_salt('bf', 12)),
        TRUE,
        now(),
        now()
    )
    ON CONFLICT (email) DO UPDATE
SET user_name = EXCLUDED.user_name,
    password = EXCLUDED.password,
    enabled = TRUE,
    updated_at = now();

-- =========================
-- ROLE -> PERMISSIONS (ADMIN = todas)
-- =========================
INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON TRUE
WHERE r.name = 'ADMIN'
    ON CONFLICT DO NOTHING;

-- =========================
-- ROLE -> PERMISSIONS (COLABORADOR = atendimento de chamados)
-- =========================
DELETE FROM role_permission rp
USING roles r, permissions p
WHERE rp.role_id = r.id
  AND rp.permission_id = p.id
  AND r.name = 'COLABORADOR'
  AND (
      p.resource = 'CHAMADO_TIPO'
      OR p.resource = 'CHAMADO_STATUS'
      OR (p.resource = 'CHAMADO' AND p.action IN ('CREATE', 'DELETE'))
  );

INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON
    (p.resource = 'CHAMADO' AND p.action IN ('READ', 'UPDATE'))
WHERE r.name = 'COLABORADOR'
    ON CONFLICT DO NOTHING;

-- =========================
-- USER -> ROLE
-- =========================
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ADMIN'
WHERE u.email = 'admin@example.com'
    ON CONFLICT DO NOTHING;

COMMIT;

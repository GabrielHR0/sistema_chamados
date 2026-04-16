INSERT INTO permissions (id, resource, action, description, created_at, updated_at)
VALUES
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
    (gen_random_uuid(), 'CHAMADO_STATUS', 'DELETE', 'Permission to delete ticket statuses', now(), now())
ON CONFLICT (resource, action) DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.resource IN ('CHAMADO', 'CHAMADO_TIPO', 'CHAMADO_STATUS')
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON (
    (p.resource = 'CHAMADO' AND p.action IN ('CREATE', 'READ', 'UPDATE')) OR
    (p.resource = 'CHAMADO_TIPO' AND p.action IN ('CREATE', 'READ', 'UPDATE')) OR
    (p.resource = 'CHAMADO_STATUS' AND p.action IN ('CREATE', 'READ', 'UPDATE'))
)
WHERE r.name = 'COLABORADOR'
ON CONFLICT DO NOTHING;

INSERT INTO permissions (id, resource, action, description, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'LOTACAO', 'CREATE', 'Permission to create lotacoes', now(), now()),
    (gen_random_uuid(), 'LOTACAO', 'READ', 'Permission to read lotacoes', now(), now()),
    (gen_random_uuid(), 'LOTACAO', 'UPDATE', 'Permission to update lotacoes', now(), now()),
    (gen_random_uuid(), 'LOTACAO', 'DELETE', 'Permission to delete lotacoes', now(), now())
ON CONFLICT (resource, action) DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.resource = 'LOTACAO'
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

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
JOIN permissions p ON p.resource = 'CHAMADO' AND p.action IN ('READ', 'UPDATE')
WHERE r.name = 'COLABORADOR'
ON CONFLICT DO NOTHING;

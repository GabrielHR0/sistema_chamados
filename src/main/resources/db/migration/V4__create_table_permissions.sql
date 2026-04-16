-- V4: create permissions table
CREATE TABLE IF NOT EXISTS permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    resource VARCHAR(150) NOT NULL,
    action VARCHAR(150) NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_permission_resource_action UNIQUE (resource, action)
);

CREATE INDEX IF NOT EXISTS idx_permissions_resource_action ON permissions (resource, action);

-- H2-compatible migration: create permissions table
CREATE TABLE IF NOT EXISTS permissions (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    resource VARCHAR(150) NOT NULL,
    action VARCHAR(150) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_permission_resource_action UNIQUE (resource, action)
);
CREATE INDEX IF NOT EXISTS idx_permissions_resource_action ON permissions (resource, action);

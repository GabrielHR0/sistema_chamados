-- H2-compatible migration: create role_permission join table
CREATE TABLE IF NOT EXISTS role_permission (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_role_permission PRIMARY KEY (role_id, permission_id)
);

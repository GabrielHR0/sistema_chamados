-- Flyway migration: enable pgcrypto extension
-- Keep this migration minimal: only enable extension required by later migrations

CREATE EXTENSION IF NOT EXISTS pgcrypto;

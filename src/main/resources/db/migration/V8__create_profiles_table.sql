-- V8: create profiles table and backfill existing users
CREATE TABLE IF NOT EXISTS profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(14),
    telefone VARCHAR(20),
    data_nascimento DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO profiles (user_id, nome_completo)
SELECT u.id, u.user_name
FROM users u
WHERE NOT EXISTS (
    SELECT 1
    FROM profiles p
    WHERE p.user_id = u.id
);

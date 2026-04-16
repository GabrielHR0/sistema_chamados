-- V25: trocar relacao de user_fotos de users -> profiles
-- Motivo: padronizar anexos/fotos na camada de perfil.

-- 1) Adiciona a nova coluna de relacionamento (temporariamente nullable para permitir backfill).
ALTER TABLE user_fotos
    ADD COLUMN profile_id UUID;

-- 2) Preenche profile_id com base no user_id atual.
UPDATE user_fotos uf
SET profile_id = p.id
FROM profiles p
WHERE p.user_id = uf.user_id
  AND uf.profile_id IS NULL;

-- 3) Garante que todo registro foi migrado antes de tornar NOT NULL.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM user_fotos
        WHERE profile_id IS NULL
    ) THEN
        RAISE EXCEPTION 'Falha ao migrar user_fotos: existem registros sem profile_id';
    END IF;
END
$$;

-- 4) Remove FK antiga e cria FK nova.
ALTER TABLE user_fotos
    DROP CONSTRAINT IF EXISTS fk_user_fotos_user,
    ADD CONSTRAINT fk_user_fotos_profile
        FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE;

-- 5) Enforce do novo relacionamento 1:1 e remoção da coluna antiga.
ALTER TABLE user_fotos
    ALTER COLUMN profile_id SET NOT NULL,
    ADD CONSTRAINT uk_user_fotos_profile_id UNIQUE (profile_id),
    DROP COLUMN user_id;

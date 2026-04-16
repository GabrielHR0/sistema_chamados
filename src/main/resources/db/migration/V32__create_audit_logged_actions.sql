CREATE SCHEMA IF NOT EXISTS audit;

CREATE TABLE IF NOT EXISTS audit.logged_actions (
    event_id BIGSERIAL PRIMARY KEY,
    schema_name TEXT NOT NULL,
    table_name TEXT NOT NULL,
    user_name TEXT,
    action_tstamp TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    action TEXT NOT NULL CHECK (action IN ('I', 'D', 'U')),
    original_data JSONB,
    new_data JSONB,
    query_text TEXT
);

CREATE INDEX IF NOT EXISTS idx_audit_logged_actions_schema_table
    ON audit.logged_actions (((schema_name || '.' || table_name)));
CREATE INDEX IF NOT EXISTS idx_audit_logged_actions_action_tstamp
    ON audit.logged_actions (action_tstamp DESC);
CREATE INDEX IF NOT EXISTS idx_audit_logged_actions_action
    ON audit.logged_actions (action);

CREATE OR REPLACE FUNCTION audit.if_modified_func() RETURNS trigger AS $$
BEGIN
    IF (TG_OP = 'UPDATE') THEN
        INSERT INTO audit.logged_actions (
            schema_name, table_name, user_name, action, original_data, new_data, query_text
        ) VALUES (
            TG_TABLE_SCHEMA::TEXT,
            TG_TABLE_NAME::TEXT,
            session_user::TEXT,
            substring(TG_OP, 1, 1),
            to_jsonb(OLD),
            to_jsonb(NEW),
            current_query()
        );
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO audit.logged_actions (
            schema_name, table_name, user_name, action, original_data, query_text
        ) VALUES (
            TG_TABLE_SCHEMA::TEXT,
            TG_TABLE_NAME::TEXT,
            session_user::TEXT,
            substring(TG_OP, 1, 1),
            to_jsonb(OLD),
            current_query()
        );
        RETURN OLD;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO audit.logged_actions (
            schema_name, table_name, user_name, action, new_data, query_text
        ) VALUES (
            TG_TABLE_SCHEMA::TEXT,
            TG_TABLE_NAME::TEXT,
            session_user::TEXT,
            substring(TG_OP, 1, 1),
            to_jsonb(NEW),
            current_query()
        );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER SET search_path = pg_catalog, audit;

DO $$
DECLARE
    t TEXT;
    tables_to_audit TEXT[] := ARRAY[
        'users',
        'roles',
        'permissions',
        'profiles',
        'blocos',
        'andares',
        'unidades',
        'moradias',
        'chamado_status',
        'tipos_chamado',
        'chamados',
        'chamado_comentarios',
        'chamado_anexos',
        'comentario_anexos',
        'lotacoes',
        'user_lotacao',
        'user_tipo_chamado',
        'lotacao_tipo_chamado',
        'user_fotos'
    ];
BEGIN
    FOREACH t IN ARRAY tables_to_audit
    LOOP
        IF to_regclass('public.' || t) IS NOT NULL THEN
            IF NOT EXISTS (
                SELECT 1
                FROM pg_trigger
                WHERE tgname = ('trg_audit_' || t)
            ) THEN
                EXECUTE format(
                    'CREATE TRIGGER %I AFTER INSERT OR UPDATE OR DELETE ON public.%I FOR EACH ROW EXECUTE FUNCTION audit.if_modified_func()',
                    'trg_audit_' || t,
                    t
                );
            END IF;
        END IF;
    END LOOP;
END$$;

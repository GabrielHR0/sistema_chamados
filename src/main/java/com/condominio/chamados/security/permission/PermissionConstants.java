package com.condominio.chamados.security.permission;

/**
 * Constantes para as permissões do sistema.
 * Padrão: RESOURCE:ACTION
 *
 * Ações padronizadas:
 * - CREATE: criar novo recurso
 * - READ: visualizar/listar recursos
 * - UPDATE: editar recurso existente
 * - DELETE: deletar recurso
 */
public final class PermissionConstants {

    private PermissionConstants() {
        throw new AssertionError("Cannot instantiate PermissionConstants");
    }

    // USER permissions
    public static final String USER_CREATE = "USER:CREATE";
    public static final String USER_READ = "USER:READ";
    public static final String USER_UPDATE = "USER:UPDATE";
    public static final String USER_DELETE = "USER:DELETE";

    // BLOCO permissions
    public static final String BLOCO_CREATE = "BLOCO:CREATE";
    public static final String BLOCO_READ = "BLOCO:READ";
    public static final String BLOCO_UPDATE = "BLOCO:UPDATE";
    public static final String BLOCO_DELETE = "BLOCO:DELETE";

    // ANDAR permissions
    public static final String ANDAR_CREATE = "ANDAR:CREATE";
    public static final String ANDAR_READ = "ANDAR:READ";
    public static final String ANDAR_UPDATE = "ANDAR:UPDATE";
    public static final String ANDAR_DELETE = "ANDAR:DELETE";

    // UNIDADE permissions
    public static final String UNIDADE_CREATE = "UNIDADE:CREATE";
    public static final String UNIDADE_READ = "UNIDADE:READ";
    public static final String UNIDADE_UPDATE = "UNIDADE:UPDATE";
    public static final String UNIDADE_DELETE = "UNIDADE:DELETE";

    // MORADIA permissions
    public static final String MORADIA_CREATE = "MORADIA:CREATE";
    public static final String MORADIA_READ = "MORADIA:READ";
    public static final String MORADIA_UPDATE = "MORADIA:UPDATE";
    public static final String MORADIA_DELETE = "MORADIA:DELETE";

    // CHAMADO permissions
    public static final String CHAMADO_CREATE = "CHAMADO:CREATE";
    public static final String CHAMADO_READ = "CHAMADO:READ";
    public static final String CHAMADO_UPDATE = "CHAMADO:UPDATE";
    public static final String CHAMADO_DELETE = "CHAMADO:DELETE";

    // CHAMADO_TIPO permissions
    public static final String CHAMADO_TIPO_CREATE = "CHAMADO_TIPO:CREATE";
    public static final String CHAMADO_TIPO_READ = "CHAMADO_TIPO:READ";
    public static final String CHAMADO_TIPO_UPDATE = "CHAMADO_TIPO:UPDATE";
    public static final String CHAMADO_TIPO_DELETE = "CHAMADO_TIPO:DELETE";

    // CHAMADO_STATUS permissions
    public static final String CHAMADO_STATUS_CREATE = "CHAMADO_STATUS:CREATE";
    public static final String CHAMADO_STATUS_READ = "CHAMADO_STATUS:READ";
    public static final String CHAMADO_STATUS_UPDATE = "CHAMADO_STATUS:UPDATE";
    public static final String CHAMADO_STATUS_DELETE = "CHAMADO_STATUS:DELETE";

    // LOTACAO permissions
    public static final String LOTACAO_CREATE = "LOTACAO:CREATE";
    public static final String LOTACAO_READ = "LOTACAO:READ";
    public static final String LOTACAO_UPDATE = "LOTACAO:UPDATE";
    public static final String LOTACAO_DELETE = "LOTACAO:DELETE";
}

# chamados

## Descricao do projeto

Aplicacao web de chamados para condominio, com interface server-side em JSP e layout AdminLTE. O sistema organiza a operacao entre moradores, colaboradores e administradores, cobrindo abertura e acompanhamento de chamados, comentarios, anexos, gestao de usuarios, escopo por lotacao/tipo e auditoria de alteracoes.

O front-end segue padrao de componentes em `src/main/resources/static` (Bootstrap 5, AdminLTE, jQuery e DataTables) e views em `src/main/webapp/WEB-INF/jsp`, com navegacao e telas orientadas por perfil.

## Decisoes tecnicas por modulo

### 1. Autenticacao e autorizacao

O modulo de autenticacao e autorizacao foi implementado com **Spring Security** usando login por formulario, sessao HTTP e `remember-me`, com politicas de acesso por papel e permissao.

**O que foi usado**

- `SecurityFilterChain` com rotas publicas restritas a login/assets e autenticacao obrigatoria para o restante (`src/main/java/com/condominio/chamados/security/config/SecurityConfig.java`).
- `CustomUserDetailsService` + `UserPrincipal` para carregar usuario do banco e montar authorities de role e permissao (`src/main/java/com/condominio/chamados/security/service/CustomUserDetailsService.java` e `src/main/java/com/condominio/chamados/security/service/UserPrincipal.java`).
- `BCryptPasswordEncoder` para hash de senha (`src/main/java/com/condominio/chamados/security/config/SecurityBeans.java`).
- Modelo RBAC persistido com `roles`, `permissions`, `user_role` e `role_permission` (migrations `V3`, `V4`, `V5`, `V6` em `src/main/resources/db/migration`).
- Protecao adicional de login com rate limit (`src/main/java/com/condominio/chamados/security/filter/LoginRateLimitFilter.java`).

**Referencia de RBAC no codigo**

- Exemplo por papel: `@PreAuthorize("isAuthenticated() and hasRole('ADMIN')")` em `src/main/java/com/condominio/chamados/audit/controller/AuditController.java`.
- Exemplo papel + permissao: expressoes `hasAuthority(...)` e `hasAnyRole(...)` centralizadas em `src/main/java/com/condominio/chamados/web/ChamadoController.java`.
- Catalogo de permissoes no padrao `RESOURCE:ACTION`: `src/main/java/com/condominio/chamados/security/permission/PermissionConstants.java`.

**Por que essa decisao**

- RBAC atende diretamente ao dominio do condominio, que tem responsabilidades bem definidas entre `ADMIN`, `COLABORADOR` e `MORADOR`.
- A combinacao **role + permissao** permite regra global por perfil e ajuste fino por capacidade sem acoplamento de regra no front.
- Spring Security reduz codigo customizado de seguranca e mantem o controle de acesso declarativo, rastreavel e testavel.

### 2. Blocos e moradias

Esse modulo modela a estrutura do condominio em cadeia (`Bloco` -> `Andar` -> `Unidade`) e separa a ocupacao em uma entidade propria: **`Moradia`**.

`Moradia` representa a relacao entre **usuario** e **unidade** ao longo do tempo. Essa decisao evita perder historico de ocupacao quando um morador sai, troca ou encerra vinculo com a unidade.

A modelagem tambem separa estados com objetivos diferentes:

- **Status da unidade**: descreve a condicao operacional da unidade no condominio.
- **Status da moradia**: descreve a situacao do vinculo do morador com a unidade (ex.: ativa, encerrada, transferida).

Essa separacao foi adotada para nao misturar estado fisico do patrimonio com estado de ocupacao por pessoa, o que melhora consistencia das regras e consultas.

Um ponto critico de consistencia e a criacao de bloco completo: o sistema cria bloco, andares e unidades dentro de uma unica transacao. Se qualquer etapa falhar, tudo e revertido, impedindo cadastro parcial da estrutura.

### 3. Chamados

O modulo de chamados foi desenhado para separar claramente **quem solicita**, **quem atende** e **como o fluxo evolui**.

Na modelagem, o chamado nao e apenas um registro textual. Ele conecta:

- unidade (onde o problema acontece),
- tipo de chamado (natureza do atendimento),
- status atual (ponto no fluxo),
- solicitante (quem abriu),
- colaborador responsavel (quem esta atendendo).

Essa separacao de papeis (solicitante x responsavel) foi uma decisao central para evitar ambiguidade no atendimento e permitir regras diferentes para morador e equipe.

Outra decisao importante foi tornar o fluxo de status orientado por **State**:

- cada status possui um `comportamento_tipo` (`INICIAL`, `INTERMEDIARIO`, `FINAL`);
- cada status pode definir seus proximos status permitidos;
- a transicao e validada pelo comportamento do estado atual.

O motivo dessa escolha foi tirar regra de transicao de `if/else` espalhado no servico e transformar o fluxo em uma regra de dominio explicita e configuravel.

Na abertura do chamado, o status inicial e automatico: o `TipoChamado` tem seu `status_inicial` proprio. Assim, o create nao depende de escolha manual de status e cada tipo ja nasce no ponto correto do fluxo.

Sobre escopo de atendimento, a arquitetura adotou **lotacao como unidade de capacidade operacional**:

- a lotacao define quais tipos de chamado ela atende;
- o usuario colaborador e vinculado a uma ou mais lotacoes;
- com isso, o que ele pode ver/assumir vem do cruzamento `usuario -> lotacao -> tipos de chamado`.

Essa abordagem foi escolhida para representar o time de atendimento de forma real (ex.: eletrica, hidraulica, portaria), sem acoplar o escopo diretamente no usuario de forma rigida.

No banco, isso fica materializado por tabelas de relacao (`lotacao_tipo_chamado` e `user_lotacao`), mantendo flexibilidade para remanejamento de equipe sem remodelar chamados ja existentes.

Tambem foi mantido o historico conversacional do chamado com comentarios e anexos em entidades proprias, para preservar contexto de atendimento e evidencias sem inflar a entidade principal.

Em arquitetura da solucao, as regras ficam concentradas na camada de servico, enquanto controllers cuidam do fluxo MVC e repositories aplicam consultas com escopo. Operacoes criticas de criacao/atualizacao rodam em transacao para manter consistencia entre chamado, status e anexos.

## O que foi configurado

- Perfis de configuracao (`dev` e `prod`) com variaveis de ambiente.
- `docker-compose.yml` com servicos `app` e `db`.
- `Dockerfile` para containerizar a aplicacao Spring Boot.
- Build Docker com cache de Maven e imagens Alpine mais leves.
- Arquivo `.env` para credenciais e configuracoes de ambiente local.
- Documentação de erros MVC em `docs/error-handling-mvc.md`.
- Setup de JSP com JSTL e Bootstrap 5 via WebJars.


## Subir tudo com Docker Compose

```powershell
docker compose up --build
```

A aplicacao fica em `http://localhost:8080` e o banco em `localhost:5432`.


### Bootstrap automatico de dados no `docker compose up`

No `up`, o servico `db-bootstrap` executa automaticamente:

1. `src/main/resources/db/seed/auth_bootstrap.sql`
2. `src/main/resources/db/seed/dev_bootstrap.sql`

Esses scripts sao idempotentes (podem rodar em todo `up` sem duplicar dados essenciais).

**Credenciais de desenvolvimento criadas pelos scripts**

| Perfil | Username (login) | Senha |
| --- | --- | --- |
| ADMIN | `admin` | `admin123` |
| COLABORADOR | `colab.manutencao` | `dev123` |
| COLABORADOR | `colab.portaria` | `dev123` |
| MORADOR | `morador.101` | `dev123` |
| MORADOR | `morador.102` | `dev123` |

## Front-end em JSP

O setup inicial usa:

- JSP como view server-side.
- JSTL para iteracao e tags padrao.
- Bootstrap 5 via WebJars para componentes prontos.

As views ficam em `src/main/webapp/WEB-INF/jsp/` e o exemplo inicial esta em `/`.

## Executar testes

```powershell
.\mvnw.cmd test
```


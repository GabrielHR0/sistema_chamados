---
name: chamados-scaffold-agent_1
description: Agent guidance for the current Spring Boot scaffold in this repository.
---
# AGENTS.md — Agente para o scaffold "chamados"

Este arquivo orienta agentes (como Copilot/assistentes) sobre decisões, convenções e fluxos presentes
no repositório `com.condominio.chamados`. Use-o como fonte operacional quando for necessário modificar
o código ou propor novas funcionalidades.

## Meta
- Nome do agente sugerido: `chamados-scaffold-agent` (arquivo: `.github/agents/copilot.agent.md`).
- Descrição curta: orientação prática para evoluir o scaffold Spring Boot deste repositório.

## Escopo e precedência
- Este arquivo aplica-se somente a este repositório.
- A fonte de verdade é o `AGENTS.md` na raiz do projeto. Se houver conflito entre este arquivo e o da raiz,
  o da raiz prevalece.

## Snapshot do projeto (observado)
- Framework: Spring Boot 3.5.13 sobre Java 21 (ver `pom.xml`).
- Build: Maven Wrapper (`mvnw`, `mvnw.cmd`) + plugin do Spring Boot.
- Pacote raiz: `com.condominio.chamados`.
- Estado atual: aplicação web já possui `ChamadosApplication`, `HomeController`, views JSP e testes de contexto/web.
- A aplicação não é mais API-only; o front-end é server-side com JSP.
- Arquivos importantes:
  - `src/main/java/com/condominio/chamados/ChamadosApplication.java`
  - `src/main/java/com/condominio/chamados/web/HomeController.java`
  - `src/test/java/com/condominio/chamados/ChamadosApplicationTests.java`
  - `src/main/resources/application.properties`
  - `src/main/webapp/WEB-INF/jsp/`
  - `src/main/resources/db/migration/` (reservado para Flyway)

## Capacidades já presentes (dependências do `pom.xml`)
- Web MVC + JSP: `spring-boot-starter-web`, `tomcat-embed-jasper`, JSTL.
- Segurança: `spring-boot-starter-security`.
- Validação: `spring-boot-starter-validation`.
- JPA: `spring-boot-starter-data-jpa`.
- Migrations: `flyway-core`, `flyway-database-postgresql`.
- Driver PostgreSQL: `org.postgresql:postgresql`.
- Observabilidade: `spring-boot-starter-actuator`, `micrometer-registry-prometheus`.
- Devtools, testes (incl. `spring-security-test`).
- UI pronta para evoluir com WebJars/local assets, incluindo Bootstrap 5, AdminLTE, jQuery e DataTables.

## Convenções de código e pastas
- Código Java: `src/main/java/com/condominio/chamados/...`.
- Testes: `src/test/java/com/condominio/chamados/...` (espelhados por pacote/funcionalidade).
- Configuração base: `src/main/resources/application.properties`.
- Migrations Flyway: `src/main/resources/db/migration` — qualquer mudança persistente começa aqui.
- Views JSP: `src/main/webapp/WEB-INF/jsp/`.
- Assets estáticos: `src/main/resources/static` para CSS/JS/plugins.

## Decisões arquiteturais e de modelagem (diretrizes que devem ser seguidas)
- Fluxo em camadas: controller -> service -> repository -> domain; use DTOs no boundary dos controllers.
- Controllers devem permanecer finos; regras de negócio pertencem a `service`/`domain`. No front JSP, controllers também podem preparar o `Model` para renderização.
- Evite criar novos módulos/pacotes sem necessidade clara — mantenha tudo sob `com.condominio.chamados`.
- Prefira layouts/includes JSP reutilizáveis antes de introduzir markup específico de página.

### Modelo de condomínio
- Entidades essenciais: `Bloco`, `Andar`, `Unidade`.
- `Unidade` deve ter identificação determinística e legível: `BLOCO-ANDAR-NUMERO` (ex.: `A-03-204`).
- Preferir gerar `Unidade` por regras: bloco -> andar -> número, em vez de IDs opacos para referência humana.

### Modelo de Chamado
- Entidade `Chamado` (ticket) com campos mínimos:
  - id (PK)
  - unidade (FK para `Unidade`)
  - tipo (FK/enum)
  - descricao (texto)
  - status (enum)
  - comentarios (coleção)
  - dataCriacao (timestamp)
  - dataFinalizacao (timestamp, nullable)

Regras de negócio críticas:
- Status inicial é definido automaticamente ao criar um chamado.
- Apenas papéis `ADMIN` e `COLABORADOR` podem alterar o `status`.
- Ao mover um chamado para um estado final (resolvido/fechado), `dataFinalizacao` é preenchida automaticamente.

### Papéis e autorizações
- Papéis: `ADMIN`, `MORADOR`, `COLABORADOR`.
- Permissões (resumo):
  - ADMIN: gerencia blocos, usuários, tipos de chamados, statuses, vincula moradores a unidades.
  - MORADOR: cria chamados associados à sua unidade, adiciona descrições/anexos, comenta apenas nos seus chamados.
  - COLABORADOR: visualiza e atualiza status dos chamados, adiciona comentários, filtra/consulta chamados.

## Persistência e migrações
- Regra obrigatória: qualquer mudança persistente começa com uma migration Flyway em
  `src/main/resources/db/migration` (nome com prefixo `V` + número).
- Não assuma que entidades JPA existam antes da migration corresponder ao esquema.
- Alvo DB: PostgreSQL (driver já no `pom.xml`).

## Segurança
- RBAC obrigatório com roles `ADMIN`, `MORADOR`, `COLABORADOR`.
- Aplicar autorização por endpoint (anotações `@PreAuthorize`/configuração `HttpSecurity`).
- JWT é uma direção futura: só considerar se o código do repositório adicionar suporte explícito.

## Contrato de erros (padrão para APIs REST)
O padrão de erro agora segue RFC 7807 (`application/problem+json`) usando `ProblemDetail` do Spring.

## Auditoria, Async/Outbox, Notificações, Docs e Observabilidade
- Auditoria é prioridade: registre criação/atualização/exclusão.
- Para auditoria persistente considere primeiro: registrar histórico (audit table) antes de adicionar ferramentas mais complexas.
- Outbox pattern é recomendado quando houver integração assíncrona (salve evento + entidade no mesmo tx.).
- Notificações (opcionais): e-mail via Spring Mail para eventos como novo chamado/comentário/atualização.
- Documentação: quando endpoints forem adicionados, habilitar OpenAPI/Swagger (ex.: `springdoc-openapi`).
- Observabilidade: Actuator já disponível; exportar métricas para Prometheus é padrão.

## Testes e qualidade
- Escrever testes unitários com JUnit 5 + Mockito; usar `spring-security-test` para testes com segurança.
- Antes de merges que toquem startup, security, persistence, contratos API ou renderização JSP, validar com testes.

## Fluxos e comandos críticos (PowerShell — Windows)
Execute a partir da raiz do projeto:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
.\mvnw.cmd clean package
```

Observação: os testes de contexto e web devem permanecer executáveis sem depender de um banco vivo quando o foco for apenas a UI shell.

## Padrões operacionais para agentes de IA
- Sempre leia o `AGENTS.md` na raiz antes de propor mudanças significativas.
- Para mudanças persistentes:
  1. Adicione uma migration Flyway em `src/main/resources/db/migration`.
  2. Então crie a entidade JPA e o repositório.
  3. Adicione testes que cubram o novo comportamento.
- Quando editar arquivos existentes, cite os caminhos reais (ex.: `src/main/java/com/condominio/chamados/service/ChamadoService.java`).
- Mantenha a granularidade mínima: prefira fazer um slice completo (migration + entidade + repo + service + controller + teste)
  ao invés de espalhar mudanças inacabadas.
- Para UI, prefira slices JSP completos com layout/fragments/componentes antes de introduzir customizações dispersas.

## Guardrails — coisas a NÃO fazer
- Não invente configurações de banco, autenticação ou infra que não existam no repositório.
- Não crie migrations ou entidades sem motivo claro; siga as decisões de modelagem acima.
- Não introduza novos módulos/pacotes sem justificativa técnica.

## Sugestões e decisões de modelagem detalhadas (quando for necessário implementar)
- Unidade: armazene bloco, andar, numero e um código legível (`codigoUnidade`) calculado por regra; indexar por código.
- Chamado.tipo: preferir uma entidade `TipoChamado` com SLA e visibilidade; isso facilita criação dinâmica de tipos.
- Status: usar `enum` com metadados (ex.: `inicial`, `em_andamento`, `resolvido`, `cancelado`) e uma flag `final`.
- Comentários: entidade `Comentario` com autor, texto, timestamp; permitir anexos separados por tabela `Anexo`.
- Front-end: usar Bootstrap como base, AdminLTE para layout (sidebar/navbar/dashboard) e jQuery + DataTables para tabelas avançadas.

## Infra e ambiente (variáveis recomendadas)
- As configurações sensíveis ficam fora do repositório; sugira `.env` local para desenvolvimento:
  - DB_URL
  - DB_USER
  - DB_PASSWORD
  - JWT_SECRET
  - MAIL_USER
  - MAIL_PASSWORD

## DevOps / Contêiner
- Docker / docker-compose são parte do setup local; documentar `docker-compose.yml` na raiz e considerar a experiência de `docker compose up` como fluxo padrão de desenvolvimento.

## Como o agente deve responder (estilo)
Ao interagir com desenvolvedores, o agente deverá responder sempre com estes 4 passos quando propor mudanças técnicas:
1. Explicação (por que)
2. Sugestão prática (passos concretos)
3. Código (patches/trechos, ou comandos para aplicar)
4. Melhorias futuras / riscos

## Referências internas
- Arquivos-chave para revisão antes de propor mudanças:
  - `AGENTS.md` (raiz)
  - `.github/agents/copilot.agent.md` (este arquivo)
  - `.github/copilot-instructions.md`
  - `src/main/resources/db/migration/`
  - `src/main/resources/application.properties`

## Observações finais
- Este arquivo é um guia operacional: os agentes devem manter ele sincronizado com o `AGENTS.md` da raiz.
- Quando houver dúvida sobre uma decisão que impacta segurança, persistência ou operações, prefira propor
  uma pequena RFC (arquivo MD) e testes que validem a mudança.

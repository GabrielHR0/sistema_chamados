# AGENTS.md

## Scope and Sync
- This is the canonical agent guidance for this repository.
- Mirror file: `.github/agents/AGENTS.md`.
- If there is any conflict between the two files, this root file wins.

## Project Snapshot
- Framework: Spring Boot `3.5.13` on Java `21` (`pom.xml`).
- Build tool: Maven Wrapper (`mvnw`, `mvnw.cmd`) + Spring Boot Maven plugin.
- Root package: `com.condominio.chamados`.
- Current code already includes the web shell: `ChamadosApplication`, `HomeController`, JSP views, and `ChamadosApplicationTests`.
- The application is no longer API-only; the front-end is server-rendered with JSP.

## What Already Exists in the Scaffold
- Boot entrypoint: `src/main/java/com/condominio/chamados/ChamadosApplication.java`.
- Context-load test: `src/test/java/com/condominio/chamados/ChamadosApplicationTests.java`.
- Base config: `src/main/resources/application.properties` defines the active profile and JSP view resolver defaults.
- Reserved migration folder: `src/main/resources/db/migration` (empty at the moment).
- JSP views live under `src/main/webapp/WEB-INF/jsp/`.
- Static assets and UI libraries are expected under `src/main/resources/static`.

## Runtime Capabilities Present in `pom.xml`
- Web MVC + JSP rendering: `spring-boot-starter-web`, `tomcat-embed-jasper`, JSTL.
- Security: `spring-boot-starter-security`.
- Validation: `spring-boot-starter-validation`.
- JPA + datasource: `spring-boot-starter-data-jpa`.
- Flyway: `flyway-core`, `flyway-database-postgresql`.
- PostgreSQL runtime driver: `org.postgresql:postgresql`.
- Actuator + metrics: `spring-boot-starter-actuator`, `micrometer-registry-prometheus`.
- Dev UX: `spring-boot-devtools`.
- UI components available through WebJars and local static assets, including Bootstrap 5 and the planned AdminLTE/jQuery/DataTables stack.
- Test support: `spring-boot-starter-test`, `spring-security-test`.

## Architecture Decisions to Use When Evolving the Code
- Prefer layered flow: controller -> service -> repository -> domain, with DTOs at the boundary.
- Keep controllers thin; business rules belong in service/domain classes. For the web layer, controllers may also prepare model attributes for JSP views.
- Keep package ownership under `src/main/java/com/condominio/chamados/...` and mirror tests under `src/test/java/com/condominio/chamados/...`.
- Use `src/main/resources/application.properties` only for base defaults; environment-specific overrides stay outside the repo unless explicitly added.
- Prefer reusable JSP includes/layout fragments before introducing custom page-specific markup.

## Domain Modeling Decisions for the Condominium Helpdesk
- Core problem: manage service tickets (“chamados”) inside a condominium with traceable ownership and status flow.
- Condominium structure:
  - Bloco: container for floors and units.
  - Andar: structural grouping inside a bloco.
  - Unidade: apartment/unit, preferably generated from bloco + andar + number.
- Unit identification should be deterministic and human-readable, e.g. `BLOCO-ANDAR-NUMERO`.
- User roles for the domain:
  - `ADMIN`: manages blocks, users, ticket types, statuses, and resident-unit links.
  - `MORADOR`: opens tickets, selects own unit, adds description/attachments, comments on own tickets.
  - `COLABORADOR`: views tickets, updates status, filters tickets, comments.
- Ticket model (`Chamado`) should carry:
  - unidade
  - tipo
  - descrição
  - status
  - comentários
  - data de criação
  - data de finalização
- Status rules:
  - status inicial automático
  - only `ADMIN`/`COLABORADOR` may change status
  - finalization date is set automatically when resolved

## Persistence and Migration Rules
- Any persistent change must start with a Flyway migration in `src/main/resources/db/migration`.
- Do not assume JPA entities or repositories exist until the schema migration is defined.
- PostgreSQL is the target database because the runtime driver is already declared.

## Security Decisions
- RBAC is required with roles `ADMIN`, `MORADOR`, `COLABORADOR`.
- Use Spring Security authorization per endpoint and least-privilege defaults.
- JWT is a possible future direction, but do not treat it as implemented unless the code adds it.

## Error Contract
- Browser/MVC errors should render the JSP view model documented in `docs/error-handling-mvc.md`.
- Non-HTML/integration responses must follow RFC 7807 (`application/problem+json`) through Spring's `ProblemDetail`.
- Keep errors predictable for browser flows, integrations, and tests.

## Audit, Async, Notifications, Docs, Observability
- Audit is a first-class concern: creation, update, deletion.
- If audit becomes complex, prefer explicit persistence of change history before introducing heavier mechanisms.
- Outbox pattern is a future option for reliable async events; do not add it without a concrete use case.
- Notifications may use email for events like new ticket, update, and comments if/when email infrastructure is added.
- OpenAPI/Swagger is the preferred API documentation direction when endpoints appear.
- Actuator is already available for health and metrics; Prometheus registry is present in the build.

## Quality and Delivery Expectations
- Validate changes with tests before merging any behavior that touches startup, security, persistence, or API contracts.
- Validate JSP/view changes with MVC or web-layer tests when they touch rendering or request flow.
- Use JUnit and Mockito for unit/integration support; JaCoCo can be added if coverage reporting becomes necessary.
- Docker / Docker Compose are deployment aids for local parity; the application should still run outside containers when needed.

## Critical Workflows
- From project root on Windows PowerShell:
```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
.\mvnw.cmd clean package
```
- The baseline context and web tests should stay green without requiring a live database when only the UI shell is exercised.

## What to Avoid
- Do not contradict this file with lower-precedence guidance.
- Do not invent banco/auth/infrastructure details that are not present in the repository.
- Do not create entities or migrations without a clear model decision.
- Do not introduce new packages or modules unless the current structure genuinely needs them.

## Existing AI Guidance Sources
- Current guidance files: `AGENTS.md`, `.github/agents/AGENTS.md`, and `.github/copilot-instructions.md`.


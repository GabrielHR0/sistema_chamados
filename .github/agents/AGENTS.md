---
name: chamados-scaffold-agent
description: Agent guidance for the current Spring Boot scaffold in this repository.
---

# AGENTS.md

## Scope
- Applies to this repository only.
- Source of truth is root `AGENTS.md`.
- If this file conflicts with root `AGENTS.md`, root file wins.

## Repository Snapshot
- Framework: Spring Boot `3.5.13` on Java `21` (`pom.xml`).
- Build tool: Maven Wrapper (`mvnw`, `mvnw.cmd`) + Spring Boot Maven plugin.
- Root package: `com.condominio.chamados`.
- Current code already includes the web shell: `ChamadosApplication`, `HomeController`, JSP views, and `ChamadosApplicationTests`.
- The application is no longer API-only; the front-end is server-rendered with JSP.

## Target Modeling Decisions
- Layered flow: controller -> service -> repository -> domain, with DTOs at the boundary.
- Web controllers may prepare model attributes for JSP views; keep page rendering reusable with includes/fragments.
- Condominium model: `Bloco` -> `Andar` -> `Unidade`, with human-readable unit codes like `BLOCO-ANDAR-NUMERO`.
- Roles: `ADMIN`, `MORADOR`, `COLABORADOR`.
- Ticket model: unidade, tipo, descrição, status, comentários, criação e finalização.
- Status flow: initial status automatic, only admin/colaborador can change it, finalization date is set on resolve.

## Practical Workflow
- Run from repository root on Windows PowerShell:
```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
.\mvnw.cmd clean package
```
- Baseline context and web-layer tests should remain green without a live database when only the UI shell is under test.
- If adding DB-backed code, create Flyway migration scripts first.

## Code and Folder Conventions
- Java source: `src/main/java/com/condominio/chamados/...`.
- Tests: `src/test/java/com/condominio/chamados/...`.
- App properties: `src/main/resources/application.properties`.
- JSP views: `src/main/webapp/WEB-INF/jsp/`.
- Static assets: `src/main/resources/static` for CSS/JS/plugins.

## Integration Points
- Database target is PostgreSQL (`org.postgresql:postgresql` runtime dependency).
- Schema management is Flyway-first.
- Security testing support is available with `spring-security-test`.
- Actuator + Micrometer Prometheus dependencies are present for metrics/health.
- UI stack direction: Bootstrap 5 as base visual layer, AdminLTE as system layout, and jQuery + DataTables for advanced tables.

## Error Contract
- Browser/MVC errors should render the JSP view model documented in `docs/error-handling-mvc.md`.
- Non-HTML/integration responses must follow RFC 7807 (`application/problem+json`) through Spring's `ProblemDetail`.

## Guardrails
- Do not invent infrastructure, auth, or persistence details not present in the repo.
- Do not create entities or migrations without a clear model decision.
- Do not treat the application as API-only; keep JSP/server-rendered views in mind when proposing UI work.
- Prefer concise changes that preserve the current scaffold until a feature slice justifies more structure.


# chamados - modulo 1 (setup)

Este projeto esta preparado para subir aplicacao + PostgreSQL com Docker Compose e front-end em JSP.

## O que foi configurado

- Perfis de configuracao (`dev` e `prod`) com variaveis de ambiente.
- `docker-compose.yml` com servicos `app` e `db`.
- `Dockerfile` para containerizar a aplicacao Spring Boot em WAR.
- Build Docker com cache de Maven e imagens Alpine mais leves.
- Arquivo `.env` para credenciais e configuracoes de ambiente local.
- Tratamento global de erros com RFC 7807 (`application/problem+json`).
- Documentação de erros MVC em `docs/error-handling-mvc.md`.
- Setup de JSP com JSTL e Bootstrap 5 via WebJars.
- Perfil de teste sem dependencia de banco para permitir `contextLoads`.

## Estrutura principal

- `src/main/resources/application.properties`
- `src/main/resources/application-dev.properties`
- `src/main/resources/application-prod.properties`
- `src/test/resources/application-test.properties`
- `src/main/webapp/WEB-INF/jsp/home.jsp`
- `src/main/java/com/condominio/chamados/web/HomeController.java`
- `src/main/java/com/condominio/chamados/config/DevWebSecurityConfig.java`
- `src/main/java/com/condominio/chamados/shared/api/GlobalExceptionHandler.java`
- `docker-compose.yml`
- `Dockerfile`
- `.env`

## Subir tudo com Docker Compose

```powershell
docker compose up --build
```

A aplicacao fica em `http://localhost:8080` e o banco em `localhost:5432`.

Obs.: o primeiro build ainda pode demorar por download de dependencias/imagens; depois disso, os rebuilds tendem a ficar bem mais rapidos por causa do cache do Maven.

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


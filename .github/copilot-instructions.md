# Copilot Instructions

## Escopo e precedência
- Este repositório usa `AGENTS.md` na raiz como fonte canonica.
- `.github/agents/AGENTS.md` e um espelho operacional; se houver conflito, a raiz vence.
- Siga estas instrucoes junto com os fatos observaveis no codigo atual.

## Contexto do projeto
- Stack atual: Spring Boot `3.5.13`, Java `21`, Maven Wrapper.
- Pacote raiz: `com.condominio.chamados`.
- O projeto deixou de ser apenas scaffold: já possui shell web com `HomeController`, JSP e testes de contexto/web.
- Dependencias presentes: Web, Security, JPA, Flyway, Actuator, Prometheus e PostgreSQL.

### Front-end (JSP + stack visual)
- A aplicacao utiliza JSP para renderizacao server-side.
- Stack visual definida:
    - Bootstrap 5 → base visual utilitaria e componentes prontos
    - AdminLTE → layout estrutural (sidebar, navbar, dashboard)
    - jQuery + DataTables → tabelas dinamicas com busca, paginacao e ordenacao

- Os arquivos estaticos devem ser organizados em:
    - `src/main/resources/static/` → CSS, JS e plugins (Bootstrap, AdminLTE, jQuery, DataTables)
    - `src/main/webapp/WEB-INF/jsp/` → paginas JSP

- O frontend deve priorizar:
    - Reutilizacao de layouts (header, sidebar, footer)
    - Componentizacao via includes JSP (`<jsp:include>`)
    - Uso de tabelas padronizadas com DataTables para listagens

- Evitar criacao de estilos customizados desnecessarios quando houver suporte nativo do Bootstrap/AdminLTE.

## Como responder
- Responda em portugues-BR, com foco pratico e direto.
- Cite arquivos reais do repositorio quando sugerir mudancas.
- Prefira passos curtos, claros e executaveis.
- Quando houver incerteza, diga o que foi observado no repositorio antes de assumir.

## Fluxo de trabalho esperado
- Primeiro entenda o estado atual dos arquivos antes de propor alteracoes.
- Para mudancas com persistencia, crie migration em `src/main/resources/db/migration` antes de depender de entidade/repository.
- Mantenha o codigo novo dentro de `src/main/java/com/condominio/chamados/...` e os testes espelhados em `src/test/java/com/condominio/chamados/...`.
- Considere o impacto em datasource, Flyway, JPA, security e actuator antes de sugerir uma mudanca.

## Padroes do projeto
- Use `src/main/resources/application.properties` para configuracao base.
- O projeto ainda esta no inicio da evolucao de dominio; nao invente estrutura sem necessidade.
- `static/` e `WEB-INF/jsp/` devem ser usados para assets e views server-side.

## Evitar
- Nao contradiga o `AGENTS.md` da raiz.
- Nao invente configuracoes de banco, auth ou infraestrutura que nao existam no repositorio.
- Nao proponha migrations ou entidades sem motivo claro.
- Nao responda com generalidades; foque no que este projeto realmente tem hoje.


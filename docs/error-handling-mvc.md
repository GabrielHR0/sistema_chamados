# Padrão de erros do projeto

Este repositório usa **Spring MVC + JSP** para as telas e mantém **RFC 7807 / `ProblemDetail`** para respostas estruturadas quando a requisição não é HTML.

## Regra prática

- **Requisições HTML / navegador**
  - renderizar uma view JSP de erro em `src/main/webapp/WEB-INF/jsp/error/error.jsp`
  - dados do erro devem ser montados em um modelo de view, hoje representado por `WebErrorModel`

- **Requisições não-HTML / integração**
  - responder com `application/problem+json`
  - usar `org.springframework.http.ProblemDetail`

## Por que esse padrão

O projeto não é mais API-only. O front-end é server-side com JSP, então os erros precisam respeitar o fluxo de navegação do navegador e, ao mesmo tempo, manter uma resposta padronizada para integrações e testes.

## Referências oficiais

- **RFC 7807 — Problem Details for HTTP APIs**
  - https://www.rfc-editor.org/rfc/rfc7807

- **Spring Framework — `ProblemDetail` / tratamento de exceções em MVC**
  - https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html

- **Spring Boot — Error Handling no Servlet MVC**
  - https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.spring-mvc.error-handling

## Onde isso está aplicado no código

- `src/main/java/com/condominio/chamados/shared/error/GlobalExceptionHandler.java`
- `src/main/java/com/condominio/chamados/shared/error/WebErrorModel.java`
- `src/main/webapp/WEB-INF/jsp/error/error.jsp`
- `src/test/java/com/condominio/chamados/shared/error/GlobalExceptionHandlerTest.java`

## Observação

Se um novo erro for adicionado para telas JSP, ele deve seguir este mesmo contrato:

1. tentar renderização HTML quando o `Accept` indicar navegador;
2. retornar `ProblemDetail` quando a requisição for de integração/JSON.


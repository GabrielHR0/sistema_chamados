package com.condominio.chamados.shared.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;

import java.time.OffsetDateTime;

@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(IllegalArgumentException.class)
	public Object handleIllegalArgument(
			IllegalArgumentException ex,
			HttpServletRequest request
	) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleValidation(
			MethodArgumentNotValidException ex,
			HttpServletRequest request
	) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.orElse("Requisicao invalida");
		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), request);
	}

	@ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
	public Object handleAccessDenied(
			Exception ex,
			HttpServletRequest request
	) {
		return buildErrorResponse(HttpStatus.FORBIDDEN, "Acesso negado", request.getRequestURI(), request);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public Object handleNotFound(
			NoResourceFoundException ex,
			HttpServletRequest request
	) {
		log.warn("Recurso nao encontrado em {}: {}", request.getRequestURI(), ex.getMessage());
		return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), request);
	}

	@ExceptionHandler(Exception.class)
	public Object handleUnexpected(
			Exception ex,
			HttpServletRequest request
	) {
		log.error("Erro inesperado ao processar a requisicao {}", request.getRequestURI(), ex);
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request.getRequestURI(), request);
	}

	private Object buildErrorResponse(HttpStatus status, String message, String path, HttpServletRequest request) {
		if (acceptsHtml(request)) {
			return buildHtmlErrorView(status, message, path);
		}
		return buildProblemDetailResponse(status, message, path);
	}

	private boolean acceptsHtml(HttpServletRequest request) {
		String accept = request.getHeader("Accept");
		return accept != null && accept.contains(MediaType.TEXT_HTML_VALUE);
	}

	private ModelAndView buildHtmlErrorView(HttpStatus status, String message, String path) {
		WebErrorModel error = new WebErrorModel(
				OffsetDateTime.now(),
				status.value(),
				status.getReasonPhrase(),
				message,
				path
		);
		ModelAndView modelAndView = new ModelAndView("error/error");
		modelAndView.setStatus(status);
		modelAndView.addObject("error", error);
		return modelAndView;
	}

	private ResponseEntity<ProblemDetail> buildProblemDetailResponse(HttpStatus status, String message, String path) {
		ProblemDetail body = ProblemDetail.forStatusAndDetail(status, message);
		body.setTitle(status.getReasonPhrase());
		body.setType(URI.create("about:blank"));
		body.setInstance(URI.create(path));
		return ResponseEntity.status(status).body(body);
	}
}




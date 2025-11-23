package com.flightapp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {

	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<Map<String, String>> handleValidationExceptions(WebExchangeBindException exception) {

		Map<String, String> errors = new HashMap<>();

		exception.getFieldErrors().forEach(error -> {
			String fieldName = error.getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
			log.warn("Validation failed - field: {}, message: {}", fieldName, errorMessage);
		});

		log.info("Returning {} validation errors", errors.size());
		return Mono.just(errors);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Mono<Map<String, String>> handleDuplicateKeyException(DuplicateKeyException ex) {

		Map<String, String> errors = new HashMap<>();
		String message = ex.getMessage();

		if (message.contains("email")) {
			errors.put("email", "Email already exists");
		} else {
			errors.put("error", "Duplicate key error");
		}

		log.error("Duplicate key exception: {}", message);
		return Mono.just(errors);
	}

	@ExceptionHandler(Exception.class)
	public Mono<Map<String, String>> handleGeneralException(Exception ex) {

		Map<String, String> error = new HashMap<>();
		error.put("error", "Internal server error");

		log.error("Unexpected error occurred", ex);
		return Mono.just(error);
	}
}

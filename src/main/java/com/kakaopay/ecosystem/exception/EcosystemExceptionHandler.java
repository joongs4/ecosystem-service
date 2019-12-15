package com.kakaopay.ecosystem.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EcosystemExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(EcosystemExceptionHandler.class.getCanonicalName());

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity handleBadRequestException(BadRequestException e) {

		ResponseEntity<String> response = new ResponseEntity<>(e.getBody(), e.getStatus());
		logger.error(e.getBody());

		return response;
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity handleInternalServerException(InternalServerException e) {

		ResponseEntity<String> response = new ResponseEntity<>(e.getBody(), e.getStatus());
		logger.error(e.getBody());

		return response;
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleRuntimeException(RuntimeException e) {

		ResponseEntity<String> response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		logger.error(e.getMessage());

		return response;
	}

}

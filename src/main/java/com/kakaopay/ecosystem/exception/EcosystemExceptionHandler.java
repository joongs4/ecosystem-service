package com.kakaopay.ecosystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EcosystemExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity test(BadRequestException e) {

		ResponseEntity<String> response = new ResponseEntity<>(e.getBody(), e.getStatus());

		return response;
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity test(InternalServerException e) {

		ResponseEntity<String> response = new ResponseEntity<>(e.getBody(), e.getStatus());

		return response;
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity test(RuntimeException e) {

		ResponseEntity<String> response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		return response;
	}

}

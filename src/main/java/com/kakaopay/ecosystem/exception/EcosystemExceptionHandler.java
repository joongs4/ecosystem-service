package com.kakaopay.ecosystem.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kakaopay.ecosystem.util.EcosystemUtil;

@ControllerAdvice
public class EcosystemExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(EcosystemExceptionHandler.class.getCanonicalName());

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequestException(BadRequestException e) {

		ExceptionResponse exceptionResposne = new ExceptionResponse(e.getBody());

		ResponseEntity<String> response = new ResponseEntity<>(EcosystemUtil.convertExceptionToJson(exceptionResposne),
				e.getStatus());
		logger.error(e.getBody());

		return response;
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<String> handleInternalServerException(InternalServerException e) {
		ExceptionResponse exceptionResposne = new ExceptionResponse(e.getBody());
		ResponseEntity<String> response = new ResponseEntity<>(EcosystemUtil.convertExceptionToJson(exceptionResposne),
				e.getStatus());
		logger.error(e.getBody());

		return response;
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
		ExceptionResponse exceptionResposne = new ExceptionResponse(e.getMessage());
		ResponseEntity<String> response = new ResponseEntity<>(EcosystemUtil.convertExceptionToJson(exceptionResposne),
				HttpStatus.INTERNAL_SERVER_ERROR);
		logger.error(e.getMessage());

		return response;
	}

}

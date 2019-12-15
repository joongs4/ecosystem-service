package com.kakaopay.ecosystem.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalServerException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8591436666279053116L;
	/**
	 * 
	 */
	private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	private String body;

	public InternalServerException(String body) {
		this.body = body;
	}
}

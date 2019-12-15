package com.kakaopay.ecosystem.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2848209886590537448L;
	private final HttpStatus status = HttpStatus.BAD_REQUEST;
	private String body;

	public BadRequestException(String body) {
		this.body = body;
	}

}

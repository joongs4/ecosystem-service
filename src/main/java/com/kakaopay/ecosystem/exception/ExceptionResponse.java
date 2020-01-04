package com.kakaopay.ecosystem.exception;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5688768189353544293L;
	private String message;

	public ExceptionResponse(String message) {
		this.message = message;
	}

}

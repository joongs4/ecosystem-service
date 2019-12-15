package com.kakaopay.ecosystem.jwt;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6861712313572095295L;
	private String token;

	public JwtResponse(String token) {
		this.token = token;
	}

}

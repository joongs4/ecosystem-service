package com.kakaopay.ecosystem.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(Include.NON_NULL)
public class JwtTokenEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 181484122395053674L;
	private String id;
	private String token;
	private String refreshToken;
	private boolean used;

	public JwtTokenEntity(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;

		this.used = false;
	}

}

package com.kakaopay.ecosystem.resource;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.ecosystem.entity.JwtTokenEntity;
import com.kakaopay.ecosystem.entity.UserEntity;
import com.kakaopay.ecosystem.jwt.JwtResponse;
import com.kakaopay.ecosystem.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {

	private final UserService service;

	public UserResource(UserService service) {
		this.service = service;
	}

	@PostMapping(path = "/signup")
	public JwtResponse signUp(@RequestBody UserEntity userEntity) {
		return this.service.signUp(userEntity);
	}

	@PostMapping(path = "/signin")
	public JwtResponse signIn(@RequestBody UserEntity userEntity) throws Exception {

		return this.service.signIn(userEntity);
	}

	@PostMapping(path = "/refresh")
	public JwtResponse getAccessTokenByRefreshToken(
			@RequestHeader(name = "Authorization") String refreshTokenWithBearer) {

		String refreshToken = refreshTokenWithBearer.substring(7);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.service.refreshToken(refreshToken, authentication.getName());
	}

	// 임시 조회용
	@GetMapping(path = "/jwt")
	public List<JwtTokenEntity> findAllJwtToken() {
		return this.service.findAllJwt();
	}

	// 임시 조회용
	@GetMapping
	public List<UserEntity> findAll() {
		return this.service.findAll();
	}

}

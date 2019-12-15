package com.kakaopay.ecosystem.resource;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public String signUp(@RequestBody UserEntity userEntity) {
		return this.service.signUp(userEntity);
	}

	@PostMapping(path = "/signin")
	public JwtResponse signIn(@RequestBody UserEntity userEntity) throws Exception {

		return this.service.signIn(userEntity);
	}

	// 삭제용
	@GetMapping
	public List<UserEntity> findAll() {
		return this.service.findAll();
	}

}

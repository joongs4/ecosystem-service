package com.kakaopay.ecosystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kakaopay.ecosystem.entity.UserEntity;
import com.kakaopay.ecosystem.exception.BadRequestException;
import com.kakaopay.ecosystem.jwt.JwtResponse;
import com.kakaopay.ecosystem.jwt.JwtTokenUtil;
import com.kakaopay.ecosystem.store.UserStore;

@Service
public class UserService implements UserDetailsService {

	private final UserStore store;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public UserService(UserStore store) {
		this.store = store;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public String signUp(UserEntity userEntity) {

		if (store.existsById(userEntity.getUserId())) {
			throw new BadRequestException("User Id already exists");
		}

		String password = userEntity.getUserPassword();
		String encryptedPassword = passwordEncoder.encode(password);
		userEntity.setUserPassword(encryptedPassword);

		store.save(userEntity);
		return userEntity.getUserId();
	}

	public JwtResponse signIn(UserEntity userEntity) {

		authenticate(userEntity.getUserId(), userEntity.getUserPassword());
		final UserDetails userDetails = loadUserByUsername(userEntity.getUserId());
		final String token = jwtTokenUtil.generateToken(userDetails);

		JwtResponse response = new JwtResponse(token);
		return response;
	}

	public UserEntity loadUserByUserId(String userId) {
		return this.store.findByUserId(userId);
	}

	public List<UserEntity> findAll() {

		return this.store.findAll();
	}

	private void authenticate(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new RuntimeException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new RuntimeException("INVALID_CREDENTIALS", e);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = loadUserByUserId(username);
		if (userEntity != null) {
			return new User(userEntity.getUserId(), userEntity.getUserPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}

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

import com.kakaopay.ecosystem.entity.JwtTokenEntity;
import com.kakaopay.ecosystem.entity.UserEntity;
import com.kakaopay.ecosystem.exception.BadRequestException;
import com.kakaopay.ecosystem.jwt.JwtResponse;
import com.kakaopay.ecosystem.jwt.JwtTokenUtil;
import com.kakaopay.ecosystem.store.JwtTokenStore;
import com.kakaopay.ecosystem.store.UserStore;

@Service
public class UserService implements UserDetailsService {

	private final UserStore store;
	private final JwtTokenStore jwtTokenStore;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public UserService(UserStore store, JwtTokenStore jwtTokenStore) {
		this.store = store;
		this.jwtTokenStore = jwtTokenStore;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public JwtResponse signUp(UserEntity userEntity) {

		if (store.existsById(userEntity.getUserId())) {
			throw new BadRequestException("User Id already exists");
		}

		String password = userEntity.getUserPassword();
		String encryptedPassword = passwordEncoder.encode(password);
		userEntity.setUserPassword(encryptedPassword);

		store.save(userEntity);

		JwtResponse response = generateJwtToken(userEntity);

		return response;
	}

	public JwtResponse signIn(UserEntity userEntity) {

		authenticate(userEntity.getUserId(), userEntity.getUserPassword());
		UserDetails userDetails = loadUserByUsername(userEntity.getUserId());
		JwtResponse response = generateJwtToken(userDetails);

		return response;
	}

	public JwtResponse refreshToken(String refreshToken, String userId) {
		JwtResponse response = null;
		JwtTokenEntity jwtTokenEntity = this.jwtTokenStore.findByRefreshToken(refreshToken);
		if (jwtTokenEntity != null && !jwtTokenEntity.isUsed()) {

			UserDetails userDetails = loadUserByUsername(userId);

			if (userDetails == null) {
				throw new RuntimeException("Unable to find the user information");
			}

			response = generateJwtToken(userDetails);

			jwtTokenEntity.setUsed(true);
			jwtTokenStore.save(jwtTokenEntity);
		} else {
			throw new RuntimeException("Invalid refresh token");
		}

		return response;
	}

	public UserEntity loadUserByUserId(String userId) {
		return this.store.findByUserId(userId);
	}

	public List<UserEntity> findAll() {

		return this.store.findAll();
	}

	public List<JwtTokenEntity> findAllJwt() {

		return this.jwtTokenStore.findAll();
	}

	private JwtResponse generateJwtToken(UserDetails userDetails) {
		String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
		String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

		JwtTokenEntity jwtTokenEntity = new JwtTokenEntity(accessToken, refreshToken);
		jwtTokenStore.save(jwtTokenEntity);

		JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken);

		return jwtResponse;
	}

	private JwtResponse generateJwtToken(UserEntity userEntity) {
		UserDetails userDetails = new User(userEntity.getUserId(), userEntity.getUserPassword(), new ArrayList<>());
		return generateJwtToken(userDetails);
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

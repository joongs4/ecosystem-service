package com.kakaopay.ecosystem;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.kakaopay.ecosystem.entity.UserEntity;
import com.kakaopay.ecosystem.jwt.JwtResponse;
import com.kakaopay.ecosystem.resource.UserResource;
import com.kakaopay.ecosystem.service.UserDetailsServiceImpl;
import com.kakaopay.ecosystem.service.UserService;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserResource userResource;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Test
	void testSignUp() {

		final String userId = "testId";
		final String userPassword = "testPassword";

		UserEntity userEntityToSignUp = new UserEntity();
		userEntityToSignUp.setUserId(userId);
		userEntityToSignUp.setUserPassword(userPassword);

		JwtResponse signUpResponse = userResource.signUp(userEntityToSignUp);
		assertNotNull(signUpResponse);

		JwtResponse signInResponse = null;
		try {

			Thread.sleep(1000);

			UserEntity userEntityToSignIn = new UserEntity();
			userEntityToSignIn.setUserId(userId);
			userEntityToSignIn.setUserPassword(userPassword);
			signInResponse = userResource.signIn(userEntityToSignIn);
		} catch (Exception e) {
			signInResponse = null;
		}
		assertNotNull(signInResponse);

	}

	@Test
	void refreshToken() {

		final String userId = "testId3";
		final String userPassword = "testPassword3";

		UserEntity userEntityToSignup = new UserEntity();
		userEntityToSignup.setUserId(userId);
		userEntityToSignup.setUserPassword(userPassword);

		JwtResponse jwtResponse = userResource.signUp(userEntityToSignup);
		assertNotNull(jwtResponse);

		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}

		UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(userEntityToSignup.getUserId());

		SimpleGrantedAuthority refreshTokenAuthority = new SimpleGrantedAuthority("ROLE_REFRESH_TOKEN");
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(refreshTokenAuthority);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, authorities);

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

		JwtResponse refreshedJwtResponse = userResource
				.getAccessTokenByRefreshToken("bearer " + jwtResponse.getRefreshToken());
		assertNotNull(refreshedJwtResponse);

		JwtResponse exceptionResponseExpected = null;
		try {
			exceptionResponseExpected = userResource
					.getAccessTokenByRefreshToken("bearer " + jwtResponse.getRefreshToken());
		} catch (Exception e) {
			exceptionResponseExpected = null;
		}

		assertNull(exceptionResponseExpected);

	}

}

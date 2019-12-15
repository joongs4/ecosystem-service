package com.kakaopay.ecosystem;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakaopay.ecosystem.entity.UserEntity;
import com.kakaopay.ecosystem.jwt.JwtResponse;
import com.kakaopay.ecosystem.resource.UserResource;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserResource userResource;

	@Test
	void testSignUp() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId("testId");
		userEntity.setUserPassword("testPassword");

		JwtResponse jwtResponse = userResource.signUp(userEntity);
		assertNotNull(jwtResponse);
	}

	@Test
	void testSignIn() {

		UserEntity userEntityToSignup = new UserEntity();
		userEntityToSignup.setUserId("testId2");
		userEntityToSignup.setUserPassword("testPassword2");

		userResource.signUp(userEntityToSignup);

		UserEntity userEntity = new UserEntity();
		userEntity.setUserId("testId2");
		userEntity.setUserPassword("testPassword2");
		JwtResponse jwtResponse = null;
		try {
			jwtResponse = userResource.signIn(userEntity);
		} catch (Exception e) {
			e.printStackTrace();
			jwtResponse = null;
		}

		assertNotNull(jwtResponse);
	}

}

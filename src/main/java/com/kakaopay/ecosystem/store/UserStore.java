package com.kakaopay.ecosystem.store;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.kakaopay.ecosystem.entity.UserEntity;
import com.kakaopay.ecosystem.store.jpo.UserJpo;
import com.kakaopay.ecosystem.store.repository.UserRepository;

@Repository
public class UserStore {

	private final UserRepository userRepository;

	public UserStore(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String save(UserEntity userEntity) {

		UserJpo jpoToSave = new UserJpo(userEntity);
		userRepository.save(jpoToSave);

		return jpoToSave.getUserId();
	}

	public boolean existsById(String userId) {
		if (userRepository.existsById(userId)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean existsByUserIdAndUserPassword(String userId, String userPassword) {
		if (userRepository.existsByUserIdAndUserPassword(userId, userPassword)) {
			return true;
		} else {
			return false;
		}

	}

	public List<UserEntity> findAll() {
		List<UserJpo> users = userRepository.findAll();

		return UserJpo.toDomains(users);
	}

	public UserEntity findByUserId(String userId) {
		Optional<UserJpo> user = userRepository.findById(userId);
		if (user.isPresent()) {
			return user.get().toDomain();
		}
		{
			return null;
		}

	}

}

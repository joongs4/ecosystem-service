package com.kakaopay.ecosystem.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.ecosystem.store.jpo.UserJpo;

public interface UserRepository extends JpaRepository<UserJpo, String> {

	boolean existsByUserIdAndUserPassword(String userId, String userPassword);

}

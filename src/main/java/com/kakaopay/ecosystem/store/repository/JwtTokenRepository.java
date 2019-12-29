package com.kakaopay.ecosystem.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.ecosystem.store.jpo.JwtTokenJpo;

public interface JwtTokenRepository extends JpaRepository<JwtTokenJpo, String> {

	Optional<JwtTokenJpo> findByRefreshToken(String refreshToken);
}

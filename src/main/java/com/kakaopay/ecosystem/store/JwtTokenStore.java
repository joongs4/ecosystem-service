package com.kakaopay.ecosystem.store;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.kakaopay.ecosystem.entity.JwtTokenEntity;
import com.kakaopay.ecosystem.store.jpo.JwtTokenJpo;
import com.kakaopay.ecosystem.store.repository.JwtTokenRepository;
import com.kakaopay.ecosystem.util.EcosystemUtil;

@Repository
public class JwtTokenStore {

	private final JwtTokenRepository repository;

	public JwtTokenStore(JwtTokenRepository repository) {
		this.repository = repository;
	}

	public JwtTokenEntity save(JwtTokenEntity entity) {

		JwtTokenJpo jpoToSave = new JwtTokenJpo(entity);
		repository.save(jpoToSave);

		return jpoToSave.toDomain();
	}

	public JwtTokenEntity findByRefreshToken(String refreshToken) {

		Optional<JwtTokenJpo> foundJpo = repository.findByRefreshToken(refreshToken);
		if (foundJpo.isPresent()) {
			return foundJpo.get().toDomain();
		}

		return null;
	}

	public List<JwtTokenEntity> findAll() {
		List<JwtTokenJpo> foundJpos = repository.findAll();

		if (!EcosystemUtil.isNullOrEmpty(foundJpos)) {
			return JwtTokenJpo.toDomains(foundJpos);
		}

		return null;
	}

}

package com.kakaopay.ecosystem.store;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.store.jpo.EcosystemServiceJpo;
import com.kakaopay.ecosystem.store.jpo.RegionJpo;
import com.kakaopay.ecosystem.store.repository.EcosystemServiceRepository;
import com.kakaopay.ecosystem.util.EcosystemUtil;

@Repository
public class EcosystemServiceStore {

	private final EcosystemServiceRepository repository;

	public EcosystemServiceStore(EcosystemServiceRepository repository) {
		this.repository = repository;
	}

	public EcosystemServiceEntity save(EcosystemServiceEntity entity) {

		EcosystemServiceJpo jpoToSave = new EcosystemServiceJpo(entity);
		repository.save(jpoToSave);

		return jpoToSave.toDomain(true);
	}

	public List<EcosystemServiceEntity> findByRegion(String region) {

		List<EcosystemServiceJpo> foundJpos = repository.findByRegionContaining(region);
		if (!EcosystemUtil.isNullOrEmpty(foundJpos)) {
			return EcosystemServiceJpo.toDomains(foundJpos, true);
		}
		return null;
	}

	public EcosystemServiceEntity findById(String id) {

		Optional<EcosystemServiceJpo> foundJpo = repository.findById(id);
		if (foundJpo.isPresent()) {
			return foundJpo.get().toDomain(true);
		}

		return null;
	}

	public List<EcosystemServiceEntity> findAll() {

		List<EcosystemServiceJpo> foundJpos = repository.findAll();
		if (!EcosystemUtil.isNullOrEmpty(foundJpos)) {
			return EcosystemServiceJpo.toDomains(foundJpos, true);
		}

		return null;

	}

}

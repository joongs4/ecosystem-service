package com.kakaopay.ecosystem.store;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.store.jpo.EcosystemServiceJpo;
import com.kakaopay.ecosystem.store.repository.EcosystemServiceRepository;

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
		if (foundJpos != null && !foundJpos.isEmpty()) {
			return EcosystemServiceJpo.toDomains(foundJpos, true);
		}
		return null;
	}

	public EcosystemServiceEntity findById(Integer id) {

		Optional<EcosystemServiceJpo> foundJpo = repository.findById(id);
		if (foundJpo.isPresent()) {
			return foundJpo.get().toDomain(true);
		}

		return null;
	}

	public List<EcosystemServiceEntity> findAll() {

		List<EcosystemServiceJpo> foundJpos = repository.findAll();
		if (foundJpos != null && !foundJpos.isEmpty()) {
			return EcosystemServiceJpo.toDomains(foundJpos, true);
		}

		return null;

	}

}

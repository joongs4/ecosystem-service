package com.kakaopay.ecosystem.store;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.store.jpo.RegionJpo;
import com.kakaopay.ecosystem.store.repository.RegionRepository;
import com.kakaopay.ecosystem.util.EcosystemUtil;

@Repository
public class RegionStore {

	private final RegionRepository repository;

	public RegionStore(RegionRepository repository) {
		this.repository = repository;
	}

	public RegionEntity save(RegionEntity entity) {

		RegionJpo jpo = new RegionJpo(entity);
		this.repository.save(jpo);
		return jpo.toDomain(true);
	}

	public List<RegionEntity> findAll() {
		List<RegionJpo> foundJpos = this.repository.findAll();
		if (!EcosystemUtil.isNullOrEmpty(foundJpos)) {
			return RegionJpo.toDomains(foundJpos, true);
		}

		return null;
	}

	public List<RegionEntity> findAllByNameContaining(String regionName) {

		List<RegionJpo> foundJpos = this.repository.findByNameContaining(regionName);
		if (!EcosystemUtil.isNullOrEmpty(foundJpos)) {
			return RegionJpo.toDomains(foundJpos, true);
		}

		return null;
	}

	public RegionEntity findByName(String regionName) {

		List<RegionJpo> foundJpos = this.repository.findByNameContaining(regionName);
		if (!EcosystemUtil.isNullOrEmpty(foundJpos)) {
			return foundJpos.get(0).toDomain(true);
		}
		return null;
	}

}

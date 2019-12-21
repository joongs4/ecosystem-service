package com.kakaopay.ecosystem.service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kakaopay.ecosystem.entity.ProgramsByRegionEntity;
import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.store.RegionStore;
import com.kakaopay.ecosystem.util.AddressManager;

@Service
@Transactional
public class RegionService {
	private final RegionStore store;
	private final AddressManager addressManager;
	private ReentrantLock reentrantLock = new ReentrantLock();

	public RegionService(RegionStore store, AddressManager addressManager) {
		this.store = store;
		this.addressManager = addressManager;
	}

	public List<ProgramsByRegionEntity> findProgramsByRegion(String region) {
		List<RegionEntity> regionsToAdd = this.store.findAllByNameContaining(region);
		if (regionsToAdd == null) {
			return null;
		}

		List<ProgramsByRegionEntity> programsByRegionList = ProgramsByRegionEntity.fromRegionEntities(regionsToAdd);
		return programsByRegionList;
	}

	public List<RegionEntity> findRegion(String region) {
		List<RegionEntity> regions = this.store.findAllByNameContaining(region);
		return regions;
	}

	public List<RegionEntity> findAll() {

		return this.store.findAll();
	}

	public RegionEntity saveIfNotExists(String region) {

		RegionEntity retVal = null;

		String realRegion = addressManager.getStandardAddress(region);

		try {
			reentrantLock.lock();
			retVal = saveRegion(realRegion);
		} finally {
			reentrantLock.unlock();
		}

		return retVal;
	}

	private RegionEntity saveRegion(String region) {
		RegionEntity savedRegion = this.store.findByName(region);
		if (savedRegion == null) {
			// 기존
//			reentrantLock.lock();
			RegionEntity entityToSave = new RegionEntity(region);
			savedRegion = this.store.save(entityToSave);
//			reentrantLock.unlock();
		}

		return savedRegion;
	}

}

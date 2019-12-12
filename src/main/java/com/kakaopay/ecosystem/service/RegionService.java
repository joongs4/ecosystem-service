package com.kakaopay.ecosystem.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kakaopay.ecosystem.entity.KakaoLocalDocumentEntity;
import com.kakaopay.ecosystem.entity.KakaoLocalDocumentsEntity;
import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.store.RegionStore;

@Service
@Transactional
public class RegionService {
	private final RegionStore store;
	private final KakaoLocalService kakaoLocalService;
	private ReentrantLock reentrantLock = new ReentrantLock();

	public RegionService(RegionStore store, KakaoLocalService kakaoLocalService) {
		this.store = store;
		this.kakaoLocalService = kakaoLocalService;
	}

	public Set<RegionEntity> findRegion(String region) {

		List<RegionEntity> regionsToAdd = this.store.findAllByNameEnd(region);
		Set<RegionEntity> regions = new HashSet<>();
		if (regionsToAdd != null) {
			regions.addAll(regionsToAdd);
			return regions;
		}

		return null;
	}

	public List<RegionEntity> findAll() {

		return this.store.findAll();
	}

	public Set<RegionEntity> saveIfNotExists(String region) {

		Set<RegionEntity> retVal = new HashSet<>();
		String regionWODetails = removeDetailsFromAddress(region);
		KakaoLocalDocumentsEntity kakaoLocalDocumentsEntity = kakaoLocalService.searchBykeyword(regionWODetails);

		if (kakaoLocalDocumentsEntity != null && !kakaoLocalDocumentsEntity.getDocuments().isEmpty()) {
			for (KakaoLocalDocumentEntity kakaoLocalDocumentEntity : kakaoLocalDocumentsEntity.getDocuments()) {

				String addressWithoutDetails = removeDetailsFromAddress(kakaoLocalDocumentEntity.getAddress_name());

				reentrantLock.lock();
				retVal.addAll(saveBySpace(addressWithoutDetails));
				reentrantLock.unlock();
			}
		} else {
			reentrantLock.lock();
			retVal.addAll(saveBySpace(regionWODetails));
			reentrantLock.unlock();
		}

		return retVal;
	}

	private Set<RegionEntity> saveBySpace(String region) {

		Set<RegionEntity> regions = new HashSet<>();
		String[] splitRegions = region.split(" ");
		StringBuilder sb = new StringBuilder();
		RegionEntity savedEntityIfNotExists = null;
		for (int i = 0; i < splitRegions.length; i++) {
			String regionNameToSave = removeReservedCharacterFromRegion(sb.append(splitRegions[i]).toString());
			savedEntityIfNotExists = this.store.findByName(regionNameToSave);
			if (savedEntityIfNotExists == null) {
				RegionEntity entityToSave = new RegionEntity(regionNameToSave);
				savedEntityIfNotExists = this.store.save(entityToSave);
			}
			regions.add(savedEntityIfNotExists);
			sb.append(" ");
		}

		return regions;
	}

	private String removeReservedCharacterFromRegion(String region) {
		if (region.endsWith("시") || region.endsWith("군") || region.endsWith("동") || region.endsWith("읍")
				|| region.endsWith("면") || region.endsWith("리") || region.endsWith("산")) {
			return region.substring(0, region.length() - 1);
		}

		return region;
	}

	private String removeDetailsFromAddress(String address) {
		String retVal = null;
		retVal = address.replaceAll("[0-9,-,번지,-]", "");
		if (retVal.endsWith(" ")) {
			retVal = retVal.substring(0, retVal.length() - 1);
		}

		return retVal;
	}

}

package com.kakaopay.ecosystem.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.store.EcosystemServiceStore;

@Service
@Transactional
public class EcosystemService {

	private final String initDataFile = "사전과제2.csv";

	private final EcosystemServiceStore store;
	private final RegionService regionService;

	public EcosystemService(EcosystemServiceStore store, RegionService regionService) {
		this.store = store;
		this.regionService = regionService;
		init();
	}

	public List<EcosystemServiceEntity> findAll() {

		return this.store.findAll();
	}

	public Set<EcosystemServiceEntity> findByRegion(String region) {

		Set<RegionEntity> regions = this.regionService.findRegion(region);
		if (regions != null && !regions.isEmpty()) {
			Set<EcosystemServiceEntity> retVal = new HashSet<>();
			for (RegionEntity currentRegion : regions) {
				if (currentRegion.getEcosystemServices() != null && !currentRegion.getEcosystemServices().isEmpty()) {
					retVal.addAll(currentRegion.getEcosystemServices());
				}
			}

			return retVal;
		}

		return null;
	}

	public EcosystemServiceEntity updateEcosystemService(Integer id, EcosystemServiceEntity entity) {

		EcosystemServiceEntity entityToUpdate = this.store.findById(id);
		if (entityToUpdate == null) {
			throw new RuntimeException("Unable to find ecosystem service by id(" + id + ")");
		}

		setValues(entity, entityToUpdate);
		this.store.save(entityToUpdate);

		return entityToUpdate;
	}

	public EcosystemServiceEntity saveEcosystemService(EcosystemServiceEntity entity) {

		// Validation

		return this.store.save(entity);
	}

	private EcosystemServiceEntity setValues(EcosystemServiceEntity sourceEntity, EcosystemServiceEntity targetEntity) {

		if (sourceEntity.getProgramDetailedIntroduction() != null) {
			targetEntity.setProgramDetailedIntroduction(sourceEntity.getProgramDetailedIntroduction());
		}

		if (sourceEntity.getProgramIntroduction() != null) {
			targetEntity.setProgramIntroduction(sourceEntity.getProgramIntroduction());
		}

		if (sourceEntity.getProgramName() != null) {
			targetEntity.setProgramName(sourceEntity.getProgramName());
		}

		if (sourceEntity.getRegion() != null) {
			targetEntity.setRegion(sourceEntity.getRegion());
		}

		if (sourceEntity.getTheme() != null) {
			targetEntity.setTheme(sourceEntity.getTheme());
		}

		return targetEntity;
	}

	private void init() {

		try (Reader reader = Files
				.newBufferedReader(Paths.get(ResourceUtils.getFile("classpath:" + initDataFile).getPath()));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

			for (CSVRecord csvRecord : csvParser) {
				EcosystemServiceEntity entityToSave = new EcosystemServiceEntity();
				entityToSave.setId(Integer.valueOf(csvRecord.get("번호")));
				entityToSave.setProgramName(csvRecord.get("프로그램명"));
				entityToSave.setTheme(csvRecord.get("테마별 분류"));
				entityToSave.setRegion(csvRecord.get("서비스 지역"));
				entityToSave.setProgramIntroduction(csvRecord.get("프로그램 소개"));
				entityToSave.setProgramDetailedIntroduction(csvRecord.get("프로그램 상세 소개"));
				
				if(entityToSave.getProgramName().startsWith("오감만족!")) {
					System.out.println("asdasd");
				}

				Set<RegionEntity> regions = regionService.saveIfNotExists(entityToSave.getRegion());
				if (regions != null) {
					entityToSave.setRegions(regions);
				}

				this.store.save(entityToSave);
			}

		} catch (IOException e) {
			throw new RuntimeException("Failed to save init data");
		}
	}

}

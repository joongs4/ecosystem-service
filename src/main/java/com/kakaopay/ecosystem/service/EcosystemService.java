package com.kakaopay.ecosystem.service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.kakaopay.ecosystem.calculate.CalculateStrategy;
import com.kakaopay.ecosystem.calculate.RecommendationCalculateStrategy;
import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.entity.ProgramRecommendationEntity;
import com.kakaopay.ecosystem.entity.ProgramRegionByKeyword;
import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.entity.WordCountEntity;
import com.kakaopay.ecosystem.exception.BadRequestException;
import com.kakaopay.ecosystem.exception.InternalServerException;
import com.kakaopay.ecosystem.search.KmpSearchStrategy;
import com.kakaopay.ecosystem.search.SearchStrategy;
import com.kakaopay.ecosystem.store.EcosystemServiceStore;
import com.kakaopay.ecosystem.util.EcosystemUtil;

@Service
@Transactional
public class EcosystemService {

	private final String initDataFile = "사전과제2.csv";

	private final EcosystemServiceStore store;
	private final RegionService regionService;
	private final SearchStrategy searchStrategy;
	private final CalculateStrategy calculateStrategy;

	public EcosystemService(EcosystemServiceStore store, RegionService regionService) {
		this.store = store;
		this.regionService = regionService;
		this.searchStrategy = new KmpSearchStrategy();
		this.calculateStrategy = new RecommendationCalculateStrategy();
		init();
	}

	public List<EcosystemServiceEntity> findAll() {

		return this.store.findAll();
	}

	public ProgramRegionByKeyword findRegionByKeyword(String keyword) {

		List<EcosystemServiceEntity> allEcosystemServiceEntities = findAll();

		EcosystemServiceEntity[] matchingEntities = allEcosystemServiceEntities.stream()
				.filter(entity -> entity.getProgramIntroduction().contains(keyword))
				.toArray(EcosystemServiceEntity[]::new);

		ProgramRegionByKeyword programRegionByKeyword = new ProgramRegionByKeyword(keyword, matchingEntities);

		return programRegionByKeyword;
	}

	public WordCountEntity findKeywordCount(String keyword) {

		WordCountEntity retVal = new WordCountEntity(keyword);
		List<EcosystemServiceEntity> allEcosystemServiceEntities = findAll();
		for (EcosystemServiceEntity ecosystemServiceEntity : allEcosystemServiceEntities) {
			if (searchStrategy.containsPattern(ecosystemServiceEntity.getProgramDetailedIntroduction(), keyword)) {
				retVal.plus();
			}
		}

		return retVal;
	}

	public ProgramRecommendationEntity findRecommendation(String region, String keyword) {
		List<RegionEntity> regions = regionService.findRegion(region);

		List<EcosystemServiceEntity> ecosystemServiceEntities = regions.stream()
				.flatMap(eachRegion -> eachRegion.getEcosystemServices().stream()).collect(Collectors.toList());

		ProgramRecommendationEntity recommendedProgram = ecosystemServiceEntities.stream()
				.map(each -> new ProgramRecommendationEntity(keyword, each, searchStrategy, calculateStrategy))
				.max(ProgramRecommendationEntity::compareTo).get();

		return recommendedProgram;

	}

	public EcosystemServiceEntity findById(String id) {
		EcosystemServiceEntity foundEntity = this.store.findById(id);

		return foundEntity;
	}

	public EcosystemServiceEntity updateEcosystemService(String id, EcosystemServiceEntity entity) {

		EcosystemServiceEntity entityToUpdate = this.store.findById(id);
		if (entityToUpdate == null) {
			throw new BadRequestException("Unable to find ecosystem service by id(" + id + ")");
		}

		setValues(entity, entityToUpdate);

		RegionEntity region = regionService.saveIfNotExists(entityToUpdate.getRegionName());
		if (region != null && !region.equals(entityToUpdate.getRegion())) {
			entityToUpdate.setRegion(region);
		}

		return this.store.save(entityToUpdate);
	}

	public EcosystemServiceEntity saveEcosystemService(EcosystemServiceEntity entity) {

		validateEcosystemServiceEntity(entity);

		RegionEntity region = regionService.saveIfNotExists(entity.getRegionName());
		if (region != null) {
			entity.setRegion(region);
		}

		return this.store.save(entity);
	}

	private void validateEcosystemServiceEntity(EcosystemServiceEntity entity) {

		if (EcosystemUtil.isNullOrEmpty(entity.getRegionName())) {
			throw new BadRequestException("regionName is required but missing");
		}

		if (EcosystemUtil.isNullOrEmpty(entity.getTheme())) {
			throw new BadRequestException("theme is required but missing");
		}

		if (EcosystemUtil.isNullOrEmpty(entity.getProgramName())) {
			throw new BadRequestException("programName is required but missing");
		}

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

		if (sourceEntity.getRegionName() != null) {
			targetEntity.setRegionName(sourceEntity.getRegionName());
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

		ClassPathResource classPathResource = new ClassPathResource(initDataFile);
		try (Reader targetReader = new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);
				CSVParser csvParser = new CSVParser(targetReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

			for (CSVRecord csvRecord : csvParser) {
				EcosystemServiceEntity entityToSave = new EcosystemServiceEntity();
				entityToSave.setProgramName(csvRecord.get("프로그램명"));
				entityToSave.setTheme(csvRecord.get("테마별 분류"));
				entityToSave.setRegionName(csvRecord.get("서비스 지역"));
				entityToSave.setProgramIntroduction(csvRecord.get("프로그램 소개"));
				entityToSave.setProgramDetailedIntroduction(csvRecord.get("프로그램 상세 소개"));

				RegionEntity region = regionService.saveIfNotExists(entityToSave.getRegionName());
				if (region != null) {
					entityToSave.setRegion(region);
				}

				this.store.save(entityToSave);
			}

		} catch (Exception e) {

			throw new InternalServerException("Failed to save init data");
		}
	}

}

package com.kakaopay.ecosystem.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.kakaopay.ecosystem.calculate.CalculateStrategy;
import com.kakaopay.ecosystem.calculate.RecommendationCalculateStrategy;
import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.entity.ProgramRecommendationEntity;
import com.kakaopay.ecosystem.entity.ProgramRegionByKeyword;
import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.entity.WordCountEntity;
import com.kakaopay.ecosystem.exception.BadRequestException;
import com.kakaopay.ecosystem.search.KmpSearchStrategy;
import com.kakaopay.ecosystem.search.SearchStrategy;
import com.kakaopay.ecosystem.store.EcosystemServiceStore;
import com.kakaopay.ecosystem.util.EcosystemUtil;

@Service
@Transactional
public class EcosystemService {

//	private final String initDataFile = "사전과제2.csv";
	private final String initDataFile = "initData.csv";
	private final ResourceLoader resourceLoader;

	private final EcosystemServiceStore store;
	private final RegionService regionService;
	private final SearchStrategy searchStrategy;
	private final CalculateStrategy calculateStrategy;

	public EcosystemService(EcosystemServiceStore store, RegionService regionService, ResourceLoader resourceLoader) {
		this.store = store;
		this.regionService = regionService;
		this.searchStrategy = new KmpSearchStrategy();
		this.calculateStrategy = new RecommendationCalculateStrategy();
		this.resourceLoader = resourceLoader;
		init();
	}

	public List<EcosystemServiceEntity> findAll() {

		return this.store.findAll();
	}

	public ProgramRegionByKeyword findRegionByKeyword(String keyword) {

		List<EcosystemServiceEntity> allEcosystemServiceEntities = findAll();
		List<EcosystemServiceEntity> matchingEntities = allEcosystemServiceEntities.stream()
				.filter(entity -> entity.getProgramIntroduction().contains(keyword)).collect(Collectors.toList());

		EcosystemServiceEntity[] matchingEntitiesInArray = new EcosystemServiceEntity[matchingEntities.size()];
		ProgramRegionByKeyword programRegionByKeyword = new ProgramRegionByKeyword(keyword,
				matchingEntities.toArray(matchingEntitiesInArray));

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
		this.store.save(entityToUpdate);

		return entityToUpdate;
	}

	public EcosystemServiceEntity saveEcosystemService(EcosystemServiceEntity entity) {

		validateEcosystemServiceEntity(entity);
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

		if (sourceEntity.getRegion() != null) {
			targetEntity.setRegion(sourceEntity.getRegion());
		}

		if (sourceEntity.getTheme() != null) {
			targetEntity.setTheme(sourceEntity.getTheme());
		}

		return targetEntity;
	}

	private void init() {

//		try {
//			File a = resourceLoader.getResource("classpath:" + initDataFile).getFile();
//
//			Files.newBufferedReader(Paths.get(resourceLoader.getResource("").getURI()));
////			Paths.get(Files.newBufferedReader(resourceLoader.getResource("").getFile().toURI()));
//		} catch (Exception e) {
//
//		}
//		String uri = null;
//		try {
//			uri = ResourceUtils.getFile("classpath:" + initDataFile).toURI().toString().split("!")[1];
//		} catch (Exception e) {
//			System.out.println("=====================Path read exception=====================");
//			e.printStackTrace();
//		}
//
//		try (Reader reader = Files.newBufferedReader(Paths.get(uri));
//				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
//
//			System.out.println("====================성공================");
//
//		} catch (Exception e) {
//			System.out.println("==========실패==============");
//			e.printStackTrace();
//		}

		
		
		try {

			ClassPathResource classPathResource = new ClassPathResource("/src/main/resources/" + initDataFile);
			InputStream inputStream = Files.newInputStream(Paths.get(classPathResource.getPath()));
			
			
		} catch (Exception e) {

		}

//		try (Reader reader = Files
//				.newBufferedReader(Paths.get(ResourceUtils.getFile("classpath:" + initDataFile).getPath()));
//				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

		try (Reader reader = Files.newBufferedReader(
				Paths.get(new ClassPathResource("classpath:" + "/src/main/resources/" + initDataFile).getPath()));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

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

		} catch (IOException e) {
			e.printStackTrace();
			throw new BadRequestException("Failed to save init data");
		}
	}

}

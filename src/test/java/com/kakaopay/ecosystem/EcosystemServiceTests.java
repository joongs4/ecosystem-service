package com.kakaopay.ecosystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.entity.ProgramRecommendationEntity;
import com.kakaopay.ecosystem.entity.ProgramRegionByKeyword;
import com.kakaopay.ecosystem.entity.ProgramsByRegionEntity;
import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.entity.WordCountEntity;
import com.kakaopay.ecosystem.service.EcosystemService;
import com.kakaopay.ecosystem.service.RegionService;

@SpringBootTest
public class EcosystemServiceTests {

	@Autowired
	private EcosystemService ecosystemService;

	@Autowired
	private RegionService regionService;

	@Test
	void testFindAllEcosystemAndFindByIdEcosystem() throws Exception {
		List<EcosystemServiceEntity> results = ecosystemService.findAll();

		assertNotNull(results);
		assertNotEquals(0, results.size());

		EcosystemServiceEntity firstEntity = results.get(0);
		EcosystemServiceEntity entityFoundById = ecosystemService.findById(firstEntity.getId());

		assertNotNull(entityFoundById);

	}

	@Test
	void testFindRegionByKeyword() throws Exception {

		final String keyword = "세계문화유산";

		ProgramRegionByKeyword programRegionByKeyword = ecosystemService.findRegionByKeyword(keyword);

		assertNotNull(programRegionByKeyword);
		assertEquals(keyword, programRegionByKeyword.getKeyword());

	}

	@Test
	void testFindKeywordCount() throws Exception {
		final String keyword = "문화";

		WordCountEntity wordCountEntity = ecosystemService.findKeywordCount(keyword);

		assertNotNull(wordCountEntity);
		assertEquals(keyword, wordCountEntity.getKeyword());
	}

	@Test
	void testFindRecommendation() throws Exception {

		final String region = "평창";
		final String keyword = "국립공원";

		ProgramRecommendationEntity programRecommendationEntity = ecosystemService.findRecommendation(region, keyword);

		assertNotNull(programRecommendationEntity);
		assertEquals(keyword, programRecommendationEntity.getKeyword());

	}

	@Test
	void testSaveEcosystemAndUpdateEcosystem() throws Exception {

		EcosystemServiceEntity entityToSave = new EcosystemServiceEntity();
		entityToSave.setTheme("Test");
		entityToSave.setProgramName("TestProgram");
		entityToSave.setRegionName("강원도 속초시");
		entityToSave.setProgramIntroduction("프로그램 소개 테스트");
		entityToSave.setProgramDetailedIntroduction("프로그램 상세 소개");

		EcosystemServiceEntity savedEntity = ecosystemService.saveEcosystemService(entityToSave);
		assertNotNull(savedEntity);

		EcosystemServiceEntity foundEntity = ecosystemService.findById(savedEntity.getId());
		assertNotNull(foundEntity);

		final String theme = "testTheme";

		foundEntity.setTheme(theme);
		EcosystemServiceEntity updatedEntity = ecosystemService.updateEcosystemService(foundEntity.getId(),
				foundEntity);

		assertNotNull(updatedEntity);
		assertEquals(theme, updatedEntity.getTheme());

	}

	@Test
	void testFindProgramsByRegion() throws Exception {
		final String region = "평창군";
		List<ProgramsByRegionEntity> programsByRegionEntities = regionService.findProgramsByRegion(region);
		assertNotNull(programsByRegionEntities);
		assertNotEquals(0, programsByRegionEntities.size());

		List<RegionEntity> regionEntitiesFoundByRegion = regionService.findRegion(region);
		assertNotNull(regionEntitiesFoundByRegion);
		assertNotEquals(0, regionEntitiesFoundByRegion.size());

		List<RegionEntity> regionEntitiesFoundAll = regionService.findAll();
		assertNotNull(regionEntitiesFoundAll);
		assertNotEquals(0, regionEntitiesFoundAll.size());

		RegionEntity regionEntitySavedIfNotExists = regionService.saveIfNotExists("강원도 평창군");
		assertNotNull(regionEntitySavedIfNotExists);

	}

}

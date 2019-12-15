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
import com.kakaopay.ecosystem.entity.WordCountEntity;
import com.kakaopay.ecosystem.resource.EcosystemServiceResource;

@SpringBootTest
public class EcosystemServiceTests {

	@Autowired
	EcosystemServiceResource ecosystemServiceResource;

	@Test
	void testFindById() {
		List<EcosystemServiceEntity> ecosystemServiceEntities = ecosystemServiceResource.findAll();
		assertNotNull(ecosystemServiceEntities);
		assertNotEquals(0, ecosystemServiceEntities.size());

		EcosystemServiceEntity entityFoundByid = ecosystemServiceResource
				.findById(ecosystemServiceEntities.get(0).getId());

		assertNotNull(entityFoundByid);
	}

	@Test
	void testSaveEcosystemServiceEntity() {

		EcosystemServiceEntity entityToSave = new EcosystemServiceEntity();
		entityToSave.setTheme("Test");
		entityToSave.setProgramName("TestProgram");
		entityToSave.setRegionName("강원도 속초시");
		entityToSave.setProgramIntroduction("프로그램 소개 테스트");
		entityToSave.setProgramDetailedIntroduction("프로그램 상세 소개");

		EcosystemServiceEntity savedEntity = ecosystemServiceResource.saveEcosystemService(entityToSave);
		assertNotNull(savedEntity);

		EcosystemServiceEntity foundEntity = ecosystemServiceResource.findById(savedEntity.getId());
		assertNotNull(foundEntity);
	}

	@Test
	void testUpdateEcosystemServiceEntity() {

		final String testThemeValue = "테스트";

		List<EcosystemServiceEntity> ecosystemServiceEntities = ecosystemServiceResource.findAll();
		assertNotNull(ecosystemServiceEntities);
		assertNotEquals(0, ecosystemServiceEntities.size());

		EcosystemServiceEntity entityFoundById = ecosystemServiceResource
				.findById(ecosystemServiceEntities.get(0).getId());

		entityFoundById.setTheme(testThemeValue);

		EcosystemServiceEntity updatedEcosystemServiceEntity = ecosystemServiceResource
				.updateEcosystemService(entityFoundById.getId(), entityFoundById);

		assertEquals(testThemeValue, updatedEcosystemServiceEntity.getTheme());
	}

	@Test
	void testFindRegionByKeyword() {

		final String testKeywordValue = "세계문화유산";

		ProgramRegionByKeyword programRegionFoundByKeyword = ecosystemServiceResource
				.findRegionByKeyword(testKeywordValue);
		assertNotNull(programRegionFoundByKeyword);
	}

	@Test
	void testFindKeywordCount() {
		final String testKeywordValue = "문화";
		WordCountEntity wordCountEntity = ecosystemServiceResource.findKeywordCount(testKeywordValue);
		assertNotNull(wordCountEntity);
	}

	@Test
	void testFindRecommendation() {

		final String testRegionValue = "남해";
		final String testKeywordValue = "생태체험";

		ProgramRecommendationEntity recommendationEntity = ecosystemServiceResource.findRecommendation(testRegionValue,
				testKeywordValue);
		assertNotNull(recommendationEntity);
	}

}

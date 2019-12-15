package com.kakaopay.ecosystem;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakaopay.ecosystem.entity.ProgramsByRegionEntity;
import com.kakaopay.ecosystem.resource.RegionResource;

@SpringBootTest
class RegionServiceTests {

	@Autowired
	private RegionResource regionResource;

	@Test
	void testFindProgramsByRegion() {
		List<ProgramsByRegionEntity> regions = regionResource.findProgramsByRegion("평창군");
		assertNotNull(regions);
		assertNotEquals(0, regions.size());
	}
}

package com.kakaopay.ecosystem.resource;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.ecosystem.entity.ProgramsByRegionEntity;
import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.service.RegionService;

@RestController
@RequestMapping("region")
public class RegionResource {

	private final RegionService service;

	public RegionResource(RegionService service) {
		this.service = service;
	}

	@GetMapping
	public List<RegionEntity> findAll() {

		return this.service.findAll();
	}

	@GetMapping(path = "/{region}")
	public List<ProgramsByRegionEntity> findProgramsByRegion(@PathVariable(name = "region") String region) {

		return this.service.findProgramsByRegion(region);
	}
}

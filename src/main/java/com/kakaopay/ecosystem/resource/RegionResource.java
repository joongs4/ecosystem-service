package com.kakaopay.ecosystem.resource;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.ecosystem.entity.RegionEntity;
import com.kakaopay.ecosystem.service.RegionService;

@RestController
public class RegionResource {

	private final RegionService service;

	public RegionResource(RegionService service) {
		this.service = service;
	}

	@GetMapping(path = "/region")
	public List<RegionEntity> findAll() {

		return this.service.findAll();
	}

}

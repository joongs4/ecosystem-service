package com.kakaopay.ecosystem.resource;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.entity.ProgramRecommendationEntity;
import com.kakaopay.ecosystem.entity.ProgramRegionByKeyword;
import com.kakaopay.ecosystem.entity.WordCountEntity;
import com.kakaopay.ecosystem.service.EcosystemService;

@RestController
@RequestMapping(path = "ecosystem")
public class EcosystemServiceResource {

	private final EcosystemService service;

	public EcosystemServiceResource(EcosystemService service) {
		this.service = service;
	}

	@GetMapping
	public List<EcosystemServiceEntity> findAll() {

		return this.service.findAll();
	}

	@GetMapping(path = "/region")
	public ProgramRegionByKeyword findRegionByKeyword(@RequestParam(name = "keyword") String keyword) {

		return this.service.findRegionByKeyword(keyword);
	}

	@GetMapping(path = "/count")
	public WordCountEntity findKeywordCount(@RequestParam(name = "keyword") String keyword) {
		return this.service.findKeywordCount(keyword);
	}

	@GetMapping(path = "/recommendation")
	public ProgramRecommendationEntity findRecommendation(@RequestParam(name = "region") String region,
			@RequestParam(name = "keyword") String keyword) {
		return this.service.findRecommendation(region, keyword);
	}

	@GetMapping(path = "/{id}")
	public EcosystemServiceEntity findById(@PathVariable String id) {
		return this.service.findById(id);
	}

	@PutMapping(path = "/{id}")
	public EcosystemServiceEntity updateEcosystemService(@PathVariable String id,
			@RequestBody EcosystemServiceEntity entity) {

		return this.service.updateEcosystemService(id, entity);
	}

	@PostMapping
	public EcosystemServiceEntity saveEcosystemService(@RequestBody EcosystemServiceEntity entity) {

		return this.service.saveEcosystemService(entity);
	}
}

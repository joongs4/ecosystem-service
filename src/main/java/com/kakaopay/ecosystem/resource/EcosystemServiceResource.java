package com.kakaopay.ecosystem.resource;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.service.EcosystemService;

@RestController
public class EcosystemServiceResource {

	private final EcosystemService service;

	public EcosystemServiceResource(EcosystemService service) {
		this.service = service;
	}

	@GetMapping(path = "/test")
	public String test() {

		return "success";
	}

	@GetMapping(path = "/findAll")
	public List<EcosystemServiceEntity> findAll() {

		return this.service.findAll();
	}

	@GetMapping(path = "/{region}")
	public Set<EcosystemServiceEntity> findByRegion(@PathVariable String region) {

		return this.service.findByRegion(region);
	}

	@PutMapping(path = "/{id}")
	public EcosystemServiceEntity updateEcosystemService(@PathVariable Integer id,
			@RequestBody EcosystemServiceEntity entity) {

		return this.service.updateEcosystemService(id, entity);
	}

	@PostMapping
	public EcosystemServiceEntity saveEcosystemService(@RequestBody EcosystemServiceEntity entity) {

		return this.service.saveEcosystemService(entity);
	}
}

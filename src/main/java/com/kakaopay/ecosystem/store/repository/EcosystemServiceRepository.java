package com.kakaopay.ecosystem.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.ecosystem.store.jpo.EcosystemServiceJpo;

public interface EcosystemServiceRepository extends JpaRepository<EcosystemServiceJpo, String> {

	@EntityGraph(attributePaths = "regions")
	List<EcosystemServiceJpo> findByRegionContaining(String region);

}

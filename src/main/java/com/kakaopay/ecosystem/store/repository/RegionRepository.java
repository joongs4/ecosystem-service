package com.kakaopay.ecosystem.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.ecosystem.store.jpo.RegionJpo;

public interface RegionRepository extends JpaRepository<RegionJpo, String> {

	@EntityGraph(attributePaths = "ecosystemServices")
	List<RegionJpo> findByNameContaining(String name);

	@EntityGraph(attributePaths = "ecosystemServices")
	List<RegionJpo> findByNameEndsWith(String name);
}

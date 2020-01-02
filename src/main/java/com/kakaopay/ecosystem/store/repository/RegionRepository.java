package com.kakaopay.ecosystem.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.ecosystem.store.jpo.RegionJpo;

public interface RegionRepository extends JpaRepository<RegionJpo, String> {

	List<RegionJpo> findByNameContaining(String name);

	List<RegionJpo> findByNameEndsWith(String name);
}

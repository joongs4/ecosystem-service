package com.kakaopay.ecosystem.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramRegionByKeyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5851836789150792902L;

	private String keyword;
	private List<Program> programs;

	public ProgramRegionByKeyword(String keyword, EcosystemServiceEntity... ecosystemServiceEntities) {
		this.keyword = keyword;

		if (ecosystemServiceEntities == null) {
			return;
		}

		programs = new ArrayList<>();
		Map<String, Program> regionCounter = new HashMap<>();

		for (EcosystemServiceEntity ecosystemServiceEntity : ecosystemServiceEntities) {
			if (regionCounter.containsKey(ecosystemServiceEntity.getRegion().getName())) {
				Program program = regionCounter.get(ecosystemServiceEntity.getRegion().getName());
				program.plus();
			} else {
				Program program = new Program(ecosystemServiceEntity.getRegion().getName());
				program.plus();
				regionCounter.put(ecosystemServiceEntity.getRegion().getName(), program);
				programs.add(program);
			}
		}
	}

	@Getter
	@Setter
	class Program implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8510967911620279963L;
		private String region;
		private int count;

		public Program(String region) {
			this.region = region;
		}

		public void plus() {
			count++;
		}
	}

}

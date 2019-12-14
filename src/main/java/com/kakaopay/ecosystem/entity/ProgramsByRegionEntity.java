package com.kakaopay.ecosystem.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramsByRegionEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6061571458104771888L;

	private String region;
	private List<Program> programs;

	public ProgramsByRegionEntity(RegionEntity regionEntity) {

		this.region = regionEntity.getId();

		if (regionEntity.getEcosystemServices() != null) {
			for (EcosystemServiceEntity ecosystemServiceEntity : regionEntity.getEcosystemServices()) {
				Program program = new Program(ecosystemServiceEntity);
				if (programs == null) {
					programs = new ArrayList<>();
				}
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
		private String prgm_name;
		private String theme;

		public Program(EcosystemServiceEntity ecosystemServiceEntity) {
			this.prgm_name = ecosystemServiceEntity.getProgramName();
			this.theme = ecosystemServiceEntity.getTheme();
		}
	}

	public static List<ProgramsByRegionEntity> fromRegionEntities(List<RegionEntity> regionEntities) {

		return regionEntities.stream().map(regionEntity -> new ProgramsByRegionEntity(regionEntity))
				.collect(Collectors.toList());

	}

}

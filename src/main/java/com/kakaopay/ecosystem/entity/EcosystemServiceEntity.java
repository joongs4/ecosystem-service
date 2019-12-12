package com.kakaopay.ecosystem.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EcosystemServiceEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3427615308169117674L;
	private Integer id;
	private String programName;
	private String theme;
	private String region;
	private String programIntroduction;
	private String programDetailedIntroduction;
	private String address;

	@JsonIgnore
	@JsonBackReference
	private Set<RegionEntity> regions;

	public void setRegions(Collection<RegionEntity> regions) {

		if (regions != null) {
			this.regions = new HashSet<>();
			this.regions.addAll(regions);
		} else {
			this.regions = null;
		}
	}

}

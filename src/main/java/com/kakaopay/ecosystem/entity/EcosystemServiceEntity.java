package com.kakaopay.ecosystem.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(Include.NON_NULL)
public class EcosystemServiceEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3427615308169117674L;
	private String id;
	private String programName;
	private String theme;
	private String regionName;
	private String programIntroduction;
	private String programDetailedIntroduction;

	@JsonBackReference
	private RegionEntity region;

}

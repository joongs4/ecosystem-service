package com.kakaopay.ecosystem.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
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

	private RegionEntity region;

}

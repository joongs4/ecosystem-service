package com.kakaopay.ecosystem.entity;

import java.io.Serializable;
import java.util.List;

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
public class RegionEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7091635153832463331L;
	private String id;
	private String name;

	private List<EcosystemServiceEntity> ecosystemServices;

	public RegionEntity(String name) {
		this.name = name;
	}

}

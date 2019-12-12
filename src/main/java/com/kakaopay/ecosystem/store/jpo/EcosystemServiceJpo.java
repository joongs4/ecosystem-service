package com.kakaopay.ecosystem.store.jpo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "EcosystemService")
@Table(name = "TB_ECOSYSTEM_SERVICE")
@EqualsAndHashCode(of = { "id" })
@SequenceGenerator(allocationSize = 1, initialValue = 1, name = "ID_SEQUENCE")
@NoArgsConstructor
public class EcosystemServiceJpo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -499578570768051786L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQUENCE")
	@Column(updatable = false)
	private Integer id;

	private String programName;
	private String theme;
	private String region;
	private String programIntroduction;

	@Lob
	private String programDetailedIntroduction;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JsonIgnoreProperties("regions")
	@JoinTable(name = "EcosystemService_Region", joinColumns = {
			@JoinColumn(name = "regionId") }, inverseJoinColumns = { @JoinColumn(name = "id") })
	private Set<RegionJpo> regions;

	public EcosystemServiceJpo(EcosystemServiceEntity entity) {
		BeanUtils.copyProperties(entity, this);

		if (entity.getRegions() != null) {
			this.regions = entity.getRegions().stream().map(each -> new RegionJpo(each)).collect(Collectors.toSet());
		} else {
			entity.setRegions(null);
		}
	}

	public EcosystemServiceEntity toDomain(boolean includeInnerEntities) {
		EcosystemServiceEntity retVal = new EcosystemServiceEntity();
		BeanUtils.copyProperties(this, retVal);
		if (this.regions != null && includeInnerEntities) {
			retVal.setRegions(RegionJpo.toDomains(this.regions, false));
		} else {
			retVal.setRegions(null);
		}

		return retVal;
	}

	public EcosystemServiceEntity toDomain(boolean includeInnerEntities, String address) {
		EcosystemServiceEntity retVal = toDomain(includeInnerEntities);
		retVal.setAddress(address);

		return retVal;
	}

	public static List<EcosystemServiceEntity> toDomains(Collection<EcosystemServiceJpo> jpos,
			boolean includeInnerEntities) {

		return jpos.stream().map(jpo -> jpo.toDomain(includeInnerEntities)).collect(Collectors.toList());
	}

	public static List<EcosystemServiceEntity> toDomains(Collection<EcosystemServiceJpo> jpos,
			boolean includeInnerEntities, String address) {

		return jpos.stream().map(jpo -> jpo.toDomain(includeInnerEntities, address)).collect(Collectors.toList());
	}

}

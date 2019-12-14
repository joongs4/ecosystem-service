package com.kakaopay.ecosystem.store.jpo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.util.StringPrefixedSequenceIdGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "EcosystemService")
@Table(name = "TB_ECOSYSTEM_SERVICE")
@EqualsAndHashCode(of = { "id" })
@NoArgsConstructor
public class EcosystemServiceJpo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -499578570768051786L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQUENCE")
//	@Column(updatable = false)
//	private Integer id;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQUENCE")
	@GenericGenerator(name = "ID_SEQUENCE", strategy = "com.kakaopay.ecosystem.util.StringPrefixedSequenceIdGenerator", parameters = {
			@Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "prg"),
			@Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%d") })
	@Column(updatable = false)
	private String id;

	private String programName;
	private String theme;
	private String regionName;
	private String programIntroduction;

	@Lob
	private String programDetailedIntroduction;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JsonIgnoreProperties("region")
	@JoinTable(name = "EcosystemService_Region", joinColumns = {
			@JoinColumn(name = "regionId") }, inverseJoinColumns = { @JoinColumn(name = "id") })
	private RegionJpo region;

	public EcosystemServiceJpo(EcosystemServiceEntity entity) {
		BeanUtils.copyProperties(entity, this);
		if (entity.getRegion() != null) {
			this.region = new RegionJpo(entity.getRegion());
		} else {
			this.region = null;
		}
	}

	public EcosystemServiceEntity toDomain(boolean includeInnerEntities) {
		EcosystemServiceEntity retVal = new EcosystemServiceEntity();
		BeanUtils.copyProperties(this, retVal);
		if (this.region != null && includeInnerEntities) {
			retVal.setRegion(this.region.toDomain(false));

		} else {
			retVal.setRegion(null);
		}

		return retVal;
	}

	public static List<EcosystemServiceEntity> toDomains(Collection<EcosystemServiceJpo> jpos,
			boolean includeInnerEntities) {

		return jpos.stream().map(jpo -> jpo.toDomain(includeInnerEntities)).collect(Collectors.toList());
	}

	public static List<EcosystemServiceEntity> toDomains(Collection<EcosystemServiceJpo> jpos,
			boolean includeInnerEntities, String address) {

		return jpos.stream().map(jpo -> jpo.toDomain(includeInnerEntities)).collect(Collectors.toList());
	}

}

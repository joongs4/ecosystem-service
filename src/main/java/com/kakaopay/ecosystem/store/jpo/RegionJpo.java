package com.kakaopay.ecosystem.store.jpo;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.kakaopay.ecosystem.entity.RegionEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Region")
@Table(name = "TB_REGION")
@EqualsAndHashCode(of = { "id" })
@SequenceGenerator(allocationSize = 1, initialValue = 1, name = "LAST_NUMBER_SEQ")
@NoArgsConstructor
public class RegionJpo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LAST_NUMBER_SEQ")
	@Column(updatable = false)
	private Integer id;
	private String name;

	@ManyToMany(mappedBy = "regions")
	private Set<EcosystemServiceJpo> ecosystemServices;

	public RegionJpo(RegionEntity entity) {
		BeanUtils.copyProperties(entity, this);
	}

	public RegionEntity toDomain(boolean includeInnerEntities) {
		RegionEntity retVal = new RegionEntity();
		BeanUtils.copyProperties(this, retVal);

		if (includeInnerEntities && ecosystemServices != null) {
			retVal.setEcosystemServices(EcosystemServiceJpo.toDomains(ecosystemServices, false, name));
		} else {
			retVal.setEcosystemServices(null);
		}

		return retVal;
	}

	public static List<RegionEntity> toDomains(Collection<RegionJpo> jpos, boolean includeInnerEntities) {

		return jpos.stream().map(jpo -> jpo.toDomain(includeInnerEntities)).collect(Collectors.toList());
	}

}

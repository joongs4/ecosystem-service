package com.kakaopay.ecosystem.store.jpo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.entity.JwtTokenEntity;
import com.kakaopay.ecosystem.util.EcosystemUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "JwtToken")
@Table(name = "TB_JWT_TOKEN")
@EqualsAndHashCode(of = { "id" })
@NoArgsConstructor
public class JwtTokenJpo {

	@Id
	private String id;

	@Column(unique = true)
	private String token;

	@Column(unique = true)
	private String refreshToken;

	private boolean used;

	public JwtTokenJpo(JwtTokenEntity entity) {
		BeanUtils.copyProperties(entity, this);
		if (EcosystemUtil.isNullOrEmpty(id)) {
			this.id = EcosystemUtil.generateGuid();
		}
	}

	public JwtTokenEntity toDomain() {
		JwtTokenEntity retVal = new JwtTokenEntity();

		BeanUtils.copyProperties(this, retVal);

		return retVal;
	}

	public static List<JwtTokenEntity> toDomains(Collection<JwtTokenJpo> jpos) {

		return jpos.stream().map(jpo -> jpo.toDomain()).collect(Collectors.toList());
	}
}

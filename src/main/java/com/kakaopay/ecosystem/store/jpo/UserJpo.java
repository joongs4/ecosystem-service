package com.kakaopay.ecosystem.store.jpo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kakaopay.ecosystem.entity.UserEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "User")
@Table(name = "TB_USERs")
@EqualsAndHashCode(of = { "userId" })
@NoArgsConstructor
public class UserJpo {

	@Id
	@Column(updatable = false)
	private String userId;
	private String userPassword;

	public UserJpo(UserEntity userEntity) {
		this.userId = userEntity.getUserId();
		this.userPassword = userEntity.getUserPassword();
	}

	public UserEntity toDomain() {
		UserEntity entity = new UserEntity();

		entity.setUserId(this.userId);
		entity.setUserPassword(this.userPassword);

		return entity;
	}

	public static List<UserEntity> toDomains(Collection<UserJpo> jpos) {

		return jpos.stream().map(jpo -> jpo.toDomain()).collect(Collectors.toList());
	}

}

package com.kakaopay.ecosystem.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

	private String userId;
	private String userPassword;
	private UserRole role = UserRole.USER;

	public GrantedAuthority getAuthority() {

		final String rolePrefix = "ROLE";
		final String roleFormat = "%s_%s";
		if (this.role != null) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
					String.format(roleFormat, rolePrefix, this.role.toString()));
			return authority;
		}

		return null;
	}

	public List<GrantedAuthority> getAuthorityInList() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = getAuthority();
		if (authority != null) {
			authorities.add(authority);
		}
		return authorities;
	}

}

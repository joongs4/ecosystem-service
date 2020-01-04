package com.kakaopay.ecosystem.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kakaopay.ecosystem.entity.UserEntity;
import com.kakaopay.ecosystem.store.UserStore;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserStore store;

	public UserDetailsServiceImpl(UserStore store) {
		this.store = store;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity userEntity = this.store.findByUserId(username);
		if (userEntity != null) {
			return new User(userEntity.getUserId(), userEntity.getUserPassword(), userEntity.getAuthorityInList());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}

package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
	
	private String username;
	private String password;
	private String email;
	private List<GrantedAuthority> authorities;
	
	public UserDetailsImpl(String username, String password, String email, boolean isAdmin) {
		this.username = username;
		this.password = password;
		this.email = email;
		authorities = new ArrayList<>();
		if (isAdmin) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getEmail() {
		return email;
	}

}

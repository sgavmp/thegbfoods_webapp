package com.ucm.ilsa.veterinaria.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ucm.ilsa.veterinaria.domain.UserDetailsImpl;
import com.ucm.ilsa.veterinaria.domain.Usuario;
import com.ucm.ilsa.veterinaria.repository.UsuarioRepository;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUserName(username);
		UserDetailsImpl userDetails = new UserDetailsImpl(username, usuario.getPassword(), usuario.getEmail(), usuario.isAdmin());
		return userDetails;
	}

}

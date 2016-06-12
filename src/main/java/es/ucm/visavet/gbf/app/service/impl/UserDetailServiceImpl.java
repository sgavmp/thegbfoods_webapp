package es.ucm.visavet.gbf.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import es.ucm.visavet.gbf.app.domain.UserDetailsImpl;
import es.ucm.visavet.gbf.app.domain.Usuario;
import es.ucm.visavet.gbf.app.repository.UsuarioRepository;

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

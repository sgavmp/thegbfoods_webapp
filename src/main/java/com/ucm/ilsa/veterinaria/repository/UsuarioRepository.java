package com.ucm.ilsa.veterinaria.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {

	public Usuario findByUserName(String userName);
	
}

package es.ucm.visavet.gbf.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.ucm.visavet.gbf.app.domain.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, String> {

	public Usuario findByUserName(String userName);
	
}

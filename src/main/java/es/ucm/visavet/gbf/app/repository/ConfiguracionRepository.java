package es.ucm.visavet.gbf.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.ucm.visavet.gbf.app.domain.Configuracion;

@Repository
public interface ConfiguracionRepository extends CrudRepository<Configuracion, String> {

}

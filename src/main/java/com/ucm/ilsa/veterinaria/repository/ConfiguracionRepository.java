package com.ucm.ilsa.veterinaria.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Configuracion;

@Repository
public interface ConfiguracionRepository extends CrudRepository<Configuracion, String> {

}

package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {

	public List<Location> findAllByOrderByNameAsc();
	
}

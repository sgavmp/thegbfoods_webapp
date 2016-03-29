package com.ucm.ilsa.veterinaria.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {

	public Set<Location> findAllByOrderByNameAsc();
	public List<Location> findAllByNews(String news);
	
}

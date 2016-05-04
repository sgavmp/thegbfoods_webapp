package com.ucm.ilsa.veterinaria.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {

	public Set<Location> findAllByOrderByNameAsc();
	public List<Location> findAllByNews(String news);
	@Query(value="SELECT l.* FROM location l left join news_locations n on(n.location_id = l.id) where n.alert_detect_id in (SELECT id from alert) group by l.id;", nativeQuery=true)
	public List<Location> findAllAfectAlert();
	@Query(value="SELECT l.* FROM location l left join news_locations n on(n.location_id = l.id) where n.alert_detect_id in (SELECT id from risk) group by l.id;", nativeQuery=true)
	public List<Location> findAllAfectRisk();
	
}

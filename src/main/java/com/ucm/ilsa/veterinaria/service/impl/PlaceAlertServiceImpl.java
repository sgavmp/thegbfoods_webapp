package com.ucm.ilsa.veterinaria.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.repository.LocationRepository;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

@Service
public class PlaceAlertServiceImpl {
	
	@Autowired
	private LocationRepository repository;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	public Set<Location> getAllLocations() {
		return repository.findAllByOrderByNameAsc();
	}
	
	public Location getOneById(Long id) {
		return repository.findOne(id);
	}
	
	public Location createLocation(Location place) throws IOException {
		place = repository.save(place);
		newsIndexService.resetLocation(place);
		return place;
	}
	
	public Location updateLocation(Location place) throws IOException {
		place = repository.save(place);
		newsIndexService.resetLocation(place);
		return place;
	}
	
	public void removeLocation(Location place) {
		repository.delete(place);
	}

}

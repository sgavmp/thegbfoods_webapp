package com.ucm.ilsa.veterinaria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.repository.LocationRepository;

@Service
public class PlaceAlertServiceImpl {
	
	@Autowired
	private LocationRepository repository;
	
	public List<Location> getAllLocations() {
		return Lists.newArrayList(repository.findAll());
	}
	
	public Location getOneById(Integer id) {
		return repository.findOne(id);
	}
	
	public Location createLocation(Location place) {
		return repository.save(place);
	}
	
	public Location updateLocation(Location place) {
		return repository.save(place);
	}
	
	public void removeLocation(Location place) {
		repository.delete(place);
	}

}

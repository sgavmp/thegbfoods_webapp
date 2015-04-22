package com.ucm.ilsa.veterinaria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.PlaceAlert;
import com.ucm.ilsa.veterinaria.repository.PlaceAlertRepository;

@Service
public class PlaceAlertService {
	
	@Autowired
	private PlaceAlertRepository repository;
	
	public List<PlaceAlert> getAllLocations() {
		return Lists.newArrayList(repository.findAll());
	}
	
	public PlaceAlert getOneById(Integer id) {
		return repository.findOne(id);
	}
	
	public PlaceAlert createLocation(PlaceAlert place) {
		return repository.save(place);
	}
	
	public PlaceAlert updateLocation(PlaceAlert place) {
		return repository.save(place);
	}
	
	public void removeLocation(PlaceAlert place) {
		repository.delete(place);
	}

}

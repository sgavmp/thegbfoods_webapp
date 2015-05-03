package com.ucm.ilsa.veterinaria.domain.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.repository.PlaceAlertRepository;

@Component
public class StringToPlaceAlertConverter implements Converter<String, Location> {
	
	@Autowired
	private PlaceAlertRepository repository;

	@Override
	public Location convert(String arg0) {
		Integer id = new Integer(arg0);
		return repository.findOne(id);
	}

}

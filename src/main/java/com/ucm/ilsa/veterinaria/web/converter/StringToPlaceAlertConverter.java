package com.ucm.ilsa.veterinaria.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.repository.LocationRepository;

@Component
public class StringToPlaceAlertConverter implements Converter<String, Location> {
	
	@Autowired
	private LocationRepository repository;

	@Override
	public Location convert(String arg0) {
		Long id = new Long(arg0);
		return repository.findOne(id);
	}

}

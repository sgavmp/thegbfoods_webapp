package com.ucm.ilsa.veterinaria.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;

public class StringToAlert implements Converter<String, Alert> {

	@Autowired
    AlertRepository alertRepository;
	
	@Override
	public Alert convert(String source) {
		return alertRepository.findOne(Long.parseLong(source));
	}

}

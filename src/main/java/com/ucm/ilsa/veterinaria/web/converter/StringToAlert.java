package com.ucm.ilsa.veterinaria.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.repository.AlertDetectRepository;

public class StringToAlert implements Converter<String, AlertDetect> {

	@Autowired
    AlertDetectRepository alertRepository;
	
	@Override
	public AlertDetect convert(String source) {
		return alertRepository.findOne(Long.parseLong(source));
	}

}

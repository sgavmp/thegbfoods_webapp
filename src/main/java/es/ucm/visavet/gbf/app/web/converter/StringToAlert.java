package es.ucm.visavet.gbf.app.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import es.ucm.visavet.gbf.app.domain.Alert;
import es.ucm.visavet.gbf.app.repository.AlertRepository;

@Component
public class StringToAlert implements Converter<String, Alert> {
	
	@Autowired
	private AlertRepository repository;

	@Override
	public Alert convert(String arg0) {
		Long id = new Long(arg0);
		return repository.findOne(id);
	}

}

package es.ucm.visavet.gbf.app.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.repository.LocationRepository;

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

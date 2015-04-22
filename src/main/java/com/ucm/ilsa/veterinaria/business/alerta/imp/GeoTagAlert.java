package com.ucm.ilsa.veterinaria.business.alerta.imp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.business.alerta.IntfAlerta;
import com.ucm.ilsa.veterinaria.business.event.alerta.GeoTagAlertEvent;
import com.ucm.ilsa.veterinaria.business.tratamiento.impl.TodoTratamiento;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Component
public class GeoTagAlert implements IntfAlerta<GeoTagAlertEvent> {
	
	@Autowired
	private AlertRepository repository;
	
	private final static Logger LOGGER = Logger.getLogger(TodoTratamiento.class);

	@Override
	@Subscribe public void responseToEvent(GeoTagAlertEvent event) {
		LOGGER.info("Ejemplo de comprobacion de alerta para el sitio: " + event.getFeed().getName());
		//Una alerta por noticia
		for (News news : event.getLocations().keySet()) {
			Alert alert = new Alert();
			alert.setDatePub(news.getPubDate());
			alert.setSite(event.getFeed());
			alert.setTitle(news.getTitle());
			alert.setLink(news.getUrl());
			String stringLocations = "";
			for (ResolvedLocation loc : event.getLocations().get(news)) {
				stringLocations = stringLocations.concat(loc.toString());
			}
			alert.setInfoAlert(stringLocations);
			alert.setLevel(AlertLevel.green);
			alert.setTypeAlert("geotag");
			try {
				repository.save(alert);
			} catch (DataIntegrityViolationException ex) {
				LOGGER.error("Se ha producico un error al guardar la alerta. Ya existe que coinciden en tipo y enlace.");
			}
		}
		BaseController.putInfoMessage("Se han comprobado las alertas. Alertas detectadas: " + event.getLocations().size());		
	}
	

}

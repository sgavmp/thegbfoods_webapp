package com.ucm.ilsa.veterinaria.business.alerta.imp;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.business.alerta.IntfAlerta;
import com.ucm.ilsa.veterinaria.business.event.alerta.GeoTagAlertEvent;
import com.ucm.ilsa.veterinaria.business.tratamiento.impl.TodoTratamiento;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.PlaceAlert;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.service.impl.PlaceAlertService;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Component
public class GeoTagAlert implements IntfAlerta<GeoTagAlertEvent> {

	@Autowired
	private AlertRepository repository;

	@Autowired
	private PlaceAlertService placeAlertService;

	private final static Logger LOGGER = Logger
			.getLogger(TodoTratamiento.class);

	@Override
	@Subscribe
	public void responseToEvent(GeoTagAlertEvent event) {
		List<PlaceAlert> lugares = placeAlertService.getAllLocations();
		LOGGER.info("Ejemplo de comprobacion de alerta para el sitio: "
				+ event.getFeed().getName());
		Integer num = 1;
		// Una alerta por noticia
		for (News news : event.getLocations().keySet()) {
			String stringLocations = "La noticia puede hablar de municipios cerca de los siguientes lugares:";
			List<PlaceAlert> lugaresCercanos = Lists.newArrayList();
			for (ResolvedLocation loc : event.getLocations().get(news)) {
				for (PlaceAlert lugar : lugares) {
					if (lugar.isNearOf(loc.getGeoname().getLatitude(), loc
							.getGeoname().getLongitude())) {
						lugaresCercanos.add(lugar);
						stringLocations = stringLocations.concat(
								lugar.getName()).concat(", ");
					}
				}
			}
			//Se guarda la alerta si entra dentro del alcance de alguna de las localizaciones
			if (!lugaresCercanos.isEmpty()) {
				Alert alert = new Alert();
				alert.setDatePub(news.getPubDate());
				alert.setSite(event.getFeed());
				alert.setTitle(news.getTitle());
				alert.setLink(news.getUrl());
				alert.setInfoAlert(stringLocations);
				alert.setLevel(AlertLevel.yellow);
				alert.setTypeAlert("geotag");
				try {
					repository.save(alert);
				} catch (DataIntegrityViolationException ex) {
					LOGGER.error("Se ha producico un error al guardar la alerta. Ya existe que coinciden en tipo y enlace.");
				}
				num++;
			}
		}
		BaseController
				.putInfoMessage("Se han comprobado las alertas de cercania. Alertas detectadas: "
						+ num);
	}

}

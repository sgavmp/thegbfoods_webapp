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
import com.ucm.ilsa.veterinaria.business.event.alerta.GeoTagAndFilterAlertEvent;
import com.ucm.ilsa.veterinaria.business.tratamiento.impl.TodoTratamiento;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.WordFilter;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.service.impl.PlaceAlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.WordFilterServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Component
public class GeoTagAndFilterAlert implements IntfAlerta<GeoTagAndFilterAlertEvent> {

	@Autowired
	private AlertRepository repository;
	
	@Autowired
	private WordFilterServiceImpl wordService;

	@Autowired
	private PlaceAlertServiceImpl placeAlertService;

	private final static Logger LOGGER = Logger
			.getLogger(GeoTagAndFilterAlert.class);

	@Override
	@Subscribe
	public void responseToEvent(GeoTagAndFilterAlertEvent event) {
		List<Location> lugares = placeAlertService.getAllLocations();
		List<WordFilter> terminos = wordService.getAllWordFilter();
		LOGGER.info("Ejemplo de comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ event.getFeed().getName());
		Integer num = 0;
		// Una alerta por noticia
		for (News news : event.getLocations().keySet()) {
			String stringLocations = "La noticia puede hablar de municipios cerca de los siguientes lugares:";
			List<Location> lugaresCercanos = Lists.newArrayList();
			List<String> palabrasAlertas = Lists.newArrayList();
			for (ResolvedLocation loc : event.getLocations().get(news)) {
				for (Location lugar : lugares) {
					if (lugar.isNearOf(loc.getGeoname().getLatitude(), loc
							.getGeoname().getLongitude())) {
						lugaresCercanos.add(lugar);
						stringLocations = stringLocations.concat(
								lugar.getName()).concat(", ");
					}
				}
			}
			//Si los lugares localizado estan dentro del rango de algunas de las localizaciones almacenadas
			//se buscan los terminos de filtrado
			if (!lugaresCercanos.isEmpty()) {
				for (WordFilter wordFilter : terminos) {
					if (news.getContent().toLowerCase().contains(wordFilter.getWord().toLowerCase())) {
						palabrasAlertas.add(wordFilter.getWord());
					}
				}
			}
			
			if (!lugaresCercanos.isEmpty() && !palabrasAlertas.isEmpty()) {
				//Se almacena la alerta si se encuentra alguna coincidencia
				Alert alert = new Alert();
				alert.setDatePub(news.getPubDate());
				alert.setSite(event.getFeed());
				alert.setTitle(news.getTitle());
				alert.setLink(news.getUrl());
				alert.setInfoAlert(stringLocations.concat("\n Se han encontrado las siguientes palabras de alertas: ").concat(palabrasAlertas.toString()));
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

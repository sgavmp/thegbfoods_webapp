package com.ucm.ilsa.veterinaria.business.alerta.imp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.ucm.ilsa.veterinaria.business.alerta.IntfAlerta;
import com.ucm.ilsa.veterinaria.business.event.alerta.TodoAlertEvent;
import com.ucm.ilsa.veterinaria.business.tratamiento.impl.TodoTratamiento;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Component
public class TodoAlerta implements IntfAlerta<TodoAlertEvent> {
	
	@Autowired
	private AlertRepository repository;
	
	private final static Logger LOGGER = Logger.getLogger(TodoTratamiento.class);

	@Override
	@Subscribe public void responseToEvent(TodoAlertEvent event) {
		LOGGER.info("Ejemplo de comprobacion de alerta para el sitio: " + event.getFeed().getName());
		//Una alerta por noticia
		for (News news : event.getNews()) {
			Alert alert = new Alert();
			alert.setDatePub(news.getPubDate());
			alert.setSite(event.getFeed());
			alert.setTitle(news.getTitle());
			alert.setLink(news.getUrl());
			alert.setInfoAlert("Esta alerta funciona a modo de ejemplo. Descripcion" + news.getDescription());
			alert.setLevel(AlertLevel.yellow);
			alert.setTypeAlert("allNews");
			try {
				repository.save(alert);
			} catch (DataIntegrityViolationException ex) {
				LOGGER.error("Se ha producico un error al guardar la alerta. Ya existe que coinciden en tipo y enlace.");
			}
		}
		BaseController.putInfoMessage("Se han comprobado las alertas. Alertas detectadas: " + event.getNews().size());
	}

}

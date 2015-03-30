package com.ucm.ilsa.veterinaria.alerta.imp;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.alerta.IntfAlerta;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.event.alerta.TodoAlertEvent;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.tratamiento.impl.TodoTratamiento;

@Component
public class TodoAlerta implements IntfAlerta<TodoAlertEvent> {
	
	@Autowired
	private AlertRepository repository;
	
	private final static Logger LOGGER = Logger.getLogger(TodoTratamiento.class);

	@Override
	@Subscribe public void responseToEvent(TodoAlertEvent event) {
		LOGGER.info("Ejemplo de comprobación de alerta para el sitio: " + event.getFeed().getName());
		//Una alerta por noticia
		for (News news : event.getNews()) {
			Alert alert = new Alert();
			alert.setDatePub(news.getPubDate());
			alert.setSite(event.getFeed());
			alert.setTitle(news.getTitle());
			alert.setLink(news.getUrl());
			alert.setInfoAlert("Esta alerta funciona a modo de ejemplo. Descripción" + news.getDescription());
			alert.setLevel(AlertLevel.green);
			alert.setTypeAlert("allNews");
			repository.save(alert);
		}
		
	}

}

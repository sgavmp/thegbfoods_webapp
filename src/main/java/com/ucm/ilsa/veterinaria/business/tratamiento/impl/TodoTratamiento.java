package com.ucm.ilsa.veterinaria.business.tratamiento.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.business.event.alerta.TodoAlertEvent;
import com.ucm.ilsa.veterinaria.business.event.config.EventBusFactoryBean;
import com.ucm.ilsa.veterinaria.business.event.tratamiento.FeedUpdateEvent;
import com.ucm.ilsa.veterinaria.business.recuperacion.impl.FeedScrapingImpl;
import com.ucm.ilsa.veterinaria.business.tratamiento.IntfTratamiento;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;

@Component
public class TodoTratamiento implements IntfTratamiento<FeedUpdateEvent> {
	
	private final static Logger LOGGER = Logger.getLogger(TodoTratamiento.class);
	

	@Override
	@Subscribe public void responseToEvent(FeedUpdateEvent event) {
		LOGGER.info("Ejemplo de tratamiento para el sitio: " + event.getFeed().getName());
		//Creamos el evento cons los datos necesario
		//En un tratamiento real habra que obtener datos de cada noticia para que las alertas los comprueben
		TodoAlertEvent evento = new TodoAlertEvent();
		evento.setFeed(event.getFeed());
		evento.setNews(event.getListNews());
		evento.setDate(new Date(System.currentTimeMillis()));
		//Enviamos el evento al bus para que las alertas interesadas los comprueben
		EventBusFactoryBean.getInstance().post(evento);
	}
}

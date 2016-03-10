package com.ucm.ilsa.veterinaria.scheduler;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.repository.NewsRepository;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

public class AlertResetTaskContainer implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(AlertResetTaskContainer.class);
	
	private AlertAbstract alert;
	@Autowired
	private NewsIndexService newsIndexService;
	
	
	public AlertResetTaskContainer(AlertAbstract alert) {
		this.alert = alert;
	}

	@Override
	public void run() {
		LOGGER.info("Inicia tarea planificada para la alerta: " + alert.getTitle());
		try {
			newsIndexService.resetAlert(alert);
		} catch (IOException e) {
			LOGGER.error("Error en la tarea de la alerta: " + alert.getTitle());
		}
		LOGGER.info("Ha finalizado la tarea planificada de la alerta: " + alert.getTitle());
		
	}

}
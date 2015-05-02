package com.ucm.ilsa.veterinaria.scheduler;

import org.apache.log4j.Logger;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.service.FeedService;

public class TaskContainer implements Runnable {
	
	private final static Logger LOGGER = Logger.getLogger(TaskContainer.class);
	
	private FeedService service;
	private Feed feed;
	
	public TaskContainer(Feed feed, FeedService service) {
		this.feed = feed;
		this.service = service;
	}



	@Override
	public void run() {
		LOGGER.info("Inicia tarea planificada para el sitio: " + feed.getCodeName());
		service.scrapFeed(feed);
		feed = service.getFeedByCodeName(feed.getCodeName());
		LOGGER.info("Finalizada tarea planificada para el sitio: " + feed.getCodeName());
	}

}

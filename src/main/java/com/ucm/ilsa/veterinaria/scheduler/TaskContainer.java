package com.ucm.ilsa.veterinaria.scheduler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckService;

public class TaskContainer implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(TaskContainer.class);

	private FeedService service;
	private Feed feed;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private NewsCheckService newsCheckService;

	public TaskContainer(Feed feed, FeedService service, SchedulerService schedulerService, NewsCheckService newsCheckService) {
		this.feed = feed;
		this.service = service;
		this.schedulerService = schedulerService;
		this.newsCheckService = newsCheckService;
	}

	@Override
	public void run() {
		LOGGER.info("Inicia tarea planificada para el sitio: " + feed.getName());
		Feed feedComp = null;
		// Obtenemos el sitio de la base de datos por si se hubiese modificado o
		// borrado
		feedComp = service.getFeedByCodeName(feed.getCode());
		if (feedComp != null) {
			feed = feedComp;
			List<News> listNews = service.scrapFeed(feed);
			if (listNews != null) {
				newsCheckService.checkNews(listNews, feed);
			}
			LOGGER.info("Finalizada tarea planificada para el sitio: " + feed.getCode());
		} else {
			schedulerService.removeFeedTask(feed);
			LOGGER.info("El sitio " + feed.getName() + " ha sido borrado mientras se obtenian nuevos datos.");
		}

	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

	public void setNewsCheckService(NewsCheckService newsCheckService) {
		this.newsCheckService = newsCheckService;
	}

}

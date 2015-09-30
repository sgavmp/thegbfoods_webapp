package com.ucm.ilsa.veterinaria.scheduler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedRisk;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.service.FeedRiskService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedRiskService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;

public class RiskTaskContainer implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(RiskTaskContainer.class);

	private FeedRiskService service;
	private FeedRisk feed;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private NewsCheckFeedRiskService newsCheckService;

	public RiskTaskContainer(FeedRisk feed, FeedRiskService service, SchedulerService schedulerService, NewsCheckFeedRiskService newsCheckService) {
		this.feed = feed;
		this.service = service;
		this.schedulerService = schedulerService;
		this.newsCheckService = newsCheckService;
	}

	@Override
	public void run() {
		LOGGER.info("Inicia tarea planificada para el sitio: " + feed.getName());
		FeedRisk feedComp = null;
		// Obtenemos el sitio de la base de datos por si se hubiese modificado o
		// borrado
		feedComp = service.getFeedByCodeName(feed.getCode());
		if (feedComp != null) {
			feed = service.setSateOfFeed(feedComp, UpdateStateEnum.GET_NEWS);
			List<News> listNews = service.scrapFeed(feed);
			if (listNews != null) {
				newsCheckService.checkNews(listNews, feed);
			}
			LOGGER.info("Finalizada tarea planificada para el sitio: " + feed.getName());
			feed = service.setSateOfFeed(feed, UpdateStateEnum.WAIT);
		} else {
			schedulerService.removeFeedTask(feed);
			LOGGER.info("El sitio " + feed.getName() + " ha sido borrado mientras se obtenian nuevos datos.");
		}

	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

	public void setNewsCheckService(NewsCheckFeedRiskService newsCheckService) {
		this.newsCheckService = newsCheckService;
	}

}

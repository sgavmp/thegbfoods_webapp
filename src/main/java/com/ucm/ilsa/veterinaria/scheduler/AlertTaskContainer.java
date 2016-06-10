package com.ucm.ilsa.veterinaria.scheduler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.repository.NewsRepository;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

public class AlertTaskContainer implements Runnable {

	private final static Logger LOGGER = Logger
			.getLogger(AlertTaskContainer.class);

	private Feed feed;
	@Autowired
	private FeedService service;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private NewsIndexService newsIndexService;
	@Autowired
	private NewsRepository newsRepository;

	public AlertTaskContainer(Feed feed) {
		this.feed = feed;
	}

	@Override
	public void run() {
		LOGGER.info("Inicia tarea planificada para el sitio: " + feed.getName());
		Feed feedComp = null;
		// Obtenemos el sitio de la base de datos por si se hubiese modificado o
		// borrado
		feedComp = service.getFeedByCodeName(feed.getId());
		if (feedComp != null) {
			feed = service.setSateOfFeed(feedComp, UpdateStateEnum.GET_NEWS);
			List<News> listNews = service.scrapFeed(feed, null, false);
			feed = service.getFeedByCodeName(feed.getId());
			if (listNews != null) {
				if (!listNews.isEmpty()) {
					LOGGER.info("Se han recuperado " + listNews.size()
							+ " nuevas noticias del sitio: " + feed.getName());
					// newsCheckService.checkNews(listNews, feed);
					for (News news : listNews) {
						try {
							if (news != null) {
								// Si no ha extraido contenido ni titulo
								if (!"".equals(news.getContent())
										&& news.getContent() != null
										|| !"".equals(news.getTitle())
										&& news.getTitle() != null)
									newsRepository.save(news);
							} else
								LOGGER.warn("La noticia es null para el sitio: "
										+ feed.getName());
						} catch (Exception ex) {
							LOGGER.error("Error al guardar una noticia. Sitio: "
									+ feed.getName()
									+ ", Link: "
									+ news.getUrl());
						}
					}
					newsIndexService.markNewNews(feed);
				} else {
					LOGGER.info("No se han recuperado nuevas noticias del sitio: "
							+ feed.getName());
				}
			}
			LOGGER.info("Finalizada tarea planificada para el sitio: "
					+ feed.getName());
			feed = service.setSateOfFeed(feed, UpdateStateEnum.WAIT);
		} else {
			schedulerService.removeFeedTask(feed);
			LOGGER.info("El sitio " + feed.getName()
					+ " ha sido borrado mientras se obtenian nuevos datos.");
		}

	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

}

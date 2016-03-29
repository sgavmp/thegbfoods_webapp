package com.ucm.ilsa.veterinaria.scheduler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.repository.NewsRepository;
import com.ucm.ilsa.veterinaria.service.ConfiguracionService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

@Service
public class SchedulerService {
	
	private final static Logger LOGGER = Logger.getLogger(SchedulerService.class);

	@Autowired
	private ConfiguracionService configuracion;
	@Autowired
	private TaskScheduler scheduler;
	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	private static final int MIN_MILIS = 60000;
	@Autowired
	private FeedService serviceFeed;
	private Map<Long, ScheduledFuture<?>> tasks = Maps.newLinkedHashMap();
	

	@PostConstruct
	public void init() {
		if (configuracion.getConfiguracion().isRunService()) {
			List<Feed> listFeeds = serviceFeed.getAllFeed();
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 10);// Las tareas se
																// comienzan a
																// ejecutar
																// despues
																// de dos
																// minutos
																// tras
																// planificar
			for (Feed feed : listFeeds) {
				if (feed.isAccepted() & feed.isActived()) {
					AlertTaskContainer task = new AlertTaskContainer(feed);
					beanFactory.autowireBean(task);
					ScheduledFuture<?> futureTask = scheduler
							.scheduleWithFixedDelay(task, startTime, MIN_MILIS
									* feed.getMinRefresh());
					startTime.setTime(startTime.getTime() + 1000 * 10);// Vamos
																		// espacioandolas
																		// cada
																		// 2
																		// minutos
					tasks.put(feed.getId(), futureTask);
				}
			}
			LOGGER.info("Planificadas " + listFeeds.size() + " tareas de scraping.");
		} else {
			LOGGER.info("Planificación de tareas de scraping desactivado. Activelo en Configuración.");
		}
	}

	public void removeFeedTask(Feed feed) {
		if (tasks.containsKey(feed.getId())) {
			ScheduledFuture<?> futureTask = tasks.get(feed.getId());
			futureTask.cancel(true);
			tasks.remove(feed.getId());
		}
	}

	public void updateFeedTask(Feed feed) {
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 120);
		if (tasks.containsKey(feed.getId())) {
			ScheduledFuture<?> futureTask = tasks.remove(feed.getId());
			futureTask.cancel(true);
		}
		if (feed.isActived() && feed.isAccepted() && configuracion.getConfiguracion().isRunService()) {
			ScheduledFuture<?> futureTask = null;
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed);
			beanFactory.autowireBean(task);
			futureTask = scheduler.scheduleWithFixedDelay(task, startTime,
					MIN_MILIS * feed.getMinRefresh());
			tasks.put(feed.getId(), futureTask);
		}
	}

	public void addFeedTask(Feed feed) {
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 120);
		if (feed.isActived() && feed.isAccepted() && configuracion.getConfiguracion().isRunService()) {
			ScheduledFuture<?> futureTask = null;
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed);
			beanFactory.autowireBean(task);
			futureTask = scheduler.scheduleWithFixedDelay(task, startTime,
					MIN_MILIS * feed.getMinRefresh());
			tasks.put(feed.getId(), futureTask);
		}
	}

	public void startTask(Feed feed) {
		if (feed.isActived() && feed.isAccepted()) {
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 5); // Iniciar en 5
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed);
			beanFactory.autowireBean(task);
			scheduler.schedule(task, startTime);

		}
	}

	public void startAllTask() {
		for (Feed feed : serviceFeed.getAllFeed()) {
			startTask(feed);
		}

	}
	
	public void stopAllTask() {
		for (Feed feed : serviceFeed.getAllFeed()) {
			removeFeedTask(feed);
		}
		LOGGER.info("Se han eliminado todas las tareas de scraping.");
	}
	
	public String getNextExecution(Feed feed) {
		if (tasks.containsKey(feed.getId())) {
			Long milis = tasks.get(feed.getId()).getDelay(TimeUnit.MILLISECONDS);
			Date now = new Date();
			now.setTime(now.getTime()+milis);
			return DateFormat.getInstance().format(now);
		}
		return "";
	}

}

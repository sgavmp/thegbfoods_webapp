package com.ucm.ilsa.veterinaria.scheduler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.SiteAbstract;
import com.ucm.ilsa.veterinaria.service.FeedRiskService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedRiskService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;

@Service
public class SchedulerService {

	private TaskScheduler scheduler;
	private FeedService serviceFeed;
	private FeedRiskService serviceFeedRisk;
	private Map<String, ScheduledFuture<?>> tasks = Maps.newLinkedHashMap();
	private static final int MIN_MILIS = 60000;
	private NewsCheckFeedService newsCheckService;
	private NewsCheckFeedRiskService newsCheckRiskService;

	@Autowired
	public SchedulerService(FeedService service, FeedRiskService serviceRisk, TaskScheduler scheduler,
			NewsCheckFeedService newsCheckService, NewsCheckFeedRiskService newsCheckRiskService) {
		this.scheduler = scheduler;
		this.serviceFeed = service;
		this.newsCheckService = newsCheckService;
		this.newsCheckRiskService = newsCheckRiskService;
		this.serviceFeedRisk = serviceRisk;
		service.setSchedulerService(this);
		init();
	}

	public void init() {
		List<Feed> listFeeds = serviceFeed.getAllFeed();
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 120);// Las tareas se
															// comienzan a
															// ejecutar despues
															// de dos minutos
															// tras planificar
		for (Feed feed : listFeeds) {
			if (feed.isAccepted() & feed.isActived()) {
				AlertTaskContainer task = new AlertTaskContainer(feed, serviceFeed, this,
						newsCheckService);
				ScheduledFuture<?> futureTask = scheduler
						.scheduleWithFixedDelay(task, startTime, MIN_MILIS
								* feed.getMinRefresh());
				startTime.setTime(startTime.getTime() + 1000 * 30);// Vamos
																	// espacioandolas
																	// cada 2
																	// minutos
				tasks.put(feed.getCode(), futureTask);
			}
		}
	}

	public void removeFeedTask(SiteAbstract feed) {
		if (tasks.containsKey(feed.getCode())) {
			ScheduledFuture<?> futureTask = tasks.get(feed.getCode());
			futureTask.cancel(true);
			tasks.remove(feed.getCode());
		}
	}

	public void updateFeedTask(SiteAbstract feed) {
		if (tasks.containsKey(feed.getCode())) {
			ScheduledFuture<?> futureTask = tasks.remove(feed.getCode());
			futureTask.cancel(true);
			if (feed.isActived() && feed.isAccepted()) {
				AlertTaskContainer task = new AlertTaskContainer((Feed) feed, serviceFeed, this,
						newsCheckService);
				futureTask = scheduler.scheduleWithFixedDelay(task, MIN_MILIS
						* feed.getMinRefresh());
				tasks.put(feed.getCode(), futureTask);
			}
		} else if (feed.isActived() && feed.isAccepted()) {
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed, serviceFeed, this,
					newsCheckService);
			ScheduledFuture<?> futureTask = scheduler.scheduleWithFixedDelay(
					task, MIN_MILIS * feed.getMinRefresh());
			tasks.put(feed.getCode(), futureTask);
		}
	}

	public void addFeedTask(Feed feed) {
		AlertTaskContainer task = new AlertTaskContainer(feed, serviceFeed, this,
				newsCheckService);
		ScheduledFuture<?> futureTask = scheduler.scheduleWithFixedDelay(task,
				MIN_MILIS * feed.getMinRefresh());
		tasks.put(feed.getCode(), futureTask);
	}

	public void startTask(Feed feed) {
		AlertTaskContainer task = new AlertTaskContainer(feed, serviceFeed, this,
				newsCheckService);
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 5); // Iniciar en 5
															// segundos
		scheduler.schedule(task, startTime);
	}

}

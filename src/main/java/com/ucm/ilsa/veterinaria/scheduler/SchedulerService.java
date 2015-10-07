package com.ucm.ilsa.veterinaria.scheduler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedRisk;
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
	private Map<Long, ScheduledFuture<?>> tasks = Maps.newLinkedHashMap();
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
		serviceFeedRisk.setSchedulerService(this);
		init();
	}

	public void init() {
		List<Feed> listFeeds = serviceFeed.getAllFeed();
		List<FeedRisk> listFeedsRisk = serviceFeedRisk.getAllFeed();
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
				tasks.put(feed.getId(), futureTask);
			}
		}
		for (FeedRisk feed : listFeedsRisk) {
			if (feed.isAccepted() & feed.isActived()) {
				RiskTaskContainer task = new RiskTaskContainer(feed, serviceFeedRisk, this,
						newsCheckRiskService);
				ScheduledFuture<?> futureTask = scheduler
						.scheduleWithFixedDelay(task, startTime, MIN_MILIS
								* feed.getMinRefresh());
				startTime.setTime(startTime.getTime() + 1000 * 30);// Vamos
																	// espacioandolas
																	// cada 2
																	// minutos
				tasks.put(feed.getId(), futureTask);
			}
		}
	}

	public void removeFeedTask(SiteAbstract feed) {
		if (tasks.containsKey(feed.getId())) {
			ScheduledFuture<?> futureTask = tasks.get(feed.getId());
			futureTask.cancel(true);
			tasks.remove(feed.getId());
		}
	}

	public void updateFeedTask(SiteAbstract feed) {
		if (tasks.containsKey(feed.getId())) {
			ScheduledFuture<?> futureTask = tasks.remove(feed.getId());
			futureTask.cancel(true);
		} 
		if (feed.isActived() && feed.isAccepted()) {
			ScheduledFuture<?> futureTask = null;
			if (feed instanceof Feed) {
				AlertTaskContainer task = new AlertTaskContainer((Feed) feed, serviceFeed, this,
						newsCheckService);
				futureTask = scheduler.scheduleWithFixedDelay(
						task, MIN_MILIS * feed.getMinRefresh());
			} else {
				RiskTaskContainer task = new RiskTaskContainer((FeedRisk) feed, serviceFeedRisk, this,
						newsCheckRiskService);
				futureTask = scheduler.scheduleWithFixedDelay(
						task, MIN_MILIS * feed.getMinRefresh());
			}
			tasks.put(feed.getId(), futureTask);
		}
	}

	public void addFeedTask(SiteAbstract feed) {
		if (feed.isActived() && feed.isAccepted()) {
			ScheduledFuture<?> futureTask = null;
			if (feed instanceof Feed) {
				AlertTaskContainer task = new AlertTaskContainer((Feed) feed, serviceFeed, this,
						newsCheckService);
				futureTask = scheduler.scheduleWithFixedDelay(
						task, MIN_MILIS * feed.getMinRefresh());
			} else {
				RiskTaskContainer task = new RiskTaskContainer((FeedRisk) feed, serviceFeedRisk, this,
						newsCheckRiskService);
				futureTask = scheduler.scheduleWithFixedDelay(
						task, MIN_MILIS * feed.getMinRefresh());
			}
			tasks.put(feed.getId(), futureTask);
		}
	}

	public void startTask(SiteAbstract feed) {
		if (feed.isActived() && feed.isAccepted()) {
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 5); // Iniciar en 5
			if (feed instanceof Feed) {
				AlertTaskContainer task = new AlertTaskContainer((Feed) feed, serviceFeed, this,
						newsCheckService);
				scheduler.schedule(task, startTime);
			} else {
				RiskTaskContainer task = new RiskTaskContainer((FeedRisk) feed, serviceFeedRisk, this,
						newsCheckRiskService);
				scheduler.schedule(task, startTime);
			}
		}
	}

}

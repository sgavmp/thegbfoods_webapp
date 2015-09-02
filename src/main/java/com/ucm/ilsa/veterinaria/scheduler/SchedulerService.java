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
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckService;

@Service
public class SchedulerService {

	private TaskScheduler scheduler;
	private FeedService serviceFeed;
	private Map<String, ScheduledFuture<?>> tasks = Maps.newLinkedHashMap();
	private static final int MIN_MILIS = 60000;
	private NewsCheckService newsCheckService;

	@Autowired
	public SchedulerService(FeedService service, TaskScheduler scheduler,
			NewsCheckService newsCheckService) {
		this.scheduler = scheduler;
		this.serviceFeed = service;
		this.newsCheckService = newsCheckService;
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
				TaskContainer task = new TaskContainer(feed, serviceFeed, this,
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

	public void removeFeedTask(Feed feed) {
		if (tasks.containsKey(feed.getCode())) {
			ScheduledFuture<?> futureTask = tasks.get(feed.getCode());
			futureTask.cancel(true);
			tasks.remove(feed.getCode());
		}
	}

	public void updateFeedTask(Feed feed) {
		if (tasks.containsKey(feed.getCode())) {
			ScheduledFuture<?> futureTask = tasks.get(feed.getCode());
			futureTask.cancel(true);
			TaskContainer task = new TaskContainer(feed, serviceFeed, this,
					newsCheckService);
			futureTask = scheduler.scheduleWithFixedDelay(task, MIN_MILIS
					* feed.getMinRefresh());
			tasks.put(feed.getCode(), futureTask);
		} else if (feed.isActived() && feed.isAccepted()) {
			TaskContainer task = new TaskContainer(feed, serviceFeed, this,
					newsCheckService);
			ScheduledFuture<?> futureTask = scheduler.scheduleWithFixedDelay(task, MIN_MILIS
					* feed.getMinRefresh());
			tasks.put(feed.getCode(), futureTask);
		}
	}

	public void addFeedTask(Feed feed) {
		TaskContainer task = new TaskContainer(feed, serviceFeed, this,
				newsCheckService);
		ScheduledFuture<?> futureTask = scheduler.scheduleWithFixedDelay(task,
				MIN_MILIS * feed.getMinRefresh());
		tasks.put(feed.getCode(), futureTask);
	}

	public void startTask(Feed feed) {
		TaskContainer task = new TaskContainer(feed, serviceFeed, this,
				newsCheckService);
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 5); // Iniciar en 5
															// segundos
		scheduler.schedule(task, startTime);
	}

}

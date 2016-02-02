package com.ucm.ilsa.veterinaria.scheduler;

import java.util.ArrayList;
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
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;

@Service
public class SchedulerService {

	private TaskScheduler scheduler;
	private FeedService serviceFeed;
	private Map<Long, ScheduledFuture<?>> tasks = Maps.newLinkedHashMap();
	private static final int MIN_MILIS = 60000;
	private NewsCheckFeedService newsCheckService;

	@Autowired
	public SchedulerService(FeedService service, TaskScheduler scheduler,
			NewsCheckFeedService newsCheckService) {
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
				AlertTaskContainer task = new AlertTaskContainer(feed,
						serviceFeed, this, newsCheckService);
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
		if (feed.isActived() && feed.isAccepted()) {
			ScheduledFuture<?> futureTask = null;
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed,
					serviceFeed, this, newsCheckService);
			futureTask = scheduler.scheduleWithFixedDelay(task, startTime,
					MIN_MILIS * feed.getMinRefresh());
			tasks.put(feed.getId(), futureTask);
		}
	}

	public void addFeedTask(Feed feed) {
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 120);
		if (feed.isActived() && feed.isAccepted()) {
			ScheduledFuture<?> futureTask = null;
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed,
					serviceFeed, this, newsCheckService);
			futureTask = scheduler.scheduleWithFixedDelay(task, startTime,
					MIN_MILIS * feed.getMinRefresh());
			tasks.put(feed.getId(), futureTask);
		}
	}

	public void startTask(Feed feed) {
		if (feed.isActived() && feed.isAccepted()) {
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 5); // Iniciar en 5
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed,
					serviceFeed, this, newsCheckService);
			scheduler.schedule(task, startTime);

		}
	}

	public void startAllTask() {
		for (Feed feed : serviceFeed.getAllFeed()) {
			startTask(feed);
		}
		
	}

}

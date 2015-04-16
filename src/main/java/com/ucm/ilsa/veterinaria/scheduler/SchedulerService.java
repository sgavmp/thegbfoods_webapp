package com.ucm.ilsa.veterinaria.scheduler;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.service.FeedService;

@Service
public class SchedulerService {

	private TaskScheduler   scheduler;
	private FeedService serviceFeed;
	private Map<String,ScheduledFuture<?>> tasks = Maps.newLinkedHashMap();
	private static final int HOUR_MILIS = 3600000;

	
	@Autowired
	public SchedulerService(FeedService service, TaskScheduler scheduler) {
		this.scheduler = scheduler;
		this.serviceFeed = service;
		service.setSchedulerService(this);
		init();
	}
	
	public void init() {
		List<Feed> listFeeds = serviceFeed.getAllFeed();
		for (Feed feed : listFeeds) {
			TaskContainer task = new TaskContainer(feed,serviceFeed);
			ScheduledFuture<?> futureTask = scheduler.scheduleWithFixedDelay(task, HOUR_MILIS * 6);
			tasks.put(feed.getCodeName(), futureTask);
		}
	}
	
	public void removeFeedTask(Feed feed) {
		ScheduledFuture<?> futureTask = tasks.get(feed.getCodeName());
		futureTask.cancel(true);
		tasks.remove(feed.getCodeName());
	}
	
	public void updateFeedTask(Feed feed) {
		ScheduledFuture<?> futureTask = tasks.get(feed.getCodeName());
		futureTask.cancel(true);
		TaskContainer task = new TaskContainer(feed,serviceFeed);
		futureTask = scheduler.scheduleWithFixedDelay(task, HOUR_MILIS * 6);
		tasks.put(feed.getCodeName(),futureTask);
	}
	
	public void addFeedTask(Feed feed) {
		TaskContainer task = new TaskContainer(feed,serviceFeed);
		ScheduledFuture<?> futureTask = scheduler.scheduleWithFixedDelay(task, HOUR_MILIS * 6);
		tasks.put(feed.getCodeName(),futureTask);
	}
	
}

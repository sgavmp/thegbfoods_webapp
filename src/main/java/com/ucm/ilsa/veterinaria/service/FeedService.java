package com.ucm.ilsa.veterinaria.service;

import java.util.List;

import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;

public interface FeedService {
	public Feed createFeed(Feed feed);
	public Feed updateFeed(Feed feed);
	public List<News> scrapFeed(Feed feed);
	public List<Feed> getAllFeed();
	public Feed getFeedByCodeName(String codeName);
	public boolean removeFeed(Feed feed);
	public void setSchedulerService(SchedulerService schedulerService);
	public News testFeed(FeedForm feed);

}

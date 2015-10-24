package com.ucm.ilsa.veterinaria.service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;

public interface FeedService {
	public Feed createFeed(Feed feed);
	public Feed updateFeed(Feed feed);
	public List<News> scrapFeed(Feed feed,Date after, boolean withOutLimit);
	public List<Feed> getAllFeed();
	public Feed getFeedByCodeName(Long codeName);
	public boolean removeFeed(Feed feed);
	public void setSchedulerService(SchedulerService schedulerService);
	public News testFeed(FeedForm feed);
	public List<NewsDetect> findAllDistinctNewsDetectByFeedOrderByDatePub(Feed feed);
	public Set<AlertAbstract> checkNewsLinkOnFeed(String link, Feed feed);
	public Feed setSateOfFeed(Feed feed, UpdateStateEnum state);

}

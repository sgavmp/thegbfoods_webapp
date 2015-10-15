package com.ucm.ilsa.veterinaria.service;

import java.util.List;

import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.FeedRisk;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;

public interface FeedRiskService {
	public FeedRisk createFeed(FeedRisk feed);
	public FeedRisk updateFeed(FeedRisk feed);
	public List<News> scrapFeed(FeedRisk feed);
	public List<FeedRisk> getAllFeed();
	public FeedRisk getFeedByCodeName(Long codeName);
	public boolean removeFeed(FeedRisk feed);
	public void setSchedulerService(SchedulerService schedulerService);
	public News testFeed(FeedForm feed);
	public List<NewsDetect> findAllDistinctNewsDetectByFeedOrderByDatePub(FeedRisk feed);
	public List<Risk> checkNewsLinkOnFeed(String link, FeedRisk feed);
	public FeedRisk setSateOfFeed(FeedRisk feed, UpdateStateEnum state);

}

package es.ucm.visavet.gbf.app.service;

import java.sql.Date;
import java.util.List;

import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.FeedForm;
import es.ucm.visavet.gbf.app.domain.News;
import es.ucm.visavet.gbf.app.domain.NewsDetect;
import es.ucm.visavet.gbf.app.domain.UpdateStateEnum;
import es.ucm.visavet.gbf.app.scheduler.SchedulerService;

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
	public Feed setSateOfFeed(Feed feed, UpdateStateEnum state);
	public List<String> createFeedAuto(String[] listURL);

}

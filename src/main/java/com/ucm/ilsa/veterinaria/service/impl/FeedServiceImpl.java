package com.ucm.ilsa.veterinaria.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;
import com.ucm.ilsa.veterinaria.service.FeedScraping;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;

@Service
public class FeedServiceImpl implements FeedService {
	
	@Autowired
	private FeedRepository repositoryFeed;
	@Autowired
	private FeedScraping scrapingFeed;
	@Autowired
	private NewsCheckFeedService newsCheckService;
	@Autowired
	private NewsDetectRepository newsDetectRepository;
	
	private SchedulerService schedulerService;

	@Override
	public List<News> scrapFeed(Feed feed) {
		return scrapingFeed.scrapNews(feed);
	}
	
	public News testFeed(FeedForm feed) {
		News news = scrapingFeed.scrapOneNews(feed);
		return news;
	}

	@Override
	public List<Feed> getAllFeed() {
		return repositoryFeed.findAllByOrderByNameAsc();
	}

	@Override
	public Feed getFeedByCodeName(Long codeName) {
		return repositoryFeed.findOne(codeName);
	}

	@Override
	public Feed createFeed(Feed feed) {
		feed = repositoryFeed.save(feed);
		schedulerService.addFeedTask(feed);
		return feed;
	}

	@Override
	public boolean removeFeed(Feed feed) {
		schedulerService.removeFeedTask(feed);
		this.repositoryFeed.delete(feed);
		return !this.repositoryFeed.exists(feed.getId());
	}

	@Override
	public Feed updateFeed(Feed feed) {
		Feed feedU = repositoryFeed.save(feed);
		schedulerService.updateFeedTask(feed);
		return feedU;
	}

	public SchedulerService getSchedulerService() {
		return schedulerService;
	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	
	public List<NewsDetect> findAllDistinctNewsDetectByFeedOrderByDatePub(Feed feed) {
		return newsDetectRepository.findAllDistinctBySiteOrderByDatePubDesc(feed);
	}
	
	public Set<AlertAbstract> checkNewsLinkOnFeed(String link, Feed feed) {
		News news = scrapingFeed.getNewsFromSite(link, feed);
		Set<AlertAbstract> alertasDetectadas = newsCheckService.checkNews(news, feed);
		return alertasDetectadas;
	}
	
	public Feed setSateOfFeed(Feed feed, UpdateStateEnum state) {
		feed = repositoryFeed.findOne(feed.getId());
		feed.setState(state);
		Feed feedU = repositoryFeed.save(feed);
		return feedU;
	}

}

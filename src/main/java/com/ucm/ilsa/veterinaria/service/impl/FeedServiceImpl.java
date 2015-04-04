package com.ucm.ilsa.veterinaria.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.ucm.ilsa.veterinaria.business.recuperacion.FeedScraping;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.PairValues;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;
import com.ucm.ilsa.veterinaria.service.FeedService;

@Service
public class FeedServiceImpl implements FeedService {
	
	private FeedRepository repositoryFeed;
	private FeedScraping scrapingFeed;
	private List<Feed> test;
	
	@Autowired
	public FeedServiceImpl(FeedRepository repositoryFeed, FeedScraping scrapingFeed) {
		//Feed 1
		Feed site1 = new Feed();
//		site1.setUrl("http://www.efsa.europa.eu/");
//		site1.setUrlRSS("http://www.efsa.europa.eu/en/press/rss");
//		site1.setLastNewsLink("http://www.efsa.europa.eu/en/press/news/150309.htm?utm_source=feed&utm_medium=rss&utm_campaign=prwns");
//		site1.setName("EFSA");
//		site1.setWithRSS(true);
//		site1.setCodeName("efsa");
//		site1.setDateFormat("dd MMMM yyyy");
//		site1.setLanguaje(Locale.ENGLISH);
//		site1.getSelectorMeta().add(new PairValues("description", "meta[property=og:description]"));
//		site1.getSelectorHtml().add(new PairValues("pubDate", "span.dates"));
//		site1.getSelectorHtml().add(new PairValues("content", "div.chapo p, div.newspr-detail > p"));
//		site1.setUseSelector(true);
//		//Feed 2
//		Feed site2 = new Feed();
//		site2.setUrl("http://www.ecdc.europa.eu");
//		site2.setDateFormat("dd MMMM yyyy");
//		site2.setLanguaje(Locale.ENGLISH);
//		site2.setName("ECDC");
//		site2.setCodeName("ecdc");
//		site2.setUrlNews("http://www.ecdc.europa.eu/en/press/news/Pages/News.aspx");
//		site2.setNewsLink("div.ECDCNewsContent div.ECDCNewsTitle a, div.ECDCNewsContentNoImage div.ECDCNewsTitle a");
//		site2.getSelectorHtml().add(new PairValues("title", "h1"));
//		site2.getSelectorHtml().add(new PairValues("pubDate", "div.date"));
//		site2.getSelectorHtml().add(new PairValues("content", "div.content"));
//		this.test = new ArrayList<Feed>();
//		test.add(site1);
//		test.add(site2);
		this.repositoryFeed = repositoryFeed;
		this.scrapingFeed = scrapingFeed;
//		this.repositoryFeed.save(test);
		site1=this.repositoryFeed.findOne("efsa");
		site1.setName("efs43");
		this.repositoryFeed.save(site1);
	}

	@Override
	public List<News> scrapFeed(Feed feed) {
		return scrapingFeed.scrapNews(feed);
	}

	@Override
	public List<Feed> getAllFeed() {
		return Lists.newArrayList(repositoryFeed.findAll());
	}

	@Override
	public Feed getFeedByCodeName(String codeName) {
		return repositoryFeed.findOne(codeName);
	}

	@Override
	public Feed createFeed(Feed feed) {
		feed = repositoryFeed.save(feed);
		scrapFeed(feed);
		return feed;
	}

	@Override
	public boolean removeFeed(Feed feed) {
		this.repositoryFeed.delete(feed);
		return !this.repositoryFeed.exists(feed.getCodeName());
	}
	
	

}

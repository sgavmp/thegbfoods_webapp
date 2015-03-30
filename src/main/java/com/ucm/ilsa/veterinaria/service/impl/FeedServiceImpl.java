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

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.recuperacion.FeedScraping;
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
		site1.setUrl("http://www.efsa.europa.eu/");
		site1.setUrlRSS("http://www.efsa.europa.eu/en/press/rss");
		site1.setLastNewsLink("http://www.efsa.europa.eu/en/press/news/150309.htm?utm_source=feed&utm_medium=rss&utm_campaign=prwns");
		site1.setName("EFSA");
		site1.setWithRSS(true);
		site1.setCodeName("efsa");
		site1.setDateFormat("dd MMMM yyyy");
		site1.setLanguaje(Locale.ENGLISH);
		site1.getSelectorMeta().put("description", "meta[property=og:description]");
		site1.getSelectorHtml().put("pubDate", "span.dates");
		site1.getSelectorHtml().put("content", "div.chapo p, div.newspr-detail > p");
		site1.setUseSelector(true);
		site1.setLimitNews(15);
		//Feed 2
		Feed site2 = new Feed();
		site2.setUrl("http://www.ecdc.europa.eu");
		site2.setDateFormat("dd MMMM yyyy");
		site2.setLanguaje(Locale.ENGLISH);
		site2.setName("ECDC");
		site2.setCodeName("ecdc");
		site2.setUrlNews("http://www.ecdc.europa.eu/en/press/news/Pages/News.aspx");
		site2.setPageLink("http://www.ecdc.europa.eu/en/press/news/Pages/News.aspx?p=");
		site2.getSelectorPagination().put("newsLink", "div.ECDCNewsContent div.ECDCNewsTitle a, div.ECDCNewsContentNoImage div.ECDCNewsTitle a");
		site2.getSelectorHtml().put("title", "h1");
		site2.getSelectorHtml().put("pubDate", "div.date");
		site2.getSelectorHtml().put("content", "div.content");
		site2.setLimitNews(15);
		site2.setNewsPerPage(15);
		this.test = new ArrayList<Feed>();
		test.add(site1);
		test.add(site2);
		this.repositoryFeed = repositoryFeed;
		this.scrapingFeed = scrapingFeed;
		this.repositoryFeed.save(test);
	}

	@Override
	public List<News> createOrUpdate(Feed feed) {
		return scrapingFeed.scrapNews(feed);
	}

	@Override
	public List<Feed> getAllFeed() {
		return test;
	}

	@Override
	public Feed getFeedByCodeName(String codeName) {
		return repositoryFeed.findOne(codeName);
	}

}

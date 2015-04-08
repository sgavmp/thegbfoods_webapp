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
		this.repositoryFeed = repositoryFeed;
		this.scrapingFeed = scrapingFeed;
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

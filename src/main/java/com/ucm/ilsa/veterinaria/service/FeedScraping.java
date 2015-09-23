package com.ucm.ilsa.veterinaria.service;

import java.util.List;
import java.util.concurrent.Future;

import com.rometools.rome.feed.synd.SyndEntry;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;

public interface FeedScraping {
	public List<News> scrapNews(Feed feed);
	public List<News> scrapNewsWithOutEvent(Feed feed);
	public News scrapOneNews(FeedForm feed);
	public News getNewsFromSite(String link, Feed feed);
}

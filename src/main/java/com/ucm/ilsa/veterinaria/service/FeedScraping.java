package com.ucm.ilsa.veterinaria.service;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.Future;

import com.rometools.rome.feed.synd.SyndEntry;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.Feed;

public interface FeedScraping {
	public List<News> scrapNews(Feed feed, Date after, boolean withOutLimit);
	public News scrapOneNews(FeedForm feed);
	public News getNewsFromSite(String link, Feed feed);
}

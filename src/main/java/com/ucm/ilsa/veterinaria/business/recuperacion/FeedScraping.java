package com.ucm.ilsa.veterinaria.business.recuperacion;

import java.util.List;
import java.util.concurrent.Future;

import com.rometools.rome.feed.synd.SyndEntry;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;

public interface FeedScraping {
	public List<News> scrapNews(Feed feed);
}

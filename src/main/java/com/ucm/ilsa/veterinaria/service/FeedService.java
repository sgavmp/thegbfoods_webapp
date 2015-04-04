package com.ucm.ilsa.veterinaria.service;

import java.util.List;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;

public interface FeedService {
	public Feed createFeed(Feed feed);
	public List<News> scrapFeed(Feed feed);
	public List<Feed> getAllFeed();
	public Feed getFeedByCodeName(String codeName);
	public boolean removeFeed(Feed feed);
}

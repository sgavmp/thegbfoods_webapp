package com.ucm.ilsa.veterinaria.service;

import java.util.List;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;

public interface FeedService {
	public List<News> createOrUpdate(Feed feed);
	public List<Feed> getAllFeed();
	public Feed getFeedByCodeName(String codeName);
}

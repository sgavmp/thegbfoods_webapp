package com.ucm.ilsa.veterinaria.service;

import java.util.List;
import java.util.Map;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.SiteAbstract;

public interface NewsCheckFeedService {
	public void checkNews(List<News> listNews, Feed feed);
	public Map<News, List<ResolvedLocation>> getLocations(List<News> listNews, boolean isTest);
	public List<Alert> checkNews(News news, Feed feed);
}

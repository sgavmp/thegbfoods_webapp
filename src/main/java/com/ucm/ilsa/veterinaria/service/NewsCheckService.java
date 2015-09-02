package com.ucm.ilsa.veterinaria.service;

import java.util.List;
import java.util.Map;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;

public interface NewsCheckService {
	public void checkNews(List<News> listNews, Feed feed);
//	public List<AlertDetect> detectAlert(Feed feed,
//			Map<News, List<ResolvedLocation>> locations);
	public Map<News, List<ResolvedLocation>> getLocations(List<News> listNews);
}

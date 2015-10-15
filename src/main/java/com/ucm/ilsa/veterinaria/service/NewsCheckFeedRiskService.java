package com.ucm.ilsa.veterinaria.service;

import java.util.List;
import java.util.Map;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedRisk;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.SiteAbstract;

public interface NewsCheckFeedRiskService {
	public void checkNews(List<News> listNews, FeedRisk feed);
	public List<Risk> checkNews(News news, FeedRisk feed);
}
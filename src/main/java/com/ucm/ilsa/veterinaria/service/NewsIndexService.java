package com.ucm.ilsa.veterinaria.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;

public interface NewsIndexService {

	public void initDirectory();
	public void stopDirectory() throws IOException;
	public boolean markNewNews(Feed feed);
	public void indexNews(News news) throws Exception;
	public void indexNews(List<News> news) throws Exception;
	public void removeNews(News news);
	public void removeNews(List<News> news);
	public void updateNews(News news);
	public void updateNews(List<News> news);
	public void emptyRamIndex() throws IOException;
	public void emptyNewsIndexBefore(Integer days);
	public List<News> search(AlertAbstract alert);
	public void resetAllAlerts() throws IOException;
	public void resetAlert(AlertAbstract alert);
}

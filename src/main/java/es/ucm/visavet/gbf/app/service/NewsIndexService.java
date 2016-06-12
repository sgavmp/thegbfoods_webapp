package es.ucm.visavet.gbf.app.service;

import java.io.IOException;
import java.util.List;

import es.ucm.visavet.gbf.app.domain.AlertAbstract;
import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.domain.News;
import es.ucm.visavet.gbf.app.domain.NewsDetect;

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
	public void updateIndex(Feed feed) throws Exception;
	public List<News> search(AlertAbstract alert);
	public void resetAllAlerts() throws IOException;
	public void resetAlert(AlertAbstract alert) throws IOException;
	public List<NewsDetect> search(String query) throws Exception;
	void resetLocation(Location loc) throws IOException;
	public void removeFeedFromIndex(Feed feed) throws Exception;
	void resetAllLocation() throws IOException;
}

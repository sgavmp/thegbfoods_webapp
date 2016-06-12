package es.ucm.visavet.gbf.app.service;

import java.io.InputStream;
import java.sql.Date;
import java.util.List;

import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.FeedForm;
import es.ucm.visavet.gbf.app.domain.News;

public interface FeedScraping {
	public List<News> scrapNews(Feed feed, Date after, boolean withOutLimit);
	public News scrapOneNews(FeedForm feed);
	public News getNewsFromSite(String link, Feed feed);
	public List<News> scrapingHistoric(Feed feed, InputStream xml);
}

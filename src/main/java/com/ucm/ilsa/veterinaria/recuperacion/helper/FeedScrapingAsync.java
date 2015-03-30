package com.ucm.ilsa.veterinaria.recuperacion.helper;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.recuperacion.impl.FeedScrapingImpl;

@Component
public class FeedScrapingAsync {
	
	private final static Logger LOGGER = Logger.getLogger(FeedScrapingAsync.class);
	
	@Async
	public Future<News> asyncGetNewsWithRSS(Feed feed, SyndEntry news){
		LOGGER.info("Comenzada llamada asincrona feed: " + feed.getCodeName());
		NewsBuilder temp = new NewsBuilder(feed);
		temp.setTitle(news.getTitle());
		temp.setUrl(news.getLink());
		temp.setDescription(news.getDescription().getValue());
		temp.setPubDate(news.getPublishedDate());
		String content = "";
		for (SyndContent part : news.getContents()) {
			content += part.getValue();
		}
		if (!content.equals(""))
			temp.setContent(content);
		if (feed.isUseSelector()) {
			Document doc = null;
			try {
				doc = Jsoup.connect(news.getLink()).get();
			} catch (IOException e) {
				System.out
						.println("Error al acceder a la dirección URL: "
								+ news.getLink());
				return null;
			}
			for (String attribute : feed.getSelectorHtml().keySet()) {
				try {
					temp.setValueOf(
							attribute,
							doc.select(
									feed.getSelectorHtml().get(
											attribute)).text());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (String attribute : feed.getSelectorMeta().keySet()) {
				try {
					temp.setValueOf(
							attribute,
							doc.select(
									feed.getSelectorMeta().get(
											attribute)).attr("content"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new AsyncResult<News>(temp.build());
	}

}

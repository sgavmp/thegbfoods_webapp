package com.ucm.ilsa.veterinaria.business.recuperacion.helper;

import java.io.IOException;
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
import com.ucm.ilsa.veterinaria.domain.PairValues;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;

@Component
public class FeedScrapingAsync {

	private final static Logger LOGGER = Logger
			.getLogger(FeedScrapingAsync.class);

	@Async
	public Future<News> asyncGetNewsWithRSS(Feed feed, SyndEntry news) {
		LOGGER.debug("Comenzada llamada asincrona feed: " + feed.getCodeName()
				+ " noticia: " + news.getLink());
		try {
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
			if (feed.getSelectorHtml().size()>0 || feed.getSelectorMeta().size()>0) {
				Document doc = Jsoup.connect(news.getLink()).get();
				for (PairValues attribute : feed.getSelectorHtml()) {
					temp.setValueOf(attribute.getKey(),
							doc.select(attribute.getValue()).text());
				}
				for (PairValues attribute : feed.getSelectorMeta()) {
					temp.setValueOf(attribute.getKey(),
							doc.select(attribute.getValue()).attr("content"));
				}
			}
			return new AsyncResult<News>(temp.build());
		} catch (IOException e) {
			LOGGER.info("Error al acceder a la direccion URL: "
					+ news.getLink());
			return null;
		}

	}

	@Async
	public Future<News> asyncGetNewsWithOutRSS(Feed feed, String linkNews) {
		LOGGER.debug("Comenzada llamada asincrona feed: " + feed.getCodeName()
				+ " noticia: " + linkNews);
		try {
			Document newsPage = null;

			newsPage = Jsoup.connect(linkNews).get();

			NewsBuilder temp = new NewsBuilder(feed);
			temp.setUrl(linkNews);
			for (PairValues attribute : feed.getSelectorHtml()) {
				temp.setValueOf(attribute.getKey(),
						newsPage.select(attribute.getValue()).text());
			}
			for (PairValues attribute : feed.getSelectorMeta()) {
				temp.setValueOf(attribute.getKey(),
						newsPage.select(attribute.getValue()).attr("content"));
			}
			return new AsyncResult<News>(temp.build());
		} catch (IOException e) {
			LOGGER.info("Error al acceder a la direccion URL: " + linkNews);
			return null;
		}

	}

}

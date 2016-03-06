package com.ucm.ilsa.veterinaria.batch;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucm.ilsa.veterinaria.domain.CharsetEnum;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.service.impl.FeedScrapingImpl;

public class FeedItemProcessor implements ItemProcessor<Feed, List<News>> {

	private final static Logger LOGGER = Logger
			.getLogger(FeedItemProcessor.class);

	@Autowired
	private NewsDetectRepository repository;

	@Override
	public List<News> process(Feed item) throws Exception {
		List<NewsDetect> listNewsDetect = repository
				.findAllDistinctBySiteOrderByDatePubDesc(item);
		List<News> listNews = new ArrayList<News>();
		int i=1;
		LOGGER.info("Procesando sitio "+ item.getName() + ", " + listNewsDetect.size() + " noticias almacendas");
		for (NewsDetect detect : listNewsDetect) {
			News news = new News();
			// Enlace de la noticia
			String linkNews = detect.getLink();
			// Titutlo de la noticia
			String title = detect.getTitle();
			news = getNewsWithOutRSS(item, linkNews, title);
			listNews.add(news);
			LOGGER.info(item.getName() + " - " + i + "/" + listNewsDetect.size());
			i++;
		}
		return listNews;
	}

	public News getNewsWithOutRSS(Feed feed, String linkNews, String title) {
		Document newsPage = null;
		Integer count = 0;
		while (true) {
			try {
				if (feed.getCharSet().equals(CharsetEnum.UTF8)) {// Codificacion
																	// por
																	// defecto
					newsPage = Jsoup.connect(linkNews).userAgent("Mozilla")
							.timeout(10000).get();
				} else {
					newsPage = Jsoup.parse(new URL(linkNews).openStream(), feed
							.getCharSet().getValue(), linkNews);
				}
				break;
			} catch (IOException e) {// En caso de error intentamos tres veces
				count++;
				LOGGER.info("Error al acceder a la direccion URL: " + linkNews
						+ " intento " + count + "/3");
				if (count == 3)
					return null;
			}
		}
		NewsBuilder temp = new NewsBuilder(feed);
		temp.setUrl(linkNews);
		temp.setTitle(title);
		if (feed.getSelectorContent() != null) {
			if (!feed.getSelectorContent().isEmpty()) {
				temp.setContent(feed.getSelectorContentMeta() ? newsPage
						.select(feed.getSelectorContent()).attr("content")
						: newsPage.select(feed.getSelectorContent()).text());
			}
		}
		if (feed.getSelectorDescription() != null) {
			if (!feed.getSelectorDescription().isEmpty()) {
				temp.setDescription(feed.getSelectorDescriptionMeta() ? newsPage
						.select(feed.getSelectorDescription()).attr("content")
						: newsPage.select(feed.getSelectorDescription()).text());
			}
		}
		if (feed.getSelectorPubDate() != null) {
			if (!feed.getSelectorPubDate().isEmpty()) {
				temp.setPubDate(feed.getSelectorPubDateMeta() ? newsPage
						.select(feed.getSelectorPubDate()).attr("content")
						: newsPage.select(feed.getSelectorPubDate()).text());
			}
		}
		if (feed.getSelectorTitle() != null) {
			if (!feed.getSelectorTitle().isEmpty()) {
				temp.setTitle(feed.getSelectorTitleMeta() ? newsPage.select(
						feed.getSelectorTitle()).attr("content") : newsPage
						.select(feed.getSelectorTitle()).text());
			}
		}
		return temp.build();
	}

}

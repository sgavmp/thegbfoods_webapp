package com.ucm.ilsa.veterinaria.business.recuperacion.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;

import com.google.common.eventbus.EventBus;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.ucm.ilsa.veterinaria.business.event.config.EventBusFactoryBean;
import com.ucm.ilsa.veterinaria.business.event.tratamiento.FeedUpdateEvent;
import com.ucm.ilsa.veterinaria.business.recuperacion.FeedScraping;
import com.ucm.ilsa.veterinaria.business.recuperacion.helper.FeedScrapingAsync;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.PairValues;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;

@Repository
public class FeedScrapingImpl implements FeedScraping {
	
	private final static Logger LOGGER = Logger.getLogger(FeedScrapingImpl.class);

	private FeedRepository repositoryFeed;
	
	@Autowired
	private ThreadPoolTaskExecutor executorService;
	
	@Autowired
	private FeedScrapingAsync asyncService;

	@Autowired
	public FeedScrapingImpl(FeedRepository repositoryFeed) {
		this.repositoryFeed = repositoryFeed;
	}

	@Override
	public List<News> scrapNews(Feed feed) {
		List<News> newsList = new ArrayList<>();
		if (feed.isWithRSS()) {
			newsList = scrapingWhitRSS(feed);
		} else {
			newsList = scrapingWithOutRSS(feed);
		}
		if (!newsList.isEmpty()) {
			// Ordenamos por fecha de publicacion
			Collections.sort(newsList, News.Comparators.PUBDATE);
		}
		//Informamos a todos los tratamientos de la actualizacion
		FeedUpdateEvent event = new FeedUpdateEvent();
		event.setFeed(feed);
		//event.setNumNews(newsList.size());
		event.setDate(new Date(System.currentTimeMillis()));
		event.setListNews(newsList);
		EventBusFactoryBean.getInstance().post(event);
		
		return newsList;
	}

	private List<News> scrapingWhitRSS(Feed feed) {
		List<News> listNews = new ArrayList<News>();
		// open a connection to the rss feed
		if (feed.getUrlRSS() != null) {
			URL url = null;
			try {
				url = new URL(feed.getUrlRSS());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			URLConnection conn = null;
			try {
				conn = url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed newsList = null;
			try {
				newsList = input.build(new XmlReader(conn.getInputStream()));
			} catch (IllegalArgumentException | FeedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Future<News>> listaTareas = new ArrayList<Future<News>>();
			boolean isFirst = true;
			String linkLastNews = "";
			for (SyndEntry news : newsList.getEntries()) {
				// Vamos comprobando el link de la entrada con el enlace de la
				// ultima noticia almacenada del feed
				if (feed.getLastNewsLink() != null) {
					// En caso de coincidencia (es decir que ya esta en el
					// sistema) devolvemos la lista (puede estar vacia)
					if (!feed.getLastNewsLink().isEmpty()
							&& feed.getLastNewsLink().equals(news.getLink())) {
						linkLastNews=feed.getLastNewsLink();
						break;
					} else if (isFirst) {
						linkLastNews = news.getLink();
						isFirst = false;
					}
				}
				listaTareas.add(asyncService.asyncGetNewsWithRSS(feed,news));
			}
			feed.setLastNewsLink(linkLastNews);
			repositoryFeed.save(feed);
			for (Future<News> result : listaTareas) {
				try {
					if (result!=null) {
						News temp = result.get();
						if (temp!=null) 
							listNews.add(temp);
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return listNews;
	}
	

	private List<News> scrapingWithOutRSS(Feed feed) {
		List<News> listNews = new ArrayList<News>();
		if (feed.getUrlNews() != null) {
				Document doc = null;
				try {
					doc = Jsoup.connect(feed.getUrlNews()).get();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Elements newsLinks = doc.select(feed.getNewsLink());
				boolean isFirst = true;
				String linkLastNews = "";
				for (Element link : newsLinks) {
					// Enlace de la noticia
					String linkNews = link.absUrl("href");
					// Vamos comprobando el link de la entrada con el enlace de
					// la ultima noticia almacenada del feed
					if (feed.getLastNewsLink() != null) {
						// En caso de coincidencia (es decir que ya esta en el
						// sistema) devolvemos la lista (puede estar vacia)
						if (!feed.getLastNewsLink().isEmpty()
								&& feed.getLastNewsLink().equals(linkNews)) {
							feed.setLastNewsLink(linkLastNews);
							repositoryFeed.save(feed);
							return listNews;
						} else if (isFirst) {
							linkLastNews = linkNews;
							isFirst = false;
						}
					}
					Document newsPage = null;
					try {
						newsPage = Jsoup.connect(linkNews).get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					NewsBuilder temp = new NewsBuilder(feed);
					temp.setUrl(linkNews);
					for (PairValues attribute : feed.getSelectorHtml()) {
						try {
							temp.setValueOf(attribute.getKey(),	newsPage.select(attribute.getValue()).text());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					for (PairValues attribute : feed.getSelectorMeta()) {
						try {
							temp.setValueOf(attribute.getKey(),	newsPage.select(attribute.getValue()).attr("content"));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					listNews.add(temp.build());
				}
		}
		return listNews;
	}

}

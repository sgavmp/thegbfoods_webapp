package com.ucm.ilsa.veterinaria.business.recuperacion.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;

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
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;

@Repository
public class FeedScrapingImpl implements FeedScraping {

	private final static Logger LOGGER = Logger
			.getLogger(FeedScrapingImpl.class);

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
		if (feed.isRSS()) {
			newsList = scrapingWhitRSS(feed);
		} else {
			newsList = scrapingWithOutRSS(feed);
		}
		if (!newsList.isEmpty()) {
			// Ordenamos por fecha de publicacion
			Collections.sort(newsList, News.Comparators.PUBDATE);
		}
		// Solo se informa si se han obtenido nuevas noticias
		if (newsList.size() > 0) {
			// Informamos a todos los tratamientos de la actualizacion
			FeedUpdateEvent event = new FeedUpdateEvent();
			event.setFeed(feed);
			event.setDate(new Date(System.currentTimeMillis()));
			event.setListNews(newsList);
			EventBusFactoryBean.getInstance().post(event);
		}
		return newsList;
	}
	
	@Override
	public News scrapOneNews(FeedForm feed) {
		if (feed.isRSS()) {
			return scrapingOneWhitRSS(feed);
		} else {
			return scrapingOneWithOutRSS(feed);
		}
	}

	private List<News> scrapingWhitRSS(Feed feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			// open a connection to the rss feed
			if (feed.getUrlNews() != null) {
				URL url = new URL(feed.getUrlNews());
				URLConnection conn = url.openConnection();
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed newsList = input.build(new XmlReader(conn
						.getInputStream()));
				List<Future<News>> listaTareas = new ArrayList<Future<News>>();
				boolean isFirst = true;
				String lastNews = null;
				for (SyndEntry news : newsList.getEntries()) {
					// Vamos comprobando el link de la entrada con el enlace de
					// la ultima noticia almacenada del feed
					if (feed.getLastNewsLink() != null) {
						// En caso de coincidencia (es decir que ya esta en el
						// sistema) devolvemos la lista (puede estar vacia)
						if (!feed.getLastNewsLink().isEmpty()
								&& feed.getLastNewsLink()
										.equals(news.getLink())) {
							break;
						} else if (isFirst) {
							lastNews = news.getLink();
							isFirst = false;
						}
					} else if (isFirst) {
						lastNews = news.getLink();
						isFirst = false;
					}
					listaTareas.add(asyncService
							.asyncGetNewsWithRSS(feed, news));
				}
				if (lastNews != null) {
					feed.setLastNewsLink(lastNews);
					repositoryFeed.save(feed);
				}
				for (Future<News> result : listaTareas) {
					if (result != null) {
						News temp = result.get();
						if (temp != null)
							listNews.add(temp);
					}
				}
			}
			return listNews;
		} catch (MalformedURLException e) {
			LOGGER.error("Error al generar a la URL. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (IllegalArgumentException | FeedException e) {
			LOGGER.error("Error al obtener la informacion RSS. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Error al acceder al resultado asincrono. Mas Info-> "
					+ e.getMessage());
			return null;
		}
	}

	private List<News> scrapingWithOutRSS(Feed feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			List<Future<News>> listaTareas = new ArrayList<Future<News>>();
			if (feed.getUrlNews() != null) {
				for (String page : feed.getUrlPages()) {
					Document doc = Jsoup.connect(page).get();
					Elements newsLinks = doc.select(feed.getNewsLink());
					boolean isFirst = true;
					String linkLastNews = null;
					for (Element link : newsLinks) {
						// Enlace de la noticia
						String linkNews = link.absUrl("href");
						// Vamos comprobando el link de la entrada con el enlace
						// de
						// la ultima noticia almacenada del feed
						if (feed.getLastNewsLink() != null) {
							// En caso de coincidencia (es decir que ya esta en
							// el
							// sistema) devolvemos la lista (puede estar vacia)
							if (!feed.getLastNewsLink().isEmpty()
									&& feed.getLastNewsLink().equals(linkNews)) {
								break;
							} else if (isFirst) {
								linkLastNews = linkNews;
								isFirst = false;
							}
						}
						listaTareas.add(asyncService.asyncGetNewsWithOutRSS(
								feed, linkNews));
					}
					if (linkLastNews != null) {
						feed.setLastNewsLink(linkLastNews);
						repositoryFeed.save(feed);
					}
					for (Future<News> result : listaTareas) {
						if (result != null) {
							News temp = result.get();
							if (temp != null)
								listNews.add(temp);
						}
					}
				}
			}
			return listNews;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Error al acceder al resultado asincrono. Mas Info-> "
					+ e.getMessage());
			return null;
		}
	}
	
	private News scrapingOneWhitRSS(FeedForm feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			// open a connection to the rss feed
			if (feed.getUrlNews() != null) {
				URL url = new URL(feed.getUrlNews());
				URLConnection conn = url.openConnection();
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed newsList = input.build(new XmlReader(conn
						.getInputStream()));
				List<Future<News>> listaTareas = new ArrayList<Future<News>>();
				boolean isFirst = true;
				String lastNews = null;
				for (SyndEntry news : newsList.getEntries()) {
					listaTareas.add(asyncService.asyncGetNewsWithRSS(new Feed(feed), news));
					break;
				}
				for (Future<News> result : listaTareas) {
					if (result != null) {
						News temp = result.get();
						if (temp != null)
							listNews.add(temp);
					}
				}
			}
			return listNews.get(0);
		} catch (MalformedURLException e) {
			LOGGER.error("Error al generar a la URL. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (IllegalArgumentException | FeedException e) {
			LOGGER.error("Error al obtener la informacion RSS. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Error al acceder al resultado asincrono. Mas Info-> "
					+ e.getMessage());
			return null;
		}
	}

	private News scrapingOneWithOutRSS(FeedForm feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			List<Future<News>> listaTareas = new ArrayList<Future<News>>();
			if (feed.getUrlNews() != null) {
				for (String page : feed.getUrlPages()) {
					Document doc = Jsoup.connect(page).get();
					Elements newsLinks = doc.select(feed.getNewsLink());
					boolean isFirst = true;
					String linkLastNews = null;
					for (Element link : newsLinks) {
						// Enlace de la noticia
						String linkNews = link.absUrl("href");
						listaTareas.add(asyncService.asyncGetNewsWithOutRSS(
								new Feed(feed), linkNews));
						break;
					}
					for (Future<News> result : listaTareas) {
						if (result != null) {
							News temp = result.get();
							if (temp != null)
								listNews.add(temp);
						}
					}
					break;
				}
			}
			return listNews.get(0);
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage());
			return null;
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Error al acceder al resultado asincrono. Mas Info-> "
					+ e.getMessage());
			return null;
		}
	}

}

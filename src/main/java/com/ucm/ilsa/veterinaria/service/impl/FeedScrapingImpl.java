package com.ucm.ilsa.veterinaria.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.CharSet;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.ucm.ilsa.veterinaria.domain.CharsetEnum;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.PairValues;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.service.FeedScraping;

@Repository
public class FeedScrapingImpl implements FeedScraping {

	private final static Logger LOGGER = Logger
			.getLogger(FeedScrapingImpl.class);

	private FeedRepository repositoryFeed;
	
	private NewsDetectRepository repositoryNewsDetect;

	@Autowired
	public FeedScrapingImpl(FeedRepository repositoryFeed, NewsDetectRepository repositoryNewsDetect) {
		this.repositoryFeed = repositoryFeed;
		this.repositoryNewsDetect = repositoryNewsDetect;
	}

	@Override
	public List<News> scrapNews(Feed feed) {
		List<News> newsList = new ArrayList<>();
		if (feed.isRSS()) {
			newsList = scrapingWhitRSS(feed);
		} else {
			newsList = scrapingWithOutRSS(feed);
		}
		feed = repositoryFeed.findOne(feed.getCode());
		feed.setUltimaRecuperacion(new Timestamp(System.currentTimeMillis()));
		feed.setNumNewNews(newsList!=null?newsList.size():0);
		repositoryFeed.save(feed);
		LOGGER.info("Noticias recuperadas del sitio " + feed.getName());
		return newsList;
	}

	@Override
	public List<News> scrapNewsWithOutEvent(Feed feed) {
		List<News> newsList = new ArrayList<>();
		if (feed.isRSS()) {
			newsList = scrapingWhitRSS(feed);
		} else {
			newsList = scrapingWithOutRSS(feed);
		}
		return newsList;
	}

	@Override
	public News scrapOneNews(FeedForm feed) {
		if (feed.getIsRSS()) {
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
					listNews.add(getNewsWithRSS(feed, news));
				}
				if (feed.getDateFirstNews()==null) {
					feed.setDateFirstNews(newsList.getEntries().get(newsList.getEntries().size()-1).getPublishedDate());
				}
				if (lastNews != null) {
					feed.setLastNewsLink(lastNews);
					repositoryFeed.save(feed);
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
		}
	}

	private List<News> scrapingWithOutRSS(Feed feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlNews() != null) {
				Document doc = Jsoup.connect(feed.getUrlNews()).get();
				Elements newsLinks = doc.select(feed.getNewsLink());
				boolean isFirst = true;
				String lastNews = null;
				News news = null;
				for (Element link : newsLinks) {
					// Enlace de la noticia
					String linkNews = link.absUrl("href");
					// Titutlo de la noticia
					String title = link.text();
					news = getNewsWithOutRSS(feed, linkNews, title);
					// Vamos comprobando el link de la entrada con el enlace de
					// la ultima noticia almacenada del feed
					if (feed.getLastNewsLink() != null) {
						// En caso de coincidencia (es decir que ya esta en el
						// sistema) devolvemos la lista (puede estar vacia)
						if (!feed.getLastNewsLink().isEmpty()
								&& feed.getLastNewsLink()
										.equals(linkNews)) {
							break;
						} else if (isFirst) {
							lastNews = linkNews;
							isFirst = false;
						}
					} else if (isFirst) {
						lastNews = linkNews;
						isFirst = false;
					}
					listNews.add(news);
				}
				if (feed.getDateFirstNews()==null) {
					if (news!=null)
							feed.setDateFirstNews(news.getPubDate());
				}
				if (lastNews != null) {
					feed.setLastNewsLink(lastNews);
					repositoryFeed.save(feed);
				}
			}
			return listNews;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
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
				for (SyndEntry news : newsList.getEntries()) {
					listNews.add(getNewsWithRSS(new Feed(feed), news));
					break;
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
		}
	}

	private News scrapingOneWithOutRSS(FeedForm feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlNews() != null) {
				Document doc = Jsoup.connect(feed.getUrlNews()).timeout(20000).get();
				Elements newsLinks = doc.select(feed.getNewsLink());
				for (Element link : newsLinks) {
					// Enlace de la noticia
					String linkNews = link.absUrl("href");
					if (linkNews != null) {
						if (!linkNews.isEmpty()) {
							// Titutlo de la noticia
							String title = link.text();
							listNews.add(getNewsWithOutRSS(new Feed(feed),
									linkNews, title));
							break;
						}
					}
				}
			}
			return listNews.get(0);
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage());
			return null;
		}
	}

	public News getNewsWithRSS(Feed feed, SyndEntry news) {
		String url = news.getLink().contains(feed.getUrlSite())?news.getLink():feed.getUrlSite().concat(news.getLink());
		NewsBuilder temp = new NewsBuilder(feed);
		temp.setTitle(news.getTitle());
		temp.setUrl(url);
		temp.setDescription(news.getDescription().getValue());
		temp.setPubDate(news.getPublishedDate());
		String content = "";
		for (SyndContent part : news.getContents()) {
			content += part.getValue();
		}
		if (!content.equals("")) {
			temp.setContent(content);
		}
		Document newsPage = null;
		Integer count = 0;
		while (true) {
			try {
				if (feed.getCharSet().equals(CharsetEnum.UTF8)) {// Codificacion
																	// por
																	// defecto
					newsPage = Jsoup.connect(url)
							.userAgent("Mozilla").timeout(10000).get();
				} else {
					newsPage = Jsoup.parse(
							new URL(url).openStream(), feed
									.getCharSet().getValue(), url);
				}
				break;
			} catch (IOException e) {// En caso de error intentamos tres veces
				count++;
				LOGGER.info("Error al acceder a la direccion URL: "
						+ url + " intento " + count + "/3");
				if (count == 3)
					return null;
			}
		}
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

	@Override
	public News getNewsFromSite(String link, Feed feed) {
		return getNewsWithOutRSS(feed, link, link);
	}

}

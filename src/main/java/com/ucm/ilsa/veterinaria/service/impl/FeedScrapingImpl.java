package com.ucm.ilsa.veterinaria.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.kohlschutter.boilerpipe.BoilerpipeExtractor;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.extractors.CommonExtractors;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.ucm.ilsa.veterinaria.domain.CharsetEnum;
import com.ucm.ilsa.veterinaria.domain.ExtractionType;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.ScrapStatistics;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.ScrapStatisticsRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.FeedScraping;
import com.ucm.ilsa.veterinaria.util.GBFoodCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Repository
public class FeedScrapingImpl implements FeedScraping {

	private final static Logger LOGGER = Logger
			.getLogger(FeedScrapingImpl.class);

	private FeedRepository repositoryFeed;

	private NewsDetectRepository repositoryNewsDetect;

	private ScrapStatisticsRepository statisticsRepository;

	@Value("${gbfood.resources.crawler}")
	private String pathCrawler = "/tomcatfolder/app/gbfood/crawler/";

	@Autowired
	public FeedScrapingImpl(FeedRepository repositoryFeed,
			NewsDetectRepository repositoryNewsDetect,
			ScrapStatisticsRepository statisticsRepository) {
		this.repositoryFeed = repositoryFeed;
		this.repositoryNewsDetect = repositoryNewsDetect;
		this.statisticsRepository = statisticsRepository;
	}

	@Override
	public List<News> scrapNews(Feed feed, Date after, boolean withOutLimit) {
		List<News> newsList = new ArrayList<>();
		if (feed.isAuto()) {
			newsList = scrapingAuto(feed, after, withOutLimit);
		} else {
			if (feed.isRSS()) {
				newsList = scrapingWhitRSS(feed, after, withOutLimit);
			} else {
				newsList = scrapingWithOutRSS(feed, after, withOutLimit);
			}
		}
		feed = repositoryFeed.findOne(feed.getId());
		feed.setUltimaRecuperacion(new Timestamp(System.currentTimeMillis()));
		feed.setNumNewNews(newsList != null ? newsList.size() : 0);
		repositoryFeed.save((Feed) feed);
		if (newsList != null) {
			Date today = new Date(System.currentTimeMillis());
			ScrapStatistics statistics = statisticsRepository.findOne(today);
			if (statistics != null) {
				statistics.setTotal(statistics.getTotal() + newsList.size());
			} else {
				statistics = new ScrapStatistics(today, newsList.size());
			}
			statisticsRepository.save(statistics);
		}
		LOGGER.info("Noticias recuperadas del sitio " + feed.getName());
		return newsList;
	}

	private List<News> scrapingAuto(Feed feed, Date after, boolean withOutLimit) {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlNews() != null) {
				String crawlStorageFolder = pathCrawler;
				int numberOfCrawlers = 1;

				CrawlConfig config = new CrawlConfig();
				config.setCrawlStorageFolder(crawlStorageFolder);
				config.setMaxDepthOfCrawling(1);
				config.setPolitenessDelay(10);
				config.setShutdownOnEmptyQueue(true);

				/*
				 * Instantiate the controller for this crawl.
				 */
				PageFetcher pageFetcher = new PageFetcher(config);
				RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
				robotstxtConfig.setEnabled(false);
				RobotstxtServer robotstxtServer = new RobotstxtServer(
						robotstxtConfig, pageFetcher);
				CrawlController controller = new CrawlController(config,
						pageFetcher, robotstxtServer);

				/*
				 * For each crawl, you need to add some seed urls. These are the
				 * first URLs that are fetched and then the crawler starts
				 * following links which are found in these pages
				 */
				controller.addSeed(feed.getUrlNews());

				/*
				 * Start the crawl. This is a blocking operation, meaning that
				 * your code will reach the line after this only when crawling
				 * is finished.
				 */
				controller.start(GBFoodCrawler.class, numberOfCrawlers);
				List<String> newsLinks = (List<String>) controller
						.getCrawlersLocalData().get(0);
				boolean isFirst = true;
				String lastNews = null;
				News news = null;
				for (String linkNews : newsLinks) {
					if (feed.getCrawlerNews().contains(linkNews))
						continue;
					news = getNewsWithOutRSS(feed, linkNews, null);
					listNews.add(news);
					feed.getCrawlerNews().add(linkNews);
				}
				repositoryFeed.save(feed);
			}
			return listNews;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		} catch (Exception e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		}
	}

	@Override
	public News scrapOneNews(FeedForm feed) {
		if (feed.getIsAuto()) {
			return scrapingOneWithAuto(feed);
		} else {
			if (feed.getIsRSS()) {
				return scrapingOneWhitRSS(feed);
			} else {
				return scrapingOneWithOutRSS(feed);
			}
		}
	}

	private News scrapingOneWithAuto(FeedForm feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlNews() != null) {
				String crawlStorageFolder = pathCrawler;
				int numberOfCrawlers = 1;

				CrawlConfig config = new CrawlConfig();
				config.setCrawlStorageFolder(crawlStorageFolder);
				config.setMaxPagesToFetch(2);//Solo un enlace
				config.setPolitenessDelay(1);
				config.setMaxDepthOfCrawling(1);
				config.setShutdownOnEmptyQueue(true);

				/*
				 * Instantiate the controller for this crawl.
				 */
				PageFetcher pageFetcher = new PageFetcher(config);
				RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
				robotstxtConfig.setEnabled(false);
				RobotstxtServer robotstxtServer = new RobotstxtServer(
						robotstxtConfig, pageFetcher);
				CrawlController controller = new CrawlController(config,
						pageFetcher, robotstxtServer);

				/*
				 * For each crawl, you need to add some seed urls. These are the
				 * first URLs that are fetched and then the crawler starts
				 * following links which are found in these pages
				 */
				controller.addSeed(feed.getUrlNews());

				/*
				 * Start the crawl. This is a blocking operation, meaning that
				 * your code will reach the line after this only when crawling
				 * is finished.
				 */
				controller.start(GBFoodCrawler.class, numberOfCrawlers);
				List<String> newsLinks = (List<String>) controller
						.getCrawlersLocalData().get(0);
				boolean isFirst = true;
				String lastNews = null;
				News news = null;
				for (String linkNews : newsLinks) {
					news = getNewsWithOutRSS(new Feed(feed), linkNews, "");
					listNews.add(news);
					break;
				}
			}
			return listNews.get(0);
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		} catch (Exception e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		}
	}

	private List<News> scrapingWhitRSS(Feed feed, Date after,
			boolean withOutLimit) {
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
					if (!withOutLimit && after == null) {
						// Vamos comprobando el link de la entrada con el enlace
						// de
						// la ultima noticia almacenada del feed
						if (feed.getLastNewsLink() != null) {
							// En caso de coincidencia (es decir que ya esta en
							// el
							// sistema) devolvemos la lista (puede estar vacia)
							if (!feed.getLastNewsLink().isEmpty()
									&& feed.getLastNewsLink().equals(
											news.getLink())) {
								break;
							} else if (isFirst) {
								lastNews = news.getLink();
								isFirst = false;
							}
						} else if (isFirst) {
							lastNews = news.getLink();
							isFirst = false;
						}
					}
					News newsData = getNewsWithRSS(feed, news);
					if (!withOutLimit && after != null) {
						if (newsData.getPubDate().before(after)) {
							break;
						}
					}
					listNews.add(newsData);
				}
				if (feed.getDateFirstNews() == null) {
					feed.setDateFirstNews(newsList.getEntries()
							.get(newsList.getEntries().size() - 1)
							.getPublishedDate());
				}
				if (after == null && !withOutLimit) {
					if (lastNews != null) {
						feed = repositoryFeed.findOne(feed.getId());
						feed.setLastNewsLink(lastNews);
						repositoryFeed.save((Feed) feed);
					}
				}
			}
			return listNews;
		} catch (MalformedURLException e) {
			LOGGER.error("Error al generar a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		} catch (IllegalArgumentException | FeedException e) {
			LOGGER.error("Error al obtener la informacion RSS. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		}
	}

	private List<News> scrapingWithOutRSS(Feed feed, Date after,
			boolean withOutLimit) {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlNews() != null) {
				Document doc = Jsoup.connect(feed.getUrlNews()).timeout(20000)
						.get();
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
					if (!withOutLimit && after == null) {
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
								lastNews = linkNews;
								isFirst = false;
							}
						} else if (isFirst) {
							lastNews = linkNews;
							isFirst = false;
						}
					} else if (!withOutLimit && after != null) {
						if (news.getPubDate().before(after)) {
							break;
						}
					}
					listNews.add(news);
				}
				if (feed.getDateFirstNews() == null) {
					if (news != null)
						feed.setDateFirstNews(news.getPubDate());
				}
				if (after == null && !withOutLimit) {
					if (lastNews != null) {
						feed.setLastNewsLink(lastNews);
						repositoryFeed.save((Feed) feed);
					}
				}
			}
			return listNews;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		} catch (Exception e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
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
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		} catch (IOException e) {
			LOGGER.error("No se ha podido acceder a la URL. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		} catch (IllegalArgumentException | FeedException e) {
			LOGGER.error("Error al obtener la informacion RSS. Mas Info-> "
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		}
	}

	private News scrapingOneWithOutRSS(FeedForm feed) {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlNews() != null) {
				Document doc = Jsoup.connect(feed.getUrlNews()).timeout(20000)
						.get();
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
					+ e.getMessage() + " -> " + feed.getNewsLink());
			return null;
		}
	}

	public News getNewsWithRSS(Feed feed, SyndEntry news) {
		String link = news.getLink();
		try {
			URL url = new URL(news.getLink());
		} catch (MalformedURLException ex) {
			link = feed.getUrlSite().concat(news.getLink());
		}
		String url = link;
		NewsBuilder temp = new NewsBuilder(feed);
		temp.setTitle(news.getTitle());
		temp.setUrl(url);
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
					newsPage = Jsoup.connect(url).userAgent("Mozilla")
							.timeout(10000).get();
				} else {
					newsPage = Jsoup.parse(new URL(url).openStream(), feed
							.getCharSet().getValue(), url);
				}
				break;
			} catch (IOException e) {// En caso de error intentamos tres veces
				count++;
				LOGGER.info("Error al acceder a la direccion URL: " + url
						+ " intento " + count + "/3");
				if (count == 3)
					return null;
			}
		}
		if (feed.getExtractionType() == null
				|| ExtractionType.CSS_EXTRACTOR
						.equals(feed.getExtractionType())) {
			if (feed.getSelectorContent() != null) {
				if (!feed.getSelectorContent().isEmpty()) {
					temp.setContent(feed.getSelectorContentMeta() ? newsPage
							.select(feed.getSelectorContent()).attr("content")
							: newsPage.select(feed.getSelectorContent()).text());
				}
			}
		} else {
			BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
			switch (feed.getExtractionType()) {
			case ARTICLE_EXTRACTOR:
				extractor = CommonExtractors.ARTICLE_EXTRACTOR;
				break;
			case DEFAULT_EXTRACTOR:
				extractor = CommonExtractors.DEFAULT_EXTRACTOR;
				break;
			case CANOLA_EXTRACTOR:
				extractor = CommonExtractors.CANOLA_EXTRACTOR;
				break;
			case LARGEST_CONTENT_EXTRACTOR:
				extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;
				break;
			}
			try {
				temp.setContent(extractor.getText(newsPage.outerHtml()));
			} catch (BoilerpipeProcessingException e) {
				LOGGER.error("Error al extraer el contenido del texto de forma automatica");
				;
				temp.setContent("Error al extraer el contenido de forma automatica");
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
		if (feed.getExtractionType() == null
				|| ExtractionType.CSS_EXTRACTOR
						.equals(feed.getExtractionType())) {
			if (feed.getSelectorContent() != null) {
				if (!feed.getSelectorContent().isEmpty()) {
					temp.setContent(feed.getSelectorContentMeta() ? newsPage
							.select(feed.getSelectorContent()).attr("content")
							: newsPage.select(feed.getSelectorContent()).text());
				}
			}
		} else {
			BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
			switch (feed.getExtractionType()) {
			case ARTICLE_EXTRACTOR:
				extractor = CommonExtractors.ARTICLE_EXTRACTOR;
				break;
			case DEFAULT_EXTRACTOR:
				extractor = CommonExtractors.DEFAULT_EXTRACTOR;
				break;
			case CANOLA_EXTRACTOR:
				extractor = CommonExtractors.CANOLA_EXTRACTOR;
				break;
			case LARGEST_CONTENT_EXTRACTOR:
				extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;
				break;
			}
			try {
				temp.setContent(extractor.getText(newsPage.outerHtml()));
			} catch (BoilerpipeProcessingException e) {
				LOGGER.error("Error al extraer el contenido del texto de forma automatica");
				;
				temp.setContent("Error al extraer el contenido de forma automatica");
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
			} else {
				temp.setTitle(newsPage.title());
			}
		} else {
			temp.setTitle(newsPage.title());
		}
		return temp.build();
	}

	@Override
	public News getNewsFromSite(String link, Feed feed) {
		return getNewsWithOutRSS(feed, link, link);
	}

}
